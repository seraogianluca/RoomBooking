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
Since the relational database is implented as core database of the system, the levelDB based database will be used to speed up the operations that are often performed by the system. This means that levelDB will be used to store informations that are retrived as view in the relational database, such as the available rooms and the booked rooms both for teachers and students. Furthermore, given the need of retrieving more than one room at time the keys will be constructed to easily retrieve a range of values through iteration on keys. In levelDB, data will be stored in a way that queryes in the relation database are easier. All the necessary data in a user session will be stored at the beginning and only for a session. Concerning the write operations, the data consistency in both and between databases is always ensured.

## 6. Implementation
The aim of the key value is to keep updated and fast to retrieve data that are often used. For us are the list of the available rooms, and the booking made by the user.
The key value database will be filled with two different data flows based in the user type as soon as the authentication is done. If the user is a teacher the `avl` available bucket will be filled with free classrooms informations, if the user is a student it will be filled with free laboratories informations. 
In the `bkg` bookings bucket the bookings of that specific user will be stored.


Keys schema
--
Keys for the `avl` and the `bkg` bucket:
````
avl:$roomtype:$roomId:roomname=""
avl:$roomtype:$roomId:buildingname=""
avl:$roomtype:$roomId:roomcapacity=""
avl:$roomtype:$roomId:available=""
````
````
bkg:$roomtype:$userId:$roomId:roomname=""
bkg:$roomtype:$userId:$roomId:schedule=""
````

Example of the database in case there are 2 available classrooms:

Available bucket:

````
avl:cla:1:roomname="a22"
avl:cla:1:buildingname="polo a"
avl:cla:1:roomcapacity="55"
avl:cla:1:available="a"	//available only in afternoon

avl:cla:2:roomname="a11"
avl:cla:2:buildingname="polo a"
avl:cla:2:roomcapacity="43"
avl:cla:2:available="f"  //available in both afternoon and morning
````
Bookings bucket:
````
bkg:cla:5:1:roomname="a22"
bkg:cla:5:1:schedule="m"
````

Example when there are 2 available laboratories:

Available bucket:
````
avl:lab:3:roomname="si4"
avl:lab:3:buildingname="polo b"
avl:lab:3:roomcapacity="150"
avl:lab:3:available="23"    //23 free workspace left

avl:lab:2:roomname="si3"
avl:lab:2:buildingname="polo b"
avl:lab:2:roomcapacity="150"
avl:lab:2:available="1" //this means that this room is almost full
````
Bookings bucket:
````
bkg:lab:1:3:roomname="si4"
````
In the classroom available bucket you can only find the list of the available classrooms with the corresponding free schedule. Because of the fact that a classroom can be booked only for two different schedule, when the available value is different from "f"(free fullday) it means that that room can be booked for just one another time.
All the rows relative to a classroom in the available bucket will be deleted in case the classroom that is getting booked has the available value equal to "m" or "a": it means that after this booking it will not be available anymore.
When a booking is deleted the room become available again, so it must be inserted in the available bucket with the corresponding free schedule.

In the laboratory available bucket you can only find the list of the available laboratories with the corresponding free workstations. The available value rapresent the number of remaining free workstations before the room gets completely full.
All the rows relative to a laboratory in the available bucket will be deleted in case the laboratory that is getting booked has the available value equal to 1: it means that after this booking it will not be available anymore.

For every booking that is made, a new data in the `bkg` bucket is inserted.

In the classroom case the rows that are inserted in the `bkg` bucket are two:
````
bkg:cla:$userId:$roomId:roomname="a22"
bkg:cla:$userId:$roomId:schedule="m"
````
For the laboratories there is no need to store the schedule because you only book a seat, so the row that is inserted in the `bkg` bucket is one:
````
bkg:lab:$userId:$roomId:roomname="si4"
````

All the checking operations will be done by the DBSManager that calls the consistency functions for both SQL and LevelDB everytime there is an upload.
