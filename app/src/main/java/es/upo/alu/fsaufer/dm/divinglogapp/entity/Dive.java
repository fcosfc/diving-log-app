package es.upo.alu.fsaufer.dm.divinglogapp.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase que implementa una inmersión
 */
public class Dive implements Serializable {

    private static final long serialVersionUID = 8799656478674716638L;

    private int diveId;
    private DiveLocation diveLocation;
    private String spot;
    private Date diveDate;
    private int minutes;
    private float maxDepth;
    private WeatherConditions weatherConditions;
    private boolean nitroxUse;
    private String remarks;

    public Dive() {
    }

    public Dive(DiveLocation diveLocation, String spot, Date diveDate, int minutes, float maxDepth,
                WeatherConditions weatherConditions, boolean nitroxUse, String remarks) {
        this.diveLocation = diveLocation;
        this.spot = spot;
        this.diveDate = diveDate;
        this.minutes = minutes;
        this.maxDepth = maxDepth;
        this.weatherConditions = weatherConditions;
        this.nitroxUse = nitroxUse;
        this.remarks = remarks;
    }

    public void setDiveId(int diveId) {
        this.diveId = diveId;
    }

    public int getDiveId() {
        return diveId;
    }

    public DiveLocation getLocation() {
        return diveLocation;
    }

    public void setLocation(DiveLocation diveLocation) {
        this.diveLocation = diveLocation;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getPlace() {
        return diveLocation.getName() + " - " + spot;
    }

    public Date getDiveDate() {
        return diveDate;
    }

    public void setDiveDate(Date diveDate) {
        this.diveDate = diveDate;
    }

    public String getFormattedDiveDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);

        return dateFormat.format(diveDate);
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public float getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(float maxDepth) {
        this.maxDepth = maxDepth;
    }

    public WeatherConditions getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(WeatherConditions weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public boolean isNitroxUse() {
        return nitroxUse;
    }

    public void setNitroxUse(boolean nitroxUse) {
        this.nitroxUse = nitroxUse;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dive dive = (Dive) o;
        return diveId == dive.diveId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diveId);
    }

    @Override
    public String toString() {
        return "Dive{" +
                "diveId=" + diveId +
                ", location=" + diveLocation +
                ", spot='" + spot + '\'' +
                ", diveDate=" + diveDate +
                ", minutes=" + minutes +
                ", maxDepth=" + maxDepth +
                ", weatherConditions=" + weatherConditions +
                ", nitroxUse=" + nitroxUse +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
