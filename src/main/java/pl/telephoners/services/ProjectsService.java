package pl.telephoners.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.telephoners.DTO.ProjectDTO;
import pl.telephoners.mappers.PersonalDataObjectMapperClass;
import pl.telephoners.mappers.ProjectObjectMapperClass;
import pl.telephoners.models.Participant;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Project;
import pl.telephoners.repositories.ParticipantRepository;
import pl.telephoners.repositories.PersonalDataRepository;
import pl.telephoners.repositories.ProjectRepository;
import java.util.*;


@Service
public class ProjectsService {

    private ProjectRepository projectRepository;
    private PersonalDataRepository personalDataRepository;
    private PersonalDataObjectMapperClass personalDataObjectMapperClass;
    private PersonalDataService personalDataService;
    private ParticipantRepository participantRepository;
    private ProjectObjectMapperClass projectObjectMapperClass;

    @Autowired
    public ProjectsService(ProjectRepository projectRepository, PersonalDataRepository personalDataRepository, PersonalDataObjectMapperClass personalDataObjectMapperClass, PersonalDataService personalDataService, ParticipantRepository participantRepository, ProjectObjectMapperClass projectObjectMapperClass) {
        this.projectRepository = projectRepository;
        this.personalDataRepository = personalDataRepository;
        this.personalDataObjectMapperClass = personalDataObjectMapperClass;
        this.personalDataService = personalDataService;
        this.participantRepository = participantRepository;
        this.projectObjectMapperClass = projectObjectMapperClass;
    }

    public Project addNewProject(Project project, long idleader){
        Optional<PersonalData> personalData = personalDataRepository.findById(idleader);
        if(project == null
                || projectRepository.findById(project.getId()).isPresent()
                || !personalData.isPresent()
        ) return null;

        try{
            project.setLeader(personalData.get());
            return projectRepository.save(project);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public Project addNewProject(Project project, PersonalData personalDataToSave){
        return addNewProject(project,personalDataToSave.getId());
    }
    public Project addNewProject(PersonalData personalDataToSave){
        Project project = new Project();
        return addNewProject(project,personalDataToSave.getId());
    }
    public Project addNewProject(Long leaderId){
        Project project = new Project();
        return addNewProject(project,leaderId);
    }

    public boolean enrolPersonToProject(Project project, PersonalData personalData){
        //if project and personalData exist, method will enroll person into project
        if(projectRepository.findById(project.getId()).isPresent() && personalDataRepository.findById(personalData.getId()).isPresent()){


            try {
                if (participantRepository.checkIfExist(personalData.getId(),project.getId()).isPresent()){
                    return false;
                };

                Participant participant = new Participant();
                participant.setProjectParticipants(project);
                participant.setPersonParticipants(personalData);
                project.addParticipant(participant);
                projectRepository.save(project);
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }

        }
        return false;
    }
    public boolean enrolPersonToProject(long projectId, long personalId){
        return enrolPersonToProject(projectRepository.findById(projectId).get(),personalDataRepository.findById(personalId).get());
    }

    public boolean leaveTheProject(long id){

        Optional<Participant> participant = participantRepository.findById(id);
        if(participant.isPresent()){
            try {
                participantRepository.delete(participant.get());
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    public Set<Participant> getProjectParticipation(long id){
        if(projectRepository.findById(id).isPresent()){
            return projectRepository.findProjectById(id).get().getParticipants();
        }
        return null;
    }

    public List<ProjectDTO> getParticipation(long id){

        Optional<List<Participant>> participants = participantRepository.getMyProjectParticipation(id);
        if(participants.isPresent()){
            List<ProjectDTO> projectDTOS = new ArrayList<>();
            participants.get().forEach(participant -> {
                projectDTOS.add(projectObjectMapperClass.mapProjectToProjectDTO(participant.getProjectParticipants()));
            });
            return projectDTOS;
        }
        return null;
    }

    public boolean deleteProject(long id){
        Optional<Project> project = projectRepository.findProjectById(id);
        if(project.isPresent()){
            try{
                projectRepository.delete(project.get());
                return true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }

        }
        return false;
    }

    public Project updateProject(ProjectDTO projectDTO){
        if(projectRepository.findById(projectDTO.getId()).isPresent()){
            Project project = projectRepository.findById(projectDTO.getId()).get();
            project.setDescription(projectDTO.getDescription());
            project.setName(projectDTO.getName());
            return projectRepository.save(project);
        }
        return null;
    }

    public boolean setRecrutiment(long id,boolean isRec){
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()){
            project.get().setRecrutiment(isRec);
            projectRepository.save(project.get());
            return true;
        }
        return false;
    }
    

}
