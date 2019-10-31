package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;
import java.util.Collection;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<ClassroomBooking> classroomBookings = new ArrayList<ClassroomBooking>();

    public void setName(String name){
        this.classroomName = name;
    }

    public void setCapacity(int capacity){
        this.classroomCapacity = capacity;
    }

    public void setAvailable(Boolean available) {
        this.classroomAvailable = available;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void setBooking(ClassroomBooking booking) {
        this.classroomBookings.add(booking);
    }

    public long getId() {
        return this.classroomId;
    }

    public String getName() {
        return this.classroomName;
    }

    public int getCapacity() {
        return this.classroomCapacity;
    }

    public boolean getAvailable() {
        return this.classroomAvailable;
    }

    public Collection<ClassroomBooking> getBooking() {
        return this.classroomBookings;
    }

    public long getBookingId(long teacherId, String schedule) {
        for(ClassroomBooking iteration : this.classroomBookings) {
            if(iteration.getPersonId() == teacherId && iteration.getSchedule().equals(schedule)) {
                return iteration.getId();
            }
        }
        return -1;
    }

    public ClassroomBooking getBookingById(long id) {
        for(ClassroomBooking iteration : this.classroomBookings){
            if(iteration.getId() == id){
                return iteration;
            }
        }
        return null;
    }

    public void deleteBooking(ClassroomBooking booking) {
        this.classroomBookings.remove(booking);
    }

    public String toString() {
        return "Classroom Information: " +
                "\nID: " + classroomId + 
                "\nName: " + classroomName +
                "\nCapacity: " + classroomCapacity +
                "\nAvailable: " + classroomAvailable +
                "\n" + building.toString() + 
                "\n" + classroomBookings.toString();
    }
}