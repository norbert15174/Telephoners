package pl.telephoners.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "Telephoners_projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData leader;
    @JsonIgnore
    @OneToMany(mappedBy = "projectParticipants", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<Participant> participants = new HashSet<>();

    private String mainPhotoUrl;

    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    private String topic;
    @Size(max=2000)
    private String description;
    private boolean isRecruitment = true;
    private boolean isFinished = false;


}
