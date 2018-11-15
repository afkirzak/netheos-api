INSERT INTO role (id, role_name) VALUES (1, 'USER');
INSERT INTO role (id, role_name) VALUES (2, 'ADMIN');


INSERT INTO user (id, username, password) VALUES (1, 'afkir', 'password');
INSERT INTO user (id, username, password) VALUES (2, 'admin', 'password');


INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
