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
    private long classroomId;

    @Column(name = "CLASSROOM_NAME")
    private String classroomName;

    @Column(name = "CLASSROOM_CAPACITY")
    private int classroomCapacity;

    @Column(name = "CLASSROOM_AVAILABLE")
    private Boolean classroomAvailable;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClassroomBooking> classroomBookings;

    // Setter
    public void setName(String name){
        this.classroomName = name;
    }

    public void setCapacity(int capacity){
        this.classroomCapacity = capacity;
    }

    public void setAvailable(Boolean available) {
        this.classroomAvailable = available;
    }
    
    // Getter
    public long getId(){
        return this.classroomId;
    }

    public String getName(){
        return this.classroomName;
    }

    public int getCapacity(){
        return this.classroomCapacity;
    }

    public boolean getAvailable() {
        return this.classroomAvailable;
    }

    public String toString(){
        return "Classroom Information: " +
                "\nID: " + classroomId + 
                "\nName: " + classroomName +
                "\nCapacity: " + classroomCapacity +
                "\nAvailable: " + classroomAvailable +
                "\n" + building.toString();
    }
}