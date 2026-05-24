package it.uniroma3.siw.calcio.controller;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.calcio.model.Comment;
import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.service.CommentService;
import it.uniroma3.siw.calcio.service.MatchService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class CommentController {

    private static final DateTimeFormatter MATCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
    private static final DateTimeFormatter MATCH_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN);

    private final CommentService commentService;
    private final MatchService matchService;

    public CommentController(CommentService commentService, MatchService matchService) {
        this.commentService = commentService;
        this.matchService = matchService;
    }

    @PostMapping("/matches/{matchId}/comments")
    public String createComment(@PathVariable Long matchId, 
                                @Valid @ModelAttribute("comment") Comment comment,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails author,
                                Model model) {
        if (bindingResult.hasErrors()) {
            addMatchDetailAttributes(matchId, model);
            return "match/detail";
        }

        commentService.createComment(matchId, comment.getText(), author.getUsername());
        return "redirect:/matches/" + matchId;
    }

    @PostMapping("/matches/{matchId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long matchId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetails user) {
        if (commentService.findByIdAndMatchId(commentId, matchId) == null || matchService.findById(matchId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment or Match not found");
        }
        commentService.deleteComment(matchId, commentId, user.getUsername());
        return "redirect:/matches/" + matchId;
    }
    
    @PostMapping("/matches/{matchId}/comments/{commentId}/update")
    public String updateComment(@PathVariable Long matchId,
                                @PathVariable Long commentId,
                                @Valid @ModelAttribute("comment") Comment comment,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails user,
                                Model model) {
        if (commentService.findByIdAndMatchId(commentId, matchId) == null || matchService.findById(matchId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment or Match not found");
        }
        if (bindingResult.hasErrors()) {
            addMatchDetailAttributes(matchId, model);
            return "match/detail";
        }

        commentService.updateComment(matchId, commentId, comment.getText(), user.getUsername());
        return "redirect:/matches/" + matchId;
    }

    private void addMatchDetailAttributes(Long matchId, Model model) {
        Match match = matchService.findById(matchId);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found");
        }

        model.addAttribute("match", match);
        model.addAttribute("comments", commentService.findByMatchId(matchId));
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
    }
}
