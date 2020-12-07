package pl.telephoners.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.telephoners.models.ContactDetails;
import pl.telephoners.models.PersonalData;
import pl.telephoners.repositories.ContactDetailsRepository;
import pl.telephoners.repositories.PersonalDataRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalDataService {

    // will change as soon as the creation of the personal data service is completed
    @Autowired
    private PersonalDataRepository personalDataRepository;
    private ContactDetailsRepository contactDetailsRepository;



    public PersonalData addPersonalData(){

        PersonalData personalData = new PersonalData();

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

    //Update PersonalData object
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

    //Delete PersonalData object
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

    //Get all PersonalData objects
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
    //Get PersonalData object by ID
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
    //Get all PersonalData objects by LastName
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


    //It will be deleted
    @EventListener(ApplicationReadyEvent.class)
    public void asd(){

        addPersonalData();


    }

}
