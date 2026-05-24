package it.uniroma3.siw.calcio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("""
        SELECT c 
        FROM Comment c 
        WHERE c.match.id = :id
        ORDER BY c.creationDateTime DESC
    """)
    List<Comment> findByMatchId(@Param("id") Long id);

    Optional<Comment> findByIdAndMatch_Id(Long id, Long matchId);

}
