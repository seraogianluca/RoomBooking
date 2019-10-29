package it.unipi.RoomBooking.Data.ORM;

import java.util.Set;

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
    private Set<Laboratory> buildingLaboratories;
    
    @OneToMany(mappedBy = "building")
    private Set<Classroom> buildingClassrooms;

    // Setter
    public void setName(String name) {
        this.buildingName = name;
    }

    public void setAddress(String address) {
        this.buildingAddress = address;
    }

    // Getter
    public long getId() {
        return this.buildingId;
    }

    public String getName() {
        return this.buildingName;
    }

    public String getAddress() {
        return this.buildingAddress;
    }

    public String toString() {
        return "Building Information: " +  
        "\nName: " + buildingName + 
        "\nAddress: " + buildingAddress;
    }
}