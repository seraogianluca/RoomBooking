package it.unipi.RoomBooking.Database;

import java.util.Collection;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;

public interface ManagerDB {
    /* Database methods */
    public void start();
    public void exit();

    /* Teacher database methods */
    public Person authenticate(String email, boolean isTeacher);
    public Collection<? extends Room> getAvailable(Person person, String schedule);
    public Collection<? extends Room> getBooked(Person person);
    public void setBooking(Person person, Room r, String schedule);
    //public void deleteBooking(long id, long roomId, String schedule);
    //public void updateBooking(long id, Room newRoom, String newSchedule);
}