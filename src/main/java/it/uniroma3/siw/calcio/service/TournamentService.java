package it.uniroma3.siw.calcio.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Partecipation;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.repository.TournamentRepository;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final MatchService matchService;

    public TournamentService(TournamentRepository tournamentRepository, MatchService matchService) {
        this.tournamentRepository = tournamentRepository;
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
        tournamentRepository.delete(tournament);
    }

    // @Transactional(readOnly = true)
    // public List<Team> findTeamsByTournamentId(Long id) {
    //     Tournament tournament = this.findById(id);
    //     if (tournament != null) {
    //         return tournament.getPartecipations().stream()
    //                 .map(partecipation -> partecipation.getTeam())
    //                 .toList();
    //     }
    //     return null;
    // }

    @Transactional(readOnly = true)
    public List<Tournament> findFirstAlphabeticallyTournaments(int limit) {
        List<Tournament> tournaments = this.findAll();
        return tournaments.stream()
                .sorted((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
                .limit(limit)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean hasMoreTournaments(int limit) {
        return this.findAll().size() > limit;
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> findTeamsWithPointsByTournamentId(Long id) {
        Tournament tournament = this.findById(id);
        if (tournament != null) {
            return tournament.getPartecipations().stream()
                    .sorted((p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()))
                    .map(partecipation -> new Object[]{partecipation.getTeam(), partecipation.getPoints()})
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
                            partecipation -> (Integer) matchService.findLastMatchPointsByTeamAndTournament(partecipation.getTeam(), tournament)
                    ));
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
        Partecipation partecipation = new Partecipation();
        partecipation.setTournament(tournament);
        partecipation.setTeam(team);
        partecipation.setPoints(0);
        tournament.getPartecipations().add(partecipation);
        this.save(tournament);
    }

    @Transactional
    public void deleteTeamFromTournament(Long tournamentId, Team team) {
        Tournament tournament = this.findById(tournamentId);
        if (tournament != null && team != null && this.findPartecipationByTeamAndTournamentId(team, tournamentId) != null) {
            Partecipation partecipation = this.findPartecipationByTeamAndTournamentId(team, tournamentId);
            tournament.getPartecipations().remove(partecipation);
            this.save(tournament);
        }
    }
}
