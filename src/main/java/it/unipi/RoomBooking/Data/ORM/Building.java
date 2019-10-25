package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "building")
public class Building {
    @Id
    @Column(name = "BUILDING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "BUILDING_NAME")
    private String name;

    @Column(name = "BUILDING_ADDRESS")
    private String address;

    @OneToMany(mappedBy = "BUILDING_ID")
    private ArrayList<Laboratory> laboratories;
    
    @OneToMany(mappedBy = "BUILDING_ID")
    private ArrayList<Classroom> classrooms;

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String toString() {
        return "Building Information: " + 
        "\nID: " + id + 
        "\nName: " + name + 
        "\nAddress: " + address;
    }
}