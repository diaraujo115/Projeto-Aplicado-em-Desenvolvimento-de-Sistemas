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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classificacoes`
--

LOCK TABLES `classificacoes` WRITE;
/*!40000 ALTER TABLE `classificacoes` DISABLE KEYS */;
INSERT INTO `classificacoes` VALUES (1,2,1,4,'2025-10-15 01:27:14'),(2,1,1,5,'2025-10-16 00:10:36'),(3,4,1,5,'2025-11-12 00:12:43'),(4,5,1,4,'2025-10-30 22:40:30');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
INSERT INTO `comentarios` VALUES (1,2,1,'Ótima receita, adorei!','2025-10-15 01:15:04'),(2,2,1,'adorei!','2025-10-15 01:15:55'),(3,4,1,'Ameeeii!!!','2025-10-28 22:48:20');
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
  `calorias` double DEFAULT NULL,
  `proteinas` double DEFAULT NULL,
  `carboidratos` double DEFAULT NULL,
  `gorduras` double DEFAULT NULL,
  `fibra` double DEFAULT NULL,
  `acucar` double DEFAULT NULL,
  `sodio` double DEFAULT NULL,
  `gordura_saturada` double DEFAULT NULL,
  `porcoes` int DEFAULT '1',
  PRIMARY KEY (`id_receita`),
  CONSTRAINT `informacoes_nutricionais_ibfk_1` FOREIGN KEY (`id_receita`) REFERENCES `receitas` (`id_receita`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informacoes_nutricionais`
--

LOCK TABLES `informacoes_nutricionais` WRITE;
/*!40000 ALTER TABLE `informacoes_nutricionais` DISABLE KEYS */;
INSERT INTO `informacoes_nutricionais` VALUES (14,2045.63,50.816500000000005,302.20399999999995,80.225,5.9,294.024,722.49,48.5623,1),(15,1193.30676,24.934307999999998,132.957108,75.83097,11.8,113.705608,207.88438,41.9078,1),(16,1448.31,44.54,265.2408,22.157899999999998,5.4,113.1393,293.1,8.886099999999999,1),(17,1026.715,46.339375,159.141875,26.033125,26.721875,24.0621875,59.0825,3.1587187500000002,4),(18,2207,51.5,481.5,4.9,13.5,101.14999999999999,1395.2880000000002,0.775,6);
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
  `nome_en` varchar(255) DEFAULT NULL,
  `is_vegetariano` tinyint(1) DEFAULT '1',
  `is_vegano` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_ingrediente`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredientes`
--

