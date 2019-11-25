# Workgroup activity report - Tutorial: Making annotations, writing CRUD operations, handling one-to-many and many-to-many relationships in JPA

## Table of contents

1. [Introduction](#1-introduction)
2. [Bidirectional One-to-many relation](#2-bidirectional-one-to-many-relation)
3. [Bidirectional Many-to-many relation](#3-bidirectional-many-to-many-relation)
4. [Simple CRUD operations](#4-simple-crud-operations)


## 1. Introduction

In this tutorial we will explain how to manage `one-to-many`, `many-to-one` and `many-to-many` relationships with the Hibernate Framework that implements the specifications of **JPA** (Java Persistance API) for data persistence.

Later, we will explain how to make simple **CRUD** operation on related entities in JPA.

You can read more about JPA on [Java EE 8 Official Documentation](https://javaee.github.io/javaee-spec/javadocs/).


### 1.1 Entities

Entities in JPA are representations of the data that can be persisted to the database. In particular, an entity represents a table in the database and each instance of it represents a row. Entities are mapped on Java's classes through Annotations.
To map an entity we need to specify the `@Entity` annotation. The table name and columns can be specified with the `@Table` and the `@Column` annotations respectively.

A simple enitity declaration is showed in the code below:

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

The `@Id` annotation represents the table primary key and can be used only once. The `@GeneratedValue` annotation is for the auto generated keys. The `strategy = GenerationType.IDENTITY` option indicates that the primary keys for the entity is assigned using a database identity column. The `@Table` and `@Column` are not mandatory but in case omitted the class and the field need to have the same name of the database table and attribute. 

### 1.2 Relations

JPA supports the same relations as the relational databases. The relations can be:

* One-to-many
* Many-to-one
* Many-to-many

Each of these relations can be mapped as unidirectional and bidirectional association. This means that you can model them as an attribute on only one of the associated entities or both. The bidirectional association mapping is the most common way to model this relations with JPA and Hibernate. 

The annotations to map the different kind of relation are self explaining: `@OneToMany`, `@ManyToOne`, `@ManyToMany`.

Hibernate manage these relations creating a join table on the database. On the `one-to-many` and `many-to-one` relations this behaviour can be avoided specifying on an attribute the `@JoinColumn` annotation, this means that the relation will be mapped on an attribute as foreign key. On the `many-to-many` this behaviour can't be avoided but the join table can be managed through the `@JoinTable` annotation.

## 2. Bidirectional One-to-many relation

A one to many relation means that one row in a table is related to multiple rows in another table.

Let's consider the relation between `teacher` and `classroom booking` entities: 

![one_to_many](/schemas/task1/one_to_many.png)

In such case, **one teacher can have many classrooms reservations**, that is a `one-to-many` relation.

At the database level *TEACHER_ID* is a primary key in the *teacher* table and a foreign key in *classroom_booking* table.

Every bidirectional association has parent and a child side. The parent side is the one in which the assciation is defined and the child is the one who refers to it. In this example, the teacher entity is the child side and the relation is mapped on the `teacher` field of the `classom booking` class.

The code below shows the mapping on the teacher side:

**Teacher.java**

````java
@Entity
@Table(name="teacher")
public class Teacher implements Person {
    @Id
    @Column(name="TEACHER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;

    // Other columns
    // ...

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teacher")
    private Collection<ClassroomBooking> classroomBookings = new ArrayList<ClassroomBooking>();

    // Getters and Setters
    // ...
}
````

Please note that in the `@OneToMany` annotation the `mappedBy = "teacher"` option is used to link the `classroomBookings` Collection field to the associated one in the parent side, in this case `teacher`.

The `fetch = FetchType.EAGER` option establishes whether or not the data belonging to association will be loaded when the entity is fetched. Fetch type is of two types: 

- **LAZY**: means that the child entities are fetched only when you try to access them.
- **EAGER**: means that the child entities are fetched at the time their parent is fetched. 

Let's consider the parent side of the relation. In this side, according to the relation *many-to-one*, **many classrooms reservations can have just one teacher**.

The code below shows the mapping of the classroom booking side:

**ClassroomBooking.java**

````java
import javax.persistence.*;

@Entity
@Table(name = "classroom_booking")
public class ClassroomBooking implements Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomBookingId;

    // Other columns
    // ...

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;
    
    // Getters and Setters
    // ...
}
````

The `@JoinColumn` annotation tells Hibernate to map the relation on the column of the table instead of creating a join table between the entities. This means that the relation will be mapped using foreign keys.

Mapping the both side make the relation bidirectional, so can be navigated from both sides. In the bidirectional relations it's a good practice to mark the *many-to-one* side as the **parent** side.

## 3. Bidirectional Many-to-many relation

A Many to Many relation occurs when multiple records in a table are associated with multiple records in the related one. 

Let’s consider the relation between `student` and `laboratory`: 

![many_to_many](/schemas/task1/many_to_many.png)

A **student may book several laboratories, while a laboratory may be booked by several students**.

In this example the parent side of the relation is `laboratory`, and the child side is `student`.

**Laboratory.java**
````java
@Entity
@Table(name = "laboratory")
public class Laboratory implements Room {
    @Id
    @Column(name = "LABORATORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laboratoryId;

    // Other columns
    // ...

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "laboratory_booking", joinColumns = {
            @JoinColumn(name = "LABORATORY_ID") }, inverseJoinColumns = { @JoinColumn(name = "STUDENT_ID") })
    private Collection<Student> students = new ArrayList<Student>();

    // Getters and Setters
    // ...
}
````
In the `many-to-many` relation the join table creation can't be avoided, but this table can be managed through the `@JoinTable` annotation. The `joinColumns` option defines the foreign key for the entity on which you define the association mapping. The `inverseJoinColumns` option specifies the foreign key of the associated entity. The default table name is the combination of both entity names, this can be modified specifying the `name` option. 

Below the code of the student side:

**Student.java**
````java
@Entity
@Table(name = "student")
public class Student implements Person {
    @Id
    @Column(name = "STUDENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;

    // Other columns
    // ...

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private Collection<Laboratory> laboratories = new ArrayList<Laboratory>();

    // Getters and Setters
    // ...
}
````
As the `one-to-many` case the parent side is specified through the `mappedBy` option.

## 4. Simple CRUD operations

In the following we will explain woth two short examples how to make **CRUD** operations on related entities.

![CRUD](/schemas/task1/CRUD.png)

The commands corresponding to these operations in MySQL are `INSERT` (adds new records), `SELECT` (selects existing records), `UPDATE` (modifies existing records) and `DELETE` (removes tables or records in a table).

Let's consider the entities and the relations discussed before. The `HibernateManager` class carries out **CRUD** operations on the entities.

**HibernateManager.java**
````java
public class HibernateManager {
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    // Implementation 
    public void setBooking(Student student, long roomId, String schedule) {...}
    public Collection<Laboratory> getBooked(Student student) {...}
    public void updateBooking(Student student, long oldRoomId, long newRoomId, long bookingId, String newSchedule) {...}
    public void deleteBooking(Student student, long bookingId) {...}
   
}
````
To make **CRUD** operations on the entities we use the entities instances, then we commit the actions in the database. 

In the example below, to book a room we read before the `classroom` we want to book from the database, then we create a new `classroom booking` instance. The new instance is initialized with the information about the `classroom booking` then stored in the database. We need to update also the related `classroom` entity adding in the list of `classroom booking` the one we create.

````java
public void setBooking(Student student, long roomId, String schedule) {
    try {

        entityManager.getTransaction().begin();
        Classroom classroom;
        ClassroomBooking classroombooking = new ClassroomBooking();

        //Retrieving the classroom
        classroom = entityManager.find(Classroom.class, roomId);

        //Creating the classroomBooking object
        classroombooking.setRoom(classroom);
        classroombooking.setSchedule(schedule);
        classroombooking.setPerson(person);

        //Store the classroomBooking on the database
        entityManager.persist(classroombooking); 
        
        //Update the classroom on the database
        classroom.setBooking(classroombooking);

        if (classroom.getBooking().size() == 2) {
            classroom.setAvailable(false);        
        }

        entityManager.merge(classroom);
        entityManager.getTransaction().commit();

    } catch (Exception ex) {
        ex.printStackTrace();
        entityManager.close();
    }
}
````
In the next example, to delete a booking we read the `classroom booking` we want to remove from the database and the related `classroom`. We need to remove the elements from the respective lists and then remove `classroom booking` from the database and update `classroom`.

````java
public void deleteBooking(Person person, long bookingId) {
    try {
        entityManager = factory.createEntityManager();
        //Retreiving the room and the reservation to delete and do it.
        entityManager.getTransaction().begin();
        ClassroomBooking classroomBooking = entityManager.find(ClassroomBooking.class, bookingId);
        Classroom classroom = entityManager.find(Classroom.class, classroomBooking.getRoomId()); 
        classroom.deleteBooking(classroomBooking);

        //Check if the room become available then update it.
        if (classroom.getBooking().size() < 2 && !classroom.getAvailable()) {
            classroom.setAvailable(true);
        }
                
        entityManager.remove(classroomBooking); 
        entityManager.merge(classroom);
        entityManager.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }
}
````

You can Install or Download the source code of this tutorial with a complete example over on [GitHub](https://github.com/seraogianluca/RoomBooking/tree/develop_task1).

![Demo](/schemas/task1/GitHub.gif)
