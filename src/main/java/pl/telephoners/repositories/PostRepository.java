package pl.telephoners.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {


    Optional<Post> findPostByPostName(String postName);

    @Query("SELECT p from Post p left join fetch p.galleries left join fetch p.mainPhoto left join fetch p.author where p.postName LIKE %:postName%")
    Optional<List<Post>> findPostsByPostName(@Param("postName") String postName);

    @Query("SELECT p from Post p left join fetch p.galleries left join fetch p.mainPhoto left join fetch p.author order by p.id desc")
    Optional<List<Post>> findAllPosts(Pageable pageable);

    @Query("SELECT p from Post p left join fetch p.galleries left join fetch p.mainPhoto left join fetch p.author where p.id = :id order by p.id desc ")
    Optional<Post> findPostById(@Param("id") long id);

    @Query("SELECT p from Post p where p.author.id = :id")
    Optional<List<Post>> findPostsByAuthor(@Param("id") long id);
}
