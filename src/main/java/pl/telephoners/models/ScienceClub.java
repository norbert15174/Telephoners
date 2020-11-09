package pl.telephoners.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Science_club")
public class ScienceClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Role role;
    @OneToMany(mappedBy = "id")
    private Set<Project> project_participation;

}
