package it.unipi.RoomBooking.Data.ORM;

import it.unipi.RoomBooking.Data.ORM.LaboratoryBooking;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Person;

@Entity
@Table(name="student")
public class Student implements Person {
    @Id
    @Column(name = "STUDENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "STUDENT_NAME")
    private String name;

    @Column(name = "STUDENT_LASTNAME")
    private String lastname;

    @Column(name = "STUDENT_EMAIL")
    private String email;

    @OneToMany(mappedBy = "STUDENT_ID")
    private ArrayList<LaboratoryBooking> laboratoryBookings;

    // Setter
    public void setEmail(String email){
        this.email = email;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    
    public void setName(String name){
        this.name = name;
    }

    // Getter
    public long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getLastname(){
        return this.lastname;
    }
    
    public String getEmail(){
        return this.email;
    }

    public String toString(){
        return "Student Information: "+
                "\nID: " + id + 
                "\nName: " + name + 
                "\nLast Name: " + lastname + 
                "\nEmail: " + email;
    }
}

