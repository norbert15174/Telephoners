package pl.telephoners.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.telephoners.DTO.PersonalDataDTO;
import pl.telephoners.mappers.PersonalDataObjectMapperClass;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Project;
import pl.telephoners.repositories.PersonalDataRepository;
import pl.telephoners.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectsService {

    ProjectRepository projectRepository;
    private PersonalDataRepository personalDataRepository;
    private PersonalDataObjectMapperClass personalDataObjectMapperClass;

    @Autowired
    public ProjectsService(ProjectRepository projectRepository, PersonalDataRepository personalDataRepository, PersonalDataObjectMapperClass personalDataObjectMapperClass) {
        this.projectRepository = projectRepository;
        this.personalDataRepository = personalDataRepository;
        this.personalDataObjectMapperClass = personalDataObjectMapperClass;
    }





    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Project project5 = new Project();
        projectRepository.save(project5);
        Project project = projectRepository.findById(1L).get();
        PersonalData personalData2 =personalDataRepository.findById(1L).get();
        List<PersonalData> personalData = new ArrayList<>();
        personalData.add(personalData2);
        project.setParticipants(personalData);
        projectRepository.save(project);

    }







}
