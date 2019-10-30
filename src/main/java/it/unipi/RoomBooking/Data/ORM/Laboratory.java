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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Room;

@Entity
@Table(name = "laboratory")
public class Laboratory implements Room {
    @Id
    @Column(name = "LABORATORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laboratoryId;

    @Column(name = "LABORATORY_NAME")
    private String laboratoryName;

    @Column(name = "LABORATORY_CAPACITY")
    private int laboratoryCapacity;

    @Column(name = "LABORATORY_AVAILABLE")
    private boolean laboratoryAvailable;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "laboratory_booking", joinColumns = {
            @JoinColumn(name = "LABORATORY_ID") }, inverseJoinColumns = { @JoinColumn(name = "STUDENT_ID") })
    private Collection<Student> students = new ArrayList<Student>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BUILDING_ID")
    private Building building;

    // Setter
    public void setName(String name) {
        this.laboratoryName = name;
    }

    public void setCapacity(int capacity) {
        this.laboratoryCapacity = capacity;
    }

    public void setStudent(Student student) {
        this.students.add(student);
    }

    public void setAvailable(Boolean available) {
        this.laboratoryAvailable = available;
    }

    // Getter
    public long getId() {
        return this.laboratoryId;
    }

    public String getName() {
        return this.laboratoryName;
    }

    public int getCapacity() {
        return this.laboratoryCapacity;
    }

    public boolean getAvailable() {
        return this.laboratoryAvailable;
    }

    public Collection<Student> getBookings() {
        return this.students;
    }

    public void removeBooking(Student student) {
        this.students.remove(student);
        student.getLaboratories().remove(this);
    }

    public int getBookingNumber() {
        return students.size();
    }


    public String toString() {
        return "Laboratory Information: " + 
        "\nID: " + laboratoryId + 
        "\nName: " + laboratoryName +
        "\n" + building.toString();
    }
}