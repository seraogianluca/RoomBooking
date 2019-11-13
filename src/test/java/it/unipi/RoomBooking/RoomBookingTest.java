package it.unipi.RoomBooking;
import org.junit.Test;

import java.util.Collection;

import it.unipi.RoomBooking.Database.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;
import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;
import it.unipi.RoomBooking.Data.NORM.User;

/**
 * Unit test for RoomBooking App.
 */

public class RoomBookingTest {

    private DBSManager database = new DBSManager();


    // @Test
    // public void studentAuthenticationTest() {
    // HibernateManager manager = new HibernateManager();
    // try {
    // System.out.println("*********************************");
    // System.out.println("* Student authentication test *");
    // System.out.println("*********************************");

    // Student student = new Student();
    // manager.start();
    // System.out.println("---------------------------------");
    // System.out.println("Logging as the student: demo@studenti.unipi.it");
    // student = (Student)manager.authenticate("demo@studenti.unipi.it", false);
    // assertEquals("Retreived the wrong student.", 1, student.getId());
    // System.out.println(student.toString());
    // System.out.println("---------------------------------");
    // System.out.println("Logging with the student: randomstring");
    // student = (Student)manager.authenticate("randomstring", false);
    // } catch(UserNotExistException uex){
    // System.out.println(uex.getMessage());
    // assertTrue(true);
    // } catch (Exception e) {
    // System.err.println(e.getMessage());
    // assertTrue(false);
    // } finally {
    // manager.exit();
    // }
    // }

    // @Test
    // public void bookALaboratoryTest() {
    // HibernateManager manager = new HibernateManager();
    // try {
    // System.out.println("****************************");
    // System.out.println("* Book a laboratory test *");
    // System.out.println("****************************");

    // Student student = new Student();
    // Collection<Room> laboratories = new ArrayList<Room>();
    // Collection<Room> booked = new ArrayList<Room>();

    // manager.start();
    // System.out.println("----------------------------");
    // System.out.println("Logging as the student: demo@studenti.unipi.it");
    // student = (Student) manager.authenticate("demo@studenti.unipi.it", false);

    // System.out.println("----------------------------");
    // laboratories.addAll(manager.getAvailable(student, null));
    // System.out.println("Available laboratories retreived:");
    // for(Room lab : laboratories) {
    // System.out.println(lab.toString());
    // }

    // System.out.println("----------------------------");
    // System.out.println("Booking the laboratory:");
    // Laboratory laboratory = (Laboratory)laboratories.iterator().next();
    // System.out.println(laboratory.toString());

    // System.out.println("----------------------------");
    // manager.setBooking(student, laboratory.getId(), null);
    // laboratory.setStudent(student);
    // booked.addAll(manager.getBooked(student));

    // //Works only for the first booking
    // assertEquals(laboratory.getId(), booked.iterator().next().getId());
    // } catch (Exception e) {
    // assertTrue(false);
    // } finally {
    // manager.exit();
    // }
    // }

    // @Test
    // public void setUnavailableLaboratoryTest() {
    // HibernateManager manager = new HibernateManager();
    // try {
    // System.out.println("****************************************");
    // System.out.println("* Set unavailable on laboratory test *");
    // System.out.println("****************************************");

    // Student student = new Student();
    // Collection<Room> booked = new ArrayList<Room>();

    // manager.start();
    // System.out.println("---------------------------------------");
    // System.out.println("Logging as the student: demo@studenti.unipi.it");
    // student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
    // System.out.println("Booking the laboratory: 2");
    // manager.setBooking(student, 2, null);

    // System.out.println("---------------------------------------");
    // System.out.println("Logging as the student: demo2@studenti.unipi.it");
    // student = (Student) manager.authenticate("demo2@studenti.unipi.it", false);
    // System.out.println("Booking the laboratory: 2");
    // manager.setBooking(student, 2, null);

    // booked.addAll(manager.getBooked(student));

    // //Works only for the first booking
    // assertFalse(booked.iterator().next().getAvailable());
    // } catch (Exception e) {
    // assertTrue(false);
    // } finally {
    // manager.exit();
    // }
    // }


    
    @Test
    public void deleteBookingRoom() {
       // deleteBooking(User user, Booked booked);
    }


    @Test
    public void getBookingLaboratory() {
        try {
            database.start();
            System.out.println("****************************************");
            System.out.println("*                Laboratory            *");
            System.out.println("****************************************");
            
            User user = database.authenticate("demo@studenti.unipi.it");
            database.initializeAvailable(user);
            database.initializeBooked(user);

            Collection<Booked> bookings = database.getBooked(user.getRole());
            System.out.println("Student booking:");
            System.out.println(bookings.toString());

            Collection<Available> availables = database.getAvailable(null, user.getRole());
            System.out.println("Student availables:");
            System.out.println(availables.toString());

        } catch (UserNotExistException ue) {
            System.out.println("UPS!" + ue);
        } finally {
            database.exit();
        }
    }

    @Test
    public void getBookingClassroom() {
        try {
            database.start();
            System.out.println("****************************************");
            System.out.println("*                 Classrooms           *");
            System.out.println("****************************************");

            User user = database.authenticate("demo@unipi.it");
            database.initializeAvailable(user);
            database.initializeBooked(user);

            Collection<Booked> bookings = database.getBooked(user.getRole());
            bookings = database.getBooked(user.getRole());
            System.out.println("Teacher booking:");
            System.out.println(bookings.toString());

            Collection<Available> availables = database.getAvailable(null, user.getRole());
            availables = database.getAvailable("m", user.getRole());
            System.out.println("Teacher availables:");
            System.out.println(availables.toString());
        } catch (UserNotExistException ue) {
            System.out.println("UPS!" + ue);
        } finally {
            database.exit();
        }
    }
}
