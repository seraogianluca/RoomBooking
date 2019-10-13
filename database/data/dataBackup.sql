CREATE DATABASE  IF NOT EXISTS `roombooking` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `roombooking`;
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: roombooking
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `belong`
--

DROP TABLE IF EXISTS `belong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `belong` (
  `id_room` int(11) NOT NULL,
  `id_building` int(11) NOT NULL,
  PRIMARY KEY (`id_room`,`id_building`),
  KEY `fk_idbuilding_idx` (`id_building`),
  CONSTRAINT `fk_id_room` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`),
  CONSTRAINT `fk_idbuilding` FOREIGN KEY (`id_building`) REFERENCES `building` (`id_building`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `belong`
--

LOCK TABLES `belong` WRITE;
/*!40000 ALTER TABLE `belong` DISABLE KEYS */;
INSERT INTO `belong` VALUES (21,1),(22,1),(25,1),(26,1),(23,2),(24,2);
/*!40000 ALTER TABLE `belong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id_person` int(11) NOT NULL,
  `id_room` int(11) NOT NULL,
  `id_schedule` char(1) NOT NULL COMMENT 'm: morning\na: afternoon',
  PRIMARY KEY (`id_person`,`id_room`,`id_schedule`),
  KEY `fk_idroom_idx` (`id_room`),
  CONSTRAINT `fk_idperson` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  CONSTRAINT `fk_idroom` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table Booking. Room Booking Data Base';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (2,21,'a'),(1,22,'m'),(2,23,'a'),(2,24,'m');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `building` (
  `id_building` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  PRIMARY KEY (`id_building`),
  UNIQUE KEY `id_building_UNIQUE` (`id_building`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Table Building. Room Booking Data Base';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `building`
--

LOCK TABLES `building` WRITE;
/*!40000 ALTER TABLE `building` DISABLE KEYS */;
INSERT INTO `building` VALUES (1,'Ingegneria','Via Diotisalvi'),(2,'Medicina','Via Diotisalvi'),(3,'Aerospace','Via Diotisalvi'),(4,'Biomecanich','Via Diotisalvi'),(5,'Sapienza','Via Diotisalvi'),(6,'Ingegneria','Via Diotisalvi'),(7,'Medicina','Via Diotisalvi'),(8,'Aerospace','Via Diotisalvi'),(9,'Biomecanich','Via Diotisalvi'),(10,'Sapienza','Via Diotisalvi');
/*!40000 ALTER TABLE `building` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `error`
--

DROP TABLE IF EXISTS `error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `error` (
  `id_error` varchar(5) NOT NULL,
  `user_message` varchar(100) NOT NULL,
  PRIMARY KEY (`id_error`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `error`
--

LOCK TABLES `error` WRITE;
/*!40000 ALTER TABLE `error` DISABLE KEYS */;
INSERT INTO `error` VALUES ('00000','Ok'),('99999','Unexpected Error');
/*!40000 ALTER TABLE `error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `id_person` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` char(1) NOT NULL,
  PRIMARY KEY (`id_person`),
  UNIQUE KEY `id_person_UNIQUE` (`id_person`)
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8 COMMENT='Table Person. Room Booking Data Base';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'anastasi','giuseppe','demo@hotmail.com','T'),(2,'avvenuti','marco','demo@hotmail.com','T'),(3,'barillaro','giuseppe','demo@hotmail.com','T'),(4,'baronti','federico','demo@hotmail.com','T'),(5,'basso','giovanni','demo@hotmail.com','T'),(6,'bechini','alessio','demo@hotmail.com','T'),(7,'berizzi','fabrizio','demo@hotmail.com','T'),(8,'bernardeschi','cinzia','demo@hotmail.com','T'),(9,'bicchi','antonio','demo@hotmail.com','T'),(10,'bruschi','paolo','demo@hotmail.com','T'),(11,'caiti','andrea','demo@hotmail.com','T'),(12,'cococcioni','marco','demo@hotmail.com','T'),(13,'corsini','giovanni','demo@hotmail.com','T'),(14,'dini','gianluca','demo@hotmail.com','T'),(15,'domenici','andrea','demo@hotmail.com','T'),(16,'ducange','pietro','demo@hotmail.com','T'),(17,'fanucci','luca','demo@hotmail.com','T'),(18,'fiori','gianluca','demo@hotmail.com','T'),(19,'foglia','pierfrancesco','demo@hotmail.com','T'),(20,'genovesi','simone','demo@hotmail.com','T'),(21,'giannetti','filippo','demo@hotmail.com','T'),(22,'gini','fulvio','demo@hotmail.com','T'),(23,'giordano','stefano','demo@hotmail.com','T'),(24,'greco','maria','demo@hotmail.com','T'),(25,'iannaccone','giuseppe','demo@hotmail.com','T'),(26,'innocenti','mario','demo@hotmail.com','T'),(27,'landi','alberto','demo@hotmail.com','T'),(28,'landini','luigi','demo@hotmail.com','T'),(29,'lazzerini','beatrice','demo@hotmail.com','T'),(30,'lettieri','giuseppe','demo@hotmail.com','T'),(31,'lombardini','fabrizio','demo@hotmail.com','T'),(32,'lopriore','lanfranco','demo@hotmail.com','T'),(33,'lottici','vincenzo','demo@hotmail.com','T'),(34,'luise','marco','demo@hotmail.com','T'),(35,'macucci','massimo','demo@hotmail.com','T'),(36,'manara','giuliano','demo@hotmail.com','T'),(37,'marcelloni','francesco','demo@hotmail.com','T'),(38,'martorella','marco','demo@hotmail.com','T'),(39,'mingozzi','enzo','demo@hotmail.com','T'),(40,'monorchio','agostino','demo@hotmail.com','T'),(41,'morelli','michele','demo@hotmail.com','T'),(42,'moretti','marco','demo@hotmail.com','T'),(43,'nannini','andrea','demo@hotmail.com','T'),(44,'nepa','paolo','demo@hotmail.com','T'),(45,'neri','bruno','demo@hotmail.com','T'),(46,'pagano','michele','demo@hotmail.com','T'),(47,'pallottino','lucia','demo@hotmail.com','T'),(48,'pennelli','giovanni','demo@hotmail.com','T'),(49,'pieri','francesco','demo@hotmail.com','T'),(50,'piotto','massimo','demo@hotmail.com','T'),(51,'pollini','lorenzo','demo@hotmail.com','T'),(52,'prete','cosimo','demo@hotmail.com','T'),(53,'procissi','gregorio','demo@hotmail.com','T'),(54,'reggiannini','ruggero','demo@hotmail.com','T'),(55,'rizzo','luigi','demo@hotmail.com','T');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id_room` int(11) NOT NULL AUTO_INCREMENT,
  `floor` int(11) DEFAULT NULL,
  `num_room` varchar(45) DEFAULT NULL,
  `max_person` int(11) DEFAULT NULL,
  `available` tinyint(4) DEFAULT '1' COMMENT '0: Not Available\\n1: Avaliable',
  PRIMARY KEY (`id_room`),
  UNIQUE KEY `id_room_UNIQUE` (`id_room`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='Table Room. Room Booking Data Base';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (21,1,'A11',15,1),(22,1,'A12',15,1),(23,2,'A21',15,1),(24,2,'A22',15,1),(25,2,'A23',15,1),(26,2,'A24',15,1);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'roombooking'
--

--
-- Dumping routines for database 'roombooking'
--
/*!50003 DROP PROCEDURE IF EXISTS `getPersonID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPersonID`(IN pname VARCHAR(45), IN plastname VARCHAR(45), OUT pid_person INT, OUT pid_error VARCHAR(5), OUT puser_message VARCHAR(100))
BEGIN
 SET pid_error = '00000';
 SET puser_message = 'OK';
 SELECT id_person INTO pid_person FROM PERSON WHERE NAME = pname AND LASTNAME = plastname LIMIT 1;
 COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-13 17:54:23
