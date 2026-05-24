package it.uniroma3.siw.calcio.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;
import it.uniroma3.siw.calcio.model.Partecipation;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.repository.MatchRepository;
import it.uniroma3.siw.calcio.repository.PartecipationRepository;
import it.uniroma3.siw.calcio.repository.TournamentRepository;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PartecipationRepository partecipationRepository;
    private final MatchRepository matchRepository;

    private static final int POINTS_FOR_WIN = 3;
    private static final int POINTS_FOR_DRAW = 1;
    private static final int POINTS_FOR_LOSS = 0;

    public TournamentService(TournamentRepository tournamentRepository, PartecipationRepository partecipationRepository,
            MatchRepository matchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.partecipationRepository = partecipationRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public Tournament findById(Long id) {
        return this.tournamentRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Tournament> findAll() {
        return (List<Tournament>) tournamentRepository.findAll();
    }

    @Transactional
    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    @Transactional
    public void delete(Tournament tournament) {
        if (tournament == null || tournament.getId() == null) {
            return;
        }

        // Removes the tournament reference from matches
        List<Match> matches = matchRepository.findByTournament_Id(tournament.getId());
        for (Match match : matches) {
            match.setTournament(null);
        }
        matchRepository.saveAll(matches);

        // Deletes all partecipations related to the tournament
        List<Partecipation> partecipations = partecipationRepository.findByTournament_Id(tournament.getId());
        partecipationRepository.deleteAll(partecipations);

        tournamentRepository.delete(tournament);
    }

    @Transactional(readOnly = true)
    public List<Tournament> findFirstAlphabeticallyTournaments(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        return tournamentRepository.findFirstAlphabeticallyTournaments(limit);
    }

    @Transactional(readOnly = true)
    public boolean hasMoreTournaments(int limit) {
        return tournamentRepository.count() > limit;
    }

    @Transactional(readOnly = true)
    public List<Partecipation> findPartecipationsByTournamentId(Long id) {
        if (id == null) {
            return List.of();
        }

        return partecipationRepository.findByTournament_Id(id);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findTeamsWithPointsByTournamentId(Long id) {
        Tournament tournament = this.findById(id);
        if (tournament != null) {
            return partecipationRepository.findByTournament_Id(id).stream()
                    .map(partecipation -> new Object[] { partecipation.getTeam(), partecipation.getPoints() })
                    .toList();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Map<Team, Integer> findTeamsWithLastPointsByTournamentId(Long id) {
        if (id == null) {
            return Map.of();
        }

        List<Partecipation> partecipations = partecipationRepository.findByTournament_Id(id);
        List<Match> playedMatches = matchRepository.findByTournamentIdAndStateWithTeamsOrderByDateTimeDesc(id,
                MatchState.PLAYED);

        Map<Long, Integer> lastPointsByTeamId = new HashMap<>();
        for (Match match : playedMatches) {
            Team homeTeam = match.getHomeTeam();
            Team awayTeam = match.getAwayTeam();
            if (homeTeam == null || awayTeam == null || homeTeam.getId() == null || awayTeam.getId() == null) {
                continue;
            }

            lastPointsByTeamId.putIfAbsent(homeTeam.getId(),
                    calculateMatchPoints(match.getGoalsHome(), match.getGoalsAway()));
            lastPointsByTeamId.putIfAbsent(awayTeam.getId(),
                    calculateMatchPoints(match.getGoalsAway(), match.getGoalsHome()));
        }

        Map<Team, Integer> teamsWithLastPoints = new LinkedHashMap<>();
        for (Partecipation partecipation : partecipations) {
            Team team = partecipation.getTeam();
            if (team != null && team.getId() != null) {
                teamsWithLastPoints.put(team, lastPointsByTeamId.getOrDefault(team.getId(), POINTS_FOR_LOSS));
            }
        }

        return teamsWithLastPoints;
    }

    private int calculateMatchPoints(int goalsFor, int goalsAgainst) {
        if (goalsFor > goalsAgainst) {
            return POINTS_FOR_WIN;
        }
        if (goalsFor == goalsAgainst) {
            return POINTS_FOR_DRAW;
        }
        return POINTS_FOR_LOSS;
    }

    @Transactional
    public void recalculateStandings(Long tournamentId) {
        if (tournamentId == null) {
            return;
        }

        List<Partecipation> partecipations = partecipationRepository.findByTournament_Id(tournamentId);
        Map<Long, Partecipation> partecipationByTeamId = partecipations.stream()
                .filter(partecipation -> partecipation.getTeam() != null && partecipation.getTeam().getId() != null)
                .peek(partecipation -> partecipation.setPoints(0))
                .collect(Collectors.toMap(
                        partecipation -> partecipation.getTeam().getId(),
                        Function.identity(),
                        (existing, duplicate) -> existing));

        List<Match> playedMatches = matchRepository.findByTournamentIdAndStateWithTeamsOrderByDateTimeDesc(
                tournamentId, MatchState.PLAYED);

        for (Match match : playedMatches) {
            Team homeTeam = match.getHomeTeam();
            Team awayTeam = match.getAwayTeam();
            if (homeTeam == null || awayTeam == null || homeTeam.getId() == null || awayTeam.getId() == null) {
                continue;
            }

            addPoints(partecipationByTeamId.get(homeTeam.getId()),
                    calculateMatchPoints(match.getGoalsHome(), match.getGoalsAway()));
            addPoints(partecipationByTeamId.get(awayTeam.getId()),
                    calculateMatchPoints(match.getGoalsAway(), match.getGoalsHome()));
        }

        partecipationRepository.saveAll(partecipations);
    }

    private void addPoints(Partecipation partecipation, int points) {
        if (partecipation != null) {
            partecipation.setPoints(partecipation.getPoints() + points);
        }
    }

    @Transactional(readOnly = true)
    public Partecipation findPartecipationByTeamAndTournamentId(Team team, Long tournamentId) {
        if (team == null || team.getId() == null || tournamentId == null) {
            return null;
        }
        return partecipationRepository.findByTournament_IdAndTeam_Id(tournamentId, team.getId());
    }

    @Transactional
    public void addTeamToTournament(Long tournamentId, Team team) {
        Tournament tournament = this.findById(tournamentId);

        if (tournament == null || team == null || team.getId() == null) {
            return;
        }

        if (partecipationRepository.existsByTournament_IdAndTeam_Id(tournamentId, team.getId())) {
            return;
        }

        Partecipation partecipation = new Partecipation();
        partecipation.setTournament(tournament);
        partecipation.setTeam(team);
        partecipation.setPoints(0);

        partecipationRepository.save(partecipation);
        recalculateStandings(tournamentId);
    }

    @Transactional
    public void deleteTeamFromTournament(Long tournamentId, Team team) {
        Tournament tournament = this.findById(tournamentId);
        if (tournament == null || team == null || team.getId() == null) {
            return;
        }
        Partecipation partecipation = partecipationRepository.findByTournament_IdAndTeam_Id(tournamentId, team.getId());
        if (partecipation != null) {
            partecipationRepository.delete(partecipation);
            recalculateStandings(tournamentId);
        }
    }
}
