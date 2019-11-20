# Workgroup activity report - Feasibility Study on the use of a Key-Value Data Storage

## Table of contents
1. [Introduction](#1-introduction)
2. [Problem](#2-problem)
3. [Possible solutions](#3-possible-solutions)
4. [LevelDB](#4-levelDB)
5. [Solution](#5-solution)
6. [Implementation](#6-implementation)

## 1. Introduction
This application can be a useful tool for teachers and students belonging to University. As shown in the use case diagram of the [Design Document](./Design.md) the system must perform a reading operation for each user action. Furthermore, these operations could be expensive, for example views on a relational database. Each time that a teacher wants to book a classroom in a specific schedule, the system must retreive in the database the informations about the available classrooms, their capacity and the building in which they are located. All these informations must be shown together to the teacher in order to let him choose the classroom that respects his needs. 
For this reason we want to explore the possibility to improve the system performances exploiting different databases tecnologies.

## 2. Problem
A possible data model for the application exploiting a relational database solution could involve a table for each entity of the application domain, such as: teacher, classroom, building and classroom booking in which information about the booking of each teacher are stored. In this scenario to show to the teacher useful informations about the available rooms, a view, joining classroom and building table in the database, is needed. Furthermore, also the write operations are expensive. For example, in the classroom table we store information about its availability as a boolean attribute in the table. Every time that a teacher books a classroom the availability must be checked and updated to mantain the consistency of the information if needed. Also the operation of checking the availability of the classroom involves a join between multiple tables, in this case: teacher, classroom booking and classroom. Likewise the teacher case, this occurs also in the case of students that want to see or book a laboratory. The amount of performed operations could affect the performance of the system.

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

LevelDB is simple embedded database with fast in memory read/write operations. There are no limitations on key and value size, both are stored in arrays sorted by key. LevelDB supports batch operations and iterations over data, but doesn't support indexing. LevelDB supports only a single process to access a particular database at time.

Since levelDB is implemented in C++ the [JNI interface](https://github.com/fusesource/leveldbjni) will be used.

## 5. Solution
Since the relational database is implented as core database of the system, the levelDB based database will be used to speed up the operations that are often performed by the system. This means that levelDB will be used to store informations that are retrived as view in the relational database, such as the available rooms and the booked rooms both for teachers and students. Furthermore, given the need of retrieving more than one room at time the keys will be constructed to easily retrieve a range of values through iteration on keys. In levelDB, data will be stored in a way that queryes in the relation database are easier. All the data needed will be retrieved from mySQL and stored in levelDB at the beginning of the session as soon as the authentication is done and only once in a session. Concerning the write operations, the data consistency in both and between databases is always ensured.

## 6. Implementation
The aim of the key value is to mantain updated and fast to retrieve data that are often needed like the list of the available rooms and the list of the bookings made by the user.
With different kinds of users the database will be filled with different kinds of data. If the user is a teacher the 'available' bucket will be filled with free classrooms informations, if the user is a student it will be filled with free laboratories informations. 
The bookings of that specific user will be stored in the 'bookings' bucket. 

### 6.1 Keys schema

Keys for the 'available' bucket:
````
$roomtype:$roomId:roomname=""
$roomtype:$roomId:buildingname=""
$roomtype:$roomId:roomcapacity=""
$roomtype:$roomId:available=""
````
In the classroom case the rows that are inserted in the bookings bucket are two because there is the need to store the schedule in which the classroom is booked. For the laboratories you only book a seat for all the day, so the row that is inserted in the bookings bucket is one.

Keys for the classrooms 'bookings' bucket:
````
$roomtype:$userId:$roomId:roomname=""
$roomtype:$userId:$roomId:schedule=""
````
Keys for the laboratories 'bookings' bucket:
````
$roomtype:$userId:$roomId:roomname=""
````


### 6.2 Classrooms procedures
In the available bucket you can only find the list of the available classrooms with the corresponding free schedule. Because of the fact that a classroom can be booked only for two different schedule, when the available value is different from "f" (free fullday) it means that the room can be booked for just once more.

#### 6.2.1 Case 1. Availability = "f".

Database state before the booking:

Available bucket:
````
cla:1:roomname="a22"
cla:1:buildingname="polo a"
cla:1:roomcapacity="55"
cla:1:available="f"	//available in both afternoon and morning

//...other available rooms...//
````
Bookings bucket:
````
//...other bookings...//
````

In this case, if a teacher (whit id=5) books the "a22" room for the afternoon, a new booking is inserted in the bookings bucket with the schedule equals to "a", and the availability value of the room will change to "m", because now the room will be only available in the morning.

Database state after the booking:

Available bucket:
````
cla:1:roomname="a22"
cla:1:buildingname="polo a"
cla:1:roomcapacity="55"
cla:1:available="m"	//available only in the morning

//...other available rooms...//
````
Bookings bucket:
````
cla:5:1:roomname="a22"
cla:5:1:schedule="a"

//...other bookings...//
````

When this booking will eventually be deleted, the availability value in the available bucket will be set to "f".

#### 6.2.2 Case 2. Availability != "f".

Database state before the booking:

Available bucket:
````
cla:1:roomname="a22"
cla:1:buildingname="polo a"
cla:1:roomcapacity="55"
cla:1:available="a"	//available only in the afternoon

//...other available classrooms...//
````
Bookings bucket:
````
//...other bookings...//
````
In this case the room availability is equal to "a", it means that someone already booked it in the morning.
If a teacher (whit id=5) books the "a22" room in the afternoon, a new booking is inserted in the bookings bucket, and all the informations about the room will be deleted from the available bucket, because now it is not available anymore.

Database state after the booking:

Available bucket:
````
//...other available classrooms...//
````
Bookings bucket:
````
cla:5:1:roomname="a22"
cla:5:1:schedule="a"

//...other bookings...//
````

When this booking will eventually be deleted, the room will become available again, so all the informations about the room will be inserted in the available bucket with the availability value equal to "a".

### 6.3 Laboratories procedures

In the laboratory available bucket you can only find the list of the available laboratories with the corresponding free workstations. The available value rapresent the number of remaining free workstations before the room gets completely full. When a laboratory has the available value equal to 1: it means that after this booking it will not be available anymore.

#### 6.3.1 Case 1. Availability != "1".

Database state before the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="23"    //23 free workspace left

//...other available laboratories...//
````
Bookings bucket:
````
//...other bookings...//
````
In this case the room availability is equal to "23", it means that there are 23 workstations left.
If a student (whit id=1) books the "si4" room, a new booking is inserted in the bookings bucket, and the availability value of the room will change to "22", because now the room has lost a workstation.

Database state after the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="22"    //22 free workspace left

//...other available laboratories...//
````
Bookings bucket:
````
lab:1:3:roomname="si4"

//...other bookings...//
````
When a booking for this lab will eventually be deleted, the availability value in the available bucket will be incremented by 1.

#### 6.3.2 Case 2. Availability = "1".

Database state before the booking:

Available bucket:
````
lab:3:roomname="si4"
lab:3:buildingname="polo b"
lab:3:roomcapacity="150"
lab:3:available="1"    //this room is almost full, 1 seat left

//...other available laboratories...//
````
Bookings bucket:
````
//...other bookings...//
````
In this case the lab availability is equal to "1", it means that after this booking the lab will be full.
If a student (whit id=1) books the "si4" room, a new booking is inserted in the bookings bucket, and all the informations about the lab will be deleted from the available bucket, because now it is not available anymore.

Database state after the booking:

Available bucket:
````
//...other available laboratories...//
````
Bookings bucket:
````
lab:1:3:roomname="si4"

//...other bookings...//
````
When a booking for this lab will eventually be deleted, the lab will become available again, so all the informations about the lab will be inserted in LevelDb with the availability value equal to 1.

So it is important to notice that all the lab informations will be deleted/inserted from the available bucket only when all the seats are gone. It is not something that happens for every booking. 
