package it.uniroma3.siw.calcio.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
