# Workgroup activity report - Feasibility study

## Table of contents
1. [Introduction](#1-introduction)
2. [Problem](#2-problem)
3. [Possible solutions](#3-possible-solutions)
4. [LevelDB](#3-levelDB)
5. [Solution](#3-solution)

## 1. Introduction
This application can be a useful tool for teachers and students belonging to University. As shown in the use case diagram of the [Design Document](./Design.md) the system must perform a reading operation for each user action. Furthermore, these operations could be expensive, for example views on a relational database. Each time that a teacher wants to book a classroom in a specific schedule, the system must retreive in the database the informations about the available classrooms, their capacity and the building in which they are located. All these informations must be shown together to the teacher in order to let him choose the classroom that respects his needs. 
For this reason we want to explore the possibility to improve the system performances exploiting different databases tecnologies.

## 2. Problem
A possible data model for the application exploiting a relational database solution could involve a table for each entity of the application domain, such as: teacher, classroom, building and classroom booking in which information about the booking of each teacher are stored. In this scenario to show to the teacher useful informations about the available rooms, a view, joining classroom and building table in the database, is needed. Furthermore, also the write operations are expensive. For example, in the classroom table we store information about its availability as a boolean attribute in the table. Every time that a teacher books a classroom the availability must be checked and updated to mantain the consistency of the information if needed. Also the operation of checking the availability of the classroom involves a join between multiple tables, in this case: teacher, classroom booking and classroom. Likewise the teacher case, this occurs also in the case of students that want to see or book a laboratory. The amount of performed operations could affect the performance of the system.

## 3. Possible solutions
Facing the reading operations problem discussed so far, a possible solution is to speed up these operations using a no-relational database. One of the fastest and simplest kind of no-relational database is the key-value database.
These database brings different advantages with them:
- Data are stored in key-value pairs, and this makes reading and writing operations very simple.
- Values and keys can be anything, including JSON files.
- Data are stored in the main memory, and this makes operations fast.
- Key-Value databases are flexible and schemaless, there is no need to have constraints between entities.

(we can build the keys in the format that best fit our need in different cases)

The disadvantes of key-value databases are:   (rivedere)
- No support for join
- No native support for range queries
- Optimized only for data with single key and value. A parser is required to store multiple values.
- Not optimized for lookup. Lookup requires scanning the whole collection or creating separate index values





        haloDB 
            - Key size is restricted to 128 bytes.
            - HaloDB don't support range scans or ordered access.
        levelDb
            - This is not a SQL database. It does not have a relational data model, it does not support SQL queries, and it has no support for indexes.
            - Only a single process (possibly multi-threaded) can access a particular database at a time.
            - There is no client-server support builtin to the library. An application that needs such support will have to wrap their own server around the library.
## 4. LevelDB
    WHY LEVELDB 
        simple
        fast 
        low memory
        no constraints on key and values
        iteration support 
        easy to implement

## 5. Solution
    Solution
        updated views stored in leveldb    
        to show views



