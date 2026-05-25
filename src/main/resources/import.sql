-- Test data for tournaments
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Serie A 2025-2026', 2026, 'Italian top division football league');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Coppa Italia 2025-2026', 2026, 'Italian national cup competition');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Champions League 2025-2026', 2026, 'Top European club tournament');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Euro 2024', 2024, 'European national teams championship');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Spring Tournament 2025-2026', 2026, 'Youth teams championship');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Summer Cup 2026', 2026, 'Summer football tournament');

-- Test data for teams
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'AS Roma', 1927, 'Roma');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Inter', 1908, 'Milano');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Juventus', 1897, 'Torino');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Milan', 1899, 'Milano');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Napoli', 1926, 'Napoli');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Atalanta', 1907, 'Bergamo');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Lazio', 1900, 'Roma');

-- Test data for players
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Paulo', 'Dybala', '1993-11-15', 'FORWARD', '', 177, 21, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Lorenzo', 'Pellegrini', '1996-06-19', 'MIDFIELDER', '', 186, 7, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Gianluca', 'Mancini', '1996-04-17', 'DEFENDER', '', 190, 23, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Mile', 'Svilar', '1999-08-27', 'GOALKEEPER', '', 189, 99, (SELECT id FROM team WHERE name = 'AS Roma'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Lautaro', 'Martinez', '1997-08-22', 'FORWARD', '', 174, 10, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Nicolo', 'Barella', '1997-02-07', 'MIDFIELDER', '', 175, 23, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Alessandro', 'Bastoni', '1999-04-13', 'DEFENDER', '', 190, 95, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Yann', 'Sommer', '1988-12-17', 'GOALKEEPER', '', 183, 1, (SELECT id FROM team WHERE name = 'Inter'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Dusan', 'Vlahovic', '2000-01-28', 'FORWARD', '', 190, 9, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Manuel', 'Locatelli', '1998-01-08', 'MIDFIELDER', '', 185, 5, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Federico', 'Gatti', '1998-06-24', 'DEFENDER', '', 190, 4, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Michele', 'Di Gregorio', '1997-07-27', 'GOALKEEPER', '', 187, 29, (SELECT id FROM team WHERE name = 'Juventus'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Rafael', 'Leao', '1999-06-10', 'FORWARD', '', 188, 10, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Tijjani', 'Reijnders', '1998-07-29', 'MIDFIELDER', '', 185, 14, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Fikayo', 'Tomori', '1997-12-19', 'DEFENDER', '', 185, 23, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Mike', 'Maignan', '1995-07-03', 'GOALKEEPER', '', 191, 16, (SELECT id FROM team WHERE name = 'Milan'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Romelu', 'Lukaku', '1993-05-13', 'FORWARD', '', 191, 11, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Stanislav', 'Lobotka', '1994-11-25', 'MIDFIELDER', '', 170, 68, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Amir', 'Rrahmani', '1994-02-24', 'DEFENDER', '', 192, 13, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, squad_number, team_id) VALUES (nextval('player_seq'), 'Alex', 'Meret', '1997-03-22', 'GOALKEEPER', '', 190, 1, (SELECT id FROM team WHERE name = 'Napoli'));

-- Test data for referees
INSERT INTO referee (id, first_name, last_name, referee_code) VALUES (nextval('referee_seq'), 'Daniele', 'Orsato', 1001);
INSERT INTO referee (id, first_name, last_name, referee_code) VALUES (nextval('referee_seq'), 'Maria', 'Sole Ferrieri Caputi', 1002);
INSERT INTO referee (id, first_name, last_name, referee_code) VALUES (nextval('referee_seq'), 'Fabio', 'Maresca', 1003);

-- Test data for participations
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 82);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Juventus'), 79);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Milan'), 75);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Napoli'), 71);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'AS Roma'), 12);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 10);

-- Test data for matches
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '10:30', 'Stadio Olimpico', 2, 1, 'PLAYED', (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'AS Roma'), (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '12:00', 'San Siro', 0, 0, 'SCHEDULED', (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '14:30', 'Allianz Stadium', 1, 1, 'PLAYED', (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Juventus'), (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '16:00', 'Stadio Diego Armando Maradona', 0, 0, 'ONGOING', (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Napoli'), (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '18:30', 'San Siro', 0, 0, 'SCHEDULED', (SELECT id FROM tournament WHERE name = 'Champions League 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Milan'), (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + TIME '20:45', 'Stadio Olimpico', 0, 0, 'SCHEDULED', (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'AS Roma'), (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE - INTERVAL '1 day' + TIME '20:45', 'San Siro', 3, 2, 'PLAYED', (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Milan'), (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO match (id, date_time, venue, goals_home, goals_away, state, tournament_id, home_team_id, away_team_id) VALUES (nextval('match_seq'), CURRENT_DATE + INTERVAL '1 day' + TIME '20:45', 'Allianz Stadium', 0, 0, 'SCHEDULED', (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Juventus'), (SELECT id FROM team WHERE name = 'Inter'));

-- Test data for users
INSERT INTO app_user (id, username, password, role) VALUES (nextval('app_user_seq'), 'admin', '$2a$12$HG5hCSR.QQlvgxvhrbX3Buj7.RddEEQWextrnSLqzq0HaJ3EAI.7O', 'ROLE_ADMIN');
