# Workgroup activity report - Implementation

## Table of contents
1. [Introduction](#1-introduction)
2. [Classes and ER diagrams](#2-classes-and-er-diagrams)

## 1. Introduction
In this document are described the highlights of the implementation starting from the description of the uml diagram that include the integration of levelDB in the solution, discussed in [Feasibility Study on the use of a Key-Value Data Storage](./FeasibilityStudy.md), and the ER diagram of the relational database. Then, a short description of the relational database implementation using JPA, deepen in [Making annotations and writing CRUD operations in JPA](./Tutorial.md), and the levelDB implementation using the JNI of levelDB is carried out. For brevity, the complete description of the command line interface code is avoided.

## 2. Classes and ER diagrams
The solution is implemented as a monolithic multi-layer application with three layers: a user interaction layer, a data access layer and a database layer. The user interaction layer let the users to interact with the application for carrying out the actions described in the Design document. The data access layer is formed by a manager that coordinates the databases in the operations and provide an interface for the user interaction layer. In this layer are also included the databases driver that provide an interface for the database level below. LevelDB is implemented as an embedded database, conversely the relational database is used with MySQL server.
Below an high level description of the solution classes through a uml analysis classes diagram in which are added the entities related to levelDB:

![Analysis Classes](/schemas/task1/ClassesUML.png)

The relational model is implemented as follow:

![ER](/schemas/task1/ER.png) 