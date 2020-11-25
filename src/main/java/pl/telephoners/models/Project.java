package pl.telephoners.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
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
    @OneToOne(fetch = FetchType.EAGER)
    private PersonalData leader;

    @OneToMany(mappedBy = "projectParticipants", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    Set<Participant> participants = new HashSet<>();


    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    private String topic;
    private String description;
    private boolean isRecrutiment;


}
