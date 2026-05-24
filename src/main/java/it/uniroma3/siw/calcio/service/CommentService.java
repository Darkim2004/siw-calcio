package it.uniroma3.siw.calcio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.exception.AccessDeniedException;
import it.uniroma3.siw.calcio.model.Comment;
import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.User;
import it.uniroma3.siw.calcio.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MatchService matchService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, MatchService matchService, UserService userService) {
        this.commentRepository = commentRepository;
        this.matchService = matchService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return this.commentRepository.findById(id).orElse(null);
    }

    @Transactional
    public void createComment(Long matchId, String commentText, String authorUsername) {
        User author = userService.findByUsername(authorUsername);
        Match match = matchService.findById(matchId);
        if (author == null || match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author or Match not found");
        }

        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setAuthor(author);
        comment.setMatch(match);
        comment.setCreationDateTime(LocalDateTime.now());
        this.commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long matchId, Long commentId, String authorUsername) {
        Comment comment = this.findByIdAndMatchId(commentId, matchId);
        User author = this.userService.findByUsername(authorUsername);
        if (comment == null ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment or Match not found");
        }
        else if(!comment.getAuthor().equals(author)){
                throw new AccessDeniedException();
        }
        else {
            
            this.commentRepository.delete(comment);
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> findByMatchId(Long id) {
        return this.commentRepository.findByMatchId(id);
    }

    @Transactional
    public void updateComment(Long matchId, Long commentId, String newText, String authorUsername) {
        Comment comment = this.findByIdAndMatchId(commentId, matchId);
        User author = this.userService.findByUsername(authorUsername);
        if(comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment or Match not found");
        }
        else if (!comment.getAuthor().equals(author)) {
            throw new AccessDeniedException();
        }
        else {
            comment.setText(newText);
            this.commentRepository.save(comment);
        }
    }

    @Transactional(readOnly = true)
    public Comment findByIdAndMatchId(Long commentId, Long matchId) {
        return this.commentRepository.findByIdAndMatch_Id(commentId, matchId).orElse(null);
    }


}
