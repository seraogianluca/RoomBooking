CREATE TABLE `room` (
  `id_room` int(11) NOT NULL,
  `floor` int(11) DEFAULT NULL,
  `num_room` varchar(45) DEFAULT NULL,
  `max_person` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_room`),
  UNIQUE KEY `id_room_UNIQUE` (`id_room`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table Room. Room Booking Data Base';
