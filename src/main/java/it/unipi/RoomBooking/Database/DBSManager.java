package it.unipi.RoomBooking.Database;

import it.unipi.RoomBooking.Data.NORM.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

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
    }

    public User authenticate(String email) throws UserNotExistException {
        User user = null;

        try {
            if(email.contains("admin")) {
                user = new User(-1, "admin", "admin", email, "A");
            } else if(email.contains("studenti")) {
                user = new User(hibernate.authenticate(email, false));
                user.setRole("S");
            } else {
                user = new User(hibernate.authenticate(email, true));
                user.setRole("T");
            }
        } catch(UserNotExistException une) {
            throw new UserNotExistException("User not found.");
        }

        return user;
    }
    
}