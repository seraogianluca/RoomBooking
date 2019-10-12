CREATE TABLE `building` (
  `id_building` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  PRIMARY KEY (`id_building`),
  UNIQUE KEY `id_building_UNIQUE` (`id_building`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Table Building. Room Booking Data Base';
