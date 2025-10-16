CREATE DATABASE  IF NOT EXISTS `receitas_de_despensa` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `receitas_de_despensa`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: receitas_de_despensa
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `classificacoes`
--

DROP TABLE IF EXISTS `classificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classificacoes` (
  `id_classificacao` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_receita` int NOT NULL,
  `nota` int NOT NULL,
  `data_avaliacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_classificacao`),
  UNIQUE KEY `id_usuario` (`id_usuario`,`id_receita`),
  KEY `id_receita` (`id_receita`),
  CONSTRAINT `classificacoes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `classificacoes_ibfk_2` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`) ON DELETE CASCADE,
  CONSTRAINT `classificacoes_chk_1` CHECK ((`nota` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classificacoes`
--

LOCK TABLES `classificacoes` WRITE;
/*!40000 ALTER TABLE `classificacoes` DISABLE KEYS */;
INSERT INTO `classificacoes` VALUES (1,2,1,4,'2025-10-15 01:27:14'),(2,1,1,5,'2025-10-16 00:10:36');
/*!40000 ALTER TABLE `classificacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentarios`
--

DROP TABLE IF EXISTS `comentarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentarios` (
  `id_comentario` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_receita` int NOT NULL,
  `texto` tinytext NOT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_comentario`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_receita` (`id_receita`),
  CONSTRAINT `comentarios_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `comentarios_ibfk_2` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
INSERT INTO `comentarios` VALUES (1,2,1,'Ótima receita, adorei!','2025-10-15 01:15:04'),(2,2,1,'adorei!','2025-10-15 01:15:55');
/*!40000 ALTER TABLE `comentarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `informacoes_nutricionais`
--

DROP TABLE IF EXISTS `informacoes_nutricionais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `informacoes_nutricionais` (
  `id_receita` int NOT NULL,
  `calorias` decimal(10,2) DEFAULT NULL,
  `proteinas` decimal(10,2) DEFAULT NULL,
  `carboidratos` decimal(10,2) DEFAULT NULL,
  `gorduras` decimal(10,2) DEFAULT NULL,
  `fibra` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id_receita`),
  CONSTRAINT `informacoes_nutricionais_ibfk_1` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informacoes_nutricionais`
--

LOCK TABLES `informacoes_nutricionais` WRITE;
/*!40000 ALTER TABLE `informacoes_nutricionais` DISABLE KEYS */;
/*!40000 ALTER TABLE `informacoes_nutricionais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredientes`
--

DROP TABLE IF EXISTS `ingredientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredientes` (
  `id_ingrediente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id_ingrediente`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredientes`
--

LOCK TABLES `ingredientes` WRITE;
/*!40000 ALTER TABLE `ingredientes` DISABLE KEYS */;
INSERT INTO `ingredientes` VALUES (4,'Banana'),(5,'Chocolate'),(7,'Creme de leite'),(3,'Farinha de Mandioca'),(2,'Farinha de trigo'),(6,'Leite'),(1,'Ovo');
/*!40000 ALTER TABLE `ingredientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `media_avaliacoes`
--

DROP TABLE IF EXISTS `media_avaliacoes`;
/*!50001 DROP VIEW IF EXISTS `media_avaliacoes`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `media_avaliacoes` AS SELECT 
 1 AS `id_receita`,
 1 AS `titulo`,
 1 AS `media_nota`,
 1 AS `total_avaliacoes`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `receita_ingredientes`
--

DROP TABLE IF EXISTS `receita_ingredientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receita_ingredientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_receita` int NOT NULL,
  `id_ingrediente` int NOT NULL,
  `quantidade` varchar(50) NOT NULL,
  `unidade` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_receita` (`id_receita`),
  KEY `id_ingrediente` (`id_ingrediente`),
  CONSTRAINT `receita_ingredientes_ibfk_1` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`),
  CONSTRAINT `receita_ingredientes_ibfk_2` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingredientes` (`id_ingrediente`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receita_ingredientes`
--

LOCK TABLES `receita_ingredientes` WRITE;
/*!40000 ALTER TABLE `receita_ingredientes` DISABLE KEYS */;
INSERT INTO `receita_ingredientes` VALUES (1,7,1,'3','unidades'),(2,7,2,'1','xícaras'),(3,7,3,'2','xícaras'),(4,8,1,'3','unidades'),(5,8,2,'1','xícaras'),(6,8,3,'2','xícaras'),(7,9,1,'3','unidades'),(8,9,2,'1','xícaras'),(9,9,3,'2','xícaras'),(10,10,4,'1','unidades'),(11,10,5,'1','barra'),(12,11,4,'2','unidades');
/*!40000 ALTER TABLE `receita_ingredientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receitas`
--

DROP TABLE IF EXISTS `receitas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receitas` (
  `id_receita` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `descricao` tinytext NOT NULL,
  `modo_preparo` tinytext NOT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `dieta` varchar(50) DEFAULT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_receita`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `receitas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receitas`
--

LOCK TABLES `receitas` WRITE;
/*!40000 ALTER TABLE `receitas` DISABLE KEYS */;
INSERT INTO `receitas` VALUES (1,'Bolo de Cenoura','Um bolo fofinho e delicioso para o café da tarde.','1. Bata os ovos, o óleo e a cenoura no liquidificador...','Bolos','Vegetariana','2025-10-14 23:41:27',2),(2,'Bolo de Milho','Um bolo fofinho e delicioso para o café da tarde.','1. Bata os ovos, o óleo e o Milho no liquidificador...','Bolos','Vegetariana','2025-10-14 23:43:12',2),(7,'Bolo de Cenoura com Cobertura','O melhor bolo de cenoura que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:31:26',2),(8,'Bolo de Chocolate com Cobertura','O melhor bolo de Chocolate que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:43:52',2),(9,'Bolo de Chocolate com Cobertura','O melhor bolo de Chocolate que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:52:05',2),(10,'Banana split','O melhor que existe.','Misture tudo.','Sobremesa','Vegetariana','2025-10-16 00:53:51',1),(11,'Banana caramelizada','O melhor que existe.','Asse.','Sobremesa','Vegetariana','2025-10-16 00:54:57',1);
/*!40000 ALTER TABLE `receitas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receitas_salvas`
--

DROP TABLE IF EXISTS `receitas_salvas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receitas_salvas` (
  `id_usuario` int NOT NULL,
  `id_receita` int NOT NULL,
  `data_salvamento` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`,`id_receita`),
  KEY `id_receita` (`id_receita`),
  CONSTRAINT `receitas_salvas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `receitas_salvas_ibfk_2` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receitas_salvas`
--

LOCK TABLES `receitas_salvas` WRITE;
/*!40000 ALTER TABLE `receitas_salvas` DISABLE KEYS */;
INSERT INTO `receitas_salvas` VALUES (1,1,'2025-10-16 00:28:33');
/*!40000 ALTER TABLE `receitas_salvas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha_hash` varchar(255) NOT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo` enum('usuario','administrador') DEFAULT 'usuario',
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Diego Araujo','diego@exemplo.com','$2a$10$tfAXzMO9OBgNiIcd81yUd.tJdsyT06LZkHjK15Bs26TZNqZRPPxs6','2025-10-14 00:57:02','usuario'),(2,'Itala Fabiola','itala@exemplo.com','$2a$10$gZ6VGC1HXMCaWyFnFZhLT.KVD0.Qb9IGnebrl6JGRhWo4G5yPZaQy','2025-10-14 00:58:17','usuario');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `media_avaliacoes`
--

/*!50001 DROP VIEW IF EXISTS `media_avaliacoes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `media_avaliacoes` AS select `r`.`id_receita` AS `id_receita`,`r`.`titulo` AS `titulo`,avg(`c`.`nota`) AS `media_nota`,count(`c`.`id_classificacao`) AS `total_avaliacoes` from (`receitas` `r` left join `classificacoes` `c` on((`r`.`id_receita` = `c`.`id_receita`))) group by `r`.`id_receita` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-15 22:32:01
