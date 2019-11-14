package it.unipi.RoomBooking.Interface;

import static java.lang.System.out;
import java.util.Collection;
import java.util.Locale;
//import java.util.Collection;
//import java.util.Locale;
import java.util.*;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;
import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;
import it.unipi.RoomBooking.Data.NORM.User;
import it.unipi.RoomBooking.Data.ORM.Classroom;
import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;
//import it.unipi.RoomBooking.Data.Interface.Room;
//import it.unipi.RoomBooking.Data.ORM.Classroom;
//import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;
//import it.unipi.RoomBooking.Exceptions.UserNotExistException;
import it.unipi.RoomBooking.Database.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

public final class RoomBookingCLI {

	private static String BLUE = new String("\u001B[34m");
	private static String YELLOW = new String("\u001B[33m");
	private static String WHITE = new String("\u001B[37m");
	private static String RED = new String("\u001B[31m");
	private static String GREEN = new String("\u001B[32m");

	private static String version = " _____                         ____              _    _                       __   ___  \n"
			+ "|  __ \\                       |  _ \\            | |  (_)                     /_ | / _ \\ \n"
			+ "| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |\n"
			+ "|  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` | \\ \\ / /  | || | | |\n"
			+ "| | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \\ V /   | || |_| |\n"
			+ "|_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |   \\_(_)  |_(_)___/ \n"
			+ "                                                               __/ |                    \n"
			+ "                                                              |___/                     \n";

	private static DBSManager database;
	private static Scanner input;

	private static User user;

	private static void ident() throws UserNotExistException {
		String email;
		boolean isValid = false;

		System.out.println(BLUE + version + WHITE);

		while (!isValid) {
			try {
				System.out.print("\nInsert your Email > ");
				email = input.next();

				user = database.authenticate(email.toString());
				isValid = true;
			} catch (UserNotExistException uex) {
				System.out.println(RED + uex.getMessage() + WHITE);
				isValid = false;
			}
		}
	}

