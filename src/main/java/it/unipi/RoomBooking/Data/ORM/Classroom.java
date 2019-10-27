package it.unipi.RoomBooking.Data.ORM;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Room;

@Entity
@Table(name="classroom")
public class Classroom implements Room {
    @Id
    @Column(name = "CLASSROOM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "CLASSROOM_NAME")
    private String name;

    @Column(name = "CLASSROOM_CAPACITY")
    private int capacity;

    @Column(name = "CLASSROOM_AVAILABLE")
    private Boolean available;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    @OneToMany(mappedBy = "classroom",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ClassroomBooking> classroomBookings;

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

    public void setRoomId(long roomId){
        this.id=roomId;
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

    public boolean getAvailable() {
        return this.available;
    }

    public String toString(){
        return "Classroom Information: " +
                "\nID: " + id + 
                "\nName: " + name +
                "\nCapacity: " + capacity +
                "\nAvailable: " + available +
                "\n" + building.toString() +
                "\n" + classroomBookings.toString();
    }
}