# Making annotations and writing CRUD operations in JPA

## 1. Introduction

In this tutorial we will explain how to manage `one-to-many`, `many-to-one` and `many-to-many` relationships with Java Hibernate Framework implements the specifications of JPA (Java Persistance API) for data persistence.

You can read more about JPA on [Java EE 8 Official Documentation](https://javaee.github.io/javaee-spec/javadocs/).

<Later we will explain to you how to making simple CRUD operation on related entities.>

### 1.1 Entities

Entities in JPA are representations of the data that can be persisted to the database. In particular, an entity represents a table in the database and each instance of it represents a row of the table. Entities are mapped on Java's classes through Annotations.
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

The `@Id` annotation represent the table primary key and can be used only once. The `@GeneratedValue` annotation is for the auto generated keys. The `strategy = GenerationType.IDENTITY` option indicates that the primary keys for the entity is assigned using a database identity column. The `@Table` and `@Column` are not mandatory but in case omitted the class and the field need to have the same name of the database table and attribute. 

### 1.2 Relations

JPA supports the same relations as the relational databases. The relations can be:

* One-to-many
* Many-to-one
* Many-to-many

Each of these relations can be mapped as bidirectional and bidirectional association. This means that you can model them as an attribute on only one of the associated entities or both. The bidirectional association mapping is the most common way to model this relationship with JPA and Hibernate. 

The annotations for map the different kind of relation are self explaining: `@OneToMany`, `@ManyToOne`, `@ManyToMany`.

Hibernate manage these relations creating a join table on the database. On the `one-to-many` and `many-to-one` relations this behaviour can be avoided specifying on an attribute the `@JoinColumn` annotation, this means that the relation will be mapped on an attribute as foreign key instead of a join table. On the `many-to-many` this behaviour can't be avoided but the join table can be managed through the `@JoinTable` annotation.

In the following the `one-to-many` and the `many-to-many` will be explained in more details.

## 2. One-to-many relation

One to many means that one row in a table is mapped to multiple rows in another table.

Let's consider the relation between `teacher` and `classroom_booking` entity. 

**Entity Relationship Diagram**

![one_to_many](/schemas/task1/one_to_many.png)

In such case, **one teacher can have many reservations of classrooms (classroom_booking)**, so here we have a *one-to-many* mapping.

The way this works at the database level is we have *TEACHER_ID* as a primary key in the *teacher* table and also a *TEACHER_ID* as a foreign key in *classroom_booking*.

In this side the attribute that models the association is *teacher*, and the relationship annotation between the join tables is `@OneToMany`. 

Let's map the *Teacher* class to the Collection *ClassroomBooking* it a way that reflects the relationship in the database:

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

Please note that the `@OneToMany` annotation is used to define the property in *teacher* class that will be used to map the `mappedBy` variable. That's why we have a property named *teacher* in the *classroom_booking* class.

The `@Fetch` decides on whether or not to load all the data belongs to associations as soon as you fetch data from parent table. Fetch type supports two types of loading: 

- **LAZY**: means that the child entities are fetched only when you try to access them.
- **EAGER**: means that the child entities are fetched at the time their parent is fetched. 

## 3. Many-to-one relation

Many to one relationship is where one entity contains values that refer to another entity that has unique values.

Consider the following relationship between *classroom_booking* to *teacher* entity. 

**Entity Relationship Diagram**

![many_to_one](/schemas/task1/many_to_one.png)

According to the relationship *many-to-one*, **many reservations of classrooms (classroom_booking) can have one teacher**. 

the attribute that models the association is *classroom_booking*, and the relationship annotation between the join tables is `@ManyToOne`. 

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

The `@JoinColumn` annotation references the mapped column adn helps Hibernate to figure out that there is a *TEACHER_ID* foreign key column in the *classroom_booking* table that defines is associated with *teacher* table. It's an optional annotation.

In this side, we are making this a **bidirectional** relationship, because we are able to access *classroom_booking* from *teacher*, and also *teachers* from *classroom_booking*.

It's a good practice to mark *many-to-one* side as the **owning** side. We mark like **inverse** side the *teacher* class including the `mappedBy` attribute.

## 4. Many-to-many relation

A Many to Many relationship occurs when multiple records in a table are associated with multiple records in another table. 

Let’s take a look at the relationship mapping between *laboratory* and *student* entity. 

**Entity Relationship Diagram**

![many_to_many](/schemas/task1/many_to_many_table.png)

The Set *laboratory* attribute models the association in the domain model and the `@ManyToMany` annotation tells Hibernate the unidirectional *many-to-many* relationship .

In real life, a **student will book several laboratories simultaneously, while a laboratory will be occupate by several students at a time**.

In the example code below the **parent** side of the association is the *laboratory*, it means that the association is defined in this side and the **child** side, *student* entity refers to it.

The following code snippet shows a mapping that tells Hibernate to use the *laboratory_booking* table with the *LABORATORY_ID* column as the foreign key to the *laboratory* table and the *STUDENT_ID* column as the foreign key to the *student* table.

The model classes *Laboratory* and *Student* need to be created with JPA annotations:

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

If you don’t provide any additional information, Hibernate uses its default mapping which expects an association table with the name of both entities and the primary key attributes of both entities. But, in this example we set the table with the name *laboratory_booking* and with the columns *LABORATORY_ID* and *STUDENT_ID*.

Like the others annotations, we can customize the table with `@JoinTable` annotation and its attributes `@JoinColumns`. The *joinColumns* attribute defines the foreign key columns for the entity on which you define the association mapping. The `inverseJoinColumns` attribute specifies the foreign key columns of the associated entity.

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
As we can see, both the *laboratory* class and *student* classes refer to one another, which means that the association between them is **bidirectional**. 

In this example *many-to-many*, we mark *laboratory* class as the **owning** side and we mark the **inverse** side *Student* class including the `mappedBy` attribute. These are the same rules used for a *bidirectional one-to-many* relationship.

## 4. Execution

In order to see all the previous annotation in action, we can write the following JUnit test:

````java
/**
 * Unit test for RoomBooking App.
 */

public class RoomBookingTest {

    public HibernateManager manager = new HibernateManager();

    /**
    * Test 1. @OneToMany, @ManyToOne.
    * Get All the Classrooms from One Teacher 
    */
    @Test
    public void testGetTeacherClassroom() {
        try {
            manager.start();
            System.out.println("Fetching booked classroom by Teacher ID");
            Collection<Room> booked = new ArrayList<Room>();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@teacher.unipi.com", true);
            System.out.println(teacher.toString());
            booked.addAll(manager.getBooked(teacher));
            for (Room b : booked) {
                System.out.println(b.toString());
            }
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    /**
    * Test 2. @ManyToMany.
    * Get All the Laboratories from One Student 
    */
    @Test
    public void testGetLaboratoryStudent() {
        try {
            manager.start();
            System.out.println("Fetching booked laboratory.");
            Collection<Room> booked_lab = new ArrayList<Room>();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.com", false);
            System.out.println(student.toString());
            booked_lab.addAll(manager.getBooked(student));
            for (Room c : booked_lab) {
                System.out.println(c.toString());
            }
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }
}
````
We can see the *many-to-many* and the *many-to-one* relationship between the two entities created in the database. We saw how to create mappings on Hibernate with a simple Database structure.

![Demo](/schemas/task1/hibernate_mapped.png)

## 5. Simple CRUD operations

![CRUD](/schemas/task1/CRUD.png)

The commands corresponding to these operations in MySQL are `INSERT`, `SELECT`, `UPDATE`
### 5.1 Create Operation
````java
Collection<Room> laboratory = new ArrayList<Room>();
````

### 5.2 Read Operation
````java
public long getId() {
    return this.laboratoryId;
}
````

### 5.3 Update Operation

````java
public void setName(String name) {
    this.laboratoryName = name;
}
````
### 5.4 Delete Operation
````java
public void deleteBooking(Student student) {
    this.students.remove(student);
}
````

You can Install or Download the source code of this tutorial with a complete example over on [GitHub](https://github.com/seraogianluca/RoomBooking/tree/develop_task1).

![Demo](/schemas/task1/GitHub.gif)