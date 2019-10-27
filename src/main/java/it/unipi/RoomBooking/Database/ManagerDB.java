package it.unipi.RoomBooking.Database;

import java.util.List;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;

public interface ManagerDB {
    /* Database methods */
    public void start();
    public void exit();

    /* Teacher database methods */
    public Person authenticate(String email, boolean isTeacher);
    public List<? extends Room> getAvailable(String schedule, boolean isTeacher);
    public List<? extends Room> getBooked(Person person);
    //public void setBooking(Room r, Person id, String schedule, boolean isTeacher);
    //public void deleteBooking(long id, long roomId, String schedule);
    //public void updateBooking(long id, Room newRoom, String newSchedule);
}