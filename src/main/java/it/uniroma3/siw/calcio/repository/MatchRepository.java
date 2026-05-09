package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Match;

public interface MatchRepository extends CrudRepository<Match, Long> {

}
