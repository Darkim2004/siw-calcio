package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Partecipation;

public interface PartecipationRepository extends CrudRepository<Partecipation, Long> {

    boolean existsByTournament_IdAndTeam_Id(Long tournamentId, Long teamId);

    Partecipation findByTournament_IdAndTeam_Id(Long tournamentId, Long teamId);


    @EntityGraph(attributePaths = "team")
    @Query("""
        select p
        from Partecipation p
        where p.tournament.id = :tournamentId
        order by p.points desc, lower(p.team.name) asc
        """)
    List<Partecipation> findByTournament_Id(@Param("tournamentId") Long tournamentId);

}
