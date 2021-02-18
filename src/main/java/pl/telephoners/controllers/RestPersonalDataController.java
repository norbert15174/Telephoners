package pl.telephoners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.UserApp;
import pl.telephoners.services.PersonalDataService;
import pl.telephoners.services.UserAppService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/persondata")
public class RestPersonalDataController {

    private PersonalDataService personalDataService;
    private UserAppService userAppService;

    @Autowired
    public RestPersonalDataController(PersonalDataService personalDataService, UserAppService userAppService) {
        this.personalDataService = personalDataService;
        this.userAppService = userAppService;
    }


    @GetMapping
    public ResponseEntity<List<PersonalData>> findAllPersonalData(@RequestParam(defaultValue = "0") int page) {
        List<PersonalData> personalData = personalDataService.getAllPersonalData(page);
        if (personalData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(personalData, HttpStatus.ACCEPTED);
        }

    }

    @GetMapping(path = "/lastname/{name}")
    public ResponseEntity<List<PersonalData>> findAllPersonalDataByLastName(@RequestParam(defaultValue = "0") int page, @PathVariable String name) {
        List<PersonalData> personalData = personalDataService.getPersonalDataByLastName(name, page);
        if (personalData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(personalData, HttpStatus.ACCEPTED);
        }

    }

    @PutMapping
    public ResponseEntity<PersonalData> updatePersonalData(@RequestBody PersonalData personalData, @AuthenticationPrincipal Principal user) {
        UserApp userApp = (UserApp) userAppService.loadUserByUsername(user.getName());
        if (userApp.getPersonalInformation().getId() != personalData.getId())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        personalData = personalDataService.updatePersonData(personalData);
        if (personalData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(personalData, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deletePersonalData(@PathVariable long id) {
        String message = personalDataService.deletePersonData(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/admin/{id}")
    public ResponseEntity<PersonalData> getPersonalDataById(@PathVariable long id) {
        PersonalData personalData = personalDataService.getPersonalDataById(id);
        if (personalData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(personalData, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<PersonalData> getUserInfo(@AuthenticationPrincipal Principal user) {
        UserApp userApp = (UserApp) userAppService.loadUserByUsername(user.getName());
        PersonalData personalData = userApp.getPersonalInformation();
        if (personalData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(personalData, HttpStatus.ACCEPTED);
        }

    }

    @PostMapping("/set/photo")
    public ResponseEntity<String> setUserPhoto(@AuthenticationPrincipal Principal user, @RequestParam("photo") MultipartFile file) {
        PersonalData personalData = getUserInformation(user);
        if (personalDataService.addPersonPhoto(file, personalData.getLastName(), personalData.getId()) == null)
            return new ResponseEntity<>("The photo could not be added", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Photo has been added", HttpStatus.OK);
    }

    private PersonalData getUserInformation(Principal user) {
        UserApp userApp = (UserApp) userAppService.loadUserByUsername(user.getName());
        PersonalData personalData = userApp.getPersonalInformation();
        return personalData;
    }


}
