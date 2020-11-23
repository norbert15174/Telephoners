package pl.telephoners.mappers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.telephoners.DTO.PersonalDataDTO;
import pl.telephoners.models.PersonalData;
import pl.telephoners.repositories.PersonalDataRepository;

import java.util.ArrayList;
import java.util.List;


//This class is responsible for mapping PersonalData to PersonalDataDTO
@Component
@NoArgsConstructor
public class PersonalDataObjectMapperClass {



    //Init model mapper PersonalData to PersonalDataDTO
    private ModelMapper personalDataObjectMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonalData, PersonalDataDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setFaculty(source.getFaculty());
                map().setFieldOfStudy(source.getFieldOfStudy());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());

            }
        });
        return modelMapper;
    }

    //Return mapped models
    public List<PersonalDataDTO> mapPersonalDataToPersonalDataDTO(List<PersonalData> personalData){
        List<PersonalDataDTO> personalDataDTOS = new ArrayList<>();
        personalData.forEach((pd -> personalDataDTOS.add(personalDataObjectMapper().map(pd,PersonalDataDTO.class))));
        return personalDataDTOS;
    }

    //Return mapped model
    public PersonalDataDTO mapPersonalDataToPersonalDataDTO(PersonalData personalData){
        PersonalDataDTO personalDataDTOS;
        personalDataDTOS = personalDataObjectMapper().map(personalData,PersonalDataDTO.class);
        return personalDataDTOS;
    }




}
