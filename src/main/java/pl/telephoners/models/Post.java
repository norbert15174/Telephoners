package pl.telephoners.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Table(name = "telephoners_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String topic;
    private String postName;
    @Size(max=8000)
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<Gallery> galleries;
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Gallery mainPhoto;
    @OneToOne
    @JsonIgnore
    private PersonalData author;
    //DATA POSTU
    //LocalDate postDate;


    public void addPhotoToGallery(Gallery gallery){
        galleries.add(gallery);
    }

}
