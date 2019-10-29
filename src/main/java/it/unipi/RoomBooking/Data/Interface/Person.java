package it.unipi.RoomBooking.Data.Interface;

public interface Person {

    public void setName(String name);
    public void setLastname(String lastname);
    public void setEmail(String email);

    public long getId();
    public String getName();
    public String getLastname();
    public String getEmail();
    
    public String toString();
    
}