package it.uniroma3.siw.calcio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.calcio.model.Torneo;
import it.uniroma3.siw.calcio.repository.TorneoRepository;

@Service
public class TournamentService {

    private final TorneoRepository torneoRepository;

    public TournamentService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public Torneo findById(Long id) {
        return this.torneoRepository.findById(id).orElse(null);
    }

    public List<Torneo> findAll() {
        return (List<Torneo>) torneoRepository.findAll();
    }
}
