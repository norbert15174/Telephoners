package pl.telephoners.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import pl.telephoners.DTO.PersonalDataDTO;
import pl.telephoners.models.PersonalData;

public class PersonalDataObjectMapper {




    @Bean
    public ModelMapper getPersonalDataObjectMapper(){
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



}
