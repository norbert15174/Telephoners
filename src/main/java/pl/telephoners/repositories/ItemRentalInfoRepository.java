package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.ItemRentalInfo;

@Repository
public interface ItemRentalInfoRepository extends JpaRepository<ItemRentalInfo,Long> {
}
