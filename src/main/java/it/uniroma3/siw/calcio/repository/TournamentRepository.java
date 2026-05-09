package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Tournament;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {
}
