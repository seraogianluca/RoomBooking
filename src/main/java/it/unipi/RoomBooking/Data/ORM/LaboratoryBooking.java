package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Booking;

@Entity
@Table(name = "laboratory_booking")
public class LaboratoryBooking implements Booking {
    @Id
    @Column(name = "BOOKING_ID")
    private long bookingId;

    @Column(name = "SCHEDULE")
    private String schedule;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID")
    private Student student;

    @ManyToMany
    @JoinColumn(name = "LABORATORY_ID")
    private ArrayList<Laboratory> laboratories;




    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getId() {
        return this.bookingId;
    }

    public long getPersonId() {
        return this.student.getId();
    }

    public String getSchedule() {
        return this.schedule;
    }

    public String toString() {
        return "Booking id: " + bookingId + "Student id: " + student.getId() + "Schedule: " + schedule;
    }
}