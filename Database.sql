CREATE TABLE `conversations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `last_message_at` datetime(6) DEFAULT NULL,
  `last_message_content` varchar(500) DEFAULT NULL,
  `status` enum('ACTIVE','ARCHIVED','BLOCKED') NOT NULL,
  `unread_count_host` int DEFAULT NULL,
  `unread_count_user` int DEFAULT NULL,
  `host_id` bigint NOT NULL,
  `house_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKarw7kjo8ujy7n4hml8u6ycsve` (`host_id`),
  KEY `FKn86j90hhs1qr93wdgushtjq5p` (`house_id`),
  KEY `FKpltqvfcbkql9svdqwh0hw4g1d` (`user_id`),
  CONSTRAINT `FKarw7kjo8ujy7n4hml8u6ycsve` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKn86j90hhs1qr93wdgushtjq5p` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`),
  CONSTRAINT `FKpltqvfcbkql9svdqwh0hw4g1d` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `unique_constraint` varchar(255) DEFAULT NULL,
  `house_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5u9374ux3xhllkfqrai3ah33d` (`unique_constraint`),
  KEY `FKsa9pb0nl2i5fulcorbbd0yqd4` (`house_id`),
  KEY `FKk7du8b8ewipawnnpg76d55fus` (`user_id`),
  CONSTRAINT `FKk7du8b8ewipawnnpg76d55fus` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsa9pb0nl2i5fulcorbbd0yqd4` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `host_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_back_photo_url` varchar(255) DEFAULT NULL,
  `id_front_photo_url` varchar(255) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `processed_date` datetime(6) DEFAULT NULL,
  `proof_of_ownership_url` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `request_date` datetime(6) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_s312eokm2s158d3r2iy0ayh0g` (`user_id`),
  CONSTRAINT `FKi5dmjqaj9repuc3wb86ich6u1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `hosts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `approved_date` datetime(6) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `proof_of_ownership_url` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6c4mwr0x8hgg1dxv4b36slmfm` (`user_id`),
  CONSTRAINT `FKim6h7w6syp1n0vvcrycxggs6x` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `house_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) NOT NULL,
  `sort_order` int DEFAULT NULL,
  `house_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1ksqd0kdf4madevx9j032jws7` (`house_id`),
  CONSTRAINT `FK1ksqd0kdf4madevx9j032jws7` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `houses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `area` double NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `house_type` enum('APARTMENT','BOARDING_HOUSE','TOWNHOUSE','VILLA','WHOLE_HOUSE') NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `status` enum('AVAILABLE','INACTIVE','RENTED') NOT NULL,
  `title` varchar(255) NOT NULL,
  `host_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdqqr9usj738qnfxou5vtcm9pb` (`host_id`),
  CONSTRAINT `FKdqqr9usj738qnfxou5vtcm9pb` FOREIGN KEY (`host_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  `message_type` enum('FILE','IMAGE','SYSTEM','TEXT') NOT NULL,
  `read_at` datetime(6) DEFAULT NULL,
  `status` enum('DELIVERED','FAILED','READ','SENT') NOT NULL,
  `conversation_id` bigint NOT NULL,
  `house_id` bigint NOT NULL,
  `receiver_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt492th6wsovh1nush5yl5jj8e` (`conversation_id`),
  KEY `FKrij6sx2quk5r2834lapd8ejh3` (`house_id`),
  KEY `FKt05r0b6n0iis8u7dfna4xdh73` (`receiver_id`),
  KEY `FK4ui4nnwntodh6wjvck53dbk9m` (`sender_id`),
  CONSTRAINT `FK4ui4nnwntodh6wjvck53dbk9m` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrij6sx2quk5r2834lapd8ejh3` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`),
  CONSTRAINT `FKt05r0b6n0iis8u7dfna4xdh73` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt492th6wsovh1nush5yl5jj8e` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `type` enum('GENERAL','HOUSE_DELETED','RENTAL_APPROVED','RENTAL_BOOKED','RENTAL_CANCELED','RENTAL_REJECTED','RENTAL_REQUEST','REVIEW_ONE_STAR') NOT NULL,
  `house_id` bigint DEFAULT NULL,
  `receiver_id` bigint NOT NULL,
  `rental_id` bigint DEFAULT NULL,
  `review_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKobp876irpxhtcey3ppdbm7shh` (`house_id`),
  KEY `FK9kxl0whvhifo6gw4tjq36v53k` (`receiver_id`),
  KEY `FK8gds4lp4lqb2jggsoed8gg0r0` (`rental_id`),
  KEY `FKrt4d5kxdyv0dhxir466q9gya2` (`review_id`),
  CONSTRAINT `FK8gds4lp4lqb2jggsoed8gg0r0` FOREIGN KEY (`rental_id`) REFERENCES `rentals` (`id`),
  CONSTRAINT `FK9kxl0whvhifo6gw4tjq36v53k` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKobp876irpxhtcey3ppdbm7shh` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`),
  CONSTRAINT `FKrt4d5kxdyv0dhxir466q9gya2` FOREIGN KEY (`review_id`) REFERENCES `reviews` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f90ivichjaokvmovxpnlm5nin` (`user_id`),
  CONSTRAINT `FK83nsrttkwkb6ym0anu051mtxn` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `rentals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `approved_at` datetime(6) DEFAULT NULL,
  `cancel_reason` varchar(500) DEFAULT NULL,
  `canceled_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) NOT NULL,
  `guest_count` int DEFAULT NULL,
  `message_to_host` varchar(1000) DEFAULT NULL,
  `reject_reason` varchar(500) DEFAULT NULL,
  `rejected_at` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) NOT NULL,
  `status` enum('APPROVED','CANCELED','CHECKED_IN','CHECKED_OUT','PENDING','REJECTED','SCHEDULED') NOT NULL,
  `total_price` double DEFAULT NULL,
  `approved_by` bigint DEFAULT NULL,
  `house_id` bigint NOT NULL,
  `rejected_by` bigint DEFAULT NULL,
  `renter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7bmtmqkyjw9om18gmvo85k67p` (`approved_by`),
  KEY `FKqaxe59h1ls6oyvijg4qcrst9v` (`house_id`),
  KEY `FK9hgojwjb5cfuhupljr8qy03y2` (`rejected_by`),
  KEY `FKifio6cht3fecproeq439t9mgl` (`renter_id`),
  CONSTRAINT `FK7bmtmqkyjw9om18gmvo85k67p` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK9hgojwjb5cfuhupljr8qy03y2` FOREIGN KEY (`rejected_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKifio6cht3fecproeq439t9mgl` FOREIGN KEY (`renter_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqaxe59h1ls6oyvijg4qcrst9v` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `is_visible` bit(1) NOT NULL,
  `rating` int NOT NULL,
  `house_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgto6vm4vaj3etd9ipjl81we2i` (`house_id`),
  KEY `FKcgy7qjc1r99dp117y9en6lxye` (`user_id`),
  CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgto6vm4vaj3etd9ipjl81we2i` FOREIGN KEY (`house_id`) REFERENCES `houses` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` enum('ADMIN','HOST','USER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `facebook_account_id` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `google_account_id` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_du5v5sr43g5bfnji4vb8hg5s3` (`phone`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci