# Workgroup activity report - User Manual

## Table of contents
1. [System Overview](#1-system-overview)
2. [Startup and log in](#2-startup-and-log-in)
3. [Administration Overview](#3-administration-overview)
4. Student or Teacher Overview

## 1. System Overview
The RoomBooking allows you to manage the laboratory and classrooms reservations. 

The system allows: 

**An Administrator to**
* Insert a student
* Insert a teacher
* Insert a room
* Insert a building

**A Teacher and Student to**
* Book a room
* Delete a booking room
* Update a booking room


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
The administration interface has been designed to make inserting the main information that the system need for a quick and easy to implement. 


**Administrator Case:** 

```
Insert your Email > demo@admin.unipi.it
```

The main menu offer to the administrator several actions that can be perfomed:

```
Welcome admin

1 - Insert a student.
2 - Insert a teacher.
3 - Insert a room or a building.
4 - Close.

Choose an action > 
```

### 3.1. Insert a Student


```
Insert the name of the student > Antonio

Insert the lastname of the Student > Bifecco

Insert the email of the Student > antoniobifecco@studenti.unipi.it

Student: Antonio Bifecco added!
```
### 3.2. Insert a Teacher



```
Choose an action >2

Insert the name of the Teacher > Pietro

Insert the lastname of the Teacher > Ducange

Insert the email of the Teacher > pietro.ducange@unipi.it

Teacher: Pietro Ducange added!

```

### 3.3. Insert a Room


```
Choose an action >3

Do you want to add a room in a existing building or add a new one?

1 - Existing building
2 - Insert a building

Choose an action > 1

Do you want to add a Classroom or a Laboratory?

1 - Classroom
2 - Laboratory

Choose an action > 1

In which building?

ID    Name
===================
1     Polo A
2     Polo B
3     Polo F

Choose a building by ID > 1

Insert the name of the room > ADII2

Insert capacity > 150

Classroom: ADII2 added!
```

### 3.4. Insert a Building

```
Choose an action > 3

Do you want to add a room in a existing building or add a new one?

1 - Existing building
2 - Insert a building

Choose an action > 2
Insert the name of the new Building > Polo B
Insert the address of the new Building: > via Giunta Pisano

Building: Polo B added!
```

## 4. Student or Teacher Overview


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

In case the user is a teacher the application asks the schedule in which book the room, in case the user is a student the book a room procedure start directly from the next step:

```
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The application shows a list of available rooms and asks to choose one of them:

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

The application shows a list of the user's bookings and asks to choose one of them:

```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              
5     B32             m              

Choose the room you booked by ID > 
```
If the user selects the correct room the procedure ends with success:

```
Booking succesfully deleted.
```

### 4.3. Update a booking

The application shows a list of the user's bookings and asks to choose the one to be updated:
```
List of your booked rooms:

ID    Room            Schedule       
=========================================
4     A13             m              
3     A22             m              

Choose the room you want to change by ID >

```
In case the user is a teacher the application asks the new schedule in which book the room, in case the user is a student the update procedure skips this step:

```
Choose the new schedule: 
[M] - Morning.
[A] - Afternoon.

Choose a schedule > 
```
The application shows a list of available rooms and asks to choose one of them:

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


