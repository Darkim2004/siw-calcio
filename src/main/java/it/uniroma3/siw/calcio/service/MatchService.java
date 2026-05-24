package it.uniroma3.siw.calcio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.repository.MatchRepository;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly = true)
    public Match findById(Long id) {
        return this.matchRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Match> findAll() {
        return (List<Match>) this.matchRepository.findAll();
    }

    @Transactional(readOnly = true)
    public int count() {
        return (int) this.matchRepository.count();
    }

    @Transactional
    public Match save(Match match) {
        return this.matchRepository.save(match);
    }

    @Transactional
    public void delete(Match match) {
        this.matchRepository.delete(match);
    }


    @Transactional(readOnly = true)
    public List<Match> findAllSortedByDateTime() {
        return matchRepository.findAllSortedByDateTime();
    }

    @Transactional(readOnly = true)
    public List<Match> findTodayMatches() {
        LocalDate today = LocalDate.now();
        LocalDateTime inizioGiorno = today.atStartOfDay();
        LocalDateTime fineGiorno = today.plusDays(1).atStartOfDay();

        return this.matchRepository.findTodayMatches(inizioGiorno, fineGiorno);
    }

    @Transactional(readOnly = true)
    public List<Match> findFirstTodayMatches(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        LocalDate today = LocalDate.now();
        LocalDateTime inizioGiorno = today.atStartOfDay();
        LocalDateTime fineGiorno = today.plusDays(1).atStartOfDay();

        return this.matchRepository.findFirstTodayMatches(limit, inizioGiorno, fineGiorno);
    }

}
