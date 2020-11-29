package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
}
