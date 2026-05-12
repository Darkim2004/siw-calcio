package it.uniroma3.siw.calcio.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.repository.TeamRepository;

@Service
public class TeamService {

    TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public Team findById(Long id) {
        return this.teamRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Team> findAll() {
        return (List<Team>) this.teamRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayersByTeamId(Long id) {
        Team team = this.teamRepository.findById(id).orElse(null);
        if (team == null) {
            return Collections.emptyList();
        }
        return team.getPlayers();
    }
}
