package it.unipi.RoomBookingDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class RoomBookingDB {
    /* Database connection util */
    String connectionStr = null;
    Connection connection = null;

    public RoomBookingDB(String connectionString) {
        connectionStr = connectionString;
    }

    /* Connection establisher */
    void openConnection() {

        try {
            connection = DriverManager.getConnection(connectionStr);
        } catch (SQLException sql) {
            System.err.println(sql.getMessage());
            sql.printStackTrace();
        }

    }

    /* Connection closer */
    void closeConnection() {

        try {
            connection.close();
        } catch (SQLException sql) {
            System.err.println(sql.getMessage());
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
            System.err.println("SQLException: " + sql.getMessage());
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
            String query = 
                "SELECT A.id_room, A.available, A.num_room, D.name, A.floor, A.max_person, B.id_schedule " + 
                "FROM ROOM A LEFT JOIN BOOKING B ON A.id_room = B.id_room AND B.id_schedule != ? " +
                "INNER JOIN BELONG C ON A.id_room = C.id_room " +
                "INNER JOIN building D ON C.id_building = D.id_building " +
                "WHERE A.AVAILABLE = '1'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, schedule);
            result = preparedStatement.executeQuery();
            ArrayList<String> roomName = new ArrayList<String>();
            ArrayList<String> buildingName = new ArrayList<String>();
            ArrayList<String> roomFloor = new ArrayList<String>();
            ArrayList<String> roomCapacity = new ArrayList<String>();

            while(result.next()) {
                roomName.add(result.getString("id_room"));
                buildingName.add(result.getString("name"));
                roomFloor.add(result.getString("floor"));
                roomCapacity.add(result.getString("max_person"));
            }

            table.add(roomName);
            table.add(buildingName);
            table.add(roomFloor);
            table.add(roomCapacity);
            return table;
        } catch (SQLException sql) {
            System.err.println("SQLException: " + sql.getMessage());
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
            String query = "SELECT id_room, id_schedule FROM booking WHERE id_person = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            result = preparedStatement.executeQuery();
            ArrayList<String> roomName = new ArrayList<String>();
            ArrayList<String> schedule = new ArrayList<String>();

            while(result.next()) {
                roomName.add(result.getString("id_room"));
                schedule.add(result.getString("id_schedule"));
            }

            table.add(roomName);
            table.add(schedule);
            return table;
        } catch (SQLException sql) {
            System.err.println("SQLException: " + sql.getMessage());
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

        return null;
    }
    
    /* Make a booking */
    public void setBooking(int personId, String schedule, String room){

        try {
            openConnection();
            String query = "INSERT INTO booking VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            preparedStatement.setString(2,room);
            preparedStatement.setString(2, schedule);
            preparedStatement.executeQuery();
            String query2 = "UPDATE ROOM" +
                            "SET AVAILABLE = 0 " +
                            "WHERE ID_ROOM IN " +
                            "(SELECT ID_ROOM FROM BOOKING GROUP BY ID_ROOM HAVING COUNT(?) = 2)";
            preparedStatement.setString(1, room);
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
    public void deleteBooking(int personId, String room, String schedule) {

        try {
            openConnection();
            String query = "DELETE FROM booking WHERE id_person = ? AND id_room = ? AND id_schedule = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, personId);
            preparedStatement.setString(2, room);
            preparedStatement.setString(3, schedule);
            preparedStatement.executeQuery();
        } catch (SQLException sql) {
            System.err.println("SQLException: " + sql.getMessage());
            sql.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    /* Update a booking */
    public void updateBooking(int personId, String room, String schedule) {
        try {
            openConnection();
            String query = "UPDATE booking SET id_schedule = ? WHERE id_person = ? AND id_room = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, schedule);
            preparedStatement.setInt(2, personId);
            preparedStatement.setString(3, room);
            preparedStatement.executeQuery();
        } catch (SQLException sql) {
            System.err.println("SQLException: " + sql.getMessage());
            sql.printStackTrace();
        } finally {
            closeConnection();
        }
    }

}