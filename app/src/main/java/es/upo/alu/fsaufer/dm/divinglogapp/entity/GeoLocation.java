package es.upo.alu.fsaufer.dm.divinglogapp.entity;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Clase que representa una localización geográfica.
 * No se usa la clase Location de Android por que ésta no es serializable.
 */
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = 6699849478674716632L;

    private double longitude;
    private double latitude;

    public GeoLocation() {
    }

    public GeoLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFormattedLongitude() {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getFormattedLatitude() {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(latitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
