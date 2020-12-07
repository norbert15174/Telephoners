package pl.telephoners.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.telephoners.services.UserAppService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private UserAppService userAppService;

    public AuthorizationController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @PostMapping("/register")
    public ResponseEntity accountRegister(@RequestBody Map<String,String> register){
        String username = register.get("username");
        String password = register.get("password");
        String email = register.get("email");
        if(username.isBlank() || password.isBlank() || email.isBlank()) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if(userAppService.UserRegister(username,password,email)) return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }


    @GetMapping("/register")
    public RedirectView enableUserAccount(@RequestParam String token){
        userAppService.enableUserAccount(token);
        return new RedirectView("https://www.baeldung.com/spring-redirect-and-forward");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> login) {
        String username = login.get("username");
        String password = login.get("password");
        if (username.isBlank() || password.isBlank()) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        String token = userAppService.login(username, password);
        if (token == null) return new ResponseEntity<>("The username or password is incorrect", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}
