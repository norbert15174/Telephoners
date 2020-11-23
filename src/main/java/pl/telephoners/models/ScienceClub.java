package pl.telephoners.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;
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
    @OneToMany(fetch=FetchType.EAGER)
    private List<Project> projectParticipation = null;

}
