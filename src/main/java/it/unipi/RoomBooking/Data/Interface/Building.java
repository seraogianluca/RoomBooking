package it.unipi.RoomBooking.Data.Interface;

public interface Building {
    // Setter
    public void setName(String name);
    public void setAddress(String address);

    // Getter
    public long getId();
    public String getName();
    public String getAddress();

    public String toString();
}