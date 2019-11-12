package it.unipi.RoomBooking.Data.NORM;

import it.unipi.RoomBooking.Data.Interface.Booking;
import it.unipi.RoomBooking.Data.Interface.Room;

public class Booked implements Booking {
    private long id;
    private String room;
    private String schedule;
    private String type;

    public Booked(long id, String room, String schedule, String type) {
        this.id = id;
        this.room = room;
        this.schedule = schedule;
        this.type = type;
    }

    public Booked(Booking cla) {
        //Constructor for classroom booking
        this.id = cla.getId();
        this.room = cla.getRoomName();
        this.schedule = cla.getSchedule();
        this.type = "cla";
    }

    public Booked(Room lab) {
        //Constructor for laboratories
        this.id = lab.getId();
        this.room = lab.getName();
        this.schedule = null;
        this.type = "lab";
    }

    public long getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getRoomName() {
        return this.room;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        String string = null;
        if(type.equals("cla")) {
            string = String.format("%-5s %-15s %-15s", id, room, schedule);
        } else if(type.equals("lab")) {
            string = String.format("%-5s %-15s", id, room);   
        }
        
        return string;
    }

}