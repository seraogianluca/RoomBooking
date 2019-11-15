package it.unipi.RoomBooking.Database;

import org.iq80.leveldb.*;
import org.jvnet.staxex.util.FinalArrayList;

import it.unipi.RoomBooking.Data.NORM.Available;
import it.unipi.RoomBooking.Data.NORM.Booked;

import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class LevelDbDriver {
	private static DB levelDb;
	private static Options options;

	public void start() {
		options = new Options();
		options.compressionType(CompressionType.NONE);
		options.createIfMissing(true);
	}

	public void exit() {
		try {
			factory.destroy(new File(".\\src\\main\\resources\\DB\\available"), options);
			factory.destroy(new File(".\\src\\main\\resources\\DB\\booked"), options);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void putAvailable(String roomType, long roomId, String roomName, String buildingName, int capacity,
			String available) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
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

	public void deleteFromAvailable(String roomType, long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			String keyName = "avl:" + roomType + ":" + roomId + ":roomname";
			levelDb.delete(bytes(keyName));
			String keyBuilding = "avl:" + roomType + ":" + roomId + ":buildingname";
			levelDb.delete(bytes(keyBuilding));
			String keyCapacity = "avl:" + roomType + ":" + roomId + ":roomcapacity";
			levelDb.delete(bytes(keyCapacity));
			String keyAvailable = "avl:" + roomType + ":" + roomId + ":available";
			levelDb.delete(bytes(keyAvailable));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void increaseLaboratoryAvailability(long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			String key = "avl:lab:" + roomId + ":available";
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			String key = "avl:lab:" + roomId + ":available";
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			String key = "avl:cla" + roomId + ":available";
			levelDb.put(bytes(key), bytes("f"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void updateClassroomAvailability(long roomId, String requestedSchedule) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			levelDb.delete(bytes("avl:cla:" + roomId + ":available"));
			if (requestedSchedule.equals("m")) {
				levelDb.put(bytes("avl:cla:" + roomId + ":available"), bytes("a"));
			} else {
				levelDb.put(bytes("avl:cla:" + roomId + ":available"), bytes("m"));
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\booked"), options);
			String keyName = "bkg:" + roomType + ":" + userId + ":" + roomId + ":roomname";
			levelDb.put(bytes(keyName), bytes(roomName));

			if (roomType.equals("cla")) {
				String keySchedule = "bkg:" + roomType + ":" + userId + ":" + roomId + ":schedule";
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\booked"), options);
			String keyName = "bkg:" + roomType + ":" + userId + ":" + roomId + ":roomname";
			levelDb.delete(bytes(keyName));

			if (roomType.equals("cla")) {
				String keySchedule = "bkg:" + roomType + ":" + userId + ":" + roomId + ":schedule";
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\booked"), options);
			DBIterator iterator = levelDb.iterator();

			if (role.equals("S")) {
				for (iterator.seek(bytes("bkg:lab:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
					String[] keySplit = key.split(":");
					String roomName = asString(iterator.peekNext().getValue());
					bookings.add(new Booked(Long.parseLong(keySplit[3]), roomName, null, "lab"));
				}
			} else {
				for (iterator.seek(bytes("bkg:cla:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());

					if (key.endsWith("roomname")) {
						String[] keySplit = key.split(":");
						String roomName = asString(levelDb.get(bytes(key)));
						String schedule = asString(
								levelDb.get(bytes("bkg:cla:" + keySplit[2] + ":" + keySplit[3] + ":schedule")));
						bookings.add(new Booked(Long.parseLong(keySplit[3]), roomName, schedule, "cla"));
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
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			DBIterator iterator = levelDb.iterator();
			String roomType;

			if (role.equals("S")) {
				roomType = "lab";
			} else {
				roomType = "cla";
			}

			for (iterator.seek(bytes("avl:" + roomType + ":")); iterator.hasNext(); iterator.next()) {
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
						String name = asString(levelDb.get(bytes("avl:" + roomType + ":" + keySplit[2] + ":roomname")));
						String building = asString(
								levelDb.get(bytes("avl:" + roomType + ":" + keySplit[2] + ":buildingname")));
						int capacity = Integer.parseInt(
								asString(levelDb.get(bytes("avl:" + roomType + ":" + keySplit[2] + ":roomcapacity"))));
						availables.add(new Available(name, building, available, roomType, Long.parseLong(keySplit[2]),
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