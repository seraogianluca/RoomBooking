package it.unipi.RoomBooking.Database;

import org.iq80.leveldb.*;

import it.unipi.RoomBooking.Data.Interface.Person; ///CHECK
import it.unipi.RoomBooking.Data.ORM.Teacher;   ///check

import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.*;

public class LevelDBManager {
    public static void initializeDB(){  //in this function we have to populate the keyvalue
    }

    public static void getavailable(Person person, String requestedSchedule){   //ma va bene importare person?
    	DB availableDB;
		Options options = new Options();
		options.compressionType(CompressionType.NONE);
        //options.createIfMissing(false);
        
		try {  
			availableDB = factory.open(new File("C:\\Users\\Matilde\\Desktop\\levelDBStore\\available"),options); //CERCA PERCORSO
			
			//put key value  //POPOLAMENTO PER TESTARE
			availableDB.put(bytes("avl:cla:1:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 1, "A13", "Polo A", 80)));
			availableDB.put(bytes("avl:cla:2:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 2, "A22", "Polo A", 80)));
			availableDB.put(bytes("avl:cla:3:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 3, "B32", "Polo B", 80)));
			availableDB.put(bytes("avl:cla:1:available"),bytes("m"));
			availableDB.put(bytes("avl:cla:2:available"),bytes("a"));
			availableDB.put(bytes("avl:cla:3:available"),bytes("f"));
			
			availableDB.put(bytes("avl:lab:1:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 1, "SI1", "Polo B", 80)));
			availableDB.put(bytes("avl:lab:2:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 2, "SI2", "Polo B", 80)));
			availableDB.put(bytes("avl:lab:3:info"),bytes(String.format("%-5s %-15s %-25s %-10s", 3, "SI4", "Polo B", 80)));
			availableDB.put(bytes("avl:lab:1:available"),bytes("3"));
			availableDB.put(bytes("avl:lab:2:available"),bytes("80"));
			availableDB.put(bytes("avl:lab:3:available"),bytes("0"));  //se è 0, c'è con lo 0 o si elimina la riga?
		
			//String requestedSchedule="a";
			//String type ="t";
			
			DBIterator iterator = availableDB.iterator();
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
							String value = asString(availableDB.get(bytes(key1)));
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
								String value = asString(availableDB.get(bytes(key1)));
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
			availableDB.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			 // Make sure you close the db to shutdown the 
             // database and avoid resource leaks.
             
		}
    }
}