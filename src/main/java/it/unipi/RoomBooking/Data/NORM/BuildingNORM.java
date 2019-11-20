package it.unipi.RoomBooking.Data.NORM;

import java.util.ArrayList;
import java.util.Collection;


import it.unipi.RoomBooking.Data.ORM.Classroom;
import it.unipi.RoomBooking.Data.ORM.Laboratory;

public class BuildingNORM{
    private String buildingName;
    private String buildingAddress;
    private long buildingId;
    private Collection<Laboratory> buildingLaboratories = new ArrayList<Laboratory>();
    private Collection <Classroom> buildingClassrooms = new ArrayList<Classroom>();



    public void setName(String name) {
        this.buildingName = name;
    }

    public void setId(long id){
        this.buildingId=id;
    }
    public void setAddress(String address) {
        this.buildingAddress = address;
    }

    public long getId() {
        return this.buildingId;
    }

    public String getName() {
        return this.buildingName;
    }

    public String getAddress() {
        return this.buildingAddress;
    }

    public void addLaboratory(Laboratory laboratory) {
        this.buildingLaboratories.add(laboratory);
    }

    public void addClassroom(Classroom classroom) {
        this.buildingClassrooms.add(classroom);
    }


    public String toString() {
        return String.format("%-5s %-15s", buildingId, buildingName);
    }
}