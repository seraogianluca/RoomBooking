CREATE TABLE `person` (
  `id_person` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` char(1) NOT NULL,
  PRIMARY KEY (`id_person`),
  UNIQUE KEY `id_person_UNIQUE` (`id_person`)
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8 COMMENT='Table Person. Room Booking Data Base';
