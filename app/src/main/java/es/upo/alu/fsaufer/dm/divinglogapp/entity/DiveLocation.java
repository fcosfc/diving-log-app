package es.upo.alu.fsaufer.dm.divinglogapp.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que implementa la localidad de la inmersión
 */
public class DiveLocation implements Serializable {

    private static final long serialVersionUID = 6699849478674716632L;

    private int locationId;
    private String name;
    private GeoLocation location;

    public DiveLocation() {
    }

    public DiveLocation(String name, GeoLocation location) {
        this.name = name;
        this.location = location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiveLocation diveLocation = (DiveLocation) o;
        return locationId == diveLocation.locationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId);
    }

    @Override
    public String toString() {
        return "DiveLocation{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
