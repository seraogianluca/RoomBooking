package it.unipi.RoomBooking.Interface;

import static java.lang.System.out;
import java.util.*;

import org.hibernate.Hibernate;

import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;
import it.unipi.RoomBooking.Data.NORM.User;
import it.unipi.RoomBooking.Data.NORM.BuildingNORM;
import it.unipi.RoomBooking.Database.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

public final class RoomBookingCLI {

	private static String version = "  _____                         ____              _    _             \r\n |  __ \\                       |  _ \\            | |  (_)            \r\n | |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _ \r\n |  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` |\r\n | | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |\r\n |_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |\r\n                                                                __/ |\r\n                                                               |___/";

	private static String BLUE = new String("\u001B[34m");
	private static String YELLOW = new String("\u001B[33m");
	private static String WHITE = new String("\u001B[37m");
	private static String RED = new String("\u001B[31m");
	private static String GREEN = new String("\u001B[32m");

	private static DBSManager database;
	private static Scanner input;
	private static User user;

	private static void ident() {
		String email;
		boolean isValid = false;

		out.println(BLUE + version + WHITE);

		while (!isValid) {
			try {
				out.print("\nInsert your Email > ");
				email = input.nextLine();

				user = database.authenticate(email.toString());
				isValid = true;
			} catch (UserNotExistException uex) {
				out.println(RED + uex.getMessage() + WHITE);
				isValid = false;
			}
		}
	}

	private static String getCommand() {
		String command;
		boolean isValid = false;
		if (user.getRole().equals("A")) {
			out.println("\n1 - Insert a student." + "\n2 - Insert a teacher." + "\n3 - Insert a room or a building."
					+ "\n4 - Close. ");
		} else {
			out.println("\n1 - Book a Room." + "\n2 - Delete a booking." + "\n3 - Update a booking." + "\n4 - Close.");
		}
		while (!isValid) {
			out.print("\nChoose an action > ");
			command = input.next();

			if (!command.equals("4") && !command.equals("3") && !command.equals("2") && !command.equals("1")) {
				out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
			} else {
				isValid = true;
				return command;
			}
		}

		return null;
	}

	private static String setSchedule() {
		String requestedSchedule = null;
		boolean isValid = false;

		out.println("\n[M] - Morning." + "\n[A] - Afternoon.");

		while (!isValid) {
			out.print("\nChoose a schedule > ");

			requestedSchedule = input.next();
			requestedSchedule = requestedSchedule.toLowerCase(Locale.ENGLISH);

			if (!requestedSchedule.equals("a") && !requestedSchedule.equals("m")) {
				out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
			} else {
				isValid = true;
			}
		}

		return requestedSchedule;
	}

	private static void showBooked(Collection<Booked> booked) {
		out.println("\nList of your booked rooms:\n");

		if (user.getRole().equals("T")) {
			// Teacher
			out.printf("%-5s %-15s %-15s", "ID", "Room", "Schedule");
		} else {
			// Student
			out.printf("%-5s %-15s", "ID", "Room");
		}
		out.println("\n=========================================");

		for (Booked i : booked) {
			out.println(i.toString());
		}
	}

	private static void showAvailable(Collection<Available> available) {
		out.printf("%-5s %-15s %-25s %-10s", "ID", "Room", "Building", "Capacity");
		out.println("\n===================================================================");
		for (Available i : available) {
			out.println(i.toString());
		}
	}

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

	private static void deleteBooking() {
		String requestedRoom = null;
		boolean isValid = false;
		Booked bookToDelete = null;
		Collection<Booked> bookedRooms;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			out.println(RED + "No booked rooms\n" + WHITE);
			return;
		}

