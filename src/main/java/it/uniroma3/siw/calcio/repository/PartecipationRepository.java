package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Partecipation;

public interface PartecipationRepository extends CrudRepository<Partecipation, Long> {

    boolean existsByTournament_IdAndTeam_Id(Long tournamentId, Long teamId);

    Partecipation findByTournament_IdAndTeam_Id(Long tournamentId, Long teamId);

}
