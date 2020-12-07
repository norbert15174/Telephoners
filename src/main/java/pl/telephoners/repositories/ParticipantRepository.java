package pl.telephoners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.telephoners.models.Participant;
import pl.telephoners.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Long>{

    @Query("select p from Participant p left join fetch p.projectParticipants where p.personParticipants.id = :id")
    Optional<List<Participant>> getMyProjectParticipation(@Param("id") long id);

    @Query("select p from Participant p left join fetch p.projectParticipants where p.personParticipants.id = :id and p.projectParticipants.id = :idProject")
    Optional<List<Participant>> checkIfExist(@Param("id") long id,@Param("idProject") long projectId);

    Optional<Participant> getParticipantByPersonParticipantsIdAndAndProjectParticipantsId(long personId,long projectId);

    Optional<List<Participant>> findAllByPersonParticipantsId(long id);

}
