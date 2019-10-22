CREATE TABLE `USER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
CREATE TABLE `swords` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `player_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
)
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
)

INSERT INTO role (id, name) VALUES (2, '权限1');
INSERT INTO swords (id, name, player_id, role_id) VALUES (1, '剑1', 1, 2);
INSERT INTO swords (id, name, player_id, role_id) VALUES (2, '剑2', 1, 2);
INSERT INTO USER (id, username, password, address, email, role_id) VALUES (1, 'cdy', 'cdy', 'c', 'cdy', 2);