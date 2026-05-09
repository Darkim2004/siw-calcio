package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {

}
