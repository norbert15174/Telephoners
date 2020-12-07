package pl.telephoners.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "perso_data")
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_id")
    private long id;
    private String firstName;
    private String lastName;
    private String fieldOfStudy;
    private Role role;



    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private ContactDetails contactDetailsId;



    private String faculty;


}
