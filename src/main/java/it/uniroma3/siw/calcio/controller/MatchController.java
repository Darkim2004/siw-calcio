package it.uniroma3.siw.calcio.controller;

import it.uniroma3.siw.calcio.service.CommentService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Comment;
import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.RefereeService;
import it.uniroma3.siw.calcio.service.TeamService;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;

@Controller
public class MatchController {

    private final CommentService commentService;
    private static final DateTimeFormatter MATCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
    private static final DateTimeFormatter MATCH_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN);

    private final MatchService matchService;
    private final TeamService teamService;
    private final TournamentService tournamentService;
    private final RefereeService refereeService;

    public MatchController(MatchService matchService, TeamService teamService, TournamentService tournamentService, RefereeService refereeService, CommentService commentService) {
        this.matchService = matchService;
        this.teamService = teamService;
        this.tournamentService = tournamentService;
        this.refereeService = refereeService;
        this.commentService = commentService;
    }

    @GetMapping("/matches")
    public String getMatches(Model model) {
        List<Match> matches = matchService.findAllSortedByDateTime();
        model.addAttribute("matches", matches);
        model.addAttribute("matchCenterMatches", buildMatchCenterMatches(matches));
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
        return "match/list";
    }

    @GetMapping("/matches/{id}")
    public String getMatch(@PathVariable Long id, Model model) {
        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("match", match);
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", commentService.findByMatchId(id));
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
        return "match/detail";
    }

    @GetMapping("/admin/matches/new")
    public String getNewMatchForm(Model model) {
        model.addAttribute("match", new Match());
        addMatchFormAttributes(model);
        return "admin/match/form";
    }

    @PostMapping("/admin/matches")
    public String createMatch(@Valid @ModelAttribute("match") Match match,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long homeTeamId,
                              @RequestParam(required = false) Long awayTeamId,
                              @RequestParam(required = false) Long tournamentId,
                              @RequestParam(required = false) Long refereeId,
                              Model model) {
        setMatchRelations(match, homeTeamId, awayTeamId, tournamentId, refereeId);
        validateMatchRelations(match, refereeId, bindingResult);

        if (bindingResult.hasErrors()) {
            addMatchFormAttributes(model);
            return "admin/match/form";
        }

        Match savedMatch = matchService.save(match);
        recalculateStandings(savedMatch);
        return "redirect:/matches";
    }

    @GetMapping("/admin/matches/{id}/edit")
    public String getEditMatchForm(@PathVariable Long id, Model model) {
        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("match", match);
        addMatchFormAttributes(model);
        return "admin/match/edit-form";
    }

    @PostMapping("/admin/matches/{id}")
    public String updateMatch(@PathVariable Long id,
                              @Valid @ModelAttribute("match") Match formMatch,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long homeTeamId,
                              @RequestParam(required = false) Long awayTeamId,
                              @RequestParam(required = false) Long tournamentId,
                              @RequestParam(required = false) Long refereeId,
                              Model model) {
        formMatch.setId(id);
        setMatchRelations(formMatch, homeTeamId, awayTeamId, tournamentId, refereeId);
        validateMatchRelations(formMatch, refereeId, bindingResult);

        if (bindingResult.hasErrors()) {
            addMatchFormAttributes(model);
            return "admin/match/edit-form";
        }

        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long previousTournamentId = getTournamentId(match);

        match.setHomeTeam(formMatch.getHomeTeam());
        match.setAwayTeam(formMatch.getAwayTeam());
        match.setDateTime(formMatch.getDateTime());
        match.setVenue(formMatch.getVenue());
        match.setState(formMatch.getState());
        match.setTournament(formMatch.getTournament());
        match.setGoalsHome(formMatch.getGoalsHome());
        match.setGoalsAway(formMatch.getGoalsAway());
        match.setReferee(formMatch.getReferee());

        Match savedMatch = matchService.save(match);
        recalculateStandings(previousTournamentId, savedMatch);

        return "redirect:/matches";
    }

    @PostMapping("/admin/matches/{id}/delete")
    public String deleteMatch(@PathVariable Long id) {
        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Long tournamentId = getTournamentId(match);

        matchService.delete(match);
        tournamentService.recalculateStandings(tournamentId);
        return "redirect:/matches";
    }

    private void addMatchFormAttributes(Model model) {
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("tournaments", tournamentService.findAll());
        model.addAttribute("referees", refereeService.findAllSortedByName());
        model.addAttribute("matchStates", MatchState.values());
    }

    private void setMatchRelations(Match match, Long homeTeamId, Long awayTeamId, Long tournamentId, Long refereeId) {
        match.setHomeTeam(homeTeamId == null ? null : teamService.findById(homeTeamId));
        match.setAwayTeam(awayTeamId == null ? null : teamService.findById(awayTeamId));
        match.setTournament(tournamentId == null ? null : tournamentService.findById(tournamentId));
        match.setReferee(refereeId == null ? null : refereeService.findById(refereeId));
    }

    private void validateMatchRelations(Match match, Long refereeId, BindingResult bindingResult) {
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        if (homeTeam == null || awayTeam == null || match.getTournament() == null) {
            bindingResult.reject("match.requiredData");
        }
        if (homeTeam != null && awayTeam != null && homeTeam.getId().equals(awayTeam.getId())) {
            bindingResult.reject("match.sameTeams");
        }
        if (refereeId != null && match.getReferee() == null) {
            bindingResult.reject("match.invalidReferee");
        }
    }

    private void recalculateStandings(Match match) {
        tournamentService.recalculateStandings(getTournamentId(match));
    }

    private void recalculateStandings(Long previousTournamentId, Match match) {
        Long currentTournamentId = getTournamentId(match);
        if (previousTournamentId != null && !previousTournamentId.equals(currentTournamentId)) {
            tournamentService.recalculateStandings(previousTournamentId);
        }
        tournamentService.recalculateStandings(currentTournamentId);
    }

    private Long getTournamentId(Match match) {
        if (match == null || match.getTournament() == null) {
            return null;
        }
        return match.getTournament().getId();
    }

    // Converte le entity Match in DTO(Data Transfer Object) leggeri, pronti per essere serializzati nel match center React.
    private List<MatchCenterMatch> buildMatchCenterMatches(List<Match> matches) {
        return matches.stream()
                .map(match -> new MatchCenterMatch(
                        match.getId(),
                        match.getDateTime() == null ? "" : match.getDateTime().format(MATCH_DATE_FORMATTER),
                        match.getDateTime() == null ? "" : match.getDateTime().format(MATCH_TIME_FORMATTER),
                        match.getDateTime() == null ? "" : match.getDateTime().toString(),
                        match.getState() == null ? "SCHEDULED" : match.getState().name(),
                        match.getVenue() == null ? "" : match.getVenue(),
                        match.getTournament() == null ? null : match.getTournament().getId(),
                        match.getTournament() == null ? "" : match.getTournament().getName(),
                        refereeName(match),
                        teamSummary(match.getHomeTeam()),
                        teamSummary(match.getAwayTeam()),
                        match.getGoalsHome(),
                        match.getGoalsAway()))
                .toList();
    }

    private TeamSummary teamSummary(Team team) {
        if (team == null) {
            return new TeamSummary(null, "", "/images/SiwCalcio_logo.png");
        }
        String logo = team.getLogo() == null || team.getLogo().isBlank()
                ? "/images/SiwCalcio_logo.png"
                : team.getLogo();
        return new TeamSummary(team.getId(), team.getName(), logo);
    }

    private String refereeName(Match match) {
        if (match == null || match.getReferee() == null) {
            return "";
        }
        return match.getReferee().getFirstName() + " " + match.getReferee().getLastName();
    }

    public record MatchCenterMatch(
            Long id,
            String dateLabel,
            String timeLabel,
            String dateTime,
            String state,
            String venue,
            Long tournamentId,
            String tournamentName,
            String refereeName,
            TeamSummary homeTeam,
            TeamSummary awayTeam,
            int goalsHome,
            int goalsAway) {
    }

    public record TeamSummary(Long id, String name, String logo) {
    }
}
