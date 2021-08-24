package pl.telephoners.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
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
    @Size(max = 8000)
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Gallery> galleries = new HashSet <>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Gallery mainPhoto;
    @OneToOne
    @JsonIgnore
    private PersonalData author;
    private LocalDate postDate = LocalDate.now();


    public void addPhotoToGallery(Gallery gallery) {
        galleries.add(gallery);
    }
    public void addPhotoToGallery(Set<Gallery> gallery) {
        galleries.addAll(gallery);
    }
    public boolean deletePhotoFromGallery(Gallery gallery) { return galleries.remove(gallery); }

}
