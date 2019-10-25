/*package it.unipi.RoomBooking.Interface;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import it.unipi.RoomBooking.Database.ManagerDB;

public final class RoomBookingCLI {
	private static String version = " _____                         ____              _    _                       __   ___  \n"
			+ "|  __ \\                       |  _ \\            | |  (_)                     /_ | / _ \\ \n"
			+ "| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |\n"
			+ "|  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` | \\ \\ / /  | || | | |\n"
			+ "| | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \\ V /   | || |_| |\n"
			+ "|_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |   \\_(_)  |_(_)___/ \n"
			+ "                                                               __/ |                    \n"
			+ "                                                              |___/                     \n";
		
	private static RoomBookingDB roomBookingDatabase;
	private static Scanner input;

	private static int userId = 0;
	private static String name = null;
	private static String lastname = null;

	private static void ident() {
		String[] user = new String[3];
		String email = null;
		boolean isValid = false;
		System.out.println(version);

		while (!isValid) {

			System.out.print("\nInsert your Email > ");
			email = input.nextLine();
			user = roomBookingDatabase.getPersonID(email);

			if (user == null) {
				System.out.println("\nUser not valid.");
			} else {
				userId = Integer.parseInt(user[0]);
				name = user[1];
				lastname = user[2];
				isValid = true;
			}
		}

		System.out.println("\n_________________________________________\n");
		System.out.println("\nHi " + name + " " + lastname + ",");
	}

	private static String getCommand() {
		String command;
		boolean isValid = false;

		System.out
				.println("\n1 - Book a Room." + "\n2 - Delete a booking." + "\n3 - Update a booking." + "\n4 - Close.");

		while (!isValid) {
			System.out.print("\nChoose an action > ");
			command = input.nextLine();

			if (!command.equals("4") && !command.equals("3") && !command.equals("2") && !command.equals("1")) {
				System.out.println("\nPlease insert a valid command.");
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
			requestedSchedule = input.nextLine();
			requestedSchedule = requestedSchedule.toLowerCase(Locale.ENGLISH);

			if (!requestedSchedule.equals("a") && !requestedSchedule.equals("m")) {
				System.out.println("\nPlease insert a valid command.");
			} else {
				isValid = true;
			}
		}

		return requestedSchedule;
	}

	private static void showRooms(ArrayList<ArrayList<String>> table, boolean booked) {
		if (booked) {
			System.out.println("\nList of your booked rooms:\n");
			System.out.printf("%-5s %-15s %-15s\n", "ID", "Room", "Schedule");
			System.out.println("\n=========================================");
			int numRows = table.get(0).size() - 1;

			while (numRows >= 0) {
				System.out.printf("%-5s %-15s %-15s\n", table.get(0).get(numRows), table.get(1).get(numRows),
						table.get(2).get(numRows).toUpperCase(Locale.ENGLISH));
				numRows--;
			}

		} else {
			System.out.println("\nList of the avaiable rooms:\n");
			System.out.printf("%-5s %-15s %-25s %-10s %-10s", "ID", "Room", "Building", "Floor", "Capacity");
			System.out.println("\n===================================================================");
			int numRows = table.get(0).size() - 1;

			while (numRows >= 0) {
				System.out.printf("%-5s %-15s %-25s %-10s %-10s\n", table.get(0).get(numRows),
						table.get(1).get(numRows), table.get(2).get(numRows), table.get(3).get(numRows),
						table.get(4).get(numRows));
				numRows--;
			}
		}
	}

	private static void bookARoom() {
		String requestedSchedule = null;
		String requestedRoom = null;
		ArrayList<ArrayList<String>> availableRooms;
		boolean isValid = false;

		requestedSchedule = setSchedule();
		availableRooms = roomBookingDatabase.getAvailableRooms(requestedSchedule);
		showRooms(availableRooms, false);

		while (!isValid) {
			System.out.print("\nChoose a room by ID > ");
			requestedRoom = input.nextLine();
			int numRows = availableRooms.get(0).size() - 1;

			while (numRows >= 0) {
				if (availableRooms.get(0).get(numRows).equals(requestedRoom)) {
					isValid = true;
					break;
				}
				numRows--;
			}

			if (!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				roomBookingDatabase.setBooking(userId, requestedSchedule, Integer.parseInt(requestedRoom));
				System.out.println("\nRoom succesfully booked.");
			}
		}

	}

	private static void deleteBooking() {
		String requestedRoom = null;
		String requestedSchedule = null;
		ArrayList<ArrayList<String>> bookedRooms;
		boolean isValid = false;

		bookedRooms = roomBookingDatabase.getBookedRooms(userId);
		showRooms(bookedRooms, true);

		while (!isValid) {
			System.out.print("\nChoose the room you booked by ID > ");
			requestedRoom = input.nextLine();
			requestedSchedule = setSchedule();
			int numRows = bookedRooms.get(0).size() - 1;

			while (numRows >= 0) {
				if (bookedRooms.get(0).get(numRows).equals(requestedRoom)
						&& bookedRooms.get(2).get(numRows).equals(requestedSchedule)) {
					isValid = true;
					break;
				}
				numRows--;
			}

			if (!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				roomBookingDatabase.deleteBooking(userId, Integer.parseInt(requestedRoom), requestedSchedule);
				System.out.println("\nBooking succesfully delete.");
			}
		}

	}

	private static void updateBooking() {
		String oldSchedule = null;
		String oldRoom = null;
		String requestedSchedule = null;
		String requestedRoom = null;
		ArrayList<ArrayList<String>> availableRooms;
		ArrayList<ArrayList<String>> bookedRooms;
		boolean isValid = false;

		bookedRooms = roomBookingDatabase.getBookedRooms(userId);
		showRooms(bookedRooms, true);

		while (!isValid) {
			System.out.print("\nChoose the room you want to change by ID > ");
			oldRoom = input.nextLine();
			System.out.print("\nChoose the schedule you want to change: ");
			oldSchedule = setSchedule();
			int numRows = bookedRooms.get(0).size() - 1;

			while (numRows >= 0) {
				if (
					bookedRooms.get(0).get(numRows).equals(oldRoom) && 
					bookedRooms.get(2).get(numRows).equals(oldSchedule)
				) {
					isValid = true;
					break;
				}
				numRows--;
			}

			if (!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				isValid = false;
				System.out.print("\nChoose the new schedule: ");
				requestedSchedule = setSchedule();
				availableRooms = roomBookingDatabase.getAvailableRooms(requestedSchedule);
				showRooms(availableRooms, false);

				while (!isValid) {
					System.out.print("\nChoose a new room by ID > ");
					requestedRoom = input.nextLine();
					numRows = availableRooms.get(0).size() - 1;

					while (numRows >= 0) {
						if (availableRooms.get(0).get(numRows).equals(requestedRoom)) {
							isValid = true;
							break;
						}
						numRows--;
					}

					if (!isValid) {
						System.out.println("\nPlease insert a valid room.");
					} else {
						roomBookingDatabase.updateBooking(userId, Integer.parseInt(requestedRoom), requestedSchedule, Integer.parseInt(oldRoom), oldSchedule);
						System.out.println("\nSchedule succesfully updated.");
					}
				}

			}
		}

	}

	public static void main(String[] args) {
		input = new Scanner(System.in);
		boolean terminate = false;
		String command = null;

		String databaseConnectionString = "jdbc:mysql://localhost:3306/roombooking?user=root&password=giovanni&serverTimezone=UTC";
		roomBookingDatabase = new RoomBookingDB(databaseConnectionString);

		ident();

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

		input.close();
	}
}

*/