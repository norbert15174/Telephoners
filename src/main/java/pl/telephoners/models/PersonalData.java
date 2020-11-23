package pl.telephoners.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "Personal_user_data")
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String fieldOfStudy;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    ScienceClub scienceClubId;

    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    ContactDetails contactDetailsId;

    private String faculty;

    //to change
    private String accountId;



}
