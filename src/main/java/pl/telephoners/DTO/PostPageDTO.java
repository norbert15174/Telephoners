package pl.telephoners.DTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.PersonalData;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostPageDTO {

    private long id;
    private String topic;
    private String postName;
    private String content;
    private Set<Gallery> galleries;
    private Gallery mainPhoto;
    private String authorName;
    private String authorSurname;
    private LocalDate postDate;


}
