package it.unipi.RoomBooking.Database;

import org.iq80.leveldb.*;

import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;

import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class LevelDbDriver {
	private static DB levelDb;
	private static Options options;

	/* Please check the correct configuration before running */
	private static String availablePath = "./src/available"; 	//Windows Path: .\\src\\available 
	private static String bookingsPath = "./src/bookings"; 		//Windows Path: .\\src\\bookings

	public void start() {
		options = new Options();
		options.compressionType(CompressionType.NONE);
		options.createIfMissing(true);
	}

	public void exit() {
		try {
			factory.destroy(new File(availablePath), options);
			factory.destroy(new File(bookingsPath), options);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void putAvailable(String roomType, long roomId, String roomName, String buildingName, int capacity,
			String available) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			String keyName = roomType + ":" + roomId + ":roomname";
			levelDb.put(bytes(keyName), bytes(roomName));
			String keyBuilding = roomType + ":" + roomId + ":buildingname";
			levelDb.put(bytes(keyBuilding), bytes(buildingName));
			String keyCapacity = roomType + ":" + roomId + ":roomcapacity";
			levelDb.put(bytes(keyCapacity), bytes(Integer.toString(capacity)));
			String keyAvailable = roomType + ":" + roomId + ":available";
			levelDb.put(bytes(keyAvailable), bytes(available));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void deleteFromAvailable(String roomType, long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			String keyName = roomType + ":" + roomId + ":roomname";
			levelDb.delete(bytes(keyName));
			String keyBuilding = roomType + ":" + roomId + ":buildingname";
			levelDb.delete(bytes(keyBuilding));
			String keyCapacity = roomType + ":" + roomId + ":roomcapacity";
			levelDb.delete(bytes(keyCapacity));
			String keyAvailable = roomType + ":" + roomId + ":available";
			levelDb.delete(bytes(keyAvailable));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void increaseLaboratoryAvailability(long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			String key = "lab:" + roomId + ":available";
			int seats = Integer.parseInt(asString(levelDb.get(bytes(key)))) + 1;
			levelDb.put(bytes(key), bytes(Integer.toString(seats)));

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void decreaseLaboratoryAvailability(long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			String key = "lab:" + roomId + ":available";
			int seats = Integer.parseInt(asString(levelDb.get(bytes(key)))) - 1;
			levelDb.put(bytes(key), bytes(Integer.toString(seats)));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void setClassroomAvailability(long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			String key = "cla" + roomId + ":available";
			levelDb.put(bytes(key), bytes("f"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void updateClassroomAvailability(long roomId, String requestedSchedule) throws IOException {
		try {
			levelDb = factory.open(new File(availablePath), options);
			levelDb.delete(bytes("cla:" + roomId + ":available"));
			if (requestedSchedule.equals("m")) {
				levelDb.put(bytes("cla:" + roomId + ":available"), bytes("a"));
			} else {
				levelDb.put(bytes("cla:" + roomId + ":available"), bytes("m"));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void putBooked(long userId, String roomType, long roomId, String roomName, String schedule)
			throws IOException {
		try {
			levelDb = factory.open(new File(bookingsPath), options);
			String keyName = roomType + ":" + userId + ":" + roomId + ":roomname";
			levelDb.put(bytes(keyName), bytes(roomName));

			if (roomType.equals("cla")) {
				String keySchedule = roomType + ":" + userId + ":" + roomId + ":schedule";
				levelDb.put(bytes(keySchedule), bytes(schedule));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void deleteBooked(String roomType, long roomId, long userId) throws IOException {
		try {
			levelDb = factory.open(new File(bookingsPath), options);
			String keyName = roomType + ":" + userId + ":" + roomId + ":roomname";
			levelDb.delete(bytes(keyName));

			if (roomType.equals("cla")) {
				String keySchedule = roomType + ":" + userId + ":" + roomId + ":schedule";
				levelDb.delete(bytes(keySchedule));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public Collection<Booked> getBooked(String role) throws IOException {
		Collection<Booked> bookings = new ArrayList<Booked>();

		try {
			levelDb = factory.open(new File(bookingsPath), options);
			DBIterator iterator = levelDb.iterator();

			if (role.equals("S")) {
				for (iterator.seek(bytes("lab:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
					String[] keySplit = key.split(":");
					String roomName = asString(iterator.peekNext().getValue());
					bookings.add(new Booked(Long.parseLong(keySplit[2]), roomName, null, "lab"));
				}
			} else {
				for (iterator.seek(bytes("cla:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());

					if (key.endsWith("roomname")) {
						String[] keySplit = key.split(":");
						String roomName = asString(levelDb.get(bytes(key)));
						String schedule = asString(
								levelDb.get(bytes("cla:" + keySplit[1] + ":" + keySplit[2] + ":schedule")));
						bookings.add(new Booked(Long.parseLong(keySplit[2]), roomName, schedule, "cla"));
					}
				}
			}

			iterator.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
		return bookings;
	}

	public Collection<Available> getAvailable(String requestedSchedule, String role) throws IOException {
		Collection<Available> availables = new ArrayList<Available>();

		try {
			levelDb = factory.open(new File(availablePath), options);
			DBIterator iterator = levelDb.iterator();
			String roomType;

			if (role.equals("S")) {
				roomType = "lab";
			} else {
				roomType = "cla";
			}

			for (iterator.seek(bytes(roomType + ":")); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());

				if (key.endsWith("available")) {
					String available = asString(levelDb.get(bytes(key)));
					boolean isAvailable = true;

					if (roomType.equals("cla")) {
						if (!(available.equals(requestedSchedule) || available.equals("f"))) {
							isAvailable = false;
						}
					} else {
						if (Integer.parseInt(available) == 0) {
							isAvailable = false;
						}
					}

					if (isAvailable) {
						String[] keySplit = key.split(":");
						String name = asString(levelDb.get(bytes(roomType + ":" + keySplit[1] + ":roomname")));
						String building = asString(
								levelDb.get(bytes(roomType + ":" + keySplit[1] + ":buildingname")));
						int capacity = Integer.parseInt(
								asString(levelDb.get(bytes(roomType + ":" + keySplit[1] + ":roomcapacity"))));
						availables.add(new Available(name, building, available, roomType, Long.parseLong(keySplit[1]),
								capacity));
					}
				}
			}
			iterator.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}

		return availables;
	}

}