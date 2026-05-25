package it.uniroma3.siw.calcio.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Referee;
import it.uniroma3.siw.calcio.repository.MatchRepository;
import it.uniroma3.siw.calcio.repository.RefereeRepository;

@Service
public class RefereeService {

    private final RefereeRepository refereeRepository;
    private final MatchRepository matchRepository;

    public RefereeService(RefereeRepository refereeRepository, MatchRepository matchRepository) {
        this.refereeRepository = refereeRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public Referee findById(Long id) {
        return this.refereeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Referee> findAllSortedByName() {
        return this.refereeRepository.findAllSortedByName();
    }

    @Transactional
    public Referee save(Referee referee) {
        return this.refereeRepository.save(referee);
    }

    @Transactional
    public void delete(Referee referee) {
        if (referee == null || referee.getId() == null) {
            return;
        }

        List<Match> matches = matchRepository.findByReferee_Id(referee.getId());
        for (Match match : matches) {
            match.setReferee(null);
        }
        matchRepository.saveAll(matches);
        refereeRepository.delete(referee);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            return;
        }

        delete(findById(id));
    }
}
