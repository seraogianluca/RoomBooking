package it.unipi.RoomBooking.Database;

import org.iq80.leveldb.*;

import it.unipi.RoomBooking.Data.Interface.Person; ///CHECK
import it.unipi.RoomBooking.Data.ORM.Teacher;   ///check
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;

public class LevelDbDriver {
	private static DB levelDb;
	private static Options options;

	public void start() {
		options = new Options();
		options.createIfMissing(true);
	}

	public void exit() {
		factory.destroy(new File("./src/main/resources/DB/available"), options);
		factory.destroy(new File("./src/main/resources/DB/booked"), options);
	}

	public void putAvailable(String roomType, long roomId, String roomName, String buildingName, int capacity, String available) {
		try {
			levelDb = factory.open(new File("./src/main/resources/DB/available"), options);
			String keyName = "avl:" + roomType + ":" + roomId + ":roomname";
			levelDb.put(bytes(keyName), bytes(roomName));
			String keyBuilding = "avl:" + roomType + ":" + roomId + ":buildingname";
			levelDb.put(bytes(keyBuilding), bytes(buildingName));
			String keyCapacity = "avl:" + roomType + ":" + roomId + ":roomcapacity";
			levelDb.put(bytes(keyCapacity), bytes(Integer.toString(capacity)));
			String keyAvailable = "avl:" + roomType + ":" + roomId + ":available";
			levelDb.put(bytes(keyAvailable), bytes(available));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void putBooked(String roomType, long roomId, long userId, String roomName, String schedule) {
		try {
			levelDb = factory.open(new File("./src/main/resources/DB/booked"), options);
			String keyName = "bkg:" + roomType + ":" + userId + ":" + roomId + ":roomname";
			levelDb.put(bytes(keyName), bytes(roomName));

			if(roomType.equals("cla")) {
				String keySchedule = "bkg:" + roomType + ":" + userId + ":" + roomId + ":schedule";
				levelDb.put(bytes(keySchedule), bytes(schedule));
			}	
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public static void getbookedClassrooms(Person person, String requestedSchedule){ 
    	DB bookingDB;
	
		try {  
			bookingDB  = factory.open(new File("./src/main/resources/DB/booked"),options);
			
			//put key value
			
			//"String.format("%-5s %-15s %-25s %-10s", $laboratoryId, $labName, $buildingName, $capacity)"
			//"String.format("%-5s %-15s", $laboratoryId, $laboratoryName)"

			String type ="t";
			String userID= "2";
			DBIterator iterator = bookingDB.iterator();
			
			if(type.equals("t")) {
				for(iterator.seek(bytes("bkg:cla:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
				    if(key.startsWith("bkg:cla:"+userID)) {
					String value = asString(iterator.peekNext().getValue());
					System.out.println(value);
				}
				}
			  iterator.close();			
			  bookingDB.close();
			
			}} catch (IOException e) {
			e.printStackTrace();
		}finally {
			 // Make sure you close the db to shutdown the 
			 // database and avoid resource leaks.
			
		}
    }
	}

	public static void getavailable(Person person, String requestedSchedule){   //ma va bene importare person?
        //options.createIfMissing(false);
        
		try {  
			levelDb = factory.open(new File("./src/main/resources/DB"),options);
			 //CERCA PERCORSO	
			//put key value  //POPOLAMENTO PER TESTARE
			levelDb.put(bytes("avl:cla:1:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 1, "A13", "Polo A", 80)));
			levelDb.put(bytes("avl:cla:1:available"),bytes("m"));
			
			levelDb.put(bytes("avl:lab:1:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 1, "SI1", "Polo B", 80)));
			levelDb.put(bytes("avl:lab:1:available"),bytes("3"));

		
			//String requestedSchedule="a";
			//String type ="t";
			
			DBIterator iterator = levelDb.iterator();
			try {
				if(person instanceof Teacher) {
				  for(iterator.seek(bytes("avl:cla:")); iterator.hasNext(); iterator.next()) {
				    String key = asString(iterator.peekNext().getKey());
				    if(key.endsWith("available")) {
				    	String avlflag = asString(iterator.peekNext().getValue());
						if(avlflag.equals(requestedSchedule)||avlflag.equals("f")) {  //in the key that end with available i select the ones that are fullday or requestedschedule available
							String[] keySplit = key.split(":");
							String roomID = keySplit[2];  //split the key of these rows and take the part in which you find the roomid of the available room
							String key1 = "avl:cla:"+roomID+":info";		//create the new key to select the info rows
							String value = asString(levelDb.get(bytes(key1)));
						    		//System.out.println(key1+" = "+ value);
									System.out.println(value);
						}
				    }
				  }
				}
				else { 
					for(iterator.seek(bytes("avl:lab:")); iterator.hasNext(); iterator.next()) {
						String key = asString(iterator.peekNext().getKey());
						if(key.endsWith("available")) {
					    	String avlflag = asString(iterator.peekNext().getValue());
							if(Integer.parseInt(avlflag)>0) {  //in the key that end with available i select the ones that are fullday or requestedschedule available
								String[] keySplit = key.split(":");
								String roomID = keySplit[2];  //split the key of these rows and take the part in which you find the roomid of the available room
								String key1 = "avl:lab:"+roomID+":info";		//create the new key to select the info rows
								String value = asString(levelDb.get(bytes(key1)));
							    		//System.out.println(key1+" = "+ value);
										System.out.println(value);
							}
						}
					}
				}
			}finally {
			  // Make sure you close the iterator to avoid resource leaks.
			  iterator.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			 // Make sure you close the db to shutdown the 
             // database and avoid resource leaks.
             
		}
    }
}