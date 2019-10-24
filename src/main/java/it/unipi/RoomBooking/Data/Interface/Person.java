package it.unipi.RoomBooking.Data.Interface;

public interface Person {
    // setter
    public void setEmail(String email);
    public void setLastname(String lastname);
    public void setName(String name);

    //getter
    public long getId();
    public String getName();
    public String getLastname();
    public String getEmail();
    
    public String toString();
}