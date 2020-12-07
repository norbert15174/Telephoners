package pl.telephoners.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Project;
import pl.telephoners.models.Role;
import pl.telephoners.models.UserApp;
import pl.telephoners.repositories.UserAppRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;


@Service
public class UserAppService implements UserDetailsService {

    @Value("${Algorithm-key}")
    private String key;

    private PasswordEncoder passwordEncoder;
    private UserAppRepository userAppRepository;
    private PersonalDataService personalDataService;
    private MailSenderService mailSenderService;
    private ProjectsService projectsService;
    @Autowired
    public UserAppService( PasswordEncoder passwordEncoder, UserAppRepository userAppRepository, PersonalDataService personalDataService, MailSenderService mailSenderService, ProjectsService projectsService) {
        this.passwordEncoder = passwordEncoder;
        this.userAppRepository = userAppRepository;
        this.personalDataService = personalDataService;
        this.mailSenderService = mailSenderService;
        this.projectsService = projectsService;
    }







    public boolean UserRegister(String username, String password, String email) {
        if(userAppRepository.findFirstByUsername(username).isPresent()) return false;
        UserApp userApp = new UserApp();
        userApp.setUsername(username);
        userApp.setPassword(passwordEncoder.encode(password));
        userApp.setEmail(email);
        userApp.setPersonalInformation(personalDataService.addPersonalData());
        if(userAppRepository.save(userApp)==null) return false;

        String registerString = "<h2>Welcome " + username + ", Thank you for your registration</h2><br>" +
                "<h3>To complete the registration process, please click on the link below</h3><br>" +
                "http://localhost:8000/auth/register?token=" + generateJwt(username,password);

        try {
            mailSenderService.sendMail(email,"Telephoners register",registerString,true);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void enableUserAccount(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(key)).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        String username = verify.getClaim("username").asString();
        Optional<UserApp> userApp = userAppRepository.findFirstByUsername(username);
        userApp.ifPresent(userAppUpdate -> {
            userAppUpdate.setEnable(true);
            userAppRepository.save(userAppUpdate);
        });
    }

    public String login(String username, String password){
        UserDetails userDetails = loadUserByUsername(username);
        if(passwordEncoder.matches(password,userDetails.getPassword()) && userDetails.isEnabled()){
            return generateJwt(username,password);
        }
        return null;
    }

    private String generateJwt(String username, String password) {
        Algorithm algorithm = Algorithm.HMAC512(key);
        return JWT.create().withClaim("username", username).withClaim("password",password).sign(algorithm);
    }



    public UserDetails accountVerify(String username, String password){
        UserDetails userDetails = loadUserByUsername(username);
        if(passwordEncoder.matches(password,userDetails.getPassword()) && userDetails.isEnabled()) return userDetails;
        return null;
    }


    public boolean deleteUser(long id){

        long personalId = userAppRepository.findFirstById(id).get().getPersonalInformation().getId();
        List<Project> projectList = projectsService.findProjectsByLeader(personalId);
        if(projectList == null){
            projectsService.leaveTheProjectByPersonalId(personalId);
            userAppRepository.deleteById(id);
            return true;
        }
        projectList.forEach(project -> projectsService.deleteProject(project.getId()));
        userAppRepository.deleteById(id);
        return true;
    }
    public boolean deleteUser(String username){
        long personalId = userAppRepository.findFirstByUsernameToGetPersonalData(username).get().getId();
        return deleteUser(personalId);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userAppRepository.findAllByUsername(s);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        //deleteUser(2L);
    }


}
