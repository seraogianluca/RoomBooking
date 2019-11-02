package it.unipi.RoomBooking.Exceptions;

public class UserNotExistException extends Exception {
    private static final long serialVersionUID = 1L;

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException() {
        super();
    }
}