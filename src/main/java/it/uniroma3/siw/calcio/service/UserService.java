package it.uniroma3.siw.calcio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.calcio.model.User;
import it.uniroma3.siw.calcio.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
