package pl.telephoners.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDTO {
    private long id;
    private String topic;
    private String postName;
}
