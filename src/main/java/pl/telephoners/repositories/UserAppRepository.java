package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.telephoners.models.UserApp;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    UserApp findAllByUsername(String username);


    Optional<UserApp> findFirstByUsername(String username);

    @Query("select u from UserApp u left join fetch u.personalInformation where u.id = :id")
    Optional<UserApp> findFirstById(@Param("id") long id);

    @Query("select u from UserApp u left join fetch u.personalInformation where u.username = :username")
    Optional<UserApp> findFirstByUsernameToGetPersonalData(@Param("username") String username);


}
