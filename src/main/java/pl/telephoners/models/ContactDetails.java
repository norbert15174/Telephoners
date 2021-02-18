package pl.telephoners.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact_details")
public class ContactDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;
    private float phoneNumber;


    @JsonIgnore
    @OneToOne(mappedBy = "contactDetailsId", fetch = FetchType.LAZY)
    private PersonalData PersonalDataId;

    @Override
    public String toString() {
        return "ContactDetails{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
