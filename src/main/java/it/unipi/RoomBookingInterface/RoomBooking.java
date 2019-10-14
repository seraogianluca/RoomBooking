package it.unipi.RoomBookingInterface;

import it.unipi.RoomBookingDatabase.RoomBookingDB;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public final class RoomBooking {
	/* Version banner */
	static String version = " _____                         ____              _    _                       __   ___  \n"
			+ "|  __ \\                       |  _ \\            | |  (_)                     /_ | / _ \\ \n"
			+ "| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |\n"
			+ "|  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` | \\ \\ / /  | || | | |\n"
			+ "| | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \\ V /   | || |_| |\n"
			+ "|_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |   \\_(_)  |_(_)___/ \n"
			+ "                                                               __/ |                    \n"
			+ "                                                              |___/                     \n";
	/* User information */
	static int userId = 0;
	static String name = null;
	static String lastname = null;

	/* User identification method */
	static void ident(Scanner input, RoomBookingDB database) {
		String[] user = new String[3];
		String email = null;
		boolean isValid = false;
		System.out.println(version);

		while (!isValid) {

			System.out.print("\nInsert your Email > ");
			email = input.nextLine();
			user = database.getPersonID(email);

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

	/* User command handler */
	static String getCommand(Scanner input) {
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

	/* Set requested schedule */
	static String setSchedule(Scanner input) {
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

	/* Show rooms in table format */
	static void showRooms(ArrayList<ArrayList<String>> table, boolean booked) {
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

	/* Booking request */
	static void bookARoom(Scanner input, RoomBookingDB database) {
		String requestedSchedule = null;
		String requestedRoom = null;
		ArrayList<ArrayList<String>> availableRooms;
		boolean isValid = false;

		requestedSchedule = setSchedule(input);
		availableRooms = database.getAvailableRooms(requestedSchedule);
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
				database.setBooking(userId, requestedSchedule, Integer.parseInt(requestedRoom));
				System.out.println("\nRoom succesfully booked.");
			}
		}

	}

	/* Delete a booking */
	static void deleteBooking(Scanner input, RoomBookingDB database) {
		String requestedRoom = null;
		String requestedSchedule = null;
		ArrayList<ArrayList<String>> bookedRooms;
		boolean isValid = false;

		bookedRooms = database.getBookedRooms(userId);
		showRooms(bookedRooms, true);

		while (!isValid) {
			System.out.print("\nChoose the room you booked by ID > ");
			requestedRoom = input.nextLine();
			requestedSchedule = setSchedule(input);
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
				database.deleteBooking(userId, Integer.parseInt(requestedRoom), requestedSchedule);
				System.out.println("\nBooking succesfully delete.");
			}
		}

	}

	/* Update a booking */
	static void updateBooking(Scanner input, RoomBookingDB database) {
		String oldSchedule = null;
		String oldRoom = null;
		String requestedSchedule = null;
		String requestedRoom = null;
		ArrayList<ArrayList<String>> availableRooms;
		ArrayList<ArrayList<String>> bookedRooms;
		boolean isValid = false;

		bookedRooms = database.getBookedRooms(userId);
		showRooms(bookedRooms, true);

		while (!isValid) {
			System.out.print("\nChoose the room you want to change by ID > ");
			oldRoom = input.nextLine();
			System.out.print("\nChoose the schedule you want to change: ");
			oldSchedule = setSchedule(input);
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
				requestedSchedule = setSchedule(input);
				availableRooms = database.getAvailableRooms(requestedSchedule);
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
						database.updateBooking(userId, Integer.parseInt(requestedRoom), requestedSchedule, Integer.parseInt(oldRoom), oldSchedule);
						System.out.println("\nSchedule succesfully updated.");
					}
				}

			}
		}

	}

	/* Main */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		boolean terminate = false;
		String command = null;

		String databaseConnectionString = "jdbc:mysql://localhost:3306/roombooking?user=root&password=giovanni&useSSL=false&serverTimezone=UTC";
		RoomBookingDB roomBookingDatabase = new RoomBookingDB(databaseConnectionString);

		ident(input, roomBookingDatabase);

		while (!terminate) {
			command = getCommand(input);

			if (command == null) {
				/* Possibile eccezione */
				input.close();
				terminate = true;
			}

			switch (Integer.parseInt(command)) {
			case 1:
				bookARoom(input, roomBookingDatabase);
				break;
			case 2:
				deleteBooking(input, roomBookingDatabase);
				break;
			case 3:
				updateBooking(input, roomBookingDatabase);
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
