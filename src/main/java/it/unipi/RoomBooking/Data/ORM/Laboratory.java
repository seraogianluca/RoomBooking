package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="laboratory")
public class Laboratory {
    @Id
    @Column(name = "LABORATORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "LABORATORY_NAME")
    private String name;

    @Column(name = "LABORATORY_CAPACITY")
    private int capacity;

    @Column(name = "LABORATORY_AVAILABLE")
    private Boolean available;

    @ManyToMany(mappedBy = "LABORATORY_ID")
    private ArrayList<LaboratoryBooking> laboratoryBookings;

    @ManyToOne
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    // Setter
    public void setName(String name){
        this.name = name;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public void setAvailable(Boolean available){
        this.available = available;
    }

    // Getter
    public long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public Boolean getAvailable(){
        return this.available;
    }

    public String toString(){
        return "Laboratory Information: "+
                "\nID: " + id + 
                "\nName: " + name +
                "\nCapacity: " + capacity +
                "\nAvailable: " + available;
    }
}