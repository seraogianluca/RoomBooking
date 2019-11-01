package it.unipi.RoomBooking.Data.Interface;

public interface BuildingInterface {

    public void setName(String name);
    public void setAddress(String address);

    public long getId();
    public String getName();
    public String getAddress();
    
    public String toString();

}