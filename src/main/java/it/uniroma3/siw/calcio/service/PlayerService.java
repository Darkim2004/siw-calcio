package it.uniroma3.siw.calcio.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.RoleSoccer;
import it.uniroma3.siw.calcio.repository.PlayerRepository;

@Service
public class PlayerService {

    private static final String PLAYER_PHOTO_FOLDER = "giocatori";

    private final PlayerRepository playerRepository;
    private final ImageStorageService imageStorageService;

    public PlayerService(PlayerRepository playerRepository, ImageStorageService imageStorageService) {
        this.playerRepository = playerRepository;
        this.imageStorageService = imageStorageService;
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
    public Player save(Player player, MultipartFile photoFile) {
        updatePhotoIfPresent(player, photoFile);
        return playerRepository.save(player);
    }

    @Transactional
    public void delete(Player player) {
        imageStorageService.delete(player.getPhoto());
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

    private void updatePhotoIfPresent(Player player, MultipartFile photoFile) {
        if (photoFile == null || photoFile.isEmpty()) {
            return;
        }

        String oldPhoto = player.getPhoto();
        String newPhoto = imageStorageService.store(photoFile, PLAYER_PHOTO_FOLDER);
        player.setPhoto(newPhoto);
        imageStorageService.delete(oldPhoto);
    }
}
