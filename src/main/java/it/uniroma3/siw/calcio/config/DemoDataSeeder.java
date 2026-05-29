package it.uniroma3.siw.calcio.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;
import it.uniroma3.siw.calcio.model.Partecipation;
import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.Referee;
import it.uniroma3.siw.calcio.model.RoleSoccer;
import it.uniroma3.siw.calcio.model.RoleWeb;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.model.User;
import it.uniroma3.siw.calcio.repository.CommentRepository;
import it.uniroma3.siw.calcio.repository.MatchRepository;
import it.uniroma3.siw.calcio.repository.PartecipationRepository;
import it.uniroma3.siw.calcio.repository.PlayerRepository;
import it.uniroma3.siw.calcio.repository.RefereeRepository;
import it.uniroma3.siw.calcio.repository.TeamRepository;
import it.uniroma3.siw.calcio.repository.TournamentRepository;
import it.uniroma3.siw.calcio.repository.UserRepository;

@Component
public class DemoDataSeeder implements ApplicationRunner {

    private final boolean seedDemoData;
    private final String adminUsername;
    private final String adminPassword;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final RefereeRepository refereeRepository;
    private final PartecipationRepository partecipationRepository;
    private final MatchRepository matchRepository;
    private final CommentRepository commentRepository;

    public DemoDataSeeder(
            @Value("${app.seed-demo-data:false}") boolean seedDemoData,
            @Value("${app.admin.username:admin}") String adminUsername,
            @Value("${app.admin.password:}") String adminPassword,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            TournamentRepository tournamentRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            RefereeRepository refereeRepository,
            PartecipationRepository partecipationRepository,
            MatchRepository matchRepository,
            CommentRepository commentRepository) {
        this.seedDemoData = seedDemoData;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.refereeRepository = refereeRepository;
        this.partecipationRepository = partecipationRepository;
        this.matchRepository = matchRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean hasAdminPassword = adminPassword != null && !adminPassword.isBlank();
        if (hasAdminPassword) {
            ensureAdminUser();
        }

        if (!seedDemoData) {
            return;
        }
        if (!hasAdminPassword) {
            throw new IllegalStateException("APP_ADMIN_PASSWORD is required when APP_SEED_DEMO_DATA=true");
        }

        if (!domainTablesAreEmpty()) {
            return;
        }

        Map<String, Tournament> tournaments = seedTournaments();
        Map<String, Team> teams = seedTeams();

        seedPlayers(teams);
        seedReferees();
        seedPartecipations(tournaments, teams);
        seedMatches(tournaments, teams);
    }

