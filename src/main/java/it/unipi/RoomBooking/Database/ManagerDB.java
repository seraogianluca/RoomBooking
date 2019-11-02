package it.unipi.RoomBooking.Database;

import java.util.Collection;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

public interface ManagerDB {
    public void start();
    public void exit();
    public Person authenticate(String email, boolean isTeacher) throws UserNotExistException;
    public Collection<? extends Room> getAvailable(Person person, String schedule);
    public Collection<? extends Room> getBooked(Person person);  
    public void setBooking(Person person, long roomId, String schedule);
    public void deleteBooking(Person person, long bookingId);
    public void updateBooking(Person person, long oldRoomID, long newRoomId, long bookingId, String schedule);
}