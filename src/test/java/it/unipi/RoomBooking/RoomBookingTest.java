package it.unipi.RoomBooking;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import it.unipi.RoomBooking.Data.Interface.*;
import it.unipi.RoomBooking.Data.ORM.*;
import it.unipi.RoomBooking.Database.*;


/**
 * Unit test for RoomBooking App.
 */

public class RoomBookingTest {

    public HibernateManager manager = new HibernateManager();

    @Test
    public void studentAuthenticationTest() {
        try {
            System.out.println("*********************************");
            System.out.println("*  Student authentication test  *");
            System.out.println("*********************************");

            Student student = new Student();
            manager.start();
            student = (Student)manager.authenticate("demo@studenti.unipi.it", false);
            assertEquals("Retreived the wrong student.", 1, student.getId());
            System.out.println(student.toString());

            student = (Student)manager.authenticate("randomstring", false);
            assertEquals("Retreived the wrong student.", 1, student.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void availableLaboratoriesTest() {
        try {
            System.out.println("*********************************");
            System.out.println("*  Available Laboratories test  *");
            System.out.println("*********************************");

            Student student = new Student();
            Collection<Room> laboratories = new ArrayList<Room>();

            manager.start();
            System.out.println("---------------------------------");
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            System.out.println(student.toString());
            laboratories.addAll(manager.getAvailable(student, null));
            boolean flags[] = new boolean[laboratories.size()];
            boolean checkArray[] = new boolean[laboratories.size()];
            Arrays.fill(checkArray, true);

            for(int i = 0; i < laboratories.size(); i++) {
                flags[i] = laboratories.iterator().next().getAvailable();
            }

            System.out.println("---------------------------------");
            System.out.println("Available laboratories retreived:");            
            for(Room lab : laboratories) {
                System.out.println(lab.toString());   
            }
            System.out.println("---------------------------------");
            
            assertArrayEquals("Laboratory not available as return.", checkArray, flags);
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

    @Test
    public void testDeleteBookingLaboratory() {
        try {
            manager.start();
            Student student = new Student();
            student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
            Collection<Room> laboratory = new ArrayList<Room>();
            laboratory.addAll(manager.getBooked(student));
            Laboratory lab = (Laboratory)laboratory.iterator().next();
            System.out.println("laboratory" + lab.toString());
          //  manager.deleteBooking(student, lab, 0);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

    @Test
    public void teacherAuthenticationTest() {
        try {
            System.out.println("**********************************");
            System.out.println("*  Teacher authentication test   *");
            System.out.println("**********************************");

            manager.start();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            assertEquals("Retreived the wrong student.", 1, teacher.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
            manager.setBooking(teacher, classroom.iterator().next().getId(), "m");
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
    
    @Test
    public void testDeleteBookingClassroom() {
        try {
            manager.start();
            Teacher teacher = new Teacher();
            teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
            Collection<Room> classroom = new ArrayList<Room>();
            classroom.addAll(manager.getAvailable(teacher, null));
            Classroom classroomToUnbook = (Classroom)classroom.iterator().next();
            System.out.println("classroom" + classroomToUnbook.toString());
          //  manager.deleteBooking(teacher, classroomToUnbook, classroomToUnbook.getBookingId(teacher.getId(), "m"));
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        } finally {
            manager.exit();
        }
    }

}