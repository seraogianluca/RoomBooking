package it.unipi.RoomBooking.Data.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Booking;

@Entity
@Table(name = "classroom_booking")
public class ClassroomBooking implements Booking {
    @Id
    @Column(name = "CLASSROOM_ID")
    private long classroomId;

    @Id
    @Column(name = "TEACHER_ID")
    private long teacherId;

    @Id
    @Column(name = "SCHEDULE")
    private String schedule;

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getId() {
        return this.classroomId;
    }

    public long getPersonId() {
        return this.teacherId;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public String toString() {
        return "Laboratory id: " + classroomId + "Teacher id: " + teacherId + "Schedule: " + schedule;
    }
}