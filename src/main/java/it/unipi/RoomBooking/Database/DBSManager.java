package it.unipi.RoomBooking.Database;

import it.unipi.RoomBooking.Data.NORM.*;
import it.unipi.RoomBooking.Data.ORM.Classroom;
import it.unipi.RoomBooking.Data.ORM.ClassroomBooking;
import it.unipi.RoomBooking.Data.ORM.Laboratory;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

import java.io.IOException;
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
        levelDb.exit();
        hibernate.exit();
    }

    public void initializeAvailable(User user) {
        try {
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
                    levelDb.putAvailable("lab", lab.getId(), lab.getName(), lab.getBuilding(), lab.getCapacity(),
                            available);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void initializeBooked(User user) {
        try {
            if (user.getRole().equals("T")) {
                Collection<ClassroomBooking> bookings = hibernate.getBookedClassrooms(user.getId());

                for (ClassroomBooking book : bookings) {
                   // levelDb.putBooked("cla", book.getId(), user.getId(), book.getRoomName(), book.getSchedule());
                }
            } else if (user.getRole().equals("S")) {

                Collection<Laboratory> laboratories = hibernate.getBookedLaboratories(user.getId());

                for (Laboratory lab : laboratories) {
                  //  levelDb.putBooked("lab", lab.getId(), user.getId(), lab.getName(), null);
                }
            }
        } catch (Exception ioe) { // <-IOException
            ioe.printStackTrace();
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

    public Collection<Available> getAvailable(String requestedSchedule, String role) {
        try {
            Collection<Available> available = levelDb.getAvailable(requestedSchedule, role);
            return available;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public Collection<Booked> getBooked(String role) {
        try {
            Collection<Booked> bookings = levelDb.getBooked(role);
            return bookings;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public void setBooking(User user, Available roomToBook, String requestedSchedule) {
        try {
            if (user.getRole().equals("T")) {
                hibernate.setClassroomBooking(user.getId(), roomToBook.getId(), requestedSchedule);
            } else {
                hibernate.setLaboratoryBooking(user.getId(), roomToBook.getId());
            }
            //levelDb.putBooked(roomToBook.getType(), roomToBook.getId(), user.getId(), roomToBook.getRoom(),
               //     requestedSchedule);
            if (checkAvailability(roomToBook)) {
                levelDb.deleteFromAvailable(roomToBook.getType(), roomToBook.getId());
                hibernate.updateAvailability(roomToBook.getType(), roomToBook.getId(), false);
            } else {
                if(user.getRole().equals("T")) {
                    levelDb.updateClassroomAvailability(roomToBook.getId(), requestedSchedule);
                } else {
                    levelDb.decreaseLaboratoryAvailability(roomToBook.getId());
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean checkAvailability(Available roomToBook) {
        // True only if the room will become unavailable after the booking.
        if (roomToBook.getType().equals("cla")) {
            if (!roomToBook.getAvailable().equals("f")) {
                return true;
            }
        } else {
            if (Integer.parseInt(roomToBook.getAvailable()) == 1) {
                return true;
            }
        }
        return false;
    }

    public void deleteBooking(User user, Booked booked) {
        try {
            if (user.getRole().equals("T")) {
                if(!hibernate.getAvailability(booked.getType(), booked.getId())) {
                    Available av = new Available(hibernate.getClassroom(booked.getId()));
                    hibernate.updateAvailability(av.getType(), av.getId(), true);
                    hibernate.deleteClassroomBooking(booked.getId());
                    levelDb.putAvailable(booked.getType(), av.getId(), booked.getRoomName(), av.getBuilding(),av.getCapacity(),booked.getSchedule());
                } else {
                    levelDb.setClassroomAvailability(hibernate.getClassroom(booked.getId()).getId());
                    hibernate.deleteClassroomBooking(booked.getId());
                }
            } else {
                if(!hibernate.getAvailability(booked.getType(), booked.getId())) {
                    Available av = new Available(hibernate.getLaboratory(booked.getId()));
                    av.setType("lab");
                    hibernate.updateAvailability(av.getType(), av.getId(), true);
                    hibernate.deleteLaboratoryBooking(user.getId(), booked.getId());
                    levelDb.putAvailable(booked.getType(), booked.getId(), booked.getRoomName(), av.getBuilding(),av.getCapacity(), "1");
                } else {
                    hibernate.deleteLaboratoryBooking(user.getId(), booked.getId());
                    levelDb.increaseLaboratoryAvailability(booked.getId());
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    
    public void updateBooking(User user, Available roomToBook, String requestedSchedule, Booked booked) {
        deleteBooking(user, booked);
        setBooking(user, roomToBook, requestedSchedule);
    }
//Admin methods
public void setStudent(String[] data){   //exceptions?  
    hibernate.createStudent(data[0], data[1], data[2]);
}
public void setTeacher(String[] data){   //exceptions?  
    hibernate.createTeacher(data[0], data[1], data[2]);
}
}
