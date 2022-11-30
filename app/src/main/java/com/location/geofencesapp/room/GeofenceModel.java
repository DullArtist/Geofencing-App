package com.location.geofencesapp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class GeofenceModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Double latitude;
    private Double longitude;
    private float radius;

    public String getGeofenceName() {
        return geofenceName;
    }

    public void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    private String geofenceName;

    public GeofenceModel(Double latitude, Double longitude, float radius, String geofenceName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.geofenceName = geofenceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
