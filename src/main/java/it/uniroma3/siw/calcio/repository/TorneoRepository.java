package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {
}
