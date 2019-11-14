# Workgroup activity report - Feasibility Study on the use of a Key-Value Data Storage

## Table of contents
1. [Introduction](#1-introduction)
2. [Problem](#2-problem)
3. [Possible solutions](#3-possible-solutions)
4. [LevelDB](#4-levelDB)
5. [Solution](#5-solution)

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


       

