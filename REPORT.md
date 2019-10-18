# Task 0 - Workgroup report

The main purpose of our system is to manage the room booking system at university. The main actors of this system are teachers and university's employee in general.
The application is a standalone application with a simple CLI that let the user to perform one of the following actions:
 1. Show the list of the available rooms and book one in a defined schedule (Morning, Afternoon),
 2. Show the list of the booked rooms and delete one,
 3. Show the list of the booked rooms and update one.
Note: For briefly we decided to let the user to book a room only for the day after.

These actions are described in the following use cases diagram:

![usecases](/schemas/UseCaseSchema2.png)


The sequence of the actions is described by the following FlowChart:

![flowchart](/schemas/flowchartUPDATED.png) 


The application is supported by a MySql database in which are stored the information about allowed users, buildings, rooms and the booking information. A detailed implementation of the database is showed in the following entity-relation diagram:

![erDiagram](/schemas/ERSchema.png)

The application is developed in java using two classes. A class that provide the front-end, in which is implemented the interaction with the user (i.e. menus that let the user to choose what to do by a prompt), and a class that provide the database interface between the java application and the mysql database using jdbc with a localhost connection. A detailed implemention is described by the following class diagram:

![classDiagram](/schemas/classDiagram.png)


# Test
The application was tested with a simulation of the user beavior and cheking if the results were consistent with the database.
In the following images is possible to see how the user iteracts with the interface during the tests.

- Startup screen

![startup](/schemas/startupSample.png)

![startup2](/schemas/startup2Sample.png)

- Booking test

![booking](/schemas/bookingSample.png)

- Delete booking test

![delete](/schemas/deleteSample.png)

- Update booking test

![update](/schemas/updateSample.png)


The application was developed by:

Marsha Gòmez

Matilde Mazzini

Lorenzo Barigliano

Gianluca Serao

