package it.unipi.RoomBooking.Database;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;

public interface ManagerDB {
    /* Database methods */
    public void start();
    public void exit();

    /* Teacher database methods */
    public Person getId(long id);
    public Room[] getAvailable(String schedule);
    public Room[] getBooked(long id);
    public void setBooking(long id, long roomId, String schedule);
    public void deleteBooking(long id, long roomId, String schedule);
    public void updateBooking(long teacherId, long newRoomId, String newSchedule, long oldRoomId, String oldSchedule);
}