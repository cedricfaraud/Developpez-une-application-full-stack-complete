DROP DATABASE IF EXISTS mdd;
CREATE DATABASE mdd;
USE mdd;

DROP TABLE IF EXISTS `users`
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USERS_index` (`email`)
);

DROP TABLE IF EXISTS `topics`
CREATE TABLE `topics` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `posts`
CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `topic_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `message` varchar(2000) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `topic_id` (`topic_id`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`)
);


CREATE TABLE `comments` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `message` VARCHAR(2000),
  `post_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `created_at`  timestamp NULL DEFAULT NULL,
  `updated_at`  timestamp NULL DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
);

CREATE TABLE `subscribtions` (
  `user_id` INT DEFAULT NULL,
  `topic_id` INT DEFAULT NULL,
  CONSTRAINT `subscribtions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `subscribtions_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`)
);