package it.unipi.RoomBooking.Database;

import it.unipi.RoomBooking.Data.NORM.*;
import it.unipi.RoomBooking.Data.ORM.Classroom;
import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;
import it.unipi.RoomBooking.Data.ORM.Laboratory;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;
import java.util.Collection;

public class DBSManager implements Manager {
    private HibernateDriver hibernate;
    private LevelDbDriver levelDb;

    public void start() {
        hibernate = new HibernateDriver();
        levelDb = new LevelDbDriver();
        hibernate.start();
        levelDb.start();
    }

    public void exit() {
        hibernate.exit();
        levelDb.exit();
    }

    public void initializeAvailable(User user) {
        if (user.getRole().equals("T")) {
            // retreive available classrooms
            Collection<Classroom> classrooms = hibernate.getAvailableClassrooms();
            // store available classes on kv
            for (Classroom cla : classrooms) {
                String available;

                if (cla.getBooking().size() != 0) {
                    if (cla.getBooking().iterator().next().getSchedule().equals("a")) {
                        available = "m";
                    } else {
                        available = "a";
                    }
                } else {
                    available = "f";
                }

                levelDb.putAvailable("cla", cla.getId(), cla.getName(), cla.getBuilding(), cla.getCapacity(),
                        available);
            }
        } else if (user.getRole().equals("S")) {
            // retreive available classrooms
            Collection<Laboratory> laboratories = hibernate.getAvailableLaboratories(user.getId());
            // store available classes on kv
            for (Laboratory lab : laboratories) {
                String available = Integer.toString(lab.getCapacity() - lab.getBookingNumber());
                levelDb.putAvailable("lab", lab.getId(), lab.getName(), lab.getBuilding(), lab.getCapacity(), available);
            }
        }
    }

    public void initializeBooked(User user) {
        if (user.getRole().equals("T")) {
            Collection<ClassroomBooking> bookings = hibernate.getBookedClassrooms(user.getId());

            for(ClassroomBooking book : bookings) {
                levelDb.putBooked("cla", book.getId(), user.getId(), book.getRoomName(), book.getSchedule());
            }
        } else if(user.getRole().equals("S")) {

            Collection<Laboratory> laboratories = hibernate.getBookedLaboratories(user.getId());

            for (Laboratory lab : laboratories) {
                levelDb.putBooked("lab", lab.getId(), user.getId(), lab.getName(), null);
            }
        }
    }

    public User authenticate(String email) throws UserNotExistException {
        User user = null;

        try {
            if (email.contains("admin")) {
                user = new User(-1, "admin", "admin", email, "A");
            } else if (email.contains("studenti")) {
                user = new User(hibernate.authenticate(email, false));
                user.setRole("S");
            } else {
                user = new User(hibernate.authenticate(email, true));
                user.setRole("T");
            }
        } catch (UserNotExistException une) {
            throw new UserNotExistException("User not found.");
        }

        return user;
    }

}