package it.uniroma3.siw.calcio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Referee;
import it.uniroma3.siw.calcio.repository.RefereeRepository;

@Service
public class RefereeService {

    private final RefereeRepository refereeRepository;

    public RefereeService(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }

    @Transactional(readOnly = true)
    public Referee findById(Long id) {
        return this.refereeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Referee> findAll() {
        return (List<Referee>) this.refereeRepository.findAll();
    }
}
