package pl.telephoners.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.telephoners.DTO.PersonalDataDTO;
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


    //Project adding method based on project class and personal data ID
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
    //Project adding method based on project class and personal data class
    public Project addNewProject(Project project, PersonalData personalDataToSave){
        return addNewProject(project,personalDataToSave.getId());
    }

    //Project adding method based on personal data class
    public Project addNewProject(PersonalData personalDataToSave){
        Project project = new Project();
        return addNewProject(project,personalDataToSave.getId());
    }

    //Project adding method based on project ID
    public Project addNewProject(Long leaderId){
        Project project = new Project();
        return addNewProject(project,leaderId);
    }

    // Method of joining the participant to the project based on the Project class and Personal Data class
    public boolean enrolPersonToProject(Project project, PersonalData personalData){
        //check if there is recruitment to the project
        if(!project.isRecruitment()) return false;
        //if project and personalData exist, method will enroll person into project
        if(projectRepository.findById(project.getId()).isPresent() && personalDataRepository.findById(personalData.getId()).isPresent()){


            try {
                //Check that the relationship does not exist
                //if not, create the relation else return null
                if (participantRepository.checkIfExist(personalData.getId(),project.getId()).isPresent()){
                    return false;
                }
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
    // Method of joining the participant to the project based on the Project ID and Personal Data ID
    public boolean enrolPersonToProject(long projectId, long personalId){
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<PersonalData> personalData = personalDataRepository.findById(personalId);
        if(project.isPresent() && personalData.isPresent()) return enrolPersonToProject(project.get(),personalData.get());
        return false;
    }

    // Method of unsubscribing from the project
    public boolean leaveTheProject(long projectId, long personId){

        Optional<Participant> participant = participantRepository.getParticipantByPersonParticipantsIdAndAndProjectParticipantsId(projectId,personId);
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

    //Get participants related to project
    public Set<PersonalDataDTO> getProjectParticipation(long id){
        if(projectRepository.findById(id).isPresent()){
            Set<Participant> participants = projectRepository.findProjectById(id).get().getParticipants();
            Set<PersonalDataDTO> personalDataDTOS = new HashSet<>();
            participants.forEach(participant -> personalDataDTOS.add(personalDataObjectMapperClass.mapPersonalDataToPersonalDataDTO(participant.getPersonParticipants())));
            return personalDataDTOS;
        }
        return null;
    }

    //Get projectDTOS related to participant
    public List<ProjectDTO> getParticipants(long id){

        Optional<List<Participant>> participants = participantRepository.getMyProjectParticipation(id);
        if(participants.isPresent()){
            List<ProjectDTO> projectDTOS = new ArrayList<>();
            participants.get().forEach(participant -> projectDTOS.add(projectObjectMapperClass.mapProjectToProjectDTO(participant.getProjectParticipants())));
            return projectDTOS;
        }
        return null;
    }

    //Delete project relation and project
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

    //Update project based on ProjectDTO class
    public Project updateProject(ProjectDTO projectDTO){
        if(projectRepository.findById(projectDTO.getId()).isPresent()){
            Project project = projectRepository.findById(projectDTO.getId()).get();
            project.setDescription(projectDTO.getDescription());
            project.setTopic(projectDTO.getTopic());
            return projectRepository.save(project);
        }
        return null;
    }

    //Determining the recruitment status for the project
    public boolean setRecruitment(long id,boolean isRec){
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()){
            project.get().setRecruitment(isRec);
            projectRepository.save(project.get());
            return true;
        }
        return false;
    }

    public boolean setProjectFinish(long id){
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()){
            project.get().setFinished(true);
            projectRepository.save(project.get());
            return true;
        }
        return false;
    }


}