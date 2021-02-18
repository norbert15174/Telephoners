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
import pl.telephoners.DTO.ProjectDTO;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Project;
import pl.telephoners.repositories.PersonalDataRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//This class is responsible for mapping PersonalData to PersonalDataDTO
@Component
@NoArgsConstructor
public class ProjectObjectMapperClass {


    //Init model mapper PersonalData to PersonalDataDTO
    private ModelMapper projectObjectMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setDescription(source.getDescription());
                map().setTopic(source.getTopic());
            }
        });
        return modelMapper;
    }

    //Return mapped models
    public List<ProjectDTO> mapProjectToProjectDTO(Set<Project> projects) {
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        projects.forEach((pd -> projectDTOS.add(projectObjectMapper().map(pd, ProjectDTO.class))));
        return projectDTOS;
    }

    //Return mapped model
    public ProjectDTO mapProjectToProjectDTO(Project project) {
        ProjectDTO personalDataDTOS;
        personalDataDTOS = projectObjectMapper().map(project, ProjectDTO.class);
        return personalDataDTOS;
    }


}

