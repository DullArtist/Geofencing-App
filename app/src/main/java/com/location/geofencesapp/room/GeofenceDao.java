package com.location.geofencesapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GeofenceDao {

    @Query("SELECT * FROM GEOFENCEMODEL")
    LiveData<List<GeofenceModel>> getAllGeofences();

    @Insert
    void insertGeofence(GeofenceModel geofence);

    @Delete
    void deleteGeofence(GeofenceModel geofence);

}
