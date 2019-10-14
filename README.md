# RoomBooking
A simple JAVA application for the first workgroup task of the Large-Scale and Multi-Structured Databases course at University of Pisa.

# Specification
In this first task we decided to make an application for booking a room for meetings and lectures.
The end users for this application are the people belonging to university like students, professors and university employees in general. 
The application allows the users to:
  -	read a list of the available rooms of a building,
  -	create a book in a specific time,
  -	update a book if the schedule changes,
  -	delete the book if necessary,

We described the dataflow of the application through this simple flowchart:

![dataflow](/schemas/DataFlowChart.png)

We decided to implement this application with two classes, one that implements the interface used by the user, and one that implements the operation on the database.
