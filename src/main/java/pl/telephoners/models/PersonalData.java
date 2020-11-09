package pl.telephoners.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Personal_data")
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String fieldOfStudy;
    @OneToOne
    ScienceClub scienceClubId;
//    @OneToOne(mappedBy = "id")
//    ContactDetails contactDetailsId;
    private String faculty;

    //to change
    private String accountId;



}
