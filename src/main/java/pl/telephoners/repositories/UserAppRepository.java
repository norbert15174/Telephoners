package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.telephoners.models.UserApp;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp,Long> {

    UserApp findAllByUsername(String username);

    Optional<UserApp> findFirstByUsername(String username);

}
