# Workgroup activity report - Design phase

## Table of contents
1. [Introduction](#1-introduction)
2. [Functional Requirements](#2-functional-requirements)
3. [Non-functional requirements](#3-non-functional-requirements)
4. [Actors of the system](#4-actors-of-the-system)
5. [Schemas](#5-schemas) 
6. [Software Architecture](#6-software-architecture)

## 1. Introduction
The Room Booking Application is a system that let users to book university classrooms and laboratories. This application is thought for teachers who need a classroom for seminaries or lectures and for the students who need workstations in the laboratories.


## 2. Functional requirements
The application has the following functional requirements:

- A user shall be able to log in as a `teacher`, a `student` or an `administrator`.
- A `teacher` shall be able to search for the available classrooms.
- A `student` shall be able to search for the available laboratories.
- A `teacher` shall be able to book a classroom in a specified schedule.
- A `student`shall be able to book a laboratory workstation for all the day.
- `teachers` and `students` shall be able to modify or delete their bookings.
- An `administrator` shall be able to insert a new classroom or a new laboratory and eventually the corresponding building into the system.
- An `administrator` shall be able to insert a new student or a new teacher into the system.
- The system shall provide an interface for the user to read a list of available rooms and book one of them.
- The system shall provide an interface for the user to read the rooms booked by him and may update or delete a booking.

## 3. Non-functional requirements
The application has the following non-functional requirements:

- The application must be written in Java.
- The application must be run once at time.
- The set, update and delete operations on the database must be atomic.
- The classrooms and the laboratories can be booked only for the day after.
- The classroom can be booked only for two different schedules in the day: Morning, Afternoon.
- The classroom in a specified schedule can be booked by only one teacher.
- The classrooms, laboratories and buildings in the system can't be deleted from the system.
- Users must be identified by email.
- Teachers' emails must belong to the [at] university domain.
- Students' emails must belong to the [at] studenti [dot] university domain.
- Administrator's email must belong to the [at] admin [dot] university domain.

## 4. Actors of the system
The system actors are divided in tree kinds of users.
- Administrator: the person that can add new rooms and buldings in the system.
- Teacher: the person that can book classrooms.
- Student: the person that can book laboratories.

## 5. Schemas
### 5.1 Use Cases
The following use cases are identified:
![Use Cases](/schemas/task1/UseCases.png)

The `choose schedule` is specified as a `extend` because only the `teacher` users can choose the part of the day (Morning, Afternoon) in which they want to book the classroom.

### 5.1 Analysis Classes
The following classes are idientified:
![Classes](/schemas/task1/ClassesUML.png)

## 6. Software architecture

The application is standalone, mono-process and single-thread. The application is multi-layer. A data access layer carries out the comunication with the database layer and all the operations related to the data management: insert, read, update and delete. A user interaction layer carries out the interaction with the user and the comunication with the data access layer below.

![Architecture](/schemas/task1/Architecture.png)