	private static String getCommand() {
		String command;
		boolean isValid = false;

		System.out
				.println("\n1 - Book a Room." + "\n2 - Delete a booking." + "\n3 - Update a booking." + "\n4 - Close.");

		while (!isValid) {
			System.out.print("\nChoose an action > ");
			command = input.next();

			if (!command.equals("4") && !command.equals("3") && !command.equals("2") && !command.equals("1")) {
				System.out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
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

		System.out.println("\n[M] - Morning." + "\n[A] - Afternoon.");

		while (!isValid) {
			System.out.print("\nChoose a schedule > ");

			requestedSchedule = input.next();
			requestedSchedule = requestedSchedule.toLowerCase(Locale.ENGLISH);

			if (!requestedSchedule.equals("a") && !requestedSchedule.equals("m")) {
				System.out.println(YELLOW + "\nPlease insert a valid command." + WHITE);
			} else {
				isValid = true;
			}
		}

		return requestedSchedule;
	}
	/*
	 * private static void showRooms(Collection<? extends Room> table, boolean
	 * booked) { if (booked) { if (isTeacher) {
	 * out.println("\nList of your booked rooms:\n");
	 * 
	 * System.out.printf("%-5s %-15s %-15s", "ID", "Room", "Schedule");
	 * 
	 * System.out.println("\n=========================================");
	 * 
	 * for (Room iteration : table) { Classroom c = (Classroom) iteration;
	 * 
	 * Collection<ClassroomBooking> collection =
	 * c.getBookedByTeacherId(user.getId()); for (ClassroomBooking iterator :
	 * collection) { System.out.println(iterator.toString()); } } } else {
	 * System.out.println("\nList of your booked rooms:\n");
	 * 
	 * System.out.printf("%-5s %-15s", "ID", "Room");
	 * 
	 * System.out.println("\n=========================================");
	 * 
	 * for (Room i : table) System.out.println(i.toStringBooked()); }
	 * 
	 * } else { System.out.println("\nList of the avaiable rooms:\n");
	 * 
	 * System.out.printf("%-5s %-15s %-25s %-10s", "ID", "Room", "Building",
	 * "Capacity"); System.out.println(
	 * "\n===================================================================");
	 * 
	 * for (Room i : table) System.out.println(i.toString()); } }
	 */
	// splitting the showrooms in showbooked and showavailable

	public static void showbooked() {
		Collection<Booked> c = database.getBooked(user.getRole());
		out.println("\nList of your booked rooms:\n");

		if (user.getRole().equals("T")) { // teacher
			out.printf("%-5s %-15s %-15s", "ID", "Room", "Schedule");
		} else { // student
			out.printf("%-5s %-15s", "ID", "Room");

		}
		out.println("\n=========================================");

		for (Booked i : c) {
			out.println(i.toString());
		}

	}

	public static void showavailable(String schedule) {
		Collection<Available> c = database.getAvailable(schedule, user.getRole());
		System.out.printf("%-5s %-15s %-25s %-10s", "ID", "Room", "Building", "Capacity");
		System.out.println("\n===================================================================");
		for (Available i : c) {
			out.println(i.toString());
		}
	}

	private static void bookARoom() {
		String requestedSchedule = null;
		String requestedRoom = null;
		Collection<Available> availableRooms;
		boolean isValid = false;
		Available room = null;
		if (user.getRole().equals("T")) {
			requestedSchedule = setSchedule();
		}
		availableRooms = database.getAvailable(requestedSchedule, user.getRole());

		if (availableRooms.size() == 0) {
			System.out.println(RED + "No available rooms\n" + WHITE);
			return;
		}
		showavailable(requestedSchedule);
		while (!isValid) {
			System.out.print("\nChoose a room by ID > ");

			requestedRoom = input.next();

			for (Available i : availableRooms) {
				if (i.getId() == Long.parseLong(requestedRoom)) {
					isValid = true;
					room = i;
					break;
				}
			}

			if (!isValid) {
				System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
			} else {
				database.setBooking(user, room, requestedSchedule);
				System.out.println(GREEN + "\nRoom succesfully booked." + WHITE);
			}

		}
	}

	private static void deleteBooking() {

		String requestedRoom = null;
		Collection<Booked> bookedRooms;
		boolean isValid = false;
		long bookingId = -1;
		Booked room = null;

		bookedRooms = database.getBooked(user.getRole());

		if (bookedRooms.size() == 0) {
			out.println(RED + "No available rooms\n" + WHITE);
			return;
		}

		while (!isValid) {
			System.out.print("\nChoose the room you booked by ID > ");
			System.out.printf("\n%-5s %-15s %-25s", "ID", "Room", "Building");
			System.out.println("\n===================================================================");
			for (Booked i : bookedRooms) {
				out.println(i.toString());
			}

			requestedRoom = input.next();

			for (Booked iteration : bookedRooms) {
				// Classroom c = (Classroom) iteration;
				// Collection<ClassroomBooking> collection =
				// c.getBookedByTeacherId(user.getId());

				// for (ClassroomBooking iterator : collection) {
				if (Long.parseLong(requestedRoom) == iteration.getId()) {
					bookingId = iteration.getId();
					isValid = true;
					room = iteration;
					break;
				}
				// }
			}

			if (!isValid) {
				System.out.println(YELLOW + "\nPlease insert a valid room." + WHITE);
			} else {
				database.deleteBooking(user, room);
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

		showbooked();

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

			showavailable(requestedSchedule);
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
			System.out.println(GREEN + "\nWelcome" + user.getName() + WHITE);

			while (!terminate) {
				command = getCommand();

				if (command == null) {
					input.close();
					terminate = true;
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
					System.out.println("\nSee you soon!");
					break;
				}
			}

			// showavailable("a");
			// showavailable("m");
			/*
			 * database.initializeAvailable(user); database.initializeBooked(user);
			 * 
			 * Collection<Booked> bookings = database.getBooked(user.getRole());
			 * System.out.println("Student booking:");
			 * System.out.println(bookings.toString());
			 * 
			 * Collection<Available> availables = database.getAvailable(null,
			 * user.getRole()); System.out.println("Student availables:");
			 * System.out.println(availables.toString());
			 * 
			 * database.exit(); database.start();
			 * 
			 * User user2 = database.authenticate("demo@unipi.it");
			 * database.initializeAvailable(user2); database.initializeBooked(user2);
			 * 
			 * bookings.clear(); bookings = database.getBooked(user2.getRole());
			 * System.out.println("Teacher booking:");
			 * System.out.println(bookings.toString());
			 * 
			 * availables.clear(); availables = database.getAvailable("m", user2.getRole());
			 * System.out.println("Teacher availables:");
			 * System.out.println(availables.toString());
			 */
		} catch (UserNotExistException ue) {
			System.out.println("UPS!" + ue);
		} finally {
			database.exit();
		}

		// input = new Scanner(System.in);
		// boolean terminate = false;
		// String command = null;

		/*
		 * database = new HibernateManager(); try { database.start(); //ident();
		 * 
		 * while (!terminate) { command = getCommand();
		 * 
		 * if (command == null) { input.close(); terminate = true; }
		 * 
		 * switch (Integer.parseInt(command)) { case 1: // bookARoom(); break; case 2:
		 * // deleteBooking(); break; case 3: // updateBooking(); break; case 4:
		 * terminate = true; System.out.println("\nSee you soon!"); break; } } } catch
		 * (Exception ex) { System.out.println(RED + "An exception occurred." + WHITE);
		 * } finally { database.exit(); input.close(); }
		 * 
		 */

	}
}
