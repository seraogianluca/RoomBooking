package it.unipi.RoomBooking.Data.ORM;

import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @OneToMany(mappedBy = "teacher")
    private Set<ClassroomBooking> classroomBookings;

    //Setter
    public void setName(String name){
        this.teacherName = name;
    }

    public void setEmail(String email){
        this.teacherEmail = email;
    }

    public void setLastname(String lastname){
        this.teacherLastname = lastname;
    }

    //Getter
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

    public String toString(){
        return "Teacher Information: " +
                "\nName: " + teacherName + 
                "\nLastname: " + teacherLastname;
    }

}