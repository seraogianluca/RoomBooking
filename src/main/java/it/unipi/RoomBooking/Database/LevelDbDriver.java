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

	public void deleteFromAvailable(String roomType, long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			levelDb = factory.open(new File(""), options);
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

	public void updateAvailability(String roomType, long roomId, String requestedSchedule) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			String keyAvailable = "avl:" + roomType + ":" + roomId + ":available";
			if (roomType.equals("cla")) {
				if (requestedSchedule.equals("m")) {
					levelDb.put(bytes(keyAvailable), bytes("a"));
				} else {
					levelDb.put(bytes(keyAvailable), bytes("m"));
				}
			} else {
				int seats = Integer.parseInt(asString(levelDb.get(bytes(keyAvailable)))) - 1;
				levelDb.put(bytes(keyAvailable), bytes(Integer.toString(seats)));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void setAvailability(String roomType, long roomId) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);

			String keyAvailable = "avl:" + roomType + ":" + roomId + ":available";
			if (roomType.equals("cla")) {
				levelDb.put(bytes(keyAvailable), bytes("f"));
			} else {
				int seats = Integer.parseInt(asString(levelDb.get(bytes(keyAvailable)))) + 1;
				levelDb.put(bytes(keyAvailable), bytes(Integer.toString(seats)));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
	}

	public void putBooked(String roomType, long roomId, long userId, String roomName, String schedule)
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
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\booked"), options);
			DBIterator iterator = levelDb.iterator();
			Collection<Booked> bookings = new ArrayList<Booked>();
			if (role.equals("S")) {
				for (iterator.seek(bytes("bkg:lab:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());
					String[] keySplit = key.split(":");
					String roomName = asString(iterator.peekNext().getValue());
					bookings.add(new Booked(Long.parseLong(keySplit[3]), roomName, null, "lab"));
				}
				return bookings;
			} else {
				for (iterator.seek(bytes("bkg:cla:")); iterator.hasNext(); iterator.next()) {
					String key = asString(iterator.peekNext().getKey());

					if (key.endsWith("roomname")) {
						String roomName = asString(iterator.peekNext().getValue());
						String[] keySplit = key.split(":");
						String keySchedule = "bkg:cla:" + keySplit[2] + ":" + keySplit[3] + ":schedule";
						String schedule = asString(levelDb.get(bytes(keySchedule)));
						bookings.add(new Booked(Long.parseLong(keySplit[3]), roomName, schedule, "cla"));
					}
				}
				return bookings;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
		return null;
	}

	public Collection<Available> getAvailable(String requestedSchedule, String role) throws IOException {
		try {
			levelDb = factory.open(new File(".\\src\\main\\resources\\DB\\available"), options);
			DBIterator iterator = levelDb.iterator();
			Collection<Available> availables = new ArrayList<Available>();
			String roomType;

			if (role.equals("S")) {
				roomType = "lab";
			} else {
				roomType = "cla";
			}

			for (iterator.seek(bytes("avl:" + roomType + ":")); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				if (key.endsWith("available")) {
					boolean isAvailable = true;

					if (roomType.equals("cla")) {
						String available = asString(iterator.peekNext().getValue());
						if (!(available.equals(requestedSchedule) || available.equals("f"))) {
							isAvailable = false;
						}
					}

					if (isAvailable) {
						String[] keySplit = key.split(":");
						String keyName = "avl:" + roomType + ":" + keySplit[2] + ":roomname";
						String name = asString(levelDb.get(bytes(keyName)));
						String keyBuilding = "avl:" + roomType + ":" + keySplit[2] + ":buildingname";
						String building = asString(levelDb.get(bytes(keyBuilding)));
						String keyCapacity = "avl:" + roomType + ":" + keySplit[2] + ":roomcapacity";
						int capacity = Integer.parseInt(asString(levelDb.get(bytes(keyCapacity))));
						String keyAvailable = "avl:" + roomType + ":" + keySplit[2] + ":available";
						String available = asString(levelDb.get(bytes(keyAvailable)));
						availables.add(new Available(name, building, available, roomType, Long.parseLong(keySplit[2]),
								capacity));
					}
				}
			}
			return availables;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			levelDb.close();
		}
		return null;
	}

}