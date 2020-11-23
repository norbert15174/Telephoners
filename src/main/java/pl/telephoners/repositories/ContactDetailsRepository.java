package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.ContactDetails;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails,Long> {
}
