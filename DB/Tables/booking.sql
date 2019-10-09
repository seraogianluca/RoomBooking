CREATE TABLE `booking` (
  `id_person` varchar(10) NOT NULL,
  `id_room` int(11) NOT NULL,
  `init_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id_person`,`id_room`),
  KEY `fk_idroom_idx` (`id_room`),
  CONSTRAINT `fk_idperson` FOREIGN KEY (`id_person`) REFERENCES `person` (`id_person`),
  CONSTRAINT `fk_idroom` FOREIGN KEY (`id_room`) REFERENCES `room` (`id_room`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table Booking. Room Booking Data Base';
