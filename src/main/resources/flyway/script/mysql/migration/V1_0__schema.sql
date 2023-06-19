CREATE SCHEMA IF NOT EXISTS `caloriecalculator` ;
DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`food` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `calories` double NOT NULL,
                        `carbs` double NOT NULL,
                        `fat` double NOT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        `protein` double NOT NULL,
                        `serving` int NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `caloriecalculator`.`food` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`food` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`food` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`privilege` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `name` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`privilege` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`privilege` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`privilege` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`role` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`role` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`role` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`role` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `roles_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`roles_privileges` (
  `role_id` int DEFAULT NULL,
  `privilege_id` int DEFAULT NULL,
  KEY `FK9h2vewsqh8luhfq71xokh4who` (`role_id`),
  KEY `FK5yjwxw2gvfyu76j3rgqwo685u` (`privilege_id`),
  CONSTRAINT `FK5yjwxw2gvfyu76j3rgqwo685u` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`),
  CONSTRAINT `FK9h2vewsqh8luhfq71xokh4who` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`roles_privileges` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`roles_privileges` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`roles_privileges` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`user_account` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `activity` int DEFAULT NULL,
                                `age` int DEFAULT NULL,
                                `email` varchar(255) NOT NULL,
                                `enabled` bit(1) NOT NULL,
                                `gender` varchar(255) DEFAULT NULL,
                                `height` double DEFAULT NULL,
                                `name` varchar(255) DEFAULT NULL,
                                `password` varchar(60) NOT NULL,
                                `weight` double DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `UK_hl02wv5hym99ys465woijmfib` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`user_account` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`user_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`user_account` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`calorie_intake` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `quantity_of_food` double DEFAULT NULL,
                                  `time_of_intake` datetime(6) DEFAULT NULL,
                                  `food_id` int DEFAULT NULL,
                                  `user_id` int DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `CI_FK_FOOD` (`food_id`),
                                  KEY `CI_FK_USER` (`user_id`),
                                  CONSTRAINT `CI_FK_FOOD` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
                                  CONSTRAINT `CI_FK_USER` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `caloriecalculator`.`calorie_intake` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`calorie_intake` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`calorie_intake` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `sport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`sport` (
  `id` int NOT NULL AUTO_INCREMENT,
  `burned_calories` double DEFAULT NULL,
  `name_of_activity` varchar(255) DEFAULT NULL,
  `time_of_activity` datetime(6) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `S_FK_USER` (`user_id`),
  CONSTRAINT `S_FK_USER` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`sport` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`sport` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`sport` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`users_roles` (
  `user_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
  KEY `FKci4mdvg1fmo9eqmwno1y9o0fa` (`user_id`),
  CONSTRAINT `FKci4mdvg1fmo9eqmwno1y9o0fa` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`users_roles` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`users_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`users_roles` ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caloriecalculator`.`verification_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `VERIFY_USER` (`user_id`),
  CONSTRAINT `VERIFY_USER` FOREIGN KEY (`user_id`) REFERENCES `user_account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `caloriecalculator`.`verification_token` WRITE;
/*!40000 ALTER TABLE `caloriecalculator`.`verification_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `caloriecalculator`.`verification_token` ENABLE KEYS */;
UNLOCK TABLES;


