package com.location.geofencesapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.location.geofencesapp.room.GeofenceDatabase;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.List;

public class GeofenceViewModel extends AndroidViewModel {

    private final GeofenceRepository repository;

    public GeofenceViewModel(@NonNull Application application) {
        super(application);
        GeofenceDatabase geofenceDatabase = GeofenceDatabase.getInstance(application);
        repository = new GeofenceRepository(geofenceDatabase);
    }

    public LiveData<List<GeofenceModel>> getAllGeofences() {
        return repository.getAllGeofences();
    }

    public void insertGeofence(GeofenceModel geofence) {
        repository.insertGeofence(geofence);
    }

    public void deleteGeofence(GeofenceModel geofence) {
        repository.deleteGeofence(geofence);
    }


}
