package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.MailContact;

import java.util.Optional;

@Repository
public interface MailContactRespository extends JpaRepository<MailContact,Long> {

    Optional<MailContact> findFirstByMailAddress(String mailAddress);

}
