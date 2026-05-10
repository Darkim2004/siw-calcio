package it.uniroma3.siw.calcio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.repository.MatchRepository;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    private final int POINTS_FOR_WIN = 3;
    private final int POINTS_FOR_DRAW = 1;
    private final int POINTS_FOR_LOSS = 0;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match findById(Long id) {
        return this.matchRepository.findById(id).orElse(null);
    }

    public List<Match> findAll() {
        return (List<Match>) this.matchRepository.findAll();
    }

    public int count() {
        return (int) this.matchRepository.count();
    }

    public List<Match> findAllSortedByDateTime() {
        List<Match> allMatches = this.findAll();
        return allMatches.stream()
                .filter(match -> match.getDateTime() != null)
                .sorted((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()))
                .toList();
    }

    public List<Match> findTodayMatches() {
        LocalDate today = LocalDate.now();
        return this.findAll().stream()
                .filter(match -> match.getDateTime() != null)
                .filter(match -> match.getDateTime().toLocalDate().equals(today))
                .sorted((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()))
                .toList();
    }

    public List<Match> findFirstTodayMatches(int limit) {
        return this.findTodayMatches().stream()
                .limit(limit)
                .toList();
    }

    public Integer findLastMatchPointsByTeamAndTournament(Team team, Tournament tournament) {
        List<Match> matches = this.matchRepository.findByTournamentAndTeam(tournament, team);
        if (matches.isEmpty()) {
            return 0;
        }
        Match lastMatch = matches.stream()
                .filter(match -> match.getDateTime() != null)
                .filter(match -> match.getDateTime().isBefore(LocalDateTime.now()))
                .max((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()))
                .orElse(null);
        if (lastMatch != null) {
            if (lastMatch.getHomeTeam().equals(team)) {
                if (lastMatch.getGoalsHome() > lastMatch.getGoalsAway()) {
                    return POINTS_FOR_WIN;
                } else if (lastMatch.getGoalsHome() == lastMatch.getGoalsAway()) {
                    return POINTS_FOR_DRAW;
                } else {
                    return POINTS_FOR_LOSS;
                }
            } else if (lastMatch.getAwayTeam().equals(team)) {
                if (lastMatch.getGoalsAway() > lastMatch.getGoalsHome()) {
                    return POINTS_FOR_WIN;
                } else if (lastMatch.getGoalsAway() == lastMatch.getGoalsHome()) {
                    return POINTS_FOR_DRAW;
                } else {
                    return POINTS_FOR_LOSS;
                }
            }
        }
        return 0;
    }

}
