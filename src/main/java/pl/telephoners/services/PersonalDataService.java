package pl.telephoners.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import pl.telephoners.models.ContactDetails;
//import pl.telephoners.models.PersonalData;
//import pl.telephoners.models.Role;
//import pl.telephoners.models.ScienceClub;
import pl.telephoners.models.ContactDetails;
import pl.telephoners.models.PersonalData;
//import pl.telephoners.repositories.ContactDetailsRepository;
import pl.telephoners.models.ScienceClub;
import pl.telephoners.repositories.ContactDetailsRepository;
import pl.telephoners.repositories.PersonalDataRepository;
import pl.telephoners.repositories.ScienceClubRepository;

import java.util.List;
import java.util.Optional;
//import pl.telephoners.repositories.ScienceClubRepository;

@Service
public class PersonalDataService {

    // will change as soon as the creation of the personal data service is completed
    @Autowired
    private PersonalDataRepository personalDataRepository;
    private ScienceClubRepository scienceClubRepository;
    private ContactDetailsRepository contactDetailsRepository;



    public PersonalData addPersonalData(){
        PersonalData personalData = new PersonalData();
        personalData.setLastName("Norbert");

        //change after
        //personalData.getAccountId()

        //Create new ScienceClub
        ScienceClub scienceClub = new ScienceClub();
        scienceClub.setRole("Role.Admin");
        personalData.setScienceClubId(scienceClub);

        //Create new ContactDetails
        ContactDetails contactDetails =  new ContactDetails();
        personalData.setContactDetailsId(contactDetails);

        //Save data
        try{
            personalDataRepository.save(personalData);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return personalDataRepository.findById(personalData.getId()).get();
    }

    public PersonalData updatePersonData(PersonalData personalData){
        if(personalDataRepository.findById(personalData.getId()).isPresent()){
            try{
                personalDataRepository.save(personalData);
                return personalDataRepository.findById(personalData.getId()).get();
            }catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }

        }else {
            return null;
        }
    }

    public String deletePersonData(long id){
        if(personalDataRepository.findById(id).isPresent()){
            try {
                personalDataRepository.deleteById(id);
            }catch (Exception e){
                return e.getMessage();
            }
            return "item was deleted";
        }else {
            return "recived item doesn't exist";
        }
    }

    public List<PersonalData> getAllPersonalData(int page){
        try {

            Optional<List<PersonalData>> personalData = personalDataRepository.findAllPersonalData(PageRequest.of(page,10));
            if (personalData.isPresent()) {
                return personalData.get();
            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public PersonalData getPersonalDataById(long id){
        try {
            Optional<PersonalData> personalData = personalDataRepository.findPersonDatabyId(id);
            if (personalData.isPresent()) {
                return personalData.get();
            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<PersonalData> getPersonalDataByLastName(String name,int page){
        try {
            Optional<List<PersonalData>> personalData = personalDataRepository.findPersonalDataByLastName(PageRequest.of(page,10),name);
            if (personalData.isPresent()) {
                return personalData.get();
            }
            return null;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void asd(){

        PersonalData person = addPersonalData();
        getPersonalDataById(1);
        getPersonalDataByLastName("Norbert",0);
//        person.setFirstName("Bolek");
//        updatePersonData(person);
//        getAllPersonalData();
    }

}
