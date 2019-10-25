package it.unipi.RoomBooking.Data.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Booking;

@Entity
@Table(name = "classroom_booking")
public class ClassroomBooking implements Booking {
    @Id
    @Column(name = "BOOKING_ID")
    private long bookingId;

    @Column(name = "SCHEDULE")
    private String schedule;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "CLASSROOM_ID")
    private Classroom classroom;

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getId() {
        return this.bookingId;
    }

    public long getPersonId() {
        return this.teacher.getId();
    }

    public long getRoomId() {
        return this.classroom.getId();
    }

    public String getSchedule() {
        return this.schedule;
    }

    public String toString() {
        return "Laboratory id: " + bookingId + "Teacher id: " + teacher.getId() + "Schedule: " + schedule;
    }
}