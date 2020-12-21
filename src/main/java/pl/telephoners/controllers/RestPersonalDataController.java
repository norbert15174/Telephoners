package pl.telephoners.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.telephoners.models.PersonalData;
import pl.telephoners.services.PersonalDataService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/persondata")
public class RestPersonalDataController {

    private PersonalDataService personalDataService;
    @Autowired
    public RestPersonalDataController(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }




    @GetMapping
    public ResponseEntity<List<PersonalData>> findAllPersonalData(@RequestParam(defaultValue = "0") int page){
        List<PersonalData> personalData = personalDataService.getAllPersonalData(page);
        if(personalData == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(personalData,HttpStatus.ACCEPTED);
        }

    }

    @GetMapping(path = "/lastname/{name}")
    public ResponseEntity<List<PersonalData>> findAllPersonalDataByLastName(@RequestParam(defaultValue = "0") int page, @PathVariable String name){
        List<PersonalData> personalData = personalDataService.getPersonalDataByLastName(name,page);
        if(personalData == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(personalData,HttpStatus.ACCEPTED);
        }

    }

    @PutMapping()
    public ResponseEntity<PersonalData> updatePersonalData(@RequestBody PersonalData personalData){
        personalData = personalDataService.updatePersonData(personalData);
        if(personalData == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(personalData,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersonalData(@PathVariable long id){
        String message = personalDataService.deletePersonData(id);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonalData> getPersonalDataById(@PathVariable long id){
        PersonalData personalData = personalDataService.getPersonalDataById(id);
        if(personalData == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(personalData,HttpStatus.ACCEPTED);
        }

    }



}
