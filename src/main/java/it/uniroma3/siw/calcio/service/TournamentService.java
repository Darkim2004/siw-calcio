package it.uniroma3.siw.calcio.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
