# Workgroup activity report - Design phase

## Table of contents
1. [Introduction](#1-introduction)
2. [Functional Requirements](#2-functional-requirements)
3. [Non-functional requirements](#3-non-functional-requirements)
4. [Data Model](#4-data-model) 
5. [Software Architecture](#5-software-architecture)
6. [Test](#6-test)
7. [User manual](#7-user-manual)

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

## 6. Test
The application was tested using the test dataset included in the database directory simulating all the use cases.

## 7. User manual

### 7.1 Startup and log in
The application at the startup ask to specify if the user is a student or a teacher before logging in:

```
 _____                         ____              _    _                       __   ___  
|  __ \                       |  _ \            | |  (_)                     /_ | / _ \ 
| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |
|  _  // _ \ / _ \| '_ ` _ \  |  _ < / _ \ / _ \| |/ / | '_ \ / _` | \ \ / /  | || | | |
| | \ \ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \ V /   | || |_| |
|_|  \_\___/ \___/|_| |_| |_| |____/ \___/ \___/|_|\_\_|_| |_|\__, |   \_(_)  |_(_)___/ 
                                                               __/ |                    
                                                              |___/                     

Are you a teacher or a student?
T - Teacher 
S - Student 
>
```

After choosing the user category the application ask for the user email:

```
Insert your Email > 
```

The main menu offer several actions that can be perfomed by the user:

```
1 - Book a Room.
2 - Delete a booking.
3 - Update a booking.
4 - Close.

Choose an action > 
```

### 7.2 Book a room

In case the user is a teacher the application ask the schedule in which book the room, in case the user is a student the book a room procedure start directly from the next step:

```
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The application shows a list of available rooms and asks to choose one of them:

```
List of the avaiable rooms:

ID    Room            Building                  Capacity  
===================================================================
3     B32             Polo B                    80        

Choose a room by ID > 
```

If the user selects the correct room the procedure ends with success:

```
Room succesfully booked.
```

### 7.3 Delete a booking

The application shows a list of the user's bookings and asks to choose one of them:

```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              
5     B32             m              

Choose the room you booked by ID > 
```
If the user selects the correct room the procedure ends with success:

```
Booking succesfully deleted.
```

### 7.4 Update a booking

The application shows a list of the user's bookings and asks to choose the one to be updated:
```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              

Choose the room you want to change by ID >

```
In case the user is a teacher the application ask the new schedule in which book the room, in case the user is a student the update procedure skips this step:

```
Choose the new schedule: 
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The application shows a list of available rooms and asks to choose one of them:

```
List of the avaiable rooms:

ID    Room            Building                  Capacity  
===================================================================
1     A13             Polo A                    80        
3     B32             Polo B                    80        

Choose a room by ID > 
```
If the user selects the correct room the procedure ends with success:

```
Booking succesfully updated.
```