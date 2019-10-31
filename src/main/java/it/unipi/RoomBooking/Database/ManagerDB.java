package it.unipi.RoomBooking.Database;

import java.util.Collection;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;

public interface ManagerDB {
    public void start();
    public void exit();
    public Person authenticate(String email, boolean isTeacher);
    public Collection<? extends Room> getAvailable(Person person, String schedule);
    public Collection<? extends Room> getBooked(Person person);  
    public void setBooking(Person person, Room room, String schedule);
    public void deleteBooking(Person person, Room room, long bookingId);
    public void updateBooking(Person person, Room oldRoom, Room newRoom, long bookingId, String schedule);
}