package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Gallery;

import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery,Long> {

    Optional<Gallery> findFirstByUrl(String url);


}
