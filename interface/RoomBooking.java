import java.util.Locale;
import java.util.Scanner;

public final class RoomBooking {
	/* Variables */
    static String version = 
    		" _____                         ____              _    _                       __   ___  \n" + 
    		"|  __ \\                       |  _ \\            | |  (_)                     /_ | / _ \\ \n" + 
    		"| |__) |___   ___  _ __ ___   | |_) | ___   ___ | | ___ _ __   __ _  __   __  | || | | |\n" + 
    		"|  _  // _ \\ / _ \\| '_ ` _ \\  |  _ < / _ \\ / _ \\| |/ / | '_ \\ / _` | \\ \\ / /  | || | | |\n" + 
    		"| | \\ \\ (_) | (_) | | | | | | | |_) | (_) | (_) |   <| | | | | (_| |  \\ V /   | || |_| |\n" + 
    		"|_|  \\_\\___/ \\___/|_| |_| |_| |____/ \\___/ \\___/|_|\\_\\_|_| |_|\\__, |   \\_(_)  |_(_)___/ \n" + 
    		"                                                               __/ |                    \n" + 
    		"                                                              |___/                     \n";
    
    static int personID = 2;
    static String name = null;
    static String lastname = null; 
    
    /* Methods */
    static void prompt() {
        System.out.print("\n>");
    }
    
    static void ident(Scanner input) {
    	boolean isValid = false;
    	
    	System.out.println(version);
    	
    	while(!isValid) {
    		
    		System.out.println("\nInsert your Name:");
    		prompt();
        	name = input.nextLine();
        	
        	System.out.println("\nInsert your Lastname:");
        	prompt();
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
            	"\nChoose an action:" +
                "\n1 - Book a Room." + 
                "\n2 - Delete a booking." +
                "\n3 - Update a booking." +
                "\n4 - Close."
            );
        
        while(!isValid) {
        	prompt();
        	command = input.nextLine();
        	
        	if(
        		command.length() != 1 ||
        		Integer.parseInt(command) > 4 ||
        		Integer.parseInt(command) < 0
        	) {
        		
        		System.out.println("\nPlease insert a valid command.");
        		
        	} else {
        		isValid = true; 
        		return command;
        	}
        }
        
        return null;
    }

    static void showRooms() {
    	System.out.println("\nList of the avaiable rooms:\n");
    	System.out.println(
    			"\n__________________________________________" +
    			"\n|Room\t|Building\t|Floor\t|Capacity\t|" +
    			"\n__________________________________________" 
    			);
    	/*
    	 * while(rs.next()) {
    	 * 	System.out.print("|" + rs.getString("Room") + "\t");
    	 * 	System.out.print("|" + rs.getString("Building") + "\t");
    	 * 	System.out.print("|" + rs.getString("Floor") + "\t");
    	 * 	System.out.print("|" + rs.getInt("Capacity") + "\t|");
    	 * 	System.out.print("\n");
    	 * }
    	*/
    }
    
    static void bookARoom(Scanner input) {
    	String requestedSchedule = null;
    	boolean isValid = false;
    	
    	System.out.println(
    			"\nChoose a schedule:" +
    			"\n[M] - Morning." +
    			"\n[A] - Afternoon." +
    			"\n[F] - Full day."
    	);
    	
        while(!isValid) {
        	prompt();
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
    	
        //ResultSet rs = RoomBookingDBManager.getAvailableRooms();
        showRooms();
    	
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
            		//deleteBooking();
            		break;
            	case 3:
            		System.out.println("Puppa 3");
            		//updateBooking();
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