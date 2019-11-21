# Workgroup activity report - Feasibility Study on the use of a Key-Value Data Storage

## Table of contents
1. [Introduction](#1-introduction)
2. [Problem](#2-problem)
3. [Possible solutions](#3-possible-solutions)
4. [LevelDB](#4-levelDB)
5. [Solution](#5-solution)

## 1. Introduction
This application can be a useful tool for teachers and students in a University context. As shown in the use case diagram of the [Design Document](./Design.md) the system must perform a reading operation for each user action. These operations need views on the relational database. Each time that a teacher wants to book a classroom in a specific schedule, the system must retreive in the database the informations about the available classrooms, their capacity and the building in which they are located. All these informations must be shown together to the teacher in order to let him choose the classroom that respects his needs. These operations could be expensive,for this reason we want to explore the possibility to improve the system performances exploiting different databases tecnologies.

## 2. Problem
A possible data model for the application exploiting a relational database solution could involve a table for each entity of the application domain, such as: teacher, classroom, building and classroom booking in which informations about the booking of every teacher are stored. In this scenario a view is needed to show to the teacher useful informations about the available rooms joining classroom and building table in the database. Furthermore, also the write operations are expensive. For example, the information about classroom availability is stored as a boolean attribute in the classroom table. If needed, the availability must be checked and updated to mantain the consistency of the information every time that a teacher books a classroom. Also the operation of checking the availability of the classroom involves a join between multiple tables, in this case: teacher, classroom booking and classroom. Like in the teacher case, this also occurs in the case of students that want to see or book a laboratory. The amount of performed operations could affect the performance of the system.

## 3. Possible solutions
Facing the reading operations problem discussed so far, a possible solution to speed up these operations is using a no-relational database. One of the fastest and simplest kind of no-relational database is the key-value database. Key-value databases have different advantages and disadvantages.
The advantages are:
- Data are stored in key-value pairs, this makes reading and writing operations very simple.
- Values and keys can be anything, including JSON files.
- Data are stored in the main memory, this makes operations fast.
- Key-Value databases are flexible and schemaless, there is no need to have constraints between entities.

The disadvantages are:
- The join operation are not supported.
- The range queries are not supported.
- Optimized only for data with single key and value. A parser is required to store multiple values.

There are several implementation of key-value databases. Two of the simplest solutions for our system are [haloDB](https://github.com/yahoo/HaloDB) from Yahoo and [levelDB](https://github.com/google/leveldb) from Google. Since the problem is to have fast reading operation on views and range queries and considering that HaloDB is not suitable for this kind of problem, levelDB is the best fit for our system.

## 4. LevelDB
LevelDB is a simple embedded database with fast in memory read/write operations. There are no limitations on key and value size, both are stored in arrays sorted by key. LevelDB supports batch operations and iterations over data, but doesn't support indexing. LevelDB supports only a single process to access a particular database at time.

Since levelDB is implemented in C++ the [JNI interface](https://github.com/fusesource/leveldbjni) will be used.

## 5. Solution
Since the relational database is implemented as core database of the system, the levelDB database will be used to speed up the operations that are often performed by the system. This means that levelDB will be used to store informations that are retrived as view in the relational database, such as the available rooms and the booked rooms both for teachers and students. Furthermore, given the need of retrieving more than one room and booking at time the keys will be constructed to easily retrieve a range of values. For this aim the levelDB database will be composed by two buckets: available and bookings. Because of the fact that retrieving multiple object at a time in key-value databases is not possible, information often required together are putted in the same bucket for faster retrieval. The available bucket contains information about available rooms, that are classrooms in the case of teachers and laboratories in the case of students. The booking bucket contains the bookings made by the user. All the data needed will be retrieved from MySQL at the beginning of the user session and stored in levelDB. LevelDB will be emptied at the end of the session. Concerning the write operations, the data consistency in both and between databases is always ensured.

### 5.1 Keys schema

Keys for the 'available' bucket:

````
$roomtype:$roomId:roomname
$roomtype:$roomId:buildingname
$roomtype:$roomId:roomcapacity
$roomtype:$roomId:available
````
The available attribute contains the schedule in which the room are free in the case of classrooms and the number of available workstations in the case of laboratories.

Keys for the classrooms 'bookings' bucket:

````
$roomtype:$userId:$roomId:roomname
$roomtype:$userId:$roomId:schedule
````

Keys for the laboratories 'bookings' bucket:
````
$roomtype:$userId:$roomId:roomname
````
In the case of teacher a booking is made by two rows for storing information about the schedule in which the classroom is booked.

### 5.2 Classrooms procedures
The available attribute in the case of classrooms can contains three values: "F", means all day available, "M", means available in morning, "A", means available in afternoon. When available attribute contains a value different from "F" the classroom can be booked only once more.

#### 5.2.1 Case 1 - All day availability
Available bucket state before the booking:

Available bucket:
````
cla:1:roomname = "a22"
cla:1:buildingname = "polo a"
cla:1:roomcapacity = "55"
cla:1:available = "f"	//available in both afternoon and morning

//other available rooms
````
Bookings bucket:
````
//other bookings
````

If a teacher, whit id=5, books the "a22" room for the afternoon, a new booking is inserted in the bookings bucket with the schedule equals to "a", and the availability value of the room will change to "m", because now the room will be only available in the morning.

Database state after the booking:

Available bucket:
````
cla:1:roomname="a22"
cla:1:buildingname="polo a"
cla:1:roomcapacity="55"
cla:1:available="m"	//available only in the morning

//other available rooms
````

Bookings bucket:
````
cla:5:1:roomname="a22"
cla:5:1:schedule="a"

//other bookings
````

When this booking will eventually be deleted and the room is still not booked in the morning, the availability value in the available bucket will be set to "f".

#### 5.2.2 Case 2 - Part of the day availability
Database state before the booking:

Available bucket:
````
cla:1:roomname="a22"
cla:1:buildingname="polo a"
cla:1:roomcapacity="55"
cla:1:available="a"	//available only in the afternoon

//other available classrooms
````

Bookings bucket:
````
//other bookings
````
In this case the room availability is equal to "a", it means that someone already booked it in the morning.
If a teacher, whit id=5, books the "a22" room in the afternoon, a new booking is inserted in the bookings bucket, and all the informations about the room will be deleted from the available bucket, because now it is not available anymore.

Database state after the booking:

Available bucket:
````
//other available classrooms
````

Bookings bucket:
````
cla:5:1:roomname="a22"
cla:5:1:schedule="a"

//other bookings
````

When this booking will eventually be deleted, the room will become available again, so all the informations about the room will be inserted in the available bucket with the availability value equal to "a".

### 5.3 Laboratories procedures
The available attribute in the case of laboratories contains the number of available workstations. When available attribute contains "1" the laboratories can be booked only once more.

#### 5.3.1 Case 1 - More than one workstation available
Database state before the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="23"    //23 free workspace left

//other available laboratories
````

Bookings bucket:
````
//other bookings
````
In this case the room availability is equal to "23", it means that there are 23 workstations left.
If a student, whit id=1, books the "si4" room, a new booking is inserted in the bookings bucket, and the availability value of the room will change to "22".

Database state after the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="22"    //22 free workspace left

//other available laboratories
````

Bookings bucket:
````
lab:1:3:roomname="si4"

//other bookings
````
When a booking for this lab will eventually be deleted, the availability value in the available bucket will be incremented by 1.

#### 5.3.2 Case 2 - One workstation available

Database state before the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="1"    //this room is almost full, 1 seat left

//other available laboratories
````

Bookings bucket:
````
//other bookings
````
In this case the available attribute is equal to 1, it means that after this booking the laboratory will be full.
If a student, whit id=1, books the "si4" room, a new booking is inserted in the bookings bucket, and all the informations about the lab will be deleted from the available bucket, because now it is not available anymore.

Database state after the booking:

Available bucket:
````
//other available laboratories
````

Bookings bucket:
````
lab:1:3:roomname="si4"

//other bookings
````
When a booking for this laboratory will eventually be deleted, the laboratory will be available again, so all the informations about the laboratory will be inserted in LevelDb with the availability value equal to 1.

It is important to notice that all the laboratory informations will be deleted/inserted from the available bucket only when all the workstations are booked.
