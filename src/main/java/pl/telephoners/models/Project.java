package pl.telephoners.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "Telephoners_projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(fetch = FetchType.LAZY)
    private PersonalData leader;

    @OneToMany(targetEntity = PersonalData.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="part_id",referencedColumnName = "id")
    private Collection<PersonalData> participants = new ArrayList<>();



    private String description;
    private boolean isRecrutiment;
//    @OneToMany(mappedBy = "id")
//    private Set<WhoNeed> whoNeeds;

}
