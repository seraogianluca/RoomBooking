# Making annotations and writing CRUD operations in JPA

## 1. Introduction

In this tutorial we will explain how to manage one-to-many and many-to-many relationships and how to making simple CRUD operation on related entities with Hibernate implements JPA.

You can read more about JPA on [Java EE 8 Official Documentation](https://javaee.github.io/javaee-spec/javadocs/).

## 2. Entities

Entities in JPA are representations of the data that can be persisted to the database. In particular, an entity represents a table in the database and each instance of it represents a row of the table. Entities are mapped on Java's classes through Annotations.
To map an entity we need to specify the `@Entity` annotation. The table name and columns can be specified with the `@Table` and the `@Column` annotations respectively.

In the 

````java
@Entity
@Table(name = "table_name")
public class Entity {
    @Id
    @Column(name = "column_name")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    //other columns

    //getters and setters
}
````

## 3. One-to-many relation

### Data Model
Let's show a simple example of an Application were we can Booking a Room,  


 with the model Teacher and ClassroomBookings entities,


 in which the ClassroomBooking and teacher are related by the Teacher_id attribute, which is a foreign key for the ClassroomBooking and a primary key for Teacher. In bidirectional relationship both side navigation is possible, this means that we exploit a one to many relation from Teacher to ClassroomBooking and a many to one relation from ClassroomBooking to Teacher: the Teacher can book multiple rooms, and a booking can be associated to just one Teacher.


* Coding Example 
In this part of the tutorial, we will show how to implement a one-to-many bidirectional mapping using JPA. Let's consider the following one-to-many relation between teacher and classroom booking entities:

![one_to_many](/schemas/task1/one_to_many.png)

A <mark style="background-color: lightgrey">one-to-many</mark> relation between these two entities means that a teacher can do multiple reservations and each reservation is related to the teacher that did it.
The bidirecationality means that the relation can be navigated from both sides.  

## 4. Many-to-many relation

## 5. Simple CRUD operations