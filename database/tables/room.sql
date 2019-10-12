CREATE TABLE `room` (
  `id_room` int(11) NOT NULL AUTO_INCREMENT,
  `floor` int(11) DEFAULT NULL,
  `num_room` varchar(45) DEFAULT NULL,
  `max_person` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_room`),
  UNIQUE KEY `id_room_UNIQUE` (`id_room`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='Table Room. Room Booking Data Base';