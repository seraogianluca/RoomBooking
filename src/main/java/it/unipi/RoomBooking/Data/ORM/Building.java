package it.unipi.RoomBooking.Data.ORM;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.unipi.RoomBooking.Data.Interface.BuildingInterface;

@Entity
@Table(name = "building")
public class Building implements BuildingInterface {
    @Id
    @Column(name = "BUILDING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long buildingId;

    @Column(name = "BUILDING_NAME")
    private String buildingName;

    @Column(name = "BUILDING_ADDRESS")
    private String buildingAddress;

    @OneToMany(mappedBy = "building")
    private Collection<Laboratory> buildingLaboratories = new ArrayList<Laboratory>();
    
    @OneToMany(mappedBy = "building")
    private Collection<Classroom> buildingClassrooms = new ArrayList<Classroom>();

    public void setName(String name) {
        this.buildingName = name;
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
        return "Building Information: " +  
        "Name: " + buildingName + " " +
        "Address: " + buildingAddress;
    }
}