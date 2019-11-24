# Workgroup activity report - User Manual

## Table of contents
1. [System Overview](#1-system-overview)
2. [Startup and log in](#2-startup-and-log-in)
3. [Administration Overview](#3-administration-overview)
4. [Student or Teacher Overview](#4-student-or-teacher-overview)

## 1. System Overview
The RoomBooking app allows you to manage the laboratories and classrooms reservations. 

The system allows: 

**An Administrator to**
* Insert a student
* Insert a teacher
* Insert a room
* Insert a building

**A Teacher and Student to**
* Book a room
* Delete a booking
* Update a booking


## 2. Startup and log in

```
 _____                         ____              _    _               
|  __ \                       |  _ \            | |  (_)              
| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  
|  _  // _ \ / _ \| '_ ` _ \  |  _ < / _ \ / _ \| |/ / | '_ \ / _` |
| | \ \ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| | 
|_|  \_\___/ \___/|_| |_| |_| |____/ \___/ \___/|_|\_\_|_| |_|\__, | 
                                                               __/ |                    
                                                              |___/                     
```
The application ask for the user email:
```
Insert your Email > 
```

## 3. Administration Overview

To login as administrator you have to insert an administrator email.


```
Insert your Email > demo@admin.unipi.it
```

The main menu offers to the administrator several actions:

```
Welcome admin

1 - Insert a student.
2 - Insert a teacher.
3 - Insert a room or a building.
4 - Close.

Choose an action > 
```

### 3.1. Insert a Student

To insert a student choose the **option 1** from the menu and insert the information about the student.

```
Insert the name of the student > Name
Insert the lastname of the Student > Lastname
Insert the email of the Student > namelastname@studenti.unipi.it
```
The email must belong to @studenti.unipi.it domain.

If the student is successfully created, the following message appears:

```
Student: Name Lastname added!
```

### 3.2. Insert a Teacher

To insert a teacher choose the **option 1** from the menu and insert the information about the teacher.


```
Insert the name of the Teacher > Name
Insert the lastname of the Teacher > Lastname
Insert the email of the Teacher > namelastname@unipi.it
```
The email must belong to @unipi.it domain.

If the teacher is successfully created, the following message appears:

```
Teacher: Name Lastname added!
```

### 3.3. Insert a Room

To insert a new Room, the system shows:

```
Do you want to add a room in a existing building or add a new building?

1 - Existing building
2 - Insert a building

Choose an action > 1
```
**Existing building**

In the case of existing building the system shows a list of buildings:
```
In which building?

ID    Name
===================
1     Polo A
2     Polo B
3     Polo F

Choose a building by ID > 1
```
**Insert building**

In the case of inserting a new building the system asks to insert the building information:
```
Insert the name of the new Building > Polo B
Insert the address of the new Building: > Via Giunta Pisano
```
If the building is successfully created, the following message appears:
```
Building: Polo B added!
```
You can add a Classroom or a Laboratory. For this example we add a Classroom, but the process is the same for the Laboratory.

```
Do you want to add a Classroom or a Laboratory?

1 - Classroom
2 - Laboratory

Choose an action > 1
Insert the name of the room > ADII2
Insert capacity > 150
```
If the room is successfully created, the following message appears:

```
Classroom: ADII2 added!
```


## 4. Student or Teacher Overview
The student and teacher interface has been designed to make room bookings depend on the role, the students can book a laboratory and the teacher can book a classroom.


**Teacher or Student:** 

The main menu offer to the users several actions:

```
1 - Book a Room.
2 - Delete a booking.
3 - Update a booking.
4 - Close.

Choose an action > 
```


### 4.1. Book a room

In case the user is a teacher the system claims for the schedule in which book the room, in case the user is a student the procedure starts directly from the next step:

```
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The system shows a list of available rooms and claims to choose one of them:

```
List of the avaiable rooms:

ID    Room            Building                  Capacity  
===================================================================
3     B32             Polo B                    80        

Choose a room by ID > 
```

If the user selects the correct room the procedure ends with success:

```
Room succesfully booked.
```

### 4.2. Delete a booking

The system shows a list of the user's bookings and claims to choose one of them:

```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              
5     B32             m              

Choose the room you booked by ID > 
```
If the user selects the correct booking the procedure ends with success:

```
Booking succesfully deleted.
```

### 4.3. Update a booking

The system shows a list of the user's bookings and claims to choose the one to be updated:
```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              

Choose the room you want to change by ID >

```
In case the user is a teacher the system claims the new schedule in which book the room, in case the user is a student the update procedure skips this step:

```
Choose the new schedule: 
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The system shows a list of available rooms and claims to choose one of them:

```
List of the avaiable rooms:

ID    Room            Building                  Capacity  
===================================================================
1     A13             Polo A                    80        
3     B32             Polo B                    80        

Choose a room by ID > 
```
If the user selects the correct room the procedure ends with success:

```
Booking succesfully updated.
```


