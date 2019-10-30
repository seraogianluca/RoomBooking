package it.unipi.RoomBooking.Data.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.Booking;
import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;

@Entity
@Table(name = "classroom_booking")
public class ClassroomBooking implements Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long classroomBookingId;

    @Column(name = "BOOKING_SCHEDULE")
    private String classroomBookingSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "CLASSROOM_ID")
    private Classroom classroom;

    public void setSchedule(String schedule) {
        this.classroomBookingSchedule = schedule;
    }

    public void setPerson(Person person) {
        this.teacher = (Teacher)person;
    }

    public void setRoom(Room c){ 
        this.classroom = (Classroom)c;
    }

    public long getId() {
        return this.classroomBookingId;
    }

    public String getSchedule() {
        return this.classroomBookingSchedule;
    }

    public long getPersonId() {
        return this.teacher.getId();
    }

    public long getRoomId() {
        return this.classroom.getId();
    }

    public Room getRoom() {
        return this.classroom;
    }

    public String toString() {
        return "\nBooking Information: " + 
                "\nBooking Id: " + classroomBookingId + 
                "\nScheduled: " + classroomBookingSchedule + 
                "\n" + teacher.toString()  + "\n";       
    }
}