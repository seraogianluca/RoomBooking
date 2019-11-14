package it.unipi.RoomBooking.Interface;

import static java.lang.System.out;
import java.util.*;

import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;
import it.unipi.RoomBooking.Data.NORM.User;
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
				email = input.next();

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

		out.println("\n1 - Book a Room." + 
					"\n2 - Delete a booking." + 
					"\n3 - Update a booking." + 
					"\n4 - Close.");

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

		out.println("\n[M] - Morning." + 
					"\n[A] - Afternoon.");

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

	public static void showBooked(Collection<Booked> booked) {
		out.println("\nList of your booked rooms:\n");

		if (user.getRole().equals("T")) {
			//Teacher
			out.printf("%-5s %-15s %-15s", "ID", "Room", "Schedule");
		} else {
			//Student
			out.printf("%-5s %-15s", "ID", "Room");
		}
		out.println("\n=========================================");

		for (Booked i : booked) {
			out.println(i.toString());
		}
	}

	public static void showAvailable(Collection<Available> available) {
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

		while (!isValid) {
			showBooked(bookedRooms);

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
				System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
			} else {
				database.deleteBooking(user, bookToDelete);
				System.out.println(GREEN + "\nBooking succesfully deleted." + WHITE);
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
		long oldRoomId = -1;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			System.out.println(RED + "No available rooms\n" + WHITE);
			return;
		}

		showBooked(bookedRooms);

		while (!isValid) {
			if (user.getRole().equals("T")) {
				while (!isValid) {
					System.out.print("\nChoose the room you want to change by ID >");

					oldbookingId = input.next();

					for (Booked iteration : bookedRooms) {
						// Classroom c = (Classroom) iteration;

						// Collection<ClassroomBooking> collection =
						// c.getBookedByTeacherId(user.getId());
						// for (ClassroomBooking iterator :collection) {
						if (Long.parseLong(oldbookingId) == iteration.getId()) {
							room = iteration;
							oldRoomId = iteration.getId();
							isValid = true;
							break;
						}
						// }
					}

					if (!isValid) {
						System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
					} else {
						System.out.print("\nChoose the new schedule: ");

						requestedSchedule = setSchedule();
					}
				}
			} else { // student
				while (!isValid) {
					System.out.print("\nChoose the room you want to change by ID > ");
					oldRoom = input.next();
					oldRoomId = Long.parseLong(oldRoom);
					for (Booked i : bookedRooms) {
						if (i.getId() == oldRoomId) {
							isValid = true;
							oldbookingId = "1";// for the student this value is not used after break;
						}
					}

					if (!isValid) {
						System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
					}
				}
			}

			isValid = false;
			availableRooms = database.getAvailable(requestedRoom, user.getRole());

			if (availableRooms.size() == 0) {
				System.out.println(RED + "\nNo available rooms." + WHITE);
				return;
			}

			showAvailable(availableRooms);
			while (!isValid) {

				System.out.print("\nChoose a room by ID > ");
				requestedRoom = input.next();

				for (Available i : availableRooms) {
					if (i.getId() == Long.parseLong(requestedRoom)) {
						roomAvailable = i;
						isValid = true;
						break;
					}
				}

				if (!isValid) {
					System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
				} else {
					database.updateBooking(user, roomAvailable, requestedSchedule, room);
					System.out.println(GREEN + "\nBooking succesfully updated." + WHITE);
				}

			}
		}
	}

	public static void main(String[] args) {

		database = new DBSManager();
		input = new Scanner(System.in);
		boolean terminate = false;
		String command = null;

		try {
			database.start();
			ident();

			database.initializeAvailable(user);
			database.initializeBooked(user);

			out.println(GREEN + "\nWelcome " + user.getName() + WHITE);

			while (!terminate) {
				command = getCommand();

				if(command == null) {
					out.println(RED + "\nInvalid command." + WHITE);
					terminate = true;
					break;
				}

				switch (Integer.parseInt(command)) {
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
				}
			}
		} finally {
			database.exit();
		}
	}
}
