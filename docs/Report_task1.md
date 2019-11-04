# Task 1 - Workgroup activity report

## Table of contents
1. [Introduction](#1-introduction)
2. [Functional Requirements](#2-functional-requirements)
3. [Non-functional requirements](#3-non-functional-requirements)
4. [Data Model](#4-data-model) 
5. [Software Architecture](#5-software-architecture)
6. [User manual](#6-user-manual)

## 1. Introduction
The Room Booking Application is a system that let users to book university classrooms and laboratories. This application is thinked for the teachers who need a classroom for seminaries or lectures and for the students who need workstations in the laboratories.

## 2. Functional requirements
The application has the following functional requirements:

- A user shall be able to log in as a `teacher` or a `student`.
- A user shall be able to search for the available rooms.
- A user shall be able to book a room.
- A user shall be able to modify or delete a booking made by him.
- The system shall provide an interface for the user to read a list of available rooms and book one of them.
- The system shall provide an interface for the user to read the rooms booked by him and may update or delete a booking.

Below the use cases schema:

![Use Cases](/schemas/task1/UseCases.png)

## 3. Non-functional requirements
The application has the following non-functional requirements:

- The application must be written in Java.
- The application must be run once at time.
- The set, update and delete operations on the database must be atomic.
- The classrooms and the laboratories can be booked only for the day after.
- The classrooms can be booked for two different schedules in the day: Morning, Afternoon.
- The laboratories can be booked only for all the day.
- The classroom in a specified schedule can be booked by only one teacher.

![Classes](/schemas/task1/ClassesUML.png)

## 4. Data Model
The entities and their relations are described in the following diagram:

![Data Model](/schemas/task1/Entities.png)

We implemented the data model over a relational database. In the following image there is the entity-relation schema extracted using MySQL Workbench:

![ER Workbench](/schemas/task1/ER.png)

## 5. Software architecture

The application is standalone, mono-process and single-thread. The application is multi-layer. A data access layer carries out the comunication with the database layer and all the operations related to the data management: insert, read, update and delete. A user interaction layer carries out the interaction with the user and the comunication with the data access layer below.

![Architecture](/schemas/task1/Architecture.png)

## 6. User manual
