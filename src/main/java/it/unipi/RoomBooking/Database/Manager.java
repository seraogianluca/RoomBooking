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
    public void setBooking(User user, Available roomToBook, String requestedSchedule);
    public void deleteBooking(User user, Booked booked);
    public void updateBooking(User user, Available roomToBook, String requestedSchedule, Booked booked);

}