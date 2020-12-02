package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Project p left join fetch p.participants WHERE p.id = :id ")
    Optional<Project> findProjectById(@Param("id") long id);

    @Query("SELECT p FROM Project p left join fetch p.leader WHERE p.id = :id")
    Optional<Project> findProjectByLeaderId(@Param("id") long id);


}
