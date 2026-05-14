package it.uniroma3.siw.calcio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.repository.PlayerRepository;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

     @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

}