LOCK TABLES `ingredientes` WRITE;
/*!40000 ALTER TABLE `ingredientes` DISABLE KEYS */;
INSERT INTO `ingredientes` VALUES (1,'Ovo','Egg',1,0),(2,'Farinha de trigo','Wheat flour',1,1),(3,'Farinha de Mandioca','Cassava flour',1,1),(4,'Banana','Banana',1,1),(5,'Chocolate','Chocolate',1,1),(6,'Leite','Milk',1,0),(7,'Creme de leite','Cream',1,0),(8,'Leite condensado','Condensed milk',1,0),(9,'Açúcar','Sugar',1,1),(10,'Água','Water',1,1),(11,'Cenoura','Carrot',1,1),(12,'Batata','Potato',1,1),(13,'Tomate','Tomato',1,1),(14,'Cebola','Onion',1,1),(15,'Alho','Garlic',1,1),(16,'Pimentão','Bell Pepper',1,1),(17,'Abobrinha','Zucchini',1,1),(18,'Berinjela','Eggplant',1,1),(19,'Brócolis','Broccoli',1,1),(20,'Couve-flor','Cauliflower',1,1),(21,'Espinafre','Spinach',1,1),(22,'Alface','Lettuce',1,1),(23,'Couve','Kale',1,1),(24,'Rúcula','Arugula',1,1),(25,'Maçã','Apple',1,1),(26,'Laranja','Orange',1,1),(27,'Limão','Lemon',1,1),(28,'Abacaxi','Pineapple',1,1),(29,'Manga','Mango',1,1),(30,'Melancia','Watermelon',1,1),(31,'Morango','Strawberry',1,1),(32,'Uva','Grape',1,1),(33,'Pêssego','Peach',1,1),(34,'Mamão','Papaya',1,1),(35,'Kiwi','Kiwi',1,1),(36,'Abacate','Avocado',1,1),(37,'Arroz','Rice',1,1),(38,'Feijão','Beans',1,1),(39,'Lentilha','Lentil',1,1),(40,'Grão-de-bico','Chickpea',1,1),(41,'Milho','Corn',1,1),(42,'Aveia','Oats',1,1),(43,'Trigo','Wheat',1,1),(44,'Quinoa','Quinoa',1,1),(45,'Soja','Soybean',1,1),(46,'Queijo','Cheese',1,0),(47,'Iogurte','Yogurt',1,0),(48,'Manteiga','Butter',1,0),(49,'Frango','Chicken',0,0),(50,'Carne bovina','Beef',0,0),(51,'Carne suína','Pork',0,0),(52,'Peixe','Fish',0,0),(53,'Camarão','Shrimp',0,0),(54,'Atum','Tuna',0,0),(55,'Salmão','Salmon',0,0),(56,'Sal','Salt',1,1),(57,'Pimenta-do-reino','Black Pepper',1,1),(58,'Orégano','Oregano',1,1),(59,'Manjericão','Basil',1,1),(60,'Salsa','Parsley',1,1),(61,'Cebolinha','Chives',1,1),(62,'Alecrim','Rosemary',1,1),(63,'Tomilho','Thyme',1,1),(64,'Azeite de oliva','Olive Oil',1,1),(65,'Vinagre','Vinegar',1,1),(66,'Molho de soja','Soy Sauce',1,1),(67,'Fermento','Yeast',1,1),(68,'Mel','Honey',1,0),(69,'Cacau em pó','Cocoa Powder',1,1),(70,'Café','Coffee',1,1),(71,'Chá verde','Green Tea',1,1),(72,'Óleo vegetal','Vegetable Oil',1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receita_ingredientes`
--

LOCK TABLES `receita_ingredientes` WRITE;
/*!40000 ALTER TABLE `receita_ingredientes` DISABLE KEYS */;
INSERT INTO `receita_ingredientes` VALUES (1,7,1,'3','unidades'),(2,7,2,'1','xícaras'),(3,7,3,'2','xícaras'),(4,8,1,'3','unidades'),(5,8,2,'1','xícaras'),(6,8,3,'2','xícaras'),(7,9,1,'3','unidades'),(8,9,2,'1','xícaras'),(9,9,3,'2','xícaras'),(10,10,4,'1','unidades'),(11,10,5,'1','barra'),(12,11,4,'2','unidades'),(13,12,8,'2','xícaras'),(14,12,1,'4','inteiro'),(15,12,9,'1','xícara'),(16,12,10,'80 ','mililitro'),(18,14,5,'100','gramas'),(19,14,8,'395','gramas'),(20,14,6,'2','xícaras'),(21,15,1,'3','inteiro'),(22,15,5,'200','gramas'),(23,15,9,'2','xícaras'),(24,15,7,'1','lata'),(25,16,1,'3','unidades'),(26,16,9,'100','gramas'),(27,16,2,'200','gramas'),(28,16,6,'1','xícara'),(29,17,40,'1','xícara'),(30,17,2,'3','colher de sopa'),(31,17,15,'50','gramas'),(32,17,14,'50','gramas'),(33,17,64,'1','colher de sopa'),(34,18,9,'100','gramas'),(35,18,56,'100','gramas'),(36,18,2,'500','gramas');
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
  `modo_preparo` text NOT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `dieta` varchar(255) DEFAULT NULL,
  `data_criacao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_receita`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `receitas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receitas`
--

LOCK TABLES `receitas` WRITE;
/*!40000 ALTER TABLE `receitas` DISABLE KEYS */;
INSERT INTO `receitas` VALUES (1,'Bolo de Cenoura','Um bolo fofinho e delicioso para o café da tarde.','1. Bata os ovos, o óleo e a cenoura no liquidificador...','Bolos','Vegetariana','2025-10-14 23:41:27',2),(2,'Bolo de Milho','Um bolo fofinho e delicioso para o café da tarde.','1. Bata os ovos, o óleo e o Milho no liquidificador...','Bolos','Vegetariana','2025-10-14 23:43:12',2),(7,'Bolo de Cenoura com Cobertura','O melhor bolo de cenoura que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:31:26',2),(8,'Bolo de Chocolate com Cobertura','O melhor bolo de Chocolate que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:43:52',2),(9,'Bolo de Chocolate com Cobertura','O melhor bolo de Chocolate que existe.','Misture tudo e asse.','Bolos','Vegetariana','2025-10-15 00:52:05',2),(10,'Banana split','O melhor que existe.','Misture tudo.','Sobremesa','Vegetariana','2025-10-16 00:53:51',1),(11,'Banana caramelizada','O melhor que existe.','Asse.','Sobremesa','Vegetariana','2025-10-16 00:54:57',1),(12,'Pudim de leite condensado','Receita clássica, fácil mas muito deliciosa!','1. Em uma panela, misture a água e o açúcar até formar uma calda.\n2. Unte uma forma com a calda e reserve.\n3. Bata todos os ingredientes no liquidificador e despeje na forma caramelizada.\n4. Leve para assar em banho-maria por 40 minutos.\n5. Desenforme e sirva','Sobremesa','','2025-11-08 23:04:03',4),(14,'brigadeiro','brigadeiro','Junte tudo e leve ao fogo','Sobremesa','','2025-11-09 02:06:36',4),(15,'Mousse de chocolate','Muito chocolatudo','1. Bata as gemas até dobrarem de volume.\n2. Junte o açúcar e continue batendo.\n3. Derreta o chocolate e acrescente à gemada.\n4. Junte o creme de leite, batendo sempre.\n5. Por fim, acrescente as claras em neve (bem batidas mesmo), misturando rapidamente na batedeira.\n6. Leve ao freezer por aproximadamente 3 horas.','Sobremesa','','2025-11-09 02:16:45',4),(16,'Bolinho de chuva','Bolinho de chuva','1. Adicione os ovos ao leite\n2. Adicione sal e açúcar a mistura\n3. adicione a farinha de trigo\n4. misture até ficar homogêneo \n5. pega uma pequena quantidade e leve ao óleo quente\n6. retire ao dourar','Lanche','Nenhuma','2025-11-09 19:41:08',4),(17,'Bolinho de Grão-de-Bico Vegano','Vegano, rico em proteína vegetal','1. Amasse o grão-de-bico cozido com um garfo ou processe até virar uma massa grossa.\n2. Adicione o alho, cebola, azeite, sal, pimenta e temperos.\n3. Misture a farinha de aveia até a massa ficar moldável.\n4. Modele os bolinhos com as mãos.\n5. Asse em forno pré-aquecido a 200 °C por cerca de 20 minutos, virando na metade do tempo.','Lanche','Vegana, Baixo Calórico, Baixo Sódio, Baixo Açúcar, Baixa Gordura','2025-11-09 21:45:16',4),(18,'Pão ','pão','1. misture tudo\n2. leve ao forno','Lanche','Vegana, Baixo Calórico','2025-11-12 02:04:19',4);
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
INSERT INTO `receitas_salvas` VALUES (1,1,'2025-10-16 00:28:33'),(4,1,'2025-11-12 00:12:40'),(4,7,'2025-10-28 23:26:04'),(5,1,'2025-11-12 02:41:55'),(5,9,'2025-11-12 02:41:59');
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
  `ativo` bit(1) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Diego Araujo','diego@exemplo.com','$2a$10$cRcBz/hjJW490x9zUvHo6uUfodOnn5VmHP/EJr5/FKtSdaOEwCUou','2025-10-14 00:57:02','usuario',_binary ''),(2,'Itala Fabiola','itala@exemplo.com','$2a$10$gZ6VGC1HXMCaWyFnFZhLT.KVD0.Qb9IGnebrl6JGRhWo4G5yPZaQy','2025-10-14 00:58:17','usuario',_binary ''),(3,'Paulo Freire','paulo@exemplo.com','$2a$10$sqWKO./ReHWsjVZ2tMkbU.xc2KfQU1t5FB2R3yeuQrliF8um27SjG','2025-10-27 22:31:14','usuario',_binary '\0'),(4,'Laura','laura@exemplo.com','$2a$10$8QhegZq2A9Iq/JP7jGEYIuVjhL5ppDr6VdgTt1RgWuvLpa8eJIR9q','2025-10-27 23:32:15','usuario',_binary ''),(5,'Pedro','pedro','$2a$10$0D0Lk9zDBQQV3z9kp3ca7u7AbTsk/QEXgKl2Vp6DH67.wERdBAq2S','2025-10-30 22:39:20','usuario',_binary '\0');
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

-- Dump completed on 2025-11-12  0:36:39
