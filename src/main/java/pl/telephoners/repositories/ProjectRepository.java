package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
}
