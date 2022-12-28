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
    private String location;
    private String spot;
    private Date diveDate;
    private int minutes;
    private float maxDepth;
    private String remarks;

    public Dive() {
    }

    public Dive(String location, String spot, Date diveDate, int minutes, float maxDepth, String remarks) {
        this.location = location;
        this.spot = spot;
        this.diveDate = diveDate;
        this.minutes = minutes;
        this.maxDepth = maxDepth;
        this.remarks = remarks;
    }

    public void setDiveId(int diveId) {
        this.diveId = diveId;
    }

    public int getDiveId() {
        return diveId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getPlace() {
        return location + " - " + spot;
    }

    public Date getDiveDate() {
        return diveDate;
    }

    public void setDiveDate(Date diveDate) {
        this.diveDate = diveDate;
    }

    public String getFormatedDiveDate() {
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
                ", location='" + location + '\'' +
                ", spot='" + spot + '\'' +
                ", diveDate=" + diveDate +
                ", minutes=" + minutes +
                ", maxDepth=" + maxDepth +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
