package it.uniroma3.siw.calcio.service;

import it.uniroma3.siw.calcio.repository.MatchRepository;
import it.uniroma3.siw.calcio.repository.PlayerRepository;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.repository.TeamRepository;

@Service
public class TeamService {

    private static final String TEAM_LOGO_FOLDER = "squadre";

    private final ImageStorageService imageStorageService;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, MatchRepository matchRepository, ImageStorageService imageStorageService) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.imageStorageService = imageStorageService;
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

    @Transactional
    public Team create(Team team, MultipartFile logoFile) {
        updateLogoIfPresent(team, logoFile);
        return this.teamRepository.save(team);
    }

    @Transactional
    public Team update(Long id, Team formTeam, MultipartFile logoFile) {
        Team team = this.findById(id);
        if (team == null) {
            return null;
        }

        team.setName(formTeam.getName());
        team.setFoundationYear(formTeam.getFoundationYear());
        team.setCity(formTeam.getCity());
        updateLogoIfPresent(team, logoFile);

        return this.teamRepository.save(team);
    }

    @Transactional(readOnly = true)
    public List<Team> findFirstAlphabeticallyTeams(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return this.teamRepository.findFirstAlphabeticallyTeams(limit);
    }

    @Transactional(readOnly = true)
    public boolean hasMoreTeams(int limit) {
        return teamRepository.count() > limit;
    }

    @Transactional
    public void delete(Long id) {
        if(id == null) {
            return;
        }
        Team team = this.findById(id);
        if (team == null) {
            return;
        }

        // Removes the team from the players
        List<Player> players = team.getPlayers();
        for (Player player : players) {
            player.setTeam(null);
            this.playerRepository.save(player);
        }

        // Removes matches of the team
        List<Match> matches = this.matchRepository.findByHomeTeam_IdOrAwayTeam_Id(id,id);
        for (Match match : matches) {
            this.matchRepository.delete(match);
        }

        this.imageStorageService.delete(team.getLogo());
        this.teamRepository.deleteById(id);
    }

    private void updateLogoIfPresent(Team team, MultipartFile logoFile) {
        if (logoFile == null || logoFile.isEmpty()) {
            return;
        }

        String oldLogo = team.getLogo();
        String newLogo = this.imageStorageService.store(logoFile, TEAM_LOGO_FOLDER);
        team.setLogo(newLogo);
        this.imageStorageService.delete(oldLogo);
    }
}
