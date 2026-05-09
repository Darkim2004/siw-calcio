-- Test data for tournaments
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Serie A 2025-2026', 2026, 'Italian top division football league');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Coppa Italia 2025-2026', 2026, 'Italian national cup competition');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Champions League 2025-2026', 2026, 'Top European club tournament');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Euro 2024', 2024, 'European national teams championship');
INSERT INTO tournament (id, name, year, description) VALUES (nextval('tournament_seq'), 'Spring Tournament 2025-2026', 2026, 'Youth teams championship');

-- Test data for teams
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'AS Roma', 1927, 'Roma');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Inter', 1908, 'Milano');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Juventus', 1897, 'Torino');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Milan', 1899, 'Milano');
INSERT INTO team (id, name, foundation_year, city) VALUES (nextval('team_seq'), 'Napoli', 1926, 'Napoli');

-- Test data for players
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Paulo', 'Dybala', '1993-11-15', 'FORWARD', '', 177, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Lorenzo', 'Pellegrini', '1996-06-19', 'MIDFIELDER', '', 186, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Gianluca', 'Mancini', '1996-04-17', 'DEFENDER', '', 190, (SELECT id FROM team WHERE name = 'AS Roma'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Mile', 'Svilar', '1999-08-27', 'GOALKEEPER', '', 189, (SELECT id FROM team WHERE name = 'AS Roma'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Lautaro', 'Martinez', '1997-08-22', 'FORWARD', '', 174, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Nicolo', 'Barella', '1997-02-07', 'MIDFIELDER', '', 175, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Alessandro', 'Bastoni', '1999-04-13', 'DEFENDER', '', 190, (SELECT id FROM team WHERE name = 'Inter'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Yann', 'Sommer', '1988-12-17', 'GOALKEEPER', '', 183, (SELECT id FROM team WHERE name = 'Inter'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Dusan', 'Vlahovic', '2000-01-28', 'FORWARD', '', 190, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Manuel', 'Locatelli', '1998-01-08', 'MIDFIELDER', '', 185, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Federico', 'Gatti', '1998-06-24', 'DEFENDER', '', 190, (SELECT id FROM team WHERE name = 'Juventus'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Michele', 'Di Gregorio', '1997-07-27', 'GOALKEEPER', '', 187, (SELECT id FROM team WHERE name = 'Juventus'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Rafael', 'Leao', '1999-06-10', 'FORWARD', '', 188, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Tijjani', 'Reijnders', '1998-07-29', 'MIDFIELDER', '', 185, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Fikayo', 'Tomori', '1997-12-19', 'DEFENDER', '', 185, (SELECT id FROM team WHERE name = 'Milan'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Mike', 'Maignan', '1995-07-03', 'GOALKEEPER', '', 191, (SELECT id FROM team WHERE name = 'Milan'));

INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Romelu', 'Lukaku', '1993-05-13', 'FORWARD', '', 191, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Stanislav', 'Lobotka', '1994-11-25', 'MIDFIELDER', '', 170, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Amir', 'Rrahmani', '1994-02-24', 'DEFENDER', '', 192, (SELECT id FROM team WHERE name = 'Napoli'));
INSERT INTO player (id, first_name, last_name, birth_date, role, photo, height, team_id) VALUES (nextval('player_seq'), 'Alex', 'Meret', '1997-03-22', 'GOALKEEPER', '', 190, (SELECT id FROM team WHERE name = 'Napoli'));

-- Test data for participations
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 82);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Juventus'), 79);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Milan'), 75);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Napoli'), 71);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'AS Roma'), 12);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 10);
