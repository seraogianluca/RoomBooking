package it.unipi.RoomBooking.Data.Interface;

public interface Room {
    // Setter
    public void setName(String name);
    public void setCapacity(int capacity);
    public void setAvailability(int availability);
    public void setAvailable(Boolean available);

    // Getter
    public long getId();
    public String getName();
    public int getCapacity();
    public int getAvailability();
    public Boolean setAvailable();

    String toString();
}