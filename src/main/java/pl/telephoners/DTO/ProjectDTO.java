package pl.telephoners.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private boolean isRecrutiment;
}
