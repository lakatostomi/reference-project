INSERT INTO privilege (id, name) VALUES (1, 'READ_PRIVILEGE');
INSERT INTO privilege (id, name) VALUES (2, 'WRITE_PRIVILEGE');

INSERT INTO role (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO role (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 1);
INSERT INTO roles_privileges (role_id, privilege_id) VALUES (2, 2);

INSERT INTO food (name, calories, serving, carbs, protein, fat) VALUES ('Pizza', 100.0, 226.0, 27.0, 9.7, 8.2);
INSERT INTO food (name, calories, serving, carbs, protein, fat) VALUES ('Beefsteak', 100.0, 205.0, 0.0, 27.2, 10.2);
INSERT INTO food (name, calories, serving, carbs, protein, fat) VALUES ('Hamburger', 100.0, 231.4, 24.6, 12.9, 9.4);