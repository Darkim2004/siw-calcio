package it.uniroma3.siw.calcio.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
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
    private final MatchService matchService;

    public TournamentService(TournamentRepository tournamentRepository, PartecipationRepository partecipationRepository,
            MatchRepository matchRepository, MatchService matchService) {
        this.tournamentRepository = tournamentRepository;
        this.partecipationRepository = partecipationRepository;
        this.matchRepository = matchRepository;
        this.matchService = matchService;
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

        return partecipationRepository.findByTournament_Id(id).stream()
                .sorted((p1, p2) -> {
                    int pointsCompare = Integer.compare(p2.getPoints(), p1.getPoints());
                    if (pointsCompare != 0) {
                        return pointsCompare;
                    }

                    String teamName1 = p1.getTeam() == null ? "" : p1.getTeam().getName();
                    String teamName2 = p2.getTeam() == null ? "" : p2.getTeam().getName();
                    return teamName1.compareToIgnoreCase(teamName2);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Object[]> findTeamsWithPointsByTournamentId(Long id) {
        Tournament tournament = this.findById(id);
        if (tournament != null) {
            return tournament.getPartecipations().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()))
                    .map(partecipation -> new Object[] { partecipation.getTeam(), partecipation.getPoints() })
                    .toList();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Map<Team, Integer> findTeamsWithLastPointsByTournamentId(Long id) {
        Tournament tournament = this.findById(id);
        if (tournament != null) {
            return tournament.getPartecipations().stream()
                    .collect(Collectors.toMap(
                            partecipation -> (Team) partecipation.getTeam(),
                            partecipation -> (Integer) matchService
                                    .findLastMatchPointsByTeamAndTournament(partecipation.getTeam(), tournament)));
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Partecipation findPartecipationByTeamAndTournamentId(Team team, Long tournamentId) {
        Tournament tournament = this.findById(tournamentId);
        if (tournament != null && team != null && tournament.getPartecipations() != null) {
            return tournament.getPartecipations().stream()
                    .filter(partecipation -> team.equals(partecipation.getTeam()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
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
        }
    }
}
