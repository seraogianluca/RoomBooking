package it.unipi.RoomBooking.Data.Interface;

public interface Booking {
    //Setter
    public void setSchedule(String schedule); 

    //Getter
    public long getId();
    public long getPersonId();
    public String getSchedule();
    public String toString();
}