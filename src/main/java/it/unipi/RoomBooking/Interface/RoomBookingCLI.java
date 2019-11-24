package it.unipi.RoomBooking.Interface;

import static java.lang.System.out;
import java.util.*;

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

				if(!email.contains("@studenti.unipi.it") && 
					!email.contains("@admin.unipi.it") && 
					!email.contains("@unipi.it")) {
					
					out.println(YELLOW + "\nPlease insert a valid email." + WHITE);
					isValid = false;
				} else {
					user = database.authenticate(email.toString());
					isValid = true;
				}				
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
			out.println(
					"\n1 - Insert a student." + "\n2 - Insert a teacher." + "\n3 - Insert a room." + "\n4 - Close. ");
		} else {
			out.println("\n1 - Book a Room." + "\n2 - Delete a booking." + "\n3 - Update a booking." + "\n4 - Close.");
		}

		while (!isValid) {
			out.print("\nChoose an action > ");
			command = input.nextLine();

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
			input.nextLine();
			requestedSchedule = requestedSchedule.toLowerCase(Locale.ENGLISH);

			if (!requestedSchedule.equals("a") && !requestedSchedule.equals("m")) {
				out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
			} else {
				isValid = true;
			}
		}

		return requestedSchedule;
	}

	private static long getLongId() {
		long inputLong = 0;

		try {
			inputLong = input.nextLong();
			if (inputLong == 0) {
				out.println(YELLOW + "\nPlease insert a valid ID." + WHITE);
			}
		} catch (InputMismatchException im) {
			out.println(YELLOW + "\nPlease insert a valid ID." + WHITE);
			return 0;
		}

		return inputLong;
	}

	private static int getInt() {
		int inputInt = 0;

		try {
			inputInt = input.nextInt();
			if (inputInt == 0) {
				out.println(YELLOW + "\nPlease insert a valid number." + WHITE);
			}
		} catch (InputMismatchException im) {
			out.println(YELLOW + "\nPlease insert a valid number." + WHITE);
			return 0;
		}

		return inputInt;
	}

	private static int getCommandAdmin() {
		int typeRoom = 0;
		boolean isValid = false;

		while (!isValid) {
			out.print("\nChoose an action > ");

			try {
				typeRoom = input.nextInt();
				input.nextLine();

				if (!(typeRoom == 2) && !(typeRoom == 1)) {
					out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
				} else {
					isValid = true;
				}
			} catch (InputMismatchException im) {
				out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
				input.nextLine();
			}

		}

		return typeRoom;
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
		out.println("\nList of available rooms:\n");
		out.printf("%-5s %-15s %-25s %-10s", "ID", "Room", "Building", "Capacity");
		out.println("\n===================================================================");
		for (Available i : available) {
			out.println(i.toString());
		}
	}

	private static void bookARoom() {
		String requestedSchedule = null;
		boolean isValid = false;
		Available room = null;
		Collection<Available> availableRooms;

		if (user.getRole().equals("T")) {
			requestedSchedule = setSchedule();
		}

		availableRooms = database.getAvailable(requestedSchedule, user.getRole());
		if (availableRooms.size() == 0) {
			out.println(RED + "\nNo available rooms." + WHITE);
			return;
		}

		showAvailable(availableRooms);
		while (!isValid) {
			long requestedRoom = 0;

			while (requestedRoom == 0) {
				out.print("\nChoose a room by ID > ");
				requestedRoom = getLongId();
				input.nextLine();
			}

			for (Available i : availableRooms) {
				if (i.getId() == requestedRoom) {
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
		boolean isValid = false;
		Booked bookToDelete = null;
		Collection<Booked> bookedRooms;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			out.println(RED + "\nNo bookings." + WHITE);
			return;
		}

		showBooked(bookedRooms);
		while (!isValid) {
			long requestedRoom = 0;

			while (requestedRoom == 0) {
				out.print("\nChoose the room you booked by ID > ");
				requestedRoom = getLongId();
				input.nextLine();
			}

			for (Booked iteration : bookedRooms) {
				if (requestedRoom == iteration.getId()) {
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
		String requestedSchedule = null;
		Collection<Available> availableRooms;
		Collection<Booked> bookedRooms;
		Booked room = null;
		Available roomAvailable = null;
		boolean isValid = false;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			out.println(RED + "\nNo bookings." + WHITE);
			return;
		}

		showBooked(bookedRooms);

		while (!isValid) {
			while (!isValid) {
				long oldbookingId = 0;

				while (oldbookingId == 0) {
					out.print("\nChoose the room you want to change by ID > ");
					oldbookingId = getLongId();
					input.nextLine();
				}

				for (Booked iteration : bookedRooms) {
					if (oldbookingId == iteration.getId()) {
						room = iteration;
						isValid = true;
						break;
					}
				}

				if (!isValid) {
					out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
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
				long requestedRoom = 0;

				while (requestedRoom == 0) {
					out.print("\nChoose a room by ID > ");
					requestedRoom = getLongId();
					input.nextLine();
				}

				for (Available i : availableRooms) {
					if (i.getId() == requestedRoom) {
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

		out.print("\nInsert the name of the student > ");
		dataString[0] = input.nextLine();
		out.print("\nInsert the lastname of the Student > ");
		dataString[1] = input.nextLine();

		boolean correctEmail = false;
		while(!correctEmail) {
			out.print("\nInsert the email of the Student > ");
			dataString[2] = input.nextLine();

			if(dataString[2].contains("@studenti.unipi.it")) {
				correctEmail = true;
			} else {
				out.println(YELLOW + "\nPlease insert a valid email." + WHITE);
			}
		}
		
		if (database.checkDuplicateUser(dataString[2], "S") == false) {
			out.println(RED + "\nUser already exists!" + WHITE);
			return;
		}

		database.setStudent(new User(0, dataString[0], dataString[1], dataString[2], "S"));
		out.println(GREEN + "\nStudent: " + dataString[0] + " " + dataString[1] + " added!" + WHITE);
	}

	private static void addTeacher() {
		String[] dataString = new String[3];

		out.print("\nInsert the name of the Teacher > ");
		dataString[0] = input.nextLine();
		out.print("\nInsert the lastname of the Teacher > ");
		dataString[1] = input.nextLine();

		boolean correctEmail = false;
		while(!correctEmail) {
			out.print("\nInsert the email of the Teacher > ");
			dataString[2] = input.nextLine();

			if(dataString[2].contains("@unipi.it")) {
				correctEmail = true;
			} else {
				out.println(YELLOW + "\nPlease insert a valid email." + WHITE);
			}
		}

		if (database.checkDuplicateUser(dataString[2], "T") == false) {
			out.println(RED + "\nUser already exists!" + WHITE);
			return;
		}

		database.setTeacher(new User(0, dataString[0], dataString[1], dataString[2], "S"));
		out.println(GREEN + "\nTeacher: " + dataString[0] + " " + dataString[1] + " added!" + WHITE);

	}

	private static void addRoom(long buildingId) {
		String name;
		int capacity = 0;
		int typeRoom = 0;

		out.print("\nDo you want to add a Classroom or a Laboratory?\n");
		out.println("\n1 - Classroom" + "\n2 - Laboratory");

		typeRoom = getCommandAdmin();

		if (typeRoom == 1) {

			out.print("\nInsert the name of the classroom > ");
			name = input.nextLine();

			while (capacity == 0) {
				out.print("\nInsert capacity > ");
				capacity = getInt();
				input.nextLine();
			}

			database.setRoom(name, capacity, "cla", buildingId);
			out.println(GREEN + "\nClassroom: " + name + " " + "added!" + WHITE);
		} else {
			out.print("\nInsert the name of the laboratory > ");
			name = input.nextLine();

			while (capacity == 0) {
				out.print("\nInsert capacity > ");
				capacity = getInt();
				input.nextLine();
			}

			database.setRoom(name, capacity, "lab", buildingId);
			out.println(GREEN + "\nSuccessful. Laboratory: " + name + " " + "added!" + WHITE);
		}
	}

	private static void addBuilding() {
		Collection<BuildingNORM> buildings = new ArrayList<>();
		int command = 0;
		long buildingId = 0;
		String name;
		String address;

		out.print("\nDo you want to add a room in a existing building or in a new one?\n");
		out.println("\n1 - Existing building" + "\n2 - Insert a building");

		command = getCommandAdmin();

		if (command == 1) {
			Boolean exitBuilding = false;
			buildings = database.getBuildings();
			out.println(String.format("\n%-5s %-15s", "ID", "Name"));
			out.println("===================");

			for (BuildingNORM b : buildings) {
				out.println(b.toString());
			}

			while (!exitBuilding) {

				while (buildingId == 0) {
					out.print("\nChoose a building by ID > ");
					buildingId = getLongId();
					input.nextLine();
				}

				if (!database.checkBuilding(buildingId)) {
					out.print(RED + "\nError. This ID doesn't exist. Choose an existing building\n" + WHITE);
					buildingId = 0;
				} else {
					exitBuilding = true;
				}
			}
		} else {
			// Adding new building
			out.print("Insert the name of the new Building > ");
			name = input.nextLine();

			out.print("Insert the address of the new Building: > ");
			address = input.nextLine();

			database.setBuilding(name, address);
			out.println(GREEN + "\nBuilding: " + name + " " + "added!" + WHITE);

			buildingId = database.getBuildingId(name);
		}

		addRoom(buildingId);
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
					addBuilding();
					break;
				case 14:
					terminate = true;
					out.println("\nSee you soon!");
					break;
                default:
                    terminate = true;
                    break;
				}
			}
		} finally {
			database.exit();
		}
	}
}
