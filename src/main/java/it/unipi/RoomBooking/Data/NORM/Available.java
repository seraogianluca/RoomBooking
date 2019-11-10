package it.unipi.RoomBooking.Data.NORM;

import it.unipi.RoomBooking.Data.Interface.Room;

public class Available {

    private String roomName;
    private String building;
    private String available;
    private String type;
    private long id;
    private int capacity;

    public Available(String roomName, String building, String available, String type, long id, int capacity) {
        this.roomName = roomName;
        this.building = building;
        this.available = available;
        this.type = type;
        this.id = id;
        this.capacity = capacity;
    }

    public Available(Room cla) {
        //Constructor for classroom booking
        this.roomName = cla.getName();
        this.building = cla.getBuilding();
        this.id = cla.getId();
        this.capacity = cla.getCapacity();
        this.type = "cla";
    }

    public String getRoom() {
        return this.roomName;
    };

    public String getBuilding() {
        return this.building;
    };

    public String getAvailable() {
        return this.available;
    };

    public String getType() {
        return this.type;
    };

    public long getId() {
        return this.id;
    };

    public int getCapacity() {
        return this.capacity;
    };

    public String toString() {
        return String.format("%-5s %-15s %-25s %-10s", this.id, this.roomName, this.building, this.capacity);
    }

}
