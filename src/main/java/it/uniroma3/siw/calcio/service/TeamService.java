package it.uniroma3.siw.calcio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.repository.TeamRepository;

@Service
public class TeamService {

    TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team findById(Long id) {
        return this.teamRepository.findById(id).orElse(null);
    }
}
