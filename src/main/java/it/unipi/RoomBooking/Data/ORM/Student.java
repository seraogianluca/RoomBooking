package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.ORM.Laboratory;

@Entity
@Table(name = "student")
public class Student implements Person {
    @Id
    @Column(name = "STUDENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long studentId;

    @Column(name = "STUDENT_NAME")
    private String studentName;

    @Column(name = "STUDENT_LASTNAME")
    private String studentLastname;

    @Column(name = "STUDENT_EMAIL")
    private String studentEmail;

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private Collection<Laboratory> laboratories = new ArrayList<Laboratory>();

    public void setName(String name) {
        this.studentName= name;
    }

    public void setLastname(String lastname) {
        this.studentLastname = lastname;
    }

    public void setEmail(String email) {
        this.studentEmail = email;
    }

    public long getId() {
        return this.studentId;
    }

    public String getName() {
        return this.studentName;
    }

    public String getLastname() {
        return this.studentLastname;
    }

    public String getEmail() {
        return this.studentEmail;
    }

    public Collection<Laboratory> getLaboratories() {
        return this.laboratories;
    }

    public void setLaboratories(Laboratory laboratory) {
        this.laboratories.add(laboratory);
    }

    public void deleteBooking(Laboratory laboratory) {
        this.laboratories.remove(laboratory);
    }

}