		showBooked(bookedRooms);
		while (!isValid) {
			out.print("\nChoose the room you booked by ID > ");
			requestedRoom = input.next();

			for (Booked iteration : bookedRooms) {
				if (Long.parseLong(requestedRoom) == iteration.getId()) {
					isValid = true;
					bookToDelete = iteration;
					break;
				}
			}

			if (!isValid) {
				out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
			} else {
				database.deleteBooking(user, bookToDelete);
				out.println(GREEN + "\nBooking succesfully deleted." + WHITE);
			}
		}
	}

	private static void updateBooking() {
		String oldRoom = null;
		String requestedSchedule = null;
		String requestedRoom = null;
		Collection<Available> availableRooms;
		Collection<Booked> bookedRooms;
		Booked room = null;
		Available roomAvailable = null;
		boolean isValid = false;
		String oldbookingId = null;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			out.println(RED + "No available rooms\n" + WHITE);
			return;
		}

		showBooked(bookedRooms);

		while (!isValid) {
			if (user.getRole().equals("T")) {
				// Teacher
				while (!isValid) {
					out.print("\nChoose the room you want to change by ID > ");
					oldbookingId = input.next();

					for (Booked iteration : bookedRooms) {
						if (Long.parseLong(oldbookingId) == iteration.getId()) {
							room = iteration;
							isValid = true;
							break;
						}
					}

					if (!isValid) {
						out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
					}
				}
			} else {
				// Student
				while (!isValid) {
					out.print("\nChoose the room you want to change by ID > ");
					oldRoom = input.next();
					for (Booked i : bookedRooms) {
						if (i.getId() == Long.parseLong(oldRoom)) {
							room = i;
							isValid = true;
							break;
						}
					}

					if (!isValid) {
						System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
					}
				}
			}

			isValid = false;

			if (user.getRole().equals("T")) {
				out.print("\nChoose the new schedule: ");
				requestedSchedule = setSchedule();
			}

			availableRooms = database.getAvailable(requestedSchedule, user.getRole());

			if (availableRooms.size() == 0) {
				out.println(RED + "\nNo available rooms." + WHITE);
				return;
			}

			showAvailable(availableRooms);
			while (!isValid) {

				out.print("\nChoose a room by ID > ");
				requestedRoom = input.next();

				for (Available i : availableRooms) {
					if (i.getId() == Long.parseLong(requestedRoom)) {
						roomAvailable = i;
						isValid = true;
						break;
					}
				}

				if (!isValid) {
					out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
				} else {
					database.updateBooking(user, roomAvailable, requestedSchedule, room);
					out.println(GREEN + "\nBooking succesfully updated." + WHITE);
				}

			}
		}
	}

	// Admin methods
	private static void addStudent() {
		String[] dataString = new String[3];
		input = new Scanner(System.in);

		out.print("\nInsert the name of the student > ");
		dataString[0] = input.nextLine();

		out.print("\nInsert the lastname of the Student > ");
		dataString[1] = input.nextLine();

		out.print("\nInsert the email of the Student > ");
		dataString[2] = input.next();

		if (database.checkDuplicateUser(dataString[2], "S") == false) {
			out.println(RED + "User already exists!" + WHITE);
			return;
		}
		database.setStudent(dataString);
		out.println(GREEN + "\nStudent: " + dataString[0] + " " + dataString[1] + " added!" + WHITE);
	}

	private static void addTeacher() {
		String[] dataString = new String[3];
		input = new Scanner(System.in);

		out.print("\nInsert the name of the Teacher > ");
		dataString[0] = input.nextLine();
		out.print("\nInsert the lastname of the Teacher > ");
		dataString[1] = input.nextLine();
		out.print("\nInsert the email of the Teacher > ");
		dataString[2] = input.next();

		if (database.checkDuplicateUser(dataString[2], "T") == false) {
			out.println(RED + "User already exists!" + WHITE);
			return;
		}
		database.setTeacher(dataString);
		out.println(GREEN + "\nTeacher: " + dataString[0] + " " + dataString[1] + " added!" + WHITE);

	}

	private static void addRoom() {
		Collection<BuildingNORM> buildings = new ArrayList<>();

		out.print("\nDo you want to add a room in a existing building or add a new one?");
		out.println("\n1 - Existing building" + "\n2 - Insert a building");
		out.print("\nChoose an action > ");

		String cmd = input.next();
		String[] data = new String[6];
		if (cmd.equals("1")) {
			// Classroom
			out.print("\nDo you want to add a Classroom or a Laboratory?\n");
			out.println("\n1 - Classroom" + "\n2 - Laboratory");
			out.print("\nChoose an action > ");
			String typeRoom = input.next();
			if (typeRoom.equals("1")) {
				Boolean exitBuilding = false;

				while (!exitBuilding) {
					data[3] = "cla";
					out.print("\nIn which building?\n");

					buildings = database.getBuildings();
					out.println(String.format("\n%-5s %-15s", "ID", "Name"));
					out.println("===================");
					for (BuildingNORM b : buildings) {
						out.println(b.toString());
					}

					out.print("\nChoose a building by ID > ");
					data[0] = input.next();

					exitBuilding = database.checkBuilding(data[0]);
					if (!database.checkBuilding(data[0])) {
						out.print(RED + "\nError. This ID doesn't exist. Choose an existing building\n" + WHITE);
					}
				}
				input = new Scanner(System.in);

				out.print("\nInsert the name of the room > ");
				data[1] = input.nextLine();

				out.print("\nInsert capacity > ");
				data[2] = input.next();

				database.setRoom(data);
				out.println(GREEN + "\nClassroom: " + data[1] + " " + "added!" + WHITE);
			} else {
				// Laboratory
				data[3] = "lab";
				Boolean exitBuilding = false;

				while (!exitBuilding) {
					data[3] = "cla";
					out.print("\nIn which building?");

					buildings = database.getBuildings();
					out.println(String.format("\n%-5s %-15s", "ID", "Name"));
					out.println("===================");
					for (BuildingNORM b : buildings) {
						out.println(b.toString());
					}

					out.print("\nChoose a building by ID > ");
					data[0] = input.next();

					exitBuilding = database.checkBuilding(data[0]);
					if (!database.checkBuilding(data[0])) {
						out.print(RED + "\nError. This ID doesn't exist. Choose an existing building\n" + WHITE);
					}
				}

				out.print("Insert the name of the Lab >");
				data[1] = input.nextLine();

				out.print("Insert capacity >");
				data[2] = input.next();
				database.setRoom(data);
				out.println(GREEN + "\nSuccessful. Laboratory: " + data[1] + " " + "added!" + WHITE);
			}
		} else {
			// Adding new building
			out.print("Insert the name of the new Building > ");
			data[0] = input.nextLine();
			out.print("Insert the address of the new Building: > ");
			data[1] = input.next();
			database.setBuilding(data);
			out.println(GREEN + "\nBuilding: " + data[0] + " " + "added!" + WHITE);
		}
	}

	public static void main(String[] args) {

		database = new DBSManager();
		input = new Scanner(System.in);
		boolean terminate = false;
		String command = null;
		int commandInt;

		try {
			database.start();
			ident();

			if (user.getRole().equals("T") || (user.getRole().equals("S"))) {
				database.initializeAvailable(user);
				database.initializeBooked(user);
			}
			out.println(GREEN + "\nWelcome " + user.getName() + WHITE);

			while (!terminate) {
				command = getCommand();

				if (command == null) {
					out.println(RED + "\nInvalid command." + WHITE);
					terminate = true;
					break;
				}

				commandInt = Integer.parseInt(command);
				if (user.getRole().equals("A")) { 
					// admin commands has +10 to distinguish
					commandInt += 10;
				}
				switch (commandInt) {

				case 1:
					bookARoom();
					break;
				case 2:
					deleteBooking();
					break;
				case 3:
					updateBooking();
					break;
				case 4:
					terminate = true;
					out.println("\nSee you soon!");
					break;

				// admin commands
				case 11:
					addStudent();
					break;
				case 12:
					addTeacher();
					break;
				case 13:
					addRoom();
					break;
				case 14:
					terminate = true;
					out.println("\nSee you soon!");
					break;
				}
			}
		} finally {
			database.exit();
		}
	}
}
