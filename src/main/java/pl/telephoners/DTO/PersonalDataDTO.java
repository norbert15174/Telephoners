package pl.telephoners.DTO;

import lombok.*;
import pl.telephoners.models.MemberRole;
import pl.telephoners.models.Role;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonalDataDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String fieldOfStudy;
    private String faculty;
    private MemberRole role;
    private String skills;
    private String photo;


}
