package pl.telephoners.controllers;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.telephoners.DTO.PersonalDataDTO;
import pl.telephoners.DTO.PostDTO;
import pl.telephoners.DTO.ProjectDTO;
import pl.telephoners.models.Participant;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Project;
import pl.telephoners.services.ProjectsService;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/projects")
public class RestProjectController {


    private ProjectsService projectsService;

    public RestProjectController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @PostMapping("/add")
    public ResponseEntity<Project> addNewProject(@RequestBody Project project , @RequestParam long leaderId){

        Project projectToReturn = projectsService.addNewProject(project,leaderId);
        if(projectToReturn == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(projectToReturn,HttpStatus.ACCEPTED);
        }


    }
    @GetMapping("/add/{leaderId}")
    public ResponseEntity<Project> createNewEmptyProject(@PathVariable long leaderId){

        Project projectToReturn = projectsService.addNewProject(leaderId);
        if(projectToReturn == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(projectToReturn,HttpStatus.ACCEPTED);
        }


    }
    @GetMapping("/add/personal")
    public ResponseEntity<Project> addNewProjectByLeader(@RequestBody PersonalData personalData){

        Project projectToReturn = projectsService.addNewProject(personalData);
        if(projectToReturn == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(projectToReturn,HttpStatus.ACCEPTED);
        }


    }

    @GetMapping("/enroltoproject")
    public ResponseEntity<String> enrolInTheProjectByPersonalAndProjectClass(@RequestBody PersonalData personalData,@RequestBody Project project){

        boolean participant = projectsService.enrolPersonInTheProject(project,personalData);
        if(participant){
            return new ResponseEntity(HttpStatus.CREATED);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }


    }
    @GetMapping("/enrol")
    public ResponseEntity<String> enrolInTheProjectByPersonalAndProjectClass(@RequestParam long projectId,@RequestParam long personId){

        boolean participant = projectsService.enrolPersonInTheProject(projectId,personId);
        if(participant){
            return new ResponseEntity(HttpStatus.CREATED);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/enrol/{id}")
    public ResponseEntity<String> enrolMe(@PathVariable long id, @AuthenticationPrincipal Principal user){
        boolean participant = projectsService.enrolMeInTheProject(id,user.getName());
        if(participant){
            return new ResponseEntity(HttpStatus.CREATED);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<List<ProjectDTO>> getParticipants(@PathVariable long id){
        List<ProjectDTO> projectDTOS = projectsService.getParticipants(id);
        if(projectDTOS == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(projectDTOS,HttpStatus.NOT_FOUND);
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<PersonalDataDTO>> getProjectParticipation(@PathVariable long id){
        Set<PersonalDataDTO> personalDataDTOS = projectsService.getProjectParticipation(id);
        if(personalDataDTOS == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(personalDataDTOS,HttpStatus.FOUND);
        }


    }

    @GetMapping("/leave")
    public ResponseEntity<String> leaveTheProject(@RequestParam long personId,@RequestParam long projectId){
        if(projectsService.leaveTheProject(personId,projectId)) return new ResponseEntity(HttpStatus.ACCEPTED);
        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTheProject(@PathVariable long id){
        if(projectsService.deleteProject(id)) return new ResponseEntity(HttpStatus.ACCEPTED);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    @PutMapping
    public ResponseEntity<Project> updateProject(@RequestBody ProjectDTO projectDTO){
        Project project = projectsService.updateProject(projectDTO);
        if(project ==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(project,HttpStatus.ACCEPTED);
    }
    @PutMapping("/recruitment")
    public ResponseEntity setRecruitment(@RequestParam long id,@RequestParam boolean isRec){
        if(projectsService.setRecruitment(id,isRec)) return new ResponseEntity(HttpStatus.ACCEPTED);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/finish")
    public ResponseEntity setProjectFinish(@RequestParam long id){
        if(projectsService.setProjectFinish(id)) return new ResponseEntity(HttpStatus.ACCEPTED);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/leader/{id}")
    public ResponseEntity<PersonalData> getLeader(@PathVariable long id){
        PersonalData personalData = projectsService.getLeader(id);
        if(personalData == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(personalData,HttpStatus.OK);
    }


}
