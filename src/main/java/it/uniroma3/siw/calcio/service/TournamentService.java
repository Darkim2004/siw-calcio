package it.uniroma3.siw.calcio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.repository.TournamentRepository;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament findById(Long id) {
        return this.tournamentRepository.findById(id).orElse(null);
    }

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
}
