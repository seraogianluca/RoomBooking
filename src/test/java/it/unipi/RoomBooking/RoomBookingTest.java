package it.unipi.RoomBooking;
/*
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import it.unipi.RoomBooking.Data.Interface.*;
import it.unipi.RoomBooking.Data.ORM.*;
import it.unipi.RoomBooking.Database.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;
*/


/**
 * Unit test for RoomBooking App.
 */

public class RoomBookingTest {
/*
    @Test
    public void studentAuthenticationTest() {
        HibernateManager manager = new HibernateManager(); 
        try {
            System.out.println("*********************************");
            System.out.println("*  Student authentication test  *");
            System.out.println("*********************************");

            Student student = new Student();
            manager.start();
            System.out.println("---------------------------------");
            System.out.println("Logging as the student: demo@studenti.unipi.it");
            student = (Student)manager.authenticate("demo@studenti.unipi.it", false);
            assertEquals("Retreived the wrong student.", 1, student.getId());
            System.out.println(student.toString());
            System.out.println("---------------------------------");
            System.out.println("Logging with the student: randomstring");
            student = (Student)manager.authenticate("randomstring", false);
        } catch(UserNotExistException uex){
            System.out.println(uex.getMessage());
            assertTrue(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void bookALaboratoryTest() {
        HibernateManager manager = new HibernateManager();
        try {
            System.out.println("****************************");
            System.out.println("*  Book a laboratory test  *");
            System.out.println("****************************");

            Student student = new Student();
            Collection<Room> laboratories = new ArrayList<Room>();
            Collection<Room> booked = new ArrayList<Room>();

            manager.start();
            System.out.println("----------------------------");
            System.out.println("Logging as the student: demo@studenti.unipi.it");
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);

            System.out.println("----------------------------");
            laboratories.addAll(manager.getAvailable(student, null));
            System.out.println("Available laboratories retreived:");            
            for(Room lab : laboratories) {
                System.out.println(lab.toString());   
            }

            System.out.println("----------------------------");
            System.out.println("Booking the laboratory:"); 
            Laboratory laboratory = (Laboratory)laboratories.iterator().next();
            System.out.println(laboratory.toString());

            System.out.println("----------------------------");
            manager.setBooking(student, laboratory.getId(), null);
            laboratory.setStudent(student);
            booked.addAll(manager.getBooked(student));

            //Works only for the first booking
            assertEquals(laboratory.getId(), booked.iterator().next().getId());
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void setUnavailableLaboratoryTest() {
        HibernateManager manager = new HibernateManager();
        try {
            System.out.println("****************************************");
            System.out.println("*  Set unavailable on laboratory test  *");
            System.out.println("****************************************");

            Student student = new Student();
            Collection<Room> booked = new ArrayList<Room>();

            manager.start();
            System.out.println("---------------------------------------");
            System.out.println("Logging as the student: demo@studenti.unipi.it");
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println("Booking the laboratory: 2");
            manager.setBooking(student, 2, null);

            System.out.println("---------------------------------------");
            System.out.println("Logging as the student: demo2@studenti.unipi.it");
            student = (Student) manager.authenticate("demo2@studenti.unipi.it", false);
            System.out.println("Booking the laboratory: 2");
            manager.setBooking(student, 2, null);

            booked.addAll(manager.getBooked(student));

            //Works only for the first booking
            assertFalse(booked.iterator().next().getAvailable());
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    */

}