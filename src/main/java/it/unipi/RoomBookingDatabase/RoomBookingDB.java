package it.unipi.RoomBookingDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class RoomBookingDB {
    /* Database connection util */
    private String connectionStr = null;
    private Connection connection = null;

    public RoomBookingDB(String connectionString) {
        connectionStr = connectionString;
    }

    /* Connection establisher */
    void openConnection() {

        try {
            connection = DriverManager.getConnection(connectionStr);
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

    }

    /* Connection closer */
    void closeConnection() {

        try {
            connection.close();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

    }

    /* Get the personal id */
    public String[] getPersonID(String email) {
        ResultSet result;
        String[] person = new String[3];

        try {
            openConnection();
            /* Need to change the table attributes name and revert it */
            String query = "SELECT id_person, firstname, lastname FROM person WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                person[0] = String.valueOf(result.getInt("id_person"));
                person[1] = result.getString("firstname");
                person[2] = result.getString("lastname");
            }

            return person;  
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }

    /* Get available rooms for the schedule of interest */
    public ArrayList<ArrayList<String>> getAvailableRooms(String schedule) {
        ResultSet result = null;
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

        try {
            openConnection();
            String query = "SELECT A.id_room, A.room_name, D.building_name, A.floor, A.capacity FROM room A LEFT JOIN booking B ON A.id_room = B.id_room INNER JOIN belong C ON A.id_room = C.id_room INNER JOIN building D ON C.id_building = D.id_building WHERE A.available = '1'AND A.id_room NOT IN (SELECT id_room FROM booking WHERE id_schedule = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, schedule);
            result = preparedStatement.executeQuery();
            ArrayList<String> roomId = new ArrayList<String>();
            ArrayList<String> roomName = new ArrayList<String>();
            ArrayList<String> buildingName = new ArrayList<String>();
            ArrayList<String> roomFloor = new ArrayList<String>();
            ArrayList<String> roomCapacity = new ArrayList<String>();

            while(result.next()) {
                roomId.add(String.valueOf(result.getInt("id_room")));
                roomName.add(result.getString("room_name"));
                buildingName.add(result.getString("building_name"));
                roomFloor.add(String.valueOf(result.getInt("floor")));
                roomCapacity.add(String.valueOf(result.getInt("capacity")));
            }

            table.add(roomId);
            table.add(roomName);
            table.add(buildingName);
            table.add(roomFloor);
            table.add(roomCapacity);
            return table;
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }

    /* Get the rooms already booked */
    public ArrayList<ArrayList<String>>  getBookedRooms(int personId) {
        ResultSet result = null;
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

        try {
            openConnection();
            String query = "SELECT A.id_room, B.room_name , A.id_schedule FROM booking A INNER JOIN room B ON A.id_room = B.id_room WHERE id_person = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            result = preparedStatement.executeQuery();
            ArrayList<String> roomId = new ArrayList<String>();
            ArrayList<String> roomName = new ArrayList<String>();
            ArrayList<String> schedule = new ArrayList<String>();

            while(result.next()) {
                roomId.add(result.getString("id_room"));
                roomName.add(result.getString("room_name"));
                schedule.add(result.getString("id_schedule"));
            }

            table.add(roomId);
            table.add(roomName);
            table.add(schedule);
            return table;
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }
    
    /* Make a booking */
    public void setBooking(int personId, String schedule, int roomId){

        try {
            openConnection();
            String query = "INSERT INTO booking VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2,roomId);
            preparedStatement.setString(3, schedule);
            preparedStatement.executeUpdate();
            String query2 = "UPDATE room SET available = 0 WHERE id_room IN (SELECT id_room FROM booking GROUP BY id_room HAVING COUNT(id_room) = 2)";
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.executeUpdate();
        } catch (SQLException sql) {
            System.err.println("SQLException: " + sql.getMessage());
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    /* Delete a booking */
    public void deleteBooking(int personId, int roomId, String schedule) {

        try {
            openConnection();
            String query = "DELETE FROM booking WHERE id_person = ? AND id_room = ? AND id_schedule = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setString(3, schedule);
            preparedStatement.executeUpdate();
            String query2 = "UPDATE room SET available = 1 WHERE id_room IN (SELECT id_room FROM booking GROUP BY id_room HAVING COUNT(id_room) = 1)";
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.executeUpdate();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    /* Update a booking */
    public void updateBooking(int personId, int newRoomId, String newSchedule, int oldRoomId, String oldSchedule) {
        try {
            openConnection();
            String query = "UPDATE booking SET id_person = ?, id_room = ?, id_schedule = ? WHERE id_person = ? AND id_room = ? AND id_schedule = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            preparedStatement.setInt(2, newRoomId);
            preparedStatement.setString(3, newSchedule);
            preparedStatement.setInt(4, personId);
            preparedStatement.setInt(5, oldRoomId);
            preparedStatement.setString(6, oldSchedule);
            preparedStatement.executeUpdate();
            String query2 = "UPDATE room SET available = 1 WHERE id_room IN (SELECT id_room FROM booking GROUP BY id_room HAVING COUNT(id_room) = 1)";
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.executeUpdate();
            String query3 = "UPDATE room SET available = 0 WHERE id_room IN (SELECT id_room FROM booking GROUP BY id_room HAVING COUNT(id_room) = 2)";
            preparedStatement = connection.prepareStatement(query3);
            preparedStatement.executeUpdate();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            closeConnection();
        }
    }

}