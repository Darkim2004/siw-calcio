package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Referee;

public interface RefereeRepository extends CrudRepository<Referee, Long> {

    @Query("""
            SELECT r
            FROM Referee r
            ORDER BY lower(r.lastName), lower(r.firstName), r.refereeCode, r.id
            """)
    List<Referee> findAllSortedByName();
}
