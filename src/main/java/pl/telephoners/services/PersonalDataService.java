package pl.telephoners.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.models.ContactDetails;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.UserApp;
import pl.telephoners.repositories.ContactDetailsRepository;
import pl.telephoners.repositories.PersonalDataRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalDataService {

    private PersonalDataRepository personalDataRepository;
    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${url-gcp}")
    private String urlGCP;


    public PersonalData addPersonPhoto(MultipartFile file, String userName, long id){
        String path = "user/" + userName + "/main/";
        BlobId blobId = BlobId.of("telephoners",path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo,file.getBytes());
            PersonalData personalData = personalDataRepository.findPersonDatabyId(id).get();
            personalData.setPhotoUrl(urlGCP + path);
            personalDataRepository.save(personalData);
            return personalData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



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

    public PersonalData addPersonalData(String surname, String name){

        PersonalData personalData = new PersonalData();
        personalData.setFirstName(name);
        personalData.setLastName(surname);

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
                return personalDataRepository.findPersonDatabyId(personalData.getId()).get();
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
            return "received item doesn't exist";
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


}
