package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.ScienceClub;

@Repository
public interface ScienceClubRepository extends JpaRepository<ScienceClub,Long> {
}
