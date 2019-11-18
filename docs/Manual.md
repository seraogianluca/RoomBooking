# Workgroup activity report - User Manual

## Table of contents
1. [Startup and log in](#1-startup-and-log-in)
2. [Book a room](#2-book-a-room)
3. [Delete a booking](#3-delete-a-booking)
4. [Update a booking](#4-update-a-booking) 

## 1. Startup and log in
The application at the startup asks to specify if the user is a student or a teacher before logging in:

```
 _____                         ____              _    _               
|  __ \                       |  _ \            | |  (_)              
| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  
|  _  // _ \ / _ \| '_ ` _ \  |  _ < / _ \ / _ \| |/ / | '_ \ / _` |
| | \ \ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| | 
|_|  \_\___/ \___/|_| |_| |_| |____/ \___/ \___/|_|\_\_|_| |_|\__, | 
                                                               __/ |                    
                                                              |___/                     

Are you a teacher or a student?
T - Teacher 
S - Student 
>
```

After choosing the user category the application ask for the user email:

```
Insert your Email > 
```

The main menu offer several actions that can be perfomed by the user:

```
1 - Book a Room.
2 - Delete a booking.
3 - Update a booking.
4 - Close.

Choose an action > 
```

## 2. Book a room

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

## 3. Delete a booking

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

## 4. Update a booking

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