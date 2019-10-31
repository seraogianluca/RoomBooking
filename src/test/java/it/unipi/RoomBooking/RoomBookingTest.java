package it.unipi.RoomBooking;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import it.unipi.RoomBooking.Data.Interface.Room;
import it.unipi.RoomBooking.Data.ORM.Classroom;
import it.unipi.RoomBooking.Data.ORM.Student;
import it.unipi.RoomBooking.Data.ORM.Teacher;
import it.unipi.RoomBooking.Database.HibernateManager;
import java.util.Collection;

/**
 * Unit test for RoomBooking App.
 */

public class RoomBookingTest {

    public HibernateManager manager = new HibernateManager();

    @Test
    public void testApp() {
        assertTrue(true);
    }

    @Test
    public void testAutenticationStudent() {
        try {
            manager.start();
            System.out.println("Student test.");
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println(student.toString());
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testBookAvailableLaboratory() {
        try {
            manager.start();
            System.out.println("Available laboratory test.");
            Collection<Room> laboratory = new ArrayList<Room>();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println(student.toString());
            laboratory.addAll(manager.getAvailable(student, null));
            for (Room l : laboratory) {
                System.out.println(l.toString());
            }
            System.out.println("Book a laboratory.");
            manager.setBooking(student, laboratory.iterator().next(), null);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testGetBookedLaboratory() {
        try {
            manager.start();
            System.out.println("Fetching booked laboratory.");
            Collection<Room> booked_lab = new ArrayList<Room>();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println(student.toString());
            booked_lab.addAll(manager.getBooked(student));
            for (Room c : booked_lab) {
                System.out.println(c.toString());
            }
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testUpdateBookingLaboratory() {
        try {
            manager.start();
            System.out.println("Update laboratory booking.");
            Collection<Room> booked_lab = new ArrayList<Room>();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println(student.toString());
            booked_lab.addAll(manager.getBooked(student));
            for (Room c : booked_lab) {
                System.out.println(c.toString());
            }
            Collection<Room> laboratory = new ArrayList<Room>();
            laboratory.addAll(manager.getAvailable(student, null));
            for (Room l : laboratory) {
                System.out.println(l.toString());
            }
            manager.updateBooking(student, booked_lab.iterator().next(), laboratory.iterator().next(), 0, null);
            assertTrue(true);
        } catch (Exception ex) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    public void testDeleteBookingClassroom() {
        try {
            manager.start();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@student.unipi.it", false);
            Collection<Room> laboratory = new ArrayList<Room>();
            laboratory.addAll(manager.getAvailable(student, null));
            // System.out.println("laboratory" + laboratory.get(0));
            // manager.deleteBooking(student, laboratory.get(0));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testAutenticationTeacher() {
        try {
            manager.start();
            System.out.println("Teacher test.");
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            System.out.println(teacher.toString());
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testBookAvailableClassroom() {
        try {
            manager.start();
            System.out.println("Available classroom test.");
            Collection<Room> classroom = new ArrayList<Room>();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            System.out.println(teacher.toString());
            classroom.addAll(manager.getAvailable(teacher, "m"));
            for (Room c : classroom) {
                System.out.println(c.toString());
            }
            System.out.println("Book a classroom.");
            manager.setBooking(teacher, classroom.iterator().next(), "m");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testGetBookedClassroom() {
        try {
            manager.start();
            System.out.println("Fetching booked classroom.");
            Collection<Room> booked = new ArrayList<Room>();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            System.out.println(teacher.toString());
            booked.addAll(manager.getBooked(teacher));
            for (Room b : booked) {
                System.out.println(b.toString());
            }
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void testUpdateBookingClassroom() {
        try {
            manager.start();
            System.out.println("Update booking classroom.");
            Collection<Room> booked = new ArrayList<Room>();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            System.out.println(teacher.toString());
            System.out.println("get booked:");
            booked.addAll(manager.getBooked(teacher));
            for (Room b : booked) {
                System.out.println(b.toString());
            }
            System.out.println("get available:");
            Collection<Room> classroom = new ArrayList<Room>();
            classroom.addAll(manager.getAvailable(teacher, "m"));
            for (Room c : classroom) {
                System.out.println(c.toString());
            }

            System.out.println("update booking:");
            Classroom oldClassroom = (Classroom)booked.iterator().next();
            long bookingId = oldClassroom.getBookingId(teacher.getId(), "m");
            manager.updateBooking(teacher, oldClassroom,  classroom.iterator().next(), bookingId , "m");

            System.out.println("get booked:");
            booked.clear();
            booked.addAll(manager.getBooked(teacher));
            for (Room b : booked) {
                System.out.println(b.toString());
            }
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }   
    

}