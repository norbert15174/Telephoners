package pl.telephoners.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.telephoners.services.UserAppService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private UserAppService userAppService;

    public AuthorizationController(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @PostMapping("/register")
    public ResponseEntity accountRegister(@RequestBody Map<String, String> register) {
        String name = register.get("name");
        String surname = register.get("surname");
        String username = register.get("username");
        String password = register.get("password");
        String email = register.get("email");
        if (username.isBlank() || password.isBlank() || email.isBlank() || name.isBlank() || surname.isBlank())
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (userAppService.UserRegister(username, password, email, name, surname))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }


    @GetMapping("/register")
    public RedirectView enableUserAccount(@RequestParam String token) {
        userAppService.enableUserAccount(token);
        return new RedirectView("https://www.baeldung.com/spring-redirect-and-forward");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> login) {
        String username = login.get("username");
        String password = login.get("password");
        if (username.isBlank() || password.isBlank()) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Map<String, String> user = userAppService.login(username, password);
        if (user == null) return new ResponseEntity("The username or password is incorrect", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal Principal user) {
        if (userAppService.deleteUser(user.getName())) return new ResponseEntity<>("Account deleted", HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/work")
    public String s() {
        return "work";
    }


}
