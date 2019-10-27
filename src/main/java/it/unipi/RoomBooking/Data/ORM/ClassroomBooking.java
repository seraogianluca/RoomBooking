package it.unipi.RoomBooking.Data.ORM;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private long bookingId;

    @Column(name = "BOOKING_SCHEDULE")
    private String schedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLASSROOM_ID")
    private Classroom classroom;

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setRoom(Room c){ 
        this.classroom = (Classroom)c;
    }

    public void setBookingId(long id) {
        this.bookingId = id;
    }

    public void setPerson(Person person) {
        this.teacher = (Teacher)person;
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
        return "\nBooking Information: " + 
                "\nBooking Id: " + bookingId + 
                "\nSchedule: " + schedule + 
                "\n" + teacher.toString()  + "\n";       
    }
}