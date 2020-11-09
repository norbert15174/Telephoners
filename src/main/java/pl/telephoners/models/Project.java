package pl.telephoners.models;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private PersonalData leader;
    @OneToMany(mappedBy = "id")
    private Set<PersonalData> participants;
    private String description;
    private boolean isRecrutiment;
    @OneToMany(mappedBy = "id")
    private Set<WhoNeed> whoNeeds;

}
