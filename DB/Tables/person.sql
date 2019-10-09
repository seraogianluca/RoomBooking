CREATE TABLE `person` (
  `id_person` varchar(10) NOT NULL,
  `name` varchar(45) NOT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id_person`),
  UNIQUE KEY `id_person_UNIQUE` (`id_person`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table Person. Room Booking Data Base';
