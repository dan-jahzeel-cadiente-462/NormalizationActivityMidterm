-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: normalization_db
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authors_2nf`
--

DROP TABLE IF EXISTS `authors_2nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors_2nf` (
  `book_id` int(11) DEFAULT NULL,
  `author` varchar(255) NOT NULL,
  KEY `book_id` (`book_id`),
  CONSTRAINT `authors_2nf_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books_2nf` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors_2nf`
--

LOCK TABLES `authors_2nf` WRITE;
/*!40000 ALTER TABLE `authors_2nf` DISABLE KEYS */;
INSERT INTO `authors_2nf` VALUES (6,'Robert Greene');
/*!40000 ALTER TABLE `authors_2nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authors_3nf`
--

DROP TABLE IF EXISTS `authors_3nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authors_3nf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `author_name` (`author_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors_3nf`
--

LOCK TABLES `authors_3nf` WRITE;
/*!40000 ALTER TABLE `authors_3nf` DISABLE KEYS */;
INSERT INTO `authors_3nf` VALUES (2,'jeiwewe'),(3,'Robert Greene');
/*!40000 ALTER TABLE `authors_3nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_1nf`
--

DROP TABLE IF EXISTS `books_1nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books_1nf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(255) NOT NULL,
  `year_published` int(11) DEFAULT NULL CHECK (`year_published` between 1000 and 9999),
  `isbn` varchar(20) NOT NULL,
  `author` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `pages` int(11) DEFAULT NULL CHECK (`pages` > 0 and `pages` <= 10000),
  PRIMARY KEY (`id`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_1nf`
--

LOCK TABLES `books_1nf` WRITE;
/*!40000 ALTER TABLE `books_1nf` DISABLE KEYS */;
INSERT INTO `books_1nf` VALUES (3,'48 Laws of Power',1998,'0306406152','Robert Greene','Self-help',452),(4,'Atomic Habits',2018,'0735211299','James Clear','Self-help',320),(5,'33 Strategies of War',2007,'0670034576','Robert Greene','Business, Management, Military history, Psychology, Self-help',496);
/*!40000 ALTER TABLE `books_1nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_2nf`
--

DROP TABLE IF EXISTS `books_2nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books_2nf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(255) NOT NULL,
  `year_published` int(11) DEFAULT NULL,
  `isbn` varchar(20) NOT NULL,
  `pages` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_2nf`
--

LOCK TABLES `books_2nf` WRITE;
/*!40000 ALTER TABLE `books_2nf` DISABLE KEYS */;
INSERT INTO `books_2nf` VALUES (6,'48 Laws of Power',1998,'0306406152',452);
/*!40000 ALTER TABLE `books_2nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books_3nf`
--

DROP TABLE IF EXISTS `books_3nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books_3nf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(255) NOT NULL,
  `year_published` int(11) DEFAULT NULL,
  `isbn` varchar(20) NOT NULL,
  `pages` int(11) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `isbn` (`isbn`),
  KEY `author_id` (`author_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_3nf_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `authors_3nf` (`id`),
  CONSTRAINT `books_3nf_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories_3nf` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books_3nf`
--

LOCK TABLES `books_3nf` WRITE;
/*!40000 ALTER TABLE `books_3nf` DISABLE KEYS */;
INSERT INTO `books_3nf` VALUES (4,'njkasdfask',2025,'979-140-23-31-4-4-1',230,2,2),(7,'48 Laws of Power',1998,'0306406152',452,3,3);
/*!40000 ALTER TABLE `books_3nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories_2nf`
--

DROP TABLE IF EXISTS `categories_2nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories_2nf` (
  `book_id` int(11) DEFAULT NULL,
  `category` varchar(255) NOT NULL,
  KEY `book_id` (`book_id`),
  CONSTRAINT `categories_2nf_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books_2nf` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories_2nf`
--

LOCK TABLES `categories_2nf` WRITE;
/*!40000 ALTER TABLE `categories_2nf` DISABLE KEYS */;
INSERT INTO `categories_2nf` VALUES (6,'Self-help');
/*!40000 ALTER TABLE `categories_2nf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories_3nf`
--

DROP TABLE IF EXISTS `categories_3nf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories_3nf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories_3nf`
--

LOCK TABLES `categories_3nf` WRITE;
/*!40000 ALTER TABLE `categories_3nf` DISABLE KEYS */;
INSERT INTO `categories_3nf` VALUES (3,'Self-help'),(2,'wqwdo');
/*!40000 ALTER TABLE `categories_3nf` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-07 20:57:43
