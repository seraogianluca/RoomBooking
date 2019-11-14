# Workgroup activity report - Design phase

## Table of contents
1. [Introduction](#1-introduction)
2. [Functional Requirements](#2-functional-requirements)
3. [Non-functional requirements](#3-non-functional-requirements)
4. [Schemas](#4-schemas) 
5. [Software Architecture](#5-software-architecture)

## 1. Introduction
The Room Booking Application is a system that let users to book university classrooms and laboratories. This application is thought for teachers who need a classroom for seminaries or lectures and for the students who need workstations in the laboratories.

## 2. Functional requirements
The application has the following functional requirements:

- A user shall be able to log in as a `teacher`, a `student` or an `administrator`.
- A `teacher` shall be able to search for the available classrooms.
- A `student` shall be able to search for the available laboratories.
- A `teacher` shall be able to book a classroom in a specified schedule.
- A `student`shall be able to book a laboratory for all the day.
- `teachers` and `students` shall be able to modify or delete their booking.
- An `administrator` shall be able to insert a new classroom or a new laboratory and the corresponding building into the system.
- The system shall provide an interface for the user to read a list of available rooms and book one of them.
- The system shall provide an interface for the user to read the rooms booked by him and may update or delete a booking.

## 3. Non-functional requirements
The application has the following non-functional requirements:

- The application must be written in Java.
- The application must be run once at time.
- The set, update and delete operations on the database must be atomic.
- The classrooms and the laboratories can be booked only for the day after.
- The classrooms can be booked only for two different schedules in the day: Morning, Afternoon.
- The laboratories can be booked only for all the day.
- The classroom in a specified schedule can be booked by only one teacher.
- New classrooms, laboratories and buildings can only be inserted by the administrator.
- The classrooms, laboratories and buildings in the system can't be deleted.
- Users must be identified by email.
- Teachers' emails must belong to the [at] university domain.
- Students' emails must belong to the [at] studenti [dot] university domain.
- Administrator's email must belong to the [at] admin [dot] university domain.

## 4. Schemas
The following use cases are identified:
![Use Cases](/schemas/task1/UseCases.png)

The `choose schedule` is specified as a `extend` because only the `teacher` users can choose the part of the day (Morning, Afternoon) in which they want to book the classroom.

The following classes are idientified:
![Classes](/schemas/task1/ClassesUMLFirst.png)

## 5. Software architecture

The application is standalone, mono-process and single-thread. The application is multi-layer. A data access layer carries out the comunication with the database layer and all the operations related to the data management: insert, read, update and delete. A user interaction layer carries out the interaction with the user and the comunication with the data access layer below.

![Architecture](/schemas/task1/Architecture.png)
