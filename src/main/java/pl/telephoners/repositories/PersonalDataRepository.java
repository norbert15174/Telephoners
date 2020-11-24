package pl.telephoners.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.PersonalData;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData,Long> {

    @Query("SELECT p FROM PersonalData p left join fetch p.contactDetailsId ")
    public Optional<List<PersonalData>> findAllPersonalData(Pageable pageable);

    @Query("SELECT p FROM PersonalData p left join fetch p.contactDetailsId where p.id = :PersonDataId")
    public Optional<PersonalData> findPersonDatabyId(@Param("PersonDataId") long id);

    @Query("SELECT p FROM PersonalData p left join fetch p.contactDetailsId  where p.lastName like %:PersonDataLastName%")
    public Optional<List<PersonalData>> findPersonalDataByLastName(Pageable pageable,@Param("PersonDataLastName") String name);
}
