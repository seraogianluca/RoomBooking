package it.unipi.RoomBooking.Data.NORM;
import it.unipi.RoomBooking.Data.Interface.Person;

public class User implements Person {

    private long id;
    private String name;
    private String lastname;
    private String email;
    private String role;

    public User(long id, String name, String lastname, String email, String role) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }

    public User(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.lastname = person.getLastname();
        this.email = person.getEmail();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRole() {
        return this.role;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}