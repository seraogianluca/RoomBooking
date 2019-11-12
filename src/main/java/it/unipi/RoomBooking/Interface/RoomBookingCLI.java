package it.unipi.RoomBooking.Interface;

import static java.lang.System.out;
import java.util.Collection;
import java.util.Locale;
//import java.util.Collection;
//import java.util.Locale;
import java.util.Scanner;

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
/*
	private static void ident() throws UserNotExistException {
		String email = null;
		boolean isValid = false;
		boolean isValid2 = false;
		String value;

		System.out.println(BLUE + version + WHITE);

		while (!isValid) {
			try {
				while (!isValid2) {
					System.out
							.print("\nAre you a teacher or a student?\n" + "\nT - Teacher" + "\nS - Student\n" + "\n>");

					value = input.next();

					if (value.toLowerCase().equals("t")) {
						isTeacher = true;
						isValid2 = true;
					} else if (value.toLowerCase().equals("s")) {
						isTeacher = false;
						isValid2 = true;
					} else {
						System.out.print(YELLOW + "Insert a correct value\n" + WHITE);

						isValid2 = false;
					}
				}

				System.out.print("\nInsert your Email > ");
				email = input.next().toString();

				user = database.authenticate(email);
				user = new User(user);
				isValid = true;
			} catch (UserNotExistException uex) {
				System.out.println(RED + uex.getMessage() + WHITE);
				isValid = false;
				isValid2 = false;
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

	private static void showRooms(Collection<? extends Room> table, boolean booked) {
		if (booked) {
			if (isTeacher) {
				out.println("\nList of your booked rooms:\n");

				System.out.printf("%-5s %-15s %-15s", "ID", "Room", "Schedule");

				System.out.println("\n=========================================");

				for (Room iteration : table) {
					Classroom c = (Classroom) iteration;

					Collection<ClassroomBooking> collection = c.getBookedByTeacherId(user.getId());
					for (ClassroomBooking iterator : collection) {
						System.out.println(iterator.toString());
					}
				}
			} else {
				System.out.println("\nList of your booked rooms:\n");

				System.out.printf("%-5s %-15s", "ID", "Room");

				System.out.println("\n=========================================");

				for (Room i : table)
					System.out.println(i.toStringBooked());
			}

		} else {
			System.out.println("\nList of the avaiable rooms:\n");

			System.out.printf("%-5s %-15s %-25s %-10s", "ID", "Room", "Building", "Capacity");
			System.out.println("\n===================================================================");

			for (Room i : table)
				System.out.println(i.toString());
		}
	}
	*/
	// splitting the showrooms in showbooked and showavailable

	public static void showbooked(Collection<Booked> c) {
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

	/*
	 * private static void bookARoom() { String requestedSchedule = null; String
	 * requestedRoom = null; Collection<? extends Room> availableRooms; boolean
	 * isValid = false;
	 * 
	 * if (isTeacher) { requestedSchedule = setSchedule(); }
	 * 
	 * availableRooms = database.getAvailable(user, requestedSchedule);
	 * 
	 * if (availableRooms.size() == 0) { System.out.println(RED +
	 * "No available rooms\n" + WHITE); return; }
	 * 
	 * // showRooms(availableRooms, false);
	 * 
	 * // while (!isValid) { // System.out.print("\nChoose a room by ID > ");
	 * 
	 * // requestedRoom = input.next();
	 * 
	 * // for (Room i : availableRooms) { // if (i.getId() ==
	 * Long.parseLong(requestedRoom)) { // isValid = true; // break; // } // }
	 * 
	 * // if (!isValid) { // System.out.println(YELLOW +
	 * "\nPlease insert a valid room." + WHITE); // } else { //
	 * database.setBooking(user, Long.parseLong(requestedRoom), requestedSchedule);
	 * // System.out.println(GREEN + "\nRoom succesfully booked." + WHITE); // } //
	 * } // }
	 * 
	 * // private static void deleteBooking() {
	 * 
	 * // String requestedRoom = null; // Collection<? extends Room> bookedRooms; //
	 * boolean isValid = false; // long bookingId = -1;
	 * 
	 * // bookedRooms = database.getBooked(user);
	 * 
	 * // if (bookedRooms.size() == 0) { // System.out.println(RED +
	 * "No available rooms\n" + WHITE); // return; // }
	 * 
	 * // showRooms(bookedRooms, true);
	 * 
	 * // while (!isValid) { //
	 * System.out.print("\nChoose the room you booked by ID > ");
	 * 
	 * // requestedRoom = input.next();
	 * 
	 * // if (isTeacher) { // for (Room iteration : bookedRooms) { // Classroom c =
	 * (Classroom) iteration; // Collection<ClassroomBooking> collection = //
	 * c.getBookedByTeacherId(user.getId());
	 * 
	 * // for (ClassroomBooking iterator : collection) { // if
	 * (Long.parseLong(requestedRoom) == iterator.getId()) { // bookingId =
	 * iterator.getId(); // isValid = true; // break; // } // } // }
	 * 
	 * // } else { // for (Room i : bookedRooms) {
	 * 
	 * // if (i.getId() == Long.parseLong(requestedRoom)) { // bookingId =
	 * i.getId();
	 * 
	 * // isValid = true; // break; // } // } // }
	 * 
	 * // if (!isValid) { // System.out.println(YELLOW +
	 * "\nPlease insert a valid room." + WHITE); // } else { //
	 * database.deleteBooking(user, bookingId);
	 * 
	 * // System.out.println(GREEN + "\nBooking succesfully deleted." + WHITE); // }
	 * // } // }
	 * 
	 * // private static void updateBooking() {
	 * 
	 * // String oldRoom = null; // String requestedSchedule = null; // String
	 * requestedRoom = null; // Collection<? extends Room> availableRooms; //
	 * Collection<? extends Room> bookedRooms; // boolean isValid = false; // String
	 * oldbookingId = null; // long oldRoomId = -1;
	 * 
	 * // bookedRooms = database.getBooked(user);
	 * 
	 * // if (bookedRooms.size() == 0) { // System.out.println(RED +
	 * "No available rooms\n" + WHITE); // return; // }
	 * 
	 * // showRooms(bookedRooms, true);
	 * 
	 * // while (!isValid) { // if (isTeacher) { // while (!isValid) { //
	 * System.out.print("\nChoose the room you want to change by ID >");
	 * 
	 * // oldbookingId = input.next();
	 * 
	 * // for (Room iteration : bookedRooms) { // Classroom c = (Classroom)
	 * iteration;
	 * 
	 * // Collection<ClassroomBooking> collection = //
	 * c.getBookedByTeacherId(user.getId()); // for (ClassroomBooking iterator :
	 * collection) { // if (Long.parseLong(oldbookingId) == iterator.getId()) { //
	 * oldRoomId = iterator.getRoomId(); // isValid = true; // break; // } // } // }
	 * 
	 * // if (!isValid) { // System.out.println(YELLOW +
	 * "\nPlease insert a valid room." + WHITE); // } else { //
	 * System.out.print("\nChoose the new schedule: ");
	 * 
	 * // requestedSchedule = setSchedule(); // } // } // } else { // while
	 * (!isValid) { //
	 * System.out.print("\nChoose the room you want to change by ID > "); // oldRoom
	 * = input.next(); // oldRoomId = Long.parseLong(oldRoom); // for (Room i :
	 * bookedRooms) { // if (i.getId() == oldRoomId) { // isValid = true; //
	 * oldbookingId = "1"; // // for the student this value is not used after break;
	 * // } // }
	 * 
	 * // if (!isValid) { // System.out.println(YELLOW +
	 * "\nPlease insert a valid room." + WHITE); // } // } // }
	 * 
	 * // isValid = false; // availableRooms = database.getAvailable(user,
	 * requestedSchedule);
	 * 
	 * // if (availableRooms.size() == 0) { // System.out.println(RED +
	 * "\nNo available rooms." + WHITE); // return; // }
	 * 
	 * // showRooms(availableRooms, false); // while (!isValid) {
	 * 
	 * // System.out.print("\nChoose a room by ID > "); // requestedRoom =
	 * input.next();
	 * 
	 * // for (Room i : availableRooms) { // if (i.getId() ==
	 * Long.parseLong(requestedRoom)) { // idroom isValid = true; // break; // } //
	 * }
	 * 
	 * // if (!isValid) { // System.out.println(YELLOW +
	 * "\nPlease insert a valid room." + WHITE); // } else { //
	 * database.updateBooking(user, oldRoomId, Long.parseLong(requestedRoom), //
	 * Long.parseLong(oldbookingId), // requestedSchedule); //
	 * System.out.println(GREEN + "\nBooking succesfully updated." + WHITE); // } //
	 * } // } // }
	 */
	public static void main(String[] args) {

		DBSManager database = new DBSManager();

		try {

			database.start();
			user = database.authenticate("demo@unipi.it");
			database.initializeAvailable(user);
			database.initializeBooked(user);
			System.out.println("User: " + user.getName() + " logged");
			showbooked(database.getBooked(user.getRole()));
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
