CREATE DATABASE  IF NOT EXISTS `roombooking` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `roombooking`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: roombooking
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `building`
--

DROP TABLE IF EXISTS `building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `building` (
  `BUILDING_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BUILDING_ADDRESS` varchar(255) DEFAULT NULL,
  `BUILDING_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`BUILDING_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classroom`
--

DROP TABLE IF EXISTS `classroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classroom` (
  `CLASSROOM_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLASSROOM_AVAILABLE` bit(1) DEFAULT NULL,
  `CLASSROOM_CAPACITY` int(11) DEFAULT NULL,
  `CLASSROOM_NAME` varchar(255) DEFAULT NULL,
  `BUILDING_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`CLASSROOM_ID`),
  KEY `FKd5gb12a7jyvkdtd7v2yr1u372` (`BUILDING_ID`),
  CONSTRAINT `FKd5gb12a7jyvkdtd7v2yr1u372` FOREIGN KEY (`BUILDING_ID`) REFERENCES `building` (`BUILDING_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classroom_booking`
--

DROP TABLE IF EXISTS `classroom_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classroom_booking` (
  `BOOKING_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BOOKING_SCHEDULE` varchar(255) DEFAULT NULL,
  `CLASSROOM_ID` bigint(20) DEFAULT NULL,
  `TEACHER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`BOOKING_ID`),
  KEY `FKmexfi0lnoy48r2aqjh3wfw0mp` (`CLASSROOM_ID`),
  KEY `FKhj92p4ugr4yt008p7rioaoj6e` (`TEACHER_ID`),
  CONSTRAINT `FKhj92p4ugr4yt008p7rioaoj6e` FOREIGN KEY (`TEACHER_ID`) REFERENCES `teacher` (`TEACHER_ID`),
  CONSTRAINT `FKmexfi0lnoy48r2aqjh3wfw0mp` FOREIGN KEY (`CLASSROOM_ID`) REFERENCES `classroom` (`CLASSROOM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laboratory`
--

DROP TABLE IF EXISTS `laboratory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `laboratory` (
  `LABORATORY_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LABORATORY_AVAILABLE` bit(1) DEFAULT NULL,
  `LABORATORY_CAPACITY` int(11) DEFAULT NULL,
  `LABORATORY_NAME` varchar(255) DEFAULT NULL,
  `BUILDING_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`LABORATORY_ID`),
  KEY `FK28wrnav1u7a6urlbera75caj1` (`BUILDING_ID`),
  CONSTRAINT `FK28wrnav1u7a6urlbera75caj1` FOREIGN KEY (`BUILDING_ID`) REFERENCES `building` (`BUILDING_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `laboratory_booking`
--

DROP TABLE IF EXISTS `laboratory_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `laboratory_booking` (
  `LABORATORY_ID` bigint(20) NOT NULL,
  `STUDENT_ID` bigint(20) NOT NULL,
  KEY `FKc70wq04hyf7gdabpc4l93hgly` (`STUDENT_ID`),
  KEY `FK21g3xhh565k8avgvhgmjpefj0` (`LABORATORY_ID`),
  CONSTRAINT `FK21g3xhh565k8avgvhgmjpefj0` FOREIGN KEY (`LABORATORY_ID`) REFERENCES `laboratory` (`LABORATORY_ID`),
  CONSTRAINT `FKc70wq04hyf7gdabpc4l93hgly` FOREIGN KEY (`STUDENT_ID`) REFERENCES `student` (`STUDENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `STUDENT_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STUDENT_EMAIL` varchar(255) DEFAULT NULL,
  `STUDENT_LASTNAME` varchar(255) DEFAULT NULL,
  `STUDENT_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`STUDENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `TEACHER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TEACHER_EMAIL` varchar(255) DEFAULT NULL,
  `TEACHER_LASTNAME` varchar(255) DEFAULT NULL,
  `TEACHER_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TEACHER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-31 20:13:40