    private void ensureAdminUser() {
        User admin = userRepository.findByUsername(adminUsername);
        if (admin == null) {
            admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(RoleWeb.ROLE_ADMIN);
            userRepository.save(admin);
            return;
        }

        boolean changed = false;
        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(adminPassword));
            changed = true;
        }
        if (admin.getRole() != RoleWeb.ROLE_ADMIN) {
            admin.setRole(RoleWeb.ROLE_ADMIN);
            changed = true;
        }
        if (changed) {
            userRepository.save(admin);
        }
    }

    private boolean domainTablesAreEmpty() {
        return tournamentRepository.count() == 0
                && teamRepository.count() == 0
                && playerRepository.count() == 0
                && refereeRepository.count() == 0
                && partecipationRepository.count() == 0
                && matchRepository.count() == 0
                && commentRepository.count() == 0;
    }

    private Map<String, Tournament> seedTournaments() {
        Map<String, Tournament> tournaments = new LinkedHashMap<>();
        tournaments.put("Serie A 2025-2026", tournament(
                "Serie A 2025-2026",
                2026,
                "Italian top division football league"));
        tournaments.put("Coppa Italia 2025-2026", tournament(
                "Coppa Italia 2025-2026",
                2026,
                "Italian national cup competition"));
        tournaments.put("Champions League 2025-2026", tournament(
                "Champions League 2025-2026",
                2026,
                "Top European club tournament"));
        tournaments.put("Euro 2024", tournament(
                "Euro 2024",
                2024,
                "European national teams championship"));
        tournaments.put("Spring Tournament 2025-2026", tournament(
                "Spring Tournament 2025-2026",
                2026,
                "Youth teams championship"));
        tournaments.put("Summer Cup 2026", tournament(
                "Summer Cup 2026",
                2026,
                "Summer football tournament"));
        return tournaments;
    }

    private Tournament tournament(String name, int year, String description) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setYear(year);
        tournament.setDescription(description);
        return tournamentRepository.save(tournament);
    }

    private Map<String, Team> seedTeams() {
        Map<String, Team> teams = new LinkedHashMap<>();
        teams.put("AS Roma", team("AS Roma", 1927, "Roma"));
        teams.put("Inter", team("Inter", 1908, "Milano"));
        teams.put("Juventus", team("Juventus", 1897, "Torino"));
        teams.put("Milan", team("Milan", 1899, "Milano"));
        teams.put("Napoli", team("Napoli", 1926, "Napoli"));
        teams.put("Atalanta", team("Atalanta", 1907, "Bergamo"));
        teams.put("Lazio", team("Lazio", 1900, "Roma"));
        return teams;
    }

    private Team team(String name, int foundationYear, String city) {
        Team team = new Team();
        team.setName(name);
        team.setFoundationYear(foundationYear);
        team.setCity(city);
        return teamRepository.save(team);
    }

    private void seedPlayers(Map<String, Team> teams) {
        player("Paulo", "Dybala", "1993-11-15", RoleSoccer.FORWARD, 177, 21, teams.get("AS Roma"));
        player("Lorenzo", "Pellegrini", "1996-06-19", RoleSoccer.MIDFIELDER, 186, 7, teams.get("AS Roma"));
        player("Gianluca", "Mancini", "1996-04-17", RoleSoccer.DEFENDER, 190, 23, teams.get("AS Roma"));
        player("Mile", "Svilar", "1999-08-27", RoleSoccer.GOALKEEPER, 189, 99, teams.get("AS Roma"));

        player("Lautaro", "Martinez", "1997-08-22", RoleSoccer.FORWARD, 174, 10, teams.get("Inter"));
        player("Nicolo", "Barella", "1997-02-07", RoleSoccer.MIDFIELDER, 175, 23, teams.get("Inter"));
        player("Alessandro", "Bastoni", "1999-04-13", RoleSoccer.DEFENDER, 190, 95, teams.get("Inter"));
        player("Yann", "Sommer", "1988-12-17", RoleSoccer.GOALKEEPER, 183, 1, teams.get("Inter"));

        player("Dusan", "Vlahovic", "2000-01-28", RoleSoccer.FORWARD, 190, 9, teams.get("Juventus"));
        player("Manuel", "Locatelli", "1998-01-08", RoleSoccer.MIDFIELDER, 185, 5, teams.get("Juventus"));
        player("Federico", "Gatti", "1998-06-24", RoleSoccer.DEFENDER, 190, 4, teams.get("Juventus"));
        player("Michele", "Di Gregorio", "1997-07-27", RoleSoccer.GOALKEEPER, 187, 29, teams.get("Juventus"));

        player("Rafael", "Leao", "1999-06-10", RoleSoccer.FORWARD, 188, 10, teams.get("Milan"));
        player("Tijjani", "Reijnders", "1998-07-29", RoleSoccer.MIDFIELDER, 185, 14, teams.get("Milan"));
        player("Fikayo", "Tomori", "1997-12-19", RoleSoccer.DEFENDER, 185, 23, teams.get("Milan"));
        player("Mike", "Maignan", "1995-07-03", RoleSoccer.GOALKEEPER, 191, 16, teams.get("Milan"));

        player("Romelu", "Lukaku", "1993-05-13", RoleSoccer.FORWARD, 191, 11, teams.get("Napoli"));
        player("Stanislav", "Lobotka", "1994-11-25", RoleSoccer.MIDFIELDER, 170, 68, teams.get("Napoli"));
        player("Amir", "Rrahmani", "1994-02-24", RoleSoccer.DEFENDER, 192, 13, teams.get("Napoli"));
        player("Alex", "Meret", "1997-03-22", RoleSoccer.GOALKEEPER, 190, 1, teams.get("Napoli"));
    }

    private void player(
            String firstName,
            String lastName,
            String birthDate,
            RoleSoccer role,
            int height,
            int squadNumber,
            Team team) {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setBirthDate(LocalDate.parse(birthDate));
        player.setRole(role);
        player.setPhoto("");
        player.setHeight(height);
        player.setSquadNumber(squadNumber);
        player.setTeam(team);
        playerRepository.save(player);
    }

    private void seedReferees() {
        referee("Daniele", "Orsato", 1001);
        referee("Maria", "Sole Ferrieri Caputi", 1002);
        referee("Fabio", "Maresca", 1003);
    }

    private void referee(String firstName, String lastName, int refereeCode) {
        Referee referee = new Referee();
        referee.setFirstName(firstName);
        referee.setLastName(lastName);
        referee.setRefereeCode(refereeCode);
        refereeRepository.save(referee);
    }

    private void seedPartecipations(Map<String, Tournament> tournaments, Map<String, Team> teams) {
        partecipation(tournaments.get("Serie A 2025-2026"), teams.get("Inter"), 82);
        partecipation(tournaments.get("Serie A 2025-2026"), teams.get("Juventus"), 79);
        partecipation(tournaments.get("Serie A 2025-2026"), teams.get("Milan"), 75);
        partecipation(tournaments.get("Serie A 2025-2026"), teams.get("Napoli"), 71);
        partecipation(tournaments.get("Coppa Italia 2025-2026"), teams.get("AS Roma"), 12);
        partecipation(tournaments.get("Coppa Italia 2025-2026"), teams.get("Inter"), 10);
    }

    private void partecipation(Tournament tournament, Team team, int points) {
        Partecipation partecipation = new Partecipation();
        partecipation.setTournament(tournament);
        partecipation.setTeam(team);
        partecipation.setPoints(points);
        partecipationRepository.save(partecipation);
    }

    private void seedMatches(Map<String, Tournament> tournaments, Map<String, Team> teams) {
        LocalDate today = LocalDate.now();
        match(today.atTime(10, 30), "Stadio Olimpico", 2, 1, MatchState.PLAYED,
                tournaments.get("Serie A 2025-2026"), teams.get("AS Roma"), teams.get("Napoli"));
        match(today.atTime(12, 0), "San Siro", 0, 0, MatchState.SCHEDULED,
                tournaments.get("Serie A 2025-2026"), teams.get("Inter"), teams.get("Milan"));
        match(today.atTime(14, 30), "Allianz Stadium", 1, 1, MatchState.PLAYED,
                tournaments.get("Serie A 2025-2026"), teams.get("Juventus"), teams.get("AS Roma"));
        match(today.atTime(16, 0), "Stadio Diego Armando Maradona", 0, 0, MatchState.ONGOING,
                tournaments.get("Coppa Italia 2025-2026"), teams.get("Napoli"), teams.get("Inter"));
        match(today.atTime(18, 30), "San Siro", 0, 0, MatchState.SCHEDULED,
                tournaments.get("Champions League 2025-2026"), teams.get("Milan"), teams.get("Juventus"));
        match(today.atTime(20, 45), "Stadio Olimpico", 0, 0, MatchState.SCHEDULED,
                tournaments.get("Coppa Italia 2025-2026"), teams.get("AS Roma"), teams.get("Inter"));
        match(today.minusDays(1).atTime(20, 45), "San Siro", 3, 2, MatchState.PLAYED,
                tournaments.get("Serie A 2025-2026"), teams.get("Milan"), teams.get("Napoli"));
        match(today.plusDays(1).atTime(20, 45), "Allianz Stadium", 0, 0, MatchState.SCHEDULED,
                tournaments.get("Serie A 2025-2026"), teams.get("Juventus"), teams.get("Inter"));
    }

    private void match(
            LocalDateTime dateTime,
            String venue,
            int goalsHome,
            int goalsAway,
            MatchState state,
            Tournament tournament,
            Team homeTeam,
            Team awayTeam) {
        Match match = new Match();
        match.setDateTime(dateTime);
        match.setVenue(venue);
        match.setGoalsHome(goalsHome);
        match.setGoalsAway(goalsAway);
        match.setState(state);
        match.setTournament(tournament);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        matchRepository.save(match);
    }
}
