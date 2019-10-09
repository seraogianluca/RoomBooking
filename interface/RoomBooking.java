public final class RoomBooking {
    String version = "Room Booking v1.0";

    void prompt() {
        System.out.println("\n>");
    }

    void init() {
        System.out.println(
            version +
            "\nHi," + 
            "\nWhat you want to do?" +
            "\n1 - Book a Room." + 
            "\n2 - Delete a booking." +
            "\n3 - Update a booking."
        );

        prompt();
    }

    

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        init();
        String command = input.newLine();

        switch(command.charAt(0)) {
            case "1":
                getBooking();
                break;
            case "2":
                deleteBooking();
                break;
            case "3":
                updateBooking();
                break;
        }
    }
}