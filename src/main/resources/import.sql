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

-- Test data for participations
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 82);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Juventus'), 79);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Milan'), 75);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Serie A 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Napoli'), 71);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'AS Roma'), 12);
INSERT INTO partecipation (id, tournament_id, team_id, points) VALUES (nextval('partecipation_seq'), (SELECT id FROM tournament WHERE name = 'Coppa Italia 2025-2026' AND year = 2026), (SELECT id FROM team WHERE name = 'Inter'), 10);
