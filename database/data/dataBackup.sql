CREATE DATABASE  IF NOT EXISTS `roombooking` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `roombooking`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: roombooking
-- ------------------------------------------------------
-- Server version	5.6.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40000 ALTER TABLE `belong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `booking` (
  `id_person` int(11) NOT NULL,
  `id_room` int(11) NOT NULL,
  `id_schedule` char(1) NOT NULL,
  PRIMARY KEY (`id_person`,`id_room`),
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
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `person` VALUES (1,'Anastasi','Giuseppe','demo@hotmail.com','T'),(2,'Avvenuti','Marco','demo@hotmail.com','T'),(3,'Barillaro','Giuseppe','demo@hotmail.com','T'),(4,'Baronti','Federico','demo@hotmail.com','T'),(5,'Basso','Giovanni','demo@hotmail.com','T'),(6,'Bechini','Alessio','demo@hotmail.com','T'),(7,'Berizzi','Fabrizio','demo@hotmail.com','T'),(8,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(9,'Bicchi','Antonio','demo@hotmail.com','T'),(10,'Bruschi','Paolo','demo@hotmail.com','T'),(11,'Caiti','Andrea','demo@hotmail.com','T'),(12,'Cococcioni','Marco','demo@hotmail.com','T'),(13,'Corsini','Giovanni','demo@hotmail.com','T'),(14,'Dini','Gianluca','demo@hotmail.com','T'),(15,'Domenici','Andrea','demo@hotmail.com','T'),(16,'Ducange','Pietro','demo@hotmail.com','T'),(17,'Fanucci','Luca','demo@hotmail.com','T'),(18,'Fiori','Gianluca','demo@hotmail.com','T'),(19,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(20,'Genovesi','Simone','demo@hotmail.com','T'),(21,'Giannetti','Filippo','demo@hotmail.com','T'),(22,'Gini','Fulvio','demo@hotmail.com','T'),(23,'Giordano','Stefano','demo@hotmail.com','T'),(24,'Greco','Maria','demo@hotmail.com','T'),(25,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(26,'Innocenti','Mario','demo@hotmail.com','T'),(27,'Landi','Alberto','demo@hotmail.com','T'),(28,'Landini','Luigi','demo@hotmail.com','T'),(29,'Lazzerini','Beatrice','demo@hotmail.com','T'),(30,'Lettieri','Giuseppe','demo@hotmail.com','T'),(31,'Lombardini','Fabrizio','demo@hotmail.com','T'),(32,'Lopriore','Lanfranco','demo@hotmail.com','T'),(33,'Lottici','Vincenzo','demo@hotmail.com','T'),(34,'Luise','Marco','demo@hotmail.com','T'),(35,'Macucci','Massimo','demo@hotmail.com','T'),(36,'Manara','Giuliano','demo@hotmail.com','T'),(37,'Marcelloni','Francesco','demo@hotmail.com','T'),(38,'Martorella','Marco','demo@hotmail.com','T'),(39,'Mingozzi','Enzo','demo@hotmail.com','T'),(40,'Monorchio','Agostino','demo@hotmail.com','T'),(41,'Morelli','Michele','demo@hotmail.com','T'),(42,'Moretti','Marco','demo@hotmail.com','T'),(43,'Nannini','Andrea','demo@hotmail.com','T'),(44,'Nepa','Paolo','demo@hotmail.com','T'),(45,'Neri','Bruno','demo@hotmail.com','T'),(46,'Pagano','Michele','demo@hotmail.com','T'),(47,'Pallottino','Lucia','demo@hotmail.com','T'),(48,'Pennelli','Giovanni','demo@hotmail.com','T'),(49,'Pieri','Francesco','demo@hotmail.com','T'),(50,'Piotto','Massimo','demo@hotmail.com','T'),(51,'Pollini','Lorenzo','demo@hotmail.com','T'),(52,'Prete','Cosimo','demo@hotmail.com','T'),(53,'Procissi','Gregorio','demo@hotmail.com','T'),(54,'Reggiannini','Ruggero','demo@hotmail.com','T'),(55,'Rizzo','Luigi','demo@hotmail.com','T'),(56,'Anastasi','Giuseppe','demo@hotmail.com','T'),(57,'Avvenuti','Marco','demo@hotmail.com','T'),(58,'Barillaro','Giuseppe','demo@hotmail.com','T'),(59,'Baronti','Federico','demo@hotmail.com','T'),(60,'Basso','Giovanni','demo@hotmail.com','T'),(61,'Bechini','Alessio','demo@hotmail.com','T'),(62,'Berizzi','Fabrizio','demo@hotmail.com','T'),(63,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(64,'Bicchi','Antonio','demo@hotmail.com','T'),(65,'Bruschi','Paolo','demo@hotmail.com','T'),(66,'Caiti','Andrea','demo@hotmail.com','T'),(67,'Cococcioni','Marco','demo@hotmail.com','T'),(68,'Corsini','Giovanni','demo@hotmail.com','T'),(69,'Dini','Gianluca','demo@hotmail.com','T'),(70,'Domenici','Andrea','demo@hotmail.com','T'),(71,'Ducange','Pietro','demo@hotmail.com','T'),(72,'Fanucci','Luca','demo@hotmail.com','T'),(73,'Fiori','Gianluca','demo@hotmail.com','T'),(74,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(75,'Genovesi','Simone','demo@hotmail.com','T'),(76,'Giannetti','Filippo','demo@hotmail.com','T'),(77,'Gini','Fulvio','demo@hotmail.com','T'),(78,'Giordano','Stefano','demo@hotmail.com','T'),(79,'Greco','Maria','demo@hotmail.com','T'),(80,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(81,'Innocenti','Mario','demo@hotmail.com','T'),(82,'Landi','Alberto','demo@hotmail.com','T'),(83,'Landini','Luigi','demo@hotmail.com','T'),(84,'Lazzerini','Beatrice','demo@hotmail.com','T'),(85,'Lettieri','Giuseppe','demo@hotmail.com','T'),(86,'Lombardini','Fabrizio','demo@hotmail.com','T'),(87,'Lopriore','Lanfranco','demo@hotmail.com','T'),(88,'Lottici','Vincenzo','demo@hotmail.com','T'),(89,'Luise','Marco','demo@hotmail.com','T'),(90,'Macucci','Massimo','demo@hotmail.com','T'),(91,'Manara','Giuliano','demo@hotmail.com','T'),(92,'Marcelloni','Francesco','demo@hotmail.com','T'),(93,'Martorella','Marco','demo@hotmail.com','T'),(94,'Mingozzi','Enzo','demo@hotmail.com','T'),(95,'Monorchio','Agostino','demo@hotmail.com','T'),(96,'Morelli','Michele','demo@hotmail.com','T'),(97,'Moretti','Marco','demo@hotmail.com','T'),(98,'Nannini','Andrea','demo@hotmail.com','T'),(99,'Nepa','Paolo','demo@hotmail.com','T'),(100,'Neri','Bruno','demo@hotmail.com','T'),(101,'Pagano','Michele','demo@hotmail.com','T'),(102,'Pallottino','Lucia','demo@hotmail.com','T'),(103,'Pennelli','Giovanni','demo@hotmail.com','T'),(104,'Pieri','Francesco','demo@hotmail.com','T'),(105,'Piotto','Massimo','demo@hotmail.com','T'),(106,'Pollini','Lorenzo','demo@hotmail.com','T'),(107,'Prete','Cosimo','demo@hotmail.com','T'),(108,'Procissi','Gregorio','demo@hotmail.com','T'),(109,'Reggiannini','Ruggero','demo@hotmail.com','T'),(110,'Rizzo','Luigi','demo@hotmail.com','T'),(111,'Anastasi','Giuseppe','demo@hotmail.com','T'),(112,'Avvenuti','Marco','demo@hotmail.com','T'),(113,'Barillaro','Giuseppe','demo@hotmail.com','T'),(114,'Baronti','Federico','demo@hotmail.com','T'),(115,'Basso','Giovanni','demo@hotmail.com','T'),(116,'Bechini','Alessio','demo@hotmail.com','T'),(117,'Berizzi','Fabrizio','demo@hotmail.com','T'),(118,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(119,'Bicchi','Antonio','demo@hotmail.com','T'),(120,'Bruschi','Paolo','demo@hotmail.com','T'),(121,'Caiti','Andrea','demo@hotmail.com','T'),(122,'Cococcioni','Marco','demo@hotmail.com','T'),(123,'Corsini','Giovanni','demo@hotmail.com','T'),(124,'Dini','Gianluca','demo@hotmail.com','T'),(125,'Domenici','Andrea','demo@hotmail.com','T'),(126,'Ducange','Pietro','demo@hotmail.com','T'),(127,'Fanucci','Luca','demo@hotmail.com','T'),(128,'Fiori','Gianluca','demo@hotmail.com','T'),(129,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(130,'Genovesi','Simone','demo@hotmail.com','T'),(131,'Giannetti','Filippo','demo@hotmail.com','T'),(132,'Gini','Fulvio','demo@hotmail.com','T'),(133,'Giordano','Stefano','demo@hotmail.com','T'),(134,'Greco','Maria','demo@hotmail.com','T'),(135,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(136,'Innocenti','Mario','demo@hotmail.com','T'),(137,'Landi','Alberto','demo@hotmail.com','T'),(138,'Landini','Luigi','demo@hotmail.com','T'),(139,'Lazzerini','Beatrice','demo@hotmail.com','T'),(140,'Lettieri','Giuseppe','demo@hotmail.com','T'),(141,'Lombardini','Fabrizio','demo@hotmail.com','T'),(142,'Lopriore','Lanfranco','demo@hotmail.com','T'),(143,'Lottici','Vincenzo','demo@hotmail.com','T'),(144,'Luise','Marco','demo@hotmail.com','T'),(145,'Macucci','Massimo','demo@hotmail.com','T'),(146,'Manara','Giuliano','demo@hotmail.com','T'),(147,'Marcelloni','Francesco','demo@hotmail.com','T'),(148,'Martorella','Marco','demo@hotmail.com','T'),(149,'Mingozzi','Enzo','demo@hotmail.com','T'),(150,'Monorchio','Agostino','demo@hotmail.com','T'),(151,'Morelli','Michele','demo@hotmail.com','T'),(152,'Moretti','Marco','demo@hotmail.com','T'),(153,'Nannini','Andrea','demo@hotmail.com','T'),(154,'Nepa','Paolo','demo@hotmail.com','T'),(155,'Neri','Bruno','demo@hotmail.com','T'),(156,'Pagano','Michele','demo@hotmail.com','T'),(157,'Pallottino','Lucia','demo@hotmail.com','T'),(158,'Pennelli','Giovanni','demo@hotmail.com','T'),(159,'Pieri','Francesco','demo@hotmail.com','T'),(160,'Piotto','Massimo','demo@hotmail.com','T'),(161,'Pollini','Lorenzo','demo@hotmail.com','T'),(162,'Prete','Cosimo','demo@hotmail.com','T'),(163,'Procissi','Gregorio','demo@hotmail.com','T'),(164,'Reggiannini','Ruggero','demo@hotmail.com','T'),(165,'Rizzo','Luigi','demo@hotmail.com','T'),(166,'Anastasi','Giuseppe','demo@hotmail.com','T'),(167,'Avvenuti','Marco','demo@hotmail.com','T'),(168,'Barillaro','Giuseppe','demo@hotmail.com','T'),(169,'Baronti','Federico','demo@hotmail.com','T'),(170,'Basso','Giovanni','demo@hotmail.com','T'),(171,'Bechini','Alessio','demo@hotmail.com','T'),(172,'Berizzi','Fabrizio','demo@hotmail.com','T'),(173,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(174,'Bicchi','Antonio','demo@hotmail.com','T'),(175,'Bruschi','Paolo','demo@hotmail.com','T'),(176,'Caiti','Andrea','demo@hotmail.com','T'),(177,'Cococcioni','Marco','demo@hotmail.com','T'),(178,'Corsini','Giovanni','demo@hotmail.com','T'),(179,'Dini','Gianluca','demo@hotmail.com','T'),(180,'Domenici','Andrea','demo@hotmail.com','T'),(181,'Ducange','Pietro','demo@hotmail.com','T'),(182,'Fanucci','Luca','demo@hotmail.com','T'),(183,'Fiori','Gianluca','demo@hotmail.com','T'),(184,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(185,'Genovesi','Simone','demo@hotmail.com','T'),(186,'Giannetti','Filippo','demo@hotmail.com','T'),(187,'Gini','Fulvio','demo@hotmail.com','T'),(188,'Giordano','Stefano','demo@hotmail.com','T'),(189,'Greco','Maria','demo@hotmail.com','T'),(190,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(191,'Innocenti','Mario','demo@hotmail.com','T'),(192,'Landi','Alberto','demo@hotmail.com','T'),(193,'Landini','Luigi','demo@hotmail.com','T'),(194,'Lazzerini','Beatrice','demo@hotmail.com','T'),(195,'Lettieri','Giuseppe','demo@hotmail.com','T'),(196,'Lombardini','Fabrizio','demo@hotmail.com','T'),(197,'Lopriore','Lanfranco','demo@hotmail.com','T'),(198,'Lottici','Vincenzo','demo@hotmail.com','T'),(199,'Luise','Marco','demo@hotmail.com','T'),(200,'Macucci','Massimo','demo@hotmail.com','T'),(201,'Manara','Giuliano','demo@hotmail.com','T'),(202,'Marcelloni','Francesco','demo@hotmail.com','T'),(203,'Martorella','Marco','demo@hotmail.com','T'),(204,'Mingozzi','Enzo','demo@hotmail.com','T'),(205,'Monorchio','Agostino','demo@hotmail.com','T'),(206,'Morelli','Michele','demo@hotmail.com','T'),(207,'Moretti','Marco','demo@hotmail.com','T'),(208,'Nannini','Andrea','demo@hotmail.com','T'),(209,'Nepa','Paolo','demo@hotmail.com','T'),(210,'Neri','Bruno','demo@hotmail.com','T'),(211,'Pagano','Michele','demo@hotmail.com','T'),(212,'Pallottino','Lucia','demo@hotmail.com','T'),(213,'Pennelli','Giovanni','demo@hotmail.com','T'),(214,'Pieri','Francesco','demo@hotmail.com','T'),(215,'Piotto','Massimo','demo@hotmail.com','T'),(216,'Pollini','Lorenzo','demo@hotmail.com','T'),(217,'Prete','Cosimo','demo@hotmail.com','T'),(218,'Procissi','Gregorio','demo@hotmail.com','T'),(219,'Reggiannini','Ruggero','demo@hotmail.com','T'),(220,'Rizzo','Luigi','demo@hotmail.com','T'),(221,'Anastasi','Giuseppe','demo@hotmail.com','T'),(222,'Avvenuti','Marco','demo@hotmail.com','T'),(223,'Barillaro','Giuseppe','demo@hotmail.com','T'),(224,'Baronti','Federico','demo@hotmail.com','T'),(225,'Basso','Giovanni','demo@hotmail.com','T'),(226,'Bechini','Alessio','demo@hotmail.com','T'),(227,'Berizzi','Fabrizio','demo@hotmail.com','T'),(228,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(229,'Bicchi','Antonio','demo@hotmail.com','T'),(230,'Bruschi','Paolo','demo@hotmail.com','T'),(231,'Caiti','Andrea','demo@hotmail.com','T'),(232,'Cococcioni','Marco','demo@hotmail.com','T'),(233,'Corsini','Giovanni','demo@hotmail.com','T'),(234,'Dini','Gianluca','demo@hotmail.com','T'),(235,'Domenici','Andrea','demo@hotmail.com','T'),(236,'Ducange','Pietro','demo@hotmail.com','T'),(237,'Fanucci','Luca','demo@hotmail.com','T'),(238,'Fiori','Gianluca','demo@hotmail.com','T'),(239,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(240,'Genovesi','Simone','demo@hotmail.com','T'),(241,'Giannetti','Filippo','demo@hotmail.com','T'),(242,'Gini','Fulvio','demo@hotmail.com','T'),(243,'Giordano','Stefano','demo@hotmail.com','T'),(244,'Greco','Maria','demo@hotmail.com','T'),(245,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(246,'Innocenti','Mario','demo@hotmail.com','T'),(247,'Landi','Alberto','demo@hotmail.com','T'),(248,'Landini','Luigi','demo@hotmail.com','T'),(249,'Lazzerini','Beatrice','demo@hotmail.com','T'),(250,'Lettieri','Giuseppe','demo@hotmail.com','T'),(251,'Lombardini','Fabrizio','demo@hotmail.com','T'),(252,'Lopriore','Lanfranco','demo@hotmail.com','T'),(253,'Lottici','Vincenzo','demo@hotmail.com','T'),(254,'Luise','Marco','demo@hotmail.com','T'),(255,'Macucci','Massimo','demo@hotmail.com','T'),(256,'Manara','Giuliano','demo@hotmail.com','T'),(257,'Marcelloni','Francesco','demo@hotmail.com','T'),(258,'Martorella','Marco','demo@hotmail.com','T'),(259,'Mingozzi','Enzo','demo@hotmail.com','T'),(260,'Monorchio','Agostino','demo@hotmail.com','T'),(261,'Morelli','Michele','demo@hotmail.com','T'),(262,'Moretti','Marco','demo@hotmail.com','T'),(263,'Nannini','Andrea','demo@hotmail.com','T'),(264,'Nepa','Paolo','demo@hotmail.com','T'),(265,'Neri','Bruno','demo@hotmail.com','T'),(266,'Pagano','Michele','demo@hotmail.com','T'),(267,'Pallottino','Lucia','demo@hotmail.com','T'),(268,'Pennelli','Giovanni','demo@hotmail.com','T'),(269,'Pieri','Francesco','demo@hotmail.com','T'),(270,'Piotto','Massimo','demo@hotmail.com','T'),(271,'Pollini','Lorenzo','demo@hotmail.com','T'),(272,'Prete','Cosimo','demo@hotmail.com','T'),(273,'Procissi','Gregorio','demo@hotmail.com','T'),(274,'Reggiannini','Ruggero','demo@hotmail.com','T'),(275,'Rizzo','Luigi','demo@hotmail.com','T'),(276,'Anastasi','Giuseppe','demo@hotmail.com','T'),(277,'Avvenuti','Marco','demo@hotmail.com','T'),(278,'Barillaro','Giuseppe','demo@hotmail.com','T'),(279,'Baronti','Federico','demo@hotmail.com','T'),(280,'Basso','Giovanni','demo@hotmail.com','T'),(281,'Bechini','Alessio','demo@hotmail.com','T'),(282,'Berizzi','Fabrizio','demo@hotmail.com','T'),(283,'Bernardeschi','Cinzia','demo@hotmail.com','T'),(284,'Bicchi','Antonio','demo@hotmail.com','T'),(285,'Bruschi','Paolo','demo@hotmail.com','T'),(286,'Caiti','Andrea','demo@hotmail.com','T'),(287,'Cococcioni','Marco','demo@hotmail.com','T'),(288,'Corsini','Giovanni','demo@hotmail.com','T'),(289,'Dini','Gianluca','demo@hotmail.com','T'),(290,'Domenici','Andrea','demo@hotmail.com','T'),(291,'Ducange','Pietro','demo@hotmail.com','T'),(292,'Fanucci','Luca','demo@hotmail.com','T'),(293,'Fiori','Gianluca','demo@hotmail.com','T'),(294,'Foglia','Pierfrancesco','demo@hotmail.com','T'),(295,'Genovesi','Simone','demo@hotmail.com','T'),(296,'Giannetti','Filippo','demo@hotmail.com','T'),(297,'Gini','Fulvio','demo@hotmail.com','T'),(298,'Giordano','Stefano','demo@hotmail.com','T'),(299,'Greco','Maria','demo@hotmail.com','T'),(300,'Iannaccone','Giuseppe','demo@hotmail.com','T'),(301,'Innocenti','Mario','demo@hotmail.com','T'),(302,'Landi','Alberto','demo@hotmail.com','T'),(303,'Landini','Luigi','demo@hotmail.com','T'),(304,'Lazzerini','Beatrice','demo@hotmail.com','T'),(305,'Lettieri','Giuseppe','demo@hotmail.com','T'),(306,'Lombardini','Fabrizio','demo@hotmail.com','T'),(307,'Lopriore','Lanfranco','demo@hotmail.com','T'),(308,'Lottici','Vincenzo','demo@hotmail.com','T'),(309,'Luise','Marco','demo@hotmail.com','T'),(310,'Macucci','Massimo','demo@hotmail.com','T'),(311,'Manara','Giuliano','demo@hotmail.com','T'),(312,'Marcelloni','Francesco','demo@hotmail.com','T'),(313,'Martorella','Marco','demo@hotmail.com','T'),(314,'Mingozzi','Enzo','demo@hotmail.com','T'),(315,'Monorchio','Agostino','demo@hotmail.com','T'),(316,'Morelli','Michele','demo@hotmail.com','T'),(317,'Moretti','Marco','demo@hotmail.com','T'),(318,'Nannini','Andrea','demo@hotmail.com','T'),(319,'Nepa','Paolo','demo@hotmail.com','T'),(320,'Neri','Bruno','demo@hotmail.com','T'),(321,'Pagano','Michele','demo@hotmail.com','T'),(322,'Pallottino','Lucia','demo@hotmail.com','T'),(323,'Pennelli','Giovanni','demo@hotmail.com','T'),(324,'Pieri','Francesco','demo@hotmail.com','T'),(325,'Piotto','Massimo','demo@hotmail.com','T'),(326,'Pollini','Lorenzo','demo@hotmail.com','T'),(327,'Prete','Cosimo','demo@hotmail.com','T'),(328,'Procissi','Gregorio','demo@hotmail.com','T'),(329,'Reggiannini','Ruggero','demo@hotmail.com','T'),(330,'Rizzo','Luigi','demo@hotmail.com','T');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id_room` int(11) NOT NULL AUTO_INCREMENT,
  `floor` int(11) DEFAULT NULL,
  `num_room` varchar(45) DEFAULT NULL,
  `max_person` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_room`),
  UNIQUE KEY `id_room_UNIQUE` (`id_room`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='Table Room. Room Booking Data Base';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,1,'B12',15),(2,1,'B13',15),(3,1,'B14',15),(4,1,'B15',15),(5,1,'B16',40),(6,1,'B17',40),(7,1,'B18',40),(8,1,'B19',40),(9,2,'B21',40),(10,2,'B22',40),(11,1,'B12',15),(12,1,'B13',15),(13,1,'B14',15),(14,1,'B15',15),(15,1,'B16',40),(16,1,'B17',40),(17,1,'B18',40),(18,1,'B19',40),(19,2,'B21',40),(20,2,'B22',40);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-12 11:22:31
