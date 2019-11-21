# Workgroup activity report - Implementation

## Table of contents
1. [Introduction](#1-introduction)
2. [Main functionalities](#2-main-functionalities)
3. [Main procedures](#3-main-procedures)
4. [Main Classes](#4-main-classes)

## 1. Introduction
In this document are described the highlights of the implementation starting from the description of the classes model, that include the integration of levelDB in the solution, discussed in [Feasibility Study on the use of a Key-Value Data Storage](./FeasibilityStudy.md), and the ER diagram of the relational database.
Then, the use cases are described. For brevity, the admin part is not reported because constists only in insert data into relational database.

## 2. Main functionalities
The solution is implemented as a monolithic multi-layer application with three layers. The interaction with the user is made through a Command Line Interface that let different kinds of user to perform different actions. The middleware consists of a Manager class that coordinates the operations between the two databases:

The key-value database, as described in the [Feasibility Study on the use of a Key-Value Data Storage](./FeasibilityStudy.md), stores the user most frequently used informations like: list of the available rooms and list of the user booking.

The relational database stores all the system informations like: users, bookings, rooms and buildings. The Java Persistence API is used for managing operations on MySQL.

The read operations are made exclusively on LevelDB and the insert and update operations are made using both databases.

The analysis classes diagram of the application including both databases is shown below:

![Analysis Classes](/schemas/task1/ClassesUML.png)

The relational model is implemented as follow:

![ER](/schemas/task1/ER.png)

The database layer includes MySQL for the relational database and LevelDB for the key-value database.

## 3 Main procedures
Below the use cases of the application are described by words.

### 3.1 Log in / Log out
A user can log into the system through the accademic email. Once the email is inserted by the user, the role is checked. If the user is a teacher or a student the application menu let these kinds of user to book a new room (a classroom for teacher, a laboratory workstation for student), delete a previous booking, update a previous booking, close the application. If the user is an administrator the application menu let the user to insert a new students and teachers, insert a new room and eventually a new building, close the application.

### 3.2 Set booking
A teacher can book a classroom choosing between two schedules: Morning or Afternoon. The procedure starts with the set booking command in the main menu. The system asks the teacher to choose in which schedule she wants to book the classroom (Morning, Afternoon), then the system shows a list of the available classrooms in the choosed schedule. Then the system asks the teacher to choose a classroom using the room ID. Once the teacher chooses the classroom, the system starts to operate on the databases. In details:
- The information about the booking are put in levelDB and MySQL.
- The classroom availability is checked.
- If the classroom was free for all the day, the availability of the classroom is updated in levelDB.
- If the classroom was free only for the choosen schedule, the availability is updated on MySQL (set the classroom unavailable) and the informations about the classroom from levelDB are deleted.

 If the procedure on the databases ends with success, an acknowledgement is showed to the teacher.

A student can book a workstation in the laboratory. The procedure starts with the set booking command in the main menu. THe system shows a list of the laboratories with available workstations. The system asks to the user to choose a laboratory using the room ID. Once the student chooses the classroom, the system starts to operate on the databases. In details:
- The informations about the booking are inserted in levelDB and MySQL.
- The laboratory availability is checked.
- If the laboratory has more than one workstation free, the availability of the laboratory is updated in levelDB.
- If the laboratory has only one available workstation, the availability is updated on MySQL(set the laboratory unavailable) and the information about the classroom from levelDB are deleted.

If the procedure on the databases ends with success an acknowledgement is showed to the student.

### 3.3 Delete booking
A teacher can delete one of the booking she made. The procedure starts with the delete booking command in the main menu. The system shows the list of bookings made by the teacher. The system asks to the teacher to choose a booking using the booking ID. Once the teacher chooses the booking, the system starts to operate on the databases. In details:
- The availability of the classroom is checked.
- The booking is deleted.
- If the classroom was unavailable the availability is updated on MySQL and all the information about the classroom are putted in levelDB. The schedule in which the classroom was booked is put as schedule in which the classroom is available.
- If the classroom was available the availability is updated on levelDB. The schedule in which the classroom is available is all the day.

If the procedure on the databases ends with success an acknowledgement is showed to the teacher.

A student can delete one of the booking she made. he procedure starts with the delete booking command in the main menu. The system ask to the student to choose a booking using the room ID. Once the student choose the booking, the system starts to operate on the databases. In details:
- The availability of the laboratory is checked
- The booking is deleted
- If the laboratory was unavailable the availability is updated on MySQL and all the information about the laboratory are putted in levelDB. One is putted in the availability information about the laboratory, that means one workstation available.
- If the laboratory was available the availability is updated on levelDB. The available number of workstation is increased by one.

If the procedure on the databases ends with success an acknowledgement is showed to the student.

### 3.4 Update booking
The update booking procedure is a sum of the delete booking and the set booking, performed in this order.

## 4. Main Classes
Below a short description for the main classes of the application:

- DBSManager: This class manages labelDB and MySQL databases, ensuring the consistency of the data.

- LevelDBDriver: This class contains all the CRUD operations for the LevelDB database.

- HibernateDriver: This class contains all the CRUD operations for the MySQL database using JPA.

- The ORM package contains all the classes that represents JPA entities. These classes are used for the Object Relational Mapping. The relations between entities are mapped as bidirectional relations. More details about JPA relation mapping are at the following link: [Making annotations and writing CRUD operations in JPA](./Tutorial.md). In details, this package contains:
    - Building: This entity maps the building table. Building contains the one-to-many relation mapping with Classroom and Laboratory.
    - Classroom: This entity maps the classroom table. Classroom contains the many-to-one relation mapping with Building and the one-to-many relation mapping with ClassroomBooking.
    - ClassroomBooking: This entity maps the classroom booking table. ClassroomBooking contains the many-to-one relation mapping with Building and the one-to-many relation mapping with ClassroomBooking.
    - Laboratory: This entity maps the laboratory table. Laboratory contains the many-to-one relation mapping with Building and the many-to-many relation mapping with Student.
    - Student: This entity maps the student table. Student contains the many-to-many relation mapping with Laboratory, that represents the student booking informations.
    - Teacher: This entity maps the teacher table. Teacher contains the many-to-one relation mapping with ClassroomBooking.

- The NORM package contains all the classes that represent the application objects, this package contains:
    - Available: This class contains all the informations about an available room.
    - Booked: This class contains all the informations about a booked room.
    - BuildingNORM: This class contains all the informations about a building.
    - User:  This class contains all the informations about a user.