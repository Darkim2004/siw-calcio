package it.uniroma3.siw.calcio.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.calcio.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}
