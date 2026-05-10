package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findByTournamentAndTeam(Tournament tournament, Team team);

}
