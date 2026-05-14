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

    private final TeamRepository teamRepository;

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

    @Transactional
    public Team save(Team team) {
        return this.teamRepository.save(team);
    }

    @Transactional(readOnly = true)
    public List<Team> findFirstAlphabeticallyTeams(int limit) {
        List<Team> teams = this.findAll();
        return teams.stream()
                .sorted((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
                .limit(limit)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean hasMoreTeams(int limit) {
        return this.findAll().size() > limit;
    }

    @Transactional
    public void delete(Long id) {
        this.teamRepository.deleteById(id);
    }
}
