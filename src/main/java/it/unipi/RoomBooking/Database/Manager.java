package it.unipi.RoomBooking.Database;

import java.util.Collection;

import it.unipi.RoomBooking.Data.NORM.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

public interface Manager {
    public void start();
    public void exit();
    public void initializeAvailable(User user);
    public void initializeBooked(User user);
    public User authenticate(String email) throws UserNotExistException;
    public Collection<Available> getAvailable(String requestedSchedule, String role);
    public Collection<Booked> getBooked(String role);
    //public void setBooking(Person person, long roomId, String schedule);
    //public void deleteBooking(Person person, long bookingId);
    //public void updateBooking(Person person, long oldRoomID, long newRoomId, long bookingId, String schedule);

}