package it.uniroma3.siw.calcio.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.RoleSoccer;
import it.uniroma3.siw.calcio.repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<Player> findAllSortedByName() {
        return playerRepository.findAllSortedByName();
    }

    public Map<String, List<Player>> groupPlayersByRole(List<Player> players) {
        Map<String, List<Player>> playersByRole = new LinkedHashMap<>();
        playersByRole.put("Forwards", filterPlayersByRole(players, RoleSoccer.FORWARD));
        playersByRole.put("Midfielders", filterPlayersByRole(players, RoleSoccer.MIDFIELDER));
        playersByRole.put("Defenders", filterPlayersByRole(players, RoleSoccer.DEFENDER));
        playersByRole.put("Goalkeepers", filterPlayersByRole(players, RoleSoccer.GOALKEEPER));
        playersByRole.put("Without role", players.stream()
                .filter(player -> player.getRole() == null)
                .toList());
        return playersByRole;
    }

    @Transactional(readOnly = true)
    public Player findById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Transactional
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        Player player = findById(id);
        if (player != null) {
            delete(player);
        }
    }

    private List<Player> filterPlayersByRole(List<Player> players, RoleSoccer role) {
        return players.stream()
                .filter(player -> role.equals(player.getRole()))
                .toList();
    }
}
