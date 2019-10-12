//import java.beans.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public class RoomBookingDB {

    public int personId;
     
  
    public int getPersonId(String name, String lastName){
  
     String connStr ="jdbc:mysql://localhost:3306/roombooking?user=root&password=";
     Connection conn=null;
    
      ResultSet rs;
      int id=0;
  
      try{
          
          conn=DriverManager.getConnection(connStr);
          String query = "{CALL get_personID(?,?)}";
          PreparedStatement stmt = conn.prepareStatement(query);
          stmt.setString(1, name);
          stmt.setString(2, lastName);
          rs=stmt.executeQuery();
          while (rs.next()) {
              id=rs.getInt("id_person");
          }
          return id;
      }
  
      catch(SQLException ex){
          System.out.println("SQLException: "+ex.getMessage());
  
      }
      return 0;
    }
    public static void main(String[] args) {
      System.out.println("yo");
      RoomBookingDB test=new RoomBookingDB();
      int k=test.getPersonId("Giuseppe", "Anastasi");
      System.out.println(k);
  }
  
  }