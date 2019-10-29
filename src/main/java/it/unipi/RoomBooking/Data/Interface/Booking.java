package it.unipi.RoomBooking.Data.Interface;

public interface Booking {

    public void setSchedule(String schedule);
    public void setPerson(Person person);
    public void setRoom(Room room);

    public long getId();
    public long getPersonId();
    public long getRoomId();
    public String getSchedule();
    
    public String toString();

}