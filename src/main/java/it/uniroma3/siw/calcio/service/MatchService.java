package it.uniroma3.siw.calcio.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.repository.MatchRepository;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match findById(Long id) {
        return this.matchRepository.findById(id).orElse(null);
    }

    public List<Match> findAll() {
        return (List<Match>) this.matchRepository.findAll();
    }

    public int count() {
        return (int) this.matchRepository.count();
    }

    public List<Match> findAllSortedByDateTime() {
        List<Match> allMatches = this.findAll();
        return allMatches.stream()
                .filter(match -> match.getDateTime() != null)
                .sorted((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()))
                .toList();
    }

    public List<Match> findTodayMatches() {
        LocalDate today = LocalDate.now();
        return this.findAll().stream()
                .filter(match -> match.getDateTime() != null)
                .filter(match -> match.getDateTime().toLocalDate().equals(today))
                .sorted((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()))
                .toList();
    }

    public List<Match> findFirstTodayMatches(int limit) {
        return this.findTodayMatches().stream()
                .limit(limit)
                .toList();
    }

}
