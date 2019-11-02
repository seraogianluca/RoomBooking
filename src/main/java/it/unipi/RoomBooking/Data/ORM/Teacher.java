package it.unipi.RoomBooking.Data.ORM;

import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Person;

@Entity
@Table(name="teacher")
public class Teacher implements Person {
    @Id
    @Column(name="TEACHER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teacherId;

    @Column(name="TEACHER_NAME")
    private String teacherName;

    @Column(name = "TEACHER_LASTNAME")
    private String teacherLastname;

    @Column(name = "TEACHER_EMAIL")
    private String teacherEmail;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teacher")
    private Collection<ClassroomBooking> classroomBookings = new ArrayList<ClassroomBooking>();

    public void setName(String name){
        this.teacherName = name;
    }

    public void setEmail(String email){
        this.teacherEmail = email;
    }

    public void setLastname(String lastname){
        this.teacherLastname = lastname;
    }

    public void setBooking(ClassroomBooking booking) {
        this.classroomBookings.add(booking);
    }

    public long getId(){
        return this.teacherId;
    }

    public String getName(){
        return this.teacherName;
    }

    public String getLastname(){
        return this.teacherLastname;
    }
    
    public String getEmail(){
        return this.teacherEmail;
    }

    public Collection<ClassroomBooking> getBooked() {
        return this.classroomBookings;
    }

    
    public String toString(){
        return "Teacher Information: " +
                "\nName: " + teacherName + 
                "\nLastname: " + teacherLastname;
    }

}