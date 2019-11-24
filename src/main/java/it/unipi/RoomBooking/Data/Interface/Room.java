package it.unipi.RoomBooking.Data.Interface;

public interface Room {

    public void setName(String name);
    public void setCapacity(int capacity);
    public void setAvailable(Boolean available);

    public long getId();
    public String getName();
    public int getCapacity();
    public String getBuilding();
    public boolean getAvailable();   
}