package pl.telephoners.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class ScienceClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //private Role role;
    private String role;
    @OneToMany(fetch=FetchType.LAZY)
    private Set<Project> projectParticipation = null;

}
