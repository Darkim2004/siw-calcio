package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Query("""
            SELECT p
            FROM Player p
            LEFT JOIN FETCH p.team
            ORDER BY lower(p.lastName), lower(p.firstName), p.id
            """)
    List<Player> findAllSortedByName();
}
