# Workgroup activity report - Implementation

## Table of contents
1. [Introduction](#1-introduction)
2. [Main functionalities](#2-main-functionalities)
3. [JPA Entities](#3-jpa-entities)

## 1. Introduction
In this document are described the highlights of the implementation starting from the description of the classes model, that include the integration of levelDB in the solution, discussed in [Feasibility Study on the use of a Key-Value Data Storage](./FeasibilityStudy.md), and the ER diagram of the relational database.
Then, the use cases are described. For brevity, only the one of the use case is reported with the code.

## 2. Main functionalities
The solution is implemented as a monolithic multi-layer application with three layers. The interaction with the user is made through a Command Line Interface that let different kind of user to perform different actions. The middleware consists of a manager class that coordinates the operations between the two databases:

The key-value database, as described in the [Feasibility Study on the use of a Key-Value Data Storage](./FeasibilityStudy.md), stores the user most frequently used informations like: list of the available rooms and list of the user booking.

The relational database stores all the system information like: users, bookings, rooms and buildings. The Java Persistence API is used for managing operations on MySQL.

The read operations are made exclusively on LevelDB and the insert and update operations are made using both databases.

Following an analysis classes diagram of the application, including both databases:

![Analysis Classes](/schemas/task1/ClassesUML.png)

The relational model is implemented as follow:

![ER](/schemas/task1/ER.png)

The database layer includes MySQL for the relational database and LevelDB for the key-value database.

## 3. Log in / Log out
A user can log into the system through the accademic email. Once the email is inserted by the user, the role is checked. If the user is a teacher or a student the application menu let these kinds of user to book a new room (a classroom for teacher, a laboratory workstation for student), delete a previous booking, update a previous booking, close the application. If the user is an administrator the application menu let the user to insert a new students and teachers, insert a new room and eventually a new building, close the application.

## 4. Set booking
A teacher can book a classroom in a choosen schedule at time (Morning, Afternoon). The procedure starts with the set booking command in the main menu. The system ask to the teacher to choose in which schedule she want to book the classroom (Morning, Afternoon), then the system shows a list of the classrooms available in the choosed schedule. The system ask to the teacher to choose a classroom using the room ID. Once the teacher choose the classroom, the system starts to operate on the databases. In details:
- The information about the booking are puttend in levelDB and MySQL
- The classroom availability is checked
- If the classroom was free for all the day, the availability of the classroom is updated in levelDB
- If the classroom was free only for the choosen schedule, the availability is updated on MySQL (set the classroom unavailable) and the information about the classroom from levelDB are deleted.

If the procedure on the databases ends with success an acknowledgement is showed to the teacher.

A student can book a workstation in the laboratory. The procedure starts with the set booking command in the main menu. THe system shows a list of the laboratories with available workstations. The system ask to the user to choose a laboratory using the room ID. Once the student choose the classroom, the system starts to operate on the databases. In details:
- The information about the booking are puttend in levelDB and MySQL
- The laboratory availability is checked
- If the laboratory has more than one workstation free, the availability of the laboratory is updated in levelDB
- If the laboratory has only one workstation available, the availability is updated on MySQL (set the laboratory unavailable) and the information about the classroom from levelDB are deleted.

If the procedure on the databases ends with success an acknowledgement is showed to the student.

## 5. Delete booking
A teacher can delete one of the booking she made. The procedure starts with the delete booking command in the main menu. The system shows a the list of bookings made by the teacher. The system ask to the teacher to choose a booking using the booking ID. Once the teacher choose the booking, the system starts to operate on the databases. In details:
- The availability of the classroom is checked
- The booking is deleted
- If the classroom was unavailable the availability is updated on MySQL and all the information about the classroom are putted in levelDB. The schedule in which the classroom was booked is putted as schedule in which the classroom is available.
- If the classroom was available the availability is updated on levelDB. The schedule in which the classroom is available is all the day.

If the procedure on the databases ends with success an acknowledgement is showed to the teacher.

A student can delete one of the booking she made. he procedure starts with the delete booking command in the main menu. The system ask to the student to choose a booking using the room ID. Once the student choose the booking, the system starts to operate on the databases. In details:
- The availability of the laboratory is checked
- The booking is deleted
- If the laboratory was unavailable the availability is updated on MySQL and all the information about the laboratory are putted in levelDB. One is putted in the availability information about the laboratory, that means one workstation available.
- If the laboratory was available the availability is updated on levelDB. The available number of workstation is increased by one.

If the procedure on the databases ends with success an acknowledgement is showed to the student.

## 6. Update booking
The update booking procedure is a sum of the delete booking and the set booking, performed in this order.

## 7. Example code
Following the code example of the set booking procedure. Starting from the user interface towards the entities of the databases.

### 7.1 Interface - Set booking
```java
private static void bookARoom() {
	String requestedSchedule = null;
	String requestedRoom = null;
	boolean isValid = false;
	Available room = null;
	Collection<Available> availableRooms;

    if (user.getRole().equals("T")) {
		requestedSchedule = setSchedule();
	}

	availableRooms = database.getAvailable(requestedSchedule, user.getRole());
    
    if (availableRooms.size() == 0) {
		out.println(RED + "No available rooms\n" + WHITE);
		return;
	}

	showAvailable(availableRooms);
	while (!isValid) {
		out.print("\nChoose a room by ID > ");
		requestedRoom = input.next();

        for (Available i : availableRooms) {
			if (i.getId() == Long.parseLong(requestedRoom)) {
				isValid = true;
				room = i;
				break;
			}
		}

		if (!isValid) {
			out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
		} else {
			database.setBooking(user, room, requestedSchedule);
			out.println(GREEN + "\nRoom succesfully booked." + WHITE);
		}
	}
}
```

### 7.2 Databases Manager - Set Booking

```java
public void setBooking(User user, Available roomToBook, String requestedSchedule) {
    try {
        if (user.getRole().equals("T")) {
            hibernate.setClassroomBooking(user.getId(), roomToBook.getId(), requestedSchedule);
            ClassroomBooking newBook = hibernate.getClassroomBooking(roomToBook.getId(), user.getId(), requestedSchedule);
            levelDb.putBooked(user.getId(), roomToBook.getType(), newBook.getId(), roomToBook.getRoom(), requestedSchedule);
        } else {
            hibernate.setLaboratoryBooking(user.getId(), roomToBook.getId());
            levelDb.putBooked(user.getId(), roomToBook.getType(), roomToBook.getId(), roomToBook.getRoom(), null);
        }

        if (checkAvailability(roomToBook)) {
            levelDb.deleteFromAvailable(roomToBook.getType(), roomToBook.getId());
            hibernate.updateAvailability(roomToBook.getType(), roomToBook.getId(), false);
        } else {
            if(user.getRole().equals("T")) {
                levelDb.updateClassroomAvailability(roomToBook.getId(), requestedSchedule);
            } else {
                levelDb.decreaseLaboratoryAvailability(roomToBook.getId());
           }
        }
    } catch (IOException ioe) {
        ioe.printStackTrace();
    }
}
```
### 7.3 LevelDB driver - Set Booking functions

```java
public void putBooked(long userId, String roomType, long roomId, String roomName, String schedule)
		throws IOException {
	try {
		levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\booked"), options);
		String keyName = "bkg:" + roomType + ":" + userId + ":" + roomId + ":roomname";
		levelDb.put(bytes(keyName), bytes(roomName));

        if (roomType.equals("cla")) {
			String keySchedule = "bkg:" + roomType + ":" + userId + ":" + roomId + ":schedule";
			levelDb.put(bytes(keySchedule), bytes(schedule));
		}
	} catch (IOException ioe) {
		ioe.printStackTrace();
	} finally {
		levelDb.close();
	}
}
````

```java
public void deleteFromAvailable(String roomType, long roomId) throws IOException {
	try {
		levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);			String keyName = "avl:" + roomType + ":" + roomId + ":roomname";
		levelDb.delete(bytes(keyName));
		String keyBuilding = "avl:" + roomType + ":" + roomId + ":buildingname";
		levelDb.delete(bytes(keyBuilding));
		String keyCapacity = "avl:" + roomType + ":" + roomId + ":roomcapacity";
		levelDb.delete(bytes(keyCapacity));
		String keyAvailable = "avl:" + roomType + ":" + roomId + ":available";
		levelDb.delete(bytes(keyAvailable));
	} catch (IOException ioe) {
		ioe.printStackTrace();
	} finally {
		levelDb.close();
	}
}
```

```java
public void decreaseLaboratoryAvailability(long roomId) throws IOException {
	try {
		levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
		String key = "avl:lab:" + roomId + ":available";
		int seats = Integer.parseInt(asString(levelDb.get(bytes(key)))) - 1;
		levelDb.put(bytes(key), bytes(Integer.toString(seats)));
	} catch (IOException ioe) {
		ioe.printStackTrace();
	} finally {
		levelDb.close();
	}
}
```

```java
public void updateClassroomAvailability(long roomId, String requestedSchedule) throws IOException {
	try {
		levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
		levelDb.delete(bytes("avl:cla:" + roomId + ":available"));
		if (requestedSchedule.equals("m")) {
			levelDb.put(bytes("avl:cla:" + roomId + ":available"), bytes("a"));
		} else {
			levelDb.put(bytes("avl:cla:" + roomId + ":available"), bytes("m"));
		}
	} catch (IOException ioe) {
		ioe.printStackTrace();
	} finally {
		levelDb.close();
	}
}
```

### 7.4 Hibernate driver - Set Booking functions

```java
public void setClassroomBooking(long teacherId, long roomId, String schedule) {
    try {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        Classroom classroom = entityManager.find(Classroom.class, roomId);
        ClassroomBooking booking = new ClassroomBooking();

        booking.setRoom(classroom);
        booking.setSchedule(schedule);
        booking.setPerson(teacher);
        classroom.setBooking(booking);
        teacher.setBooking(booking);

        entityManager.persist(booking);
        entityManager.merge(classroom);
        entityManager.merge(teacher);

        entityManager.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }
}
```

```java
public void setLaboratoryBooking(long studentId, long roomId) {
    try {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
        Student student = entityManager.find(Student.class, studentId);

        laboratory.setStudent(student);
        student.setLaboratories(laboratory);
        entityManager.merge(laboratory);
        entityManager.merge(student);

        entityManager.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }
}
```

```java 
public ClassroomBooking getClassroomBooking(long classroomId, long userId, String schedule) {
    try {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Classroom classroom = entityManager.find(Classroom.class, classroomId);
        Collection<ClassroomBooking> bookings = classroom.getBooking();
        ClassroomBooking bookToRetrieve = null;

        for(ClassroomBooking cla : bookings) {
            if((cla.getPersonId() == userId) &&
                (cla.getSchedule().equals(schedule)) &&
                (cla.getClassroom().getId() == classroomId)) {
                    bookToRetrieve = cla;
                    break;
                }
        }

        entityManager.getTransaction().commit();
        return bookToRetrieve;
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }

    return null;
}
```

```java
public void setLaboratoryBooking(long studentId, long roomId) {
    try {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
        Student student = entityManager.find(Student.class, studentId);

        laboratory.setStudent(student);
        student.setLaboratories(laboratory);
        entityManager.merge(laboratory);
        entityManager.merge(student);

        entityManager.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }
}
```

```java
public void updateAvailability(String roomType, long roomId, boolean flag) {
    try {
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        if (roomType.equals("cla")) {
            Classroom classroom = entityManager.find(Classroom.class, roomId);
            classroom.setAvailable(flag);
            entityManager.merge(classroom);
        } else {
            Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
            laboratory.setAvailable(flag);
            entityManager.merge(laboratory);
        }
        entityManager.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        entityManager.close();
    }
}
```

### 7.5 JPA entities
For brevity, only the teacher procedure entities are reported without the getter and setter methods.

```java
@Entity
@Table(name="teacher")
public class Teacher implements Person {
    @Id
    @Column(name="TEACHER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;

    @Column(name="TEACHER_NAME")
    private String teacherName;

    @Column(name = "TEACHER_LASTNAME")
    private String teacherLastname;

    @Column(name = "TEACHER_EMAIL")
    private String teacherEmail;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teacher")
    private Collection<ClassroomBooking> classroomBookings = new ArrayList<ClassroomBooking>();

    public void setBooking(ClassroomBooking booking) {
        this.classroomBookings.add(booking);
    }

    public Collection<ClassroomBooking> getBooked() {
        return this.classroomBookings;
    }
}
```

```java
@Entity
@Table(name = "classroom_booking")
public class ClassroomBooking implements Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomBookingId;

    @Column(name = "BOOKING_SCHEDULE")
    private String classroomBookingSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "CLASSROOM_ID")
    private Classroom classroom;

    public void setPerson(Person person) {
        this.teacher = (Teacher)person;
    }

    public void setRoom(Room c){ 
        this.classroom = (Classroom)c;
    }

    public long getPersonId() {
        return this.teacher.getId();
    }

    public String getRoomName() {
        return this.classroom.getName();
    }

    public Room getClassroom() {
        return this.classroom;
    }
}
```

```java
@Entity
@Table(name="classroom")
public class Classroom implements Room {
    @Id
    @Column(name = "CLASSROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomId;

    @Column(name = "CLASSROOM_NAME")
    private String classroomName;

    @Column(name = "CLASSROOM_CAPACITY")
    private int classroomCapacity;

    @Column(name = "CLASSROOM_AVAILABLE")
    private Boolean classroomAvailable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<ClassroomBooking> classroomBookings = new ArrayList<ClassroomBooking>();

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setBooking(ClassroomBooking booking) {
        this.classroomBookings.add(booking);
    }

    public Collection<ClassroomBooking> getBooking() {
        return this.classroomBookings;
    }

    public long getBookingId(long teacherId, String schedule) {
        for(ClassroomBooking iteration : this.classroomBookings) {
            if(iteration.getPersonId() == teacherId && iteration.getSchedule().equals(schedule)) {
                return iteration.getId();
            }
        }
        return -1;
    }
    public Collection<ClassroomBooking> getBookedByTeacherId(long teacherId){
        Collection<ClassroomBooking> collection=new ArrayList<ClassroomBooking>();
        for(ClassroomBooking iteration : this.classroomBookings){
            if(iteration.getPersonId() == teacherId){
                collection.add(iteration);
            }
        }
        return collection;
    }
    public ClassroomBooking getBookingById(long id) {
        for(ClassroomBooking iteration : this.classroomBookings){
            if(iteration.getId() == id){
                return iteration;
            }
        }
        return null;
    }

    public void deleteBooking(ClassroomBooking booking) {
        this.classroomBookings.remove(booking);
    }
}
```

```java
@Entity
@Table(name = "building")
public class Building implements BuildingInterface {
    @Id
    @Column(name = "BUILDING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long buildingId;

    @Column(name = "BUILDING_NAME")
    private String buildingName;

    @Column(name = "BUILDING_ADDRESS")
    private String buildingAddress;

    @OneToMany(mappedBy = "building")
    private Collection<Laboratory> buildingLaboratories = new ArrayList<Laboratory>();
    
    @OneToMany(mappedBy = "building")
    private Collection<Classroom> buildingClassrooms = new ArrayList<Classroom>();

    public void addLaboratory(Laboratory laboratory) {
        this.buildingLaboratories.add(laboratory);
    }

    public void addClassroom(Classroom classroom) {
        this.buildingClassrooms.add(classroom);
    }
}
```