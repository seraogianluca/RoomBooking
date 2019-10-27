package it.unipi.RoomBooking.Data.Interface;

public interface Booking {//interface di classroombooking
    //Setter
    public void setSchedule(String schedule);
    public void setRoom(Room room);
    public void setBookingId(long id);
    public void setPerson(Person person);

    //Getter
    public long getId();
    public long getPersonId();
    public String getSchedule();
    public String toString();
}