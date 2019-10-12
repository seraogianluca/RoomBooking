import java.util.Locale;
import java.util.Scanner;

public final class RoomBooking {
	/* Version banner */
    static String version = 
    		" _____                         ____              _    _                       __   ___  \n" + 
    		"|  __ \\                       |  _ \\            | |  (_)                     /_ | / _ \\ \n" + 
    		"| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |\n" + 
    		"|  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` | \\ \\ / /  | || | | |\n" + 
    		"| | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \\ V /   | || |_| |\n" + 
    		"|_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |   \\_(_)  |_(_)___/ \n" + 
    		"                                                               __/ |                    \n" + 
    		"                                                              |___/                     \n";
    /* User information */
    static int personID = 2;
    static String name = null;
    static String lastname = null; 
    
    /* Methods */
    static void ident(Scanner input) {
    	boolean isValid = false;
    	
    	System.out.println(version);
    	
    	while(!isValid) {
    		
    		System.out.print("\nInsert your Name > ");
        	name = input.nextLine();
        	
        	System.out.print("\nInsert your Lastname > ");
        	lastname = input.nextLine();
        	
        	//personID = RoomBookingDBManager.getPersonID(name, lastname);
        	
        	if(personID == -1) {
        		System.out.println("\nUser not valid.");
        	} else {
        		isValid = true;
        	}
    	}
    	
    	System.out.println("\n_________________________________________\n");
        System.out.println("\nHi " + name + " " + lastname + ",");
    }
    
    static String getCommand(Scanner input) {
    	String command;
        boolean isValid = false;
        
        System.out.println(
                "\n1 - Book a Room." + 
                "\n2 - Delete a booking." +
                "\n3 - Update a booking." +
                "\n4 - Close."
            );
        
        while(!isValid) {
        	System.out.print("\nChoose an action > ");
        	command = input.nextLine();
        	
        	if(
				!command.equals("4") &&
				!command.equals("3") &&
				!command.equals("2") &&
				!command.equals("1")
        	) {
        		
        		System.out.println("\nPlease insert a valid command.");
        		
        	} else {
        		isValid = true; 
        		return command;
        	}
        }
        
        return null;
    }

	static String setSchedule(Scanner input) {
		String requestedSchedule = null;
    	boolean isValid = false;

		System.out.println(
    			"\n[M] - Morning." +
    			"\n[A] - Afternoon." +
    			"\n[F] - Full day."
    	);
    	
        while(!isValid) {
			System.out.print("\nChoose a schedule > ");
        	requestedSchedule = input.nextLine();
        	requestedSchedule = requestedSchedule.toLowerCase(Locale.ENGLISH);
        	
        	if(
        		!requestedSchedule.equals("a") &&
        		!requestedSchedule.equals("m") &&
        		!requestedSchedule.equals("f")
        	) {
        		
        		System.out.println("\nPlease insert a valid command.");
        		
        	} else {
        		isValid = true;
        	}
        }

		return requestedSchedule;
	}

    static void showRooms(/*ResultSet rs */boolean booked) {
		if(booked) {
			System.out.println("\nList of your booked rooms:\n");
			System.out.printf("%-15s %-15s", "Room", "Schedule");
			System.out.println("\n=========================================");
			/*
			 * while(rs.next()) {
			 * System.out.printf("%-15s %-15s", 
			 * 					 rs.getString("Room"),
			 * 					 rs.getString("Schedule").toUpperCase(Local.ENGLISH)
			 * );
			 * }
			*/
		} else {
			System.out.println("\nList of the avaiable rooms:\n");
			System.out.printf("%-15s %-25s %-10s %-10s", "Room", "Building", "Floor", "Capacity");
			System.out.println("\n==============================================================");
			/*
			 * while(rs.next()) {
			 * System.out.printf("%-15s %-25s %-10d %-10d", 
			 * 					 rs.getString("Room"),
			 * 					 rs.getString("Building"),
			 * 					 rs.getInt("Floor"),
			 * 					 rs.getInt("Capacity")
			 * );
			 * }
			*/
		}
    }

    static void bookARoom(Scanner input) {
		String requestedSchedule = null;
		String requestedRoom = null;
    	boolean isValid = false;
    	
    	requestedSchedule = setSchedule(input);
        //ResultSet rs = RoomBookingDBManager.getAvailableRooms(resquestedSchedule);
		showRooms(/* rs*/false);
		
		System.out.print("\nChoose a room by name > ");

		while(!isValid) {
			requestedRoom = input.nextLine();
			requestedRoom = requestedRoom.toLowerCase(Locale.ENGLISH);
			/*
			while(rs.next()) {
				if(rs.getString("Name").equal(requestedRoom)) {
					isValid = true;
					break;
				}
			}

			if(!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				RoomBookingDBManager.setBooking(personID, requestedSchedule, requestedRoom);
				System.out.println("\nRoom succesfully booked.");
			}
			*/

			System.out.println("\nRoom succesfully booked.");
			isValid = true;
		}
    }
	
	static void deleteBooking(Scanner input) {
		String requestedRoom = null;
		boolean isValid = false;

		//ResultSet rs = RoomBookingDBManager.getBookedRooms(PersonID);
		showRooms(/* rs*/true);
		System.out.print("\nChoose the room you booked > ");

		while(!isValid) {
			requestedRoom = input.nextLine();
			requestedRoom = requestedRoom.toLowerCase(Locale.ENGLISH);
			/*
			while(rs.next()) {
				if(rs.getString("Room").equal(requestedRoom)) {
					isValid = true;
					break;
				}
			}

			if(!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				RoomBookingDBManager.deleteBooking(personID, requestedRoom);
				System.out.println("\nBooking succesfully delete.");
			}
			*/

			System.out.println("\nRoom succesfully deleted.");
			isValid = true;
		}
	}

	static void updateBooking(Scanner input) {
		String requestedSchedule = null;
		String requestedRoom = null;
    	boolean isValid = false;
		
		//ResultSet rs = RoomBookingDBManager.getBookedRooms(PersonID);
		showRooms(/* rs*/true);
		
		System.out.print("\nChoose the room to update > ");

		while(!isValid) {
			requestedRoom = input.nextLine();
			requestedRoom = requestedRoom.toLowerCase(Locale.ENGLISH);
			/*
			while(rs.next()) {
				if(rs.getString("Room").equal(requestedRoom)) {
					isValid = true;
					break;
				}
			}

			if(!isValid) {
				System.out.println("\nPlease insert a valid room.");
			} else {
				requestedSchedule = setSchedule(input);
				RoomBookingDBManager.updateBooking(personID, requestedRoom, requestedSchedule);
				System.out.println("\nSchedule succesfully updated.");
			}
			*/

			System.out.println("\nSchedule succesfully updated.");
			isValid = true;
		}

	}

    /* Main */
    public static void main(String[] args) {
    	Scanner input = new Scanner(System.in);
    	boolean terminate = false;
    	String command = null;
    	
    	ident(input);
    	
    	while(!terminate) {
    		command = getCommand(input);
    		
    		if(command == null) {
    			/* Possibile eccezione */
        		input.close();
        		terminate = true;
        	}
    		
    		switch(Integer.parseInt(command)) {
            	case 1:
            		bookARoom(input);
            		break;
            	case 2:
            		System.out.println("Puppa 2");
            		deleteBooking(input);
            		break;
            	case 3:
            		System.out.println("Puppa 3");
            		updateBooking(input);
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