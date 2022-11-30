package com.location.geofencesapp;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.location.geofencesapp.room.GeofenceDao;
import com.location.geofencesapp.room.GeofenceDatabase;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.List;

public class GeofenceRepository {

    private final GeofenceDatabase geofenceDatabase;
    private GeofenceDao geofenceDao;

    public GeofenceRepository(GeofenceDatabase geofenceDatabase) {
        this.geofenceDatabase = geofenceDatabase;
        geofenceDao = geofenceDatabase.geofenceDao();
    }

    public LiveData<List<GeofenceModel>> getAllGeofences() {
        return geofenceDatabase.geofenceDao().getAllGeofences();
    }

    public void insertGeofence(GeofenceModel geofence) {
        new insertGeofenceTask(geofenceDao).execute(geofence);
        Log.i("ROOM", "insertGeofence: inserted");
    }

    public void deleteGeofence(GeofenceModel geofence) {
        new deleteGeofenceTask(geofenceDao).execute(geofence);
    }


    private static class insertGeofenceTask extends AsyncTask<GeofenceModel,Void,Void> {

        private final GeofenceDao geofenceDao;

        public insertGeofenceTask(GeofenceDao geofenceDao) {
            this.geofenceDao = geofenceDao;
        }

        @Override
        protected Void doInBackground(GeofenceModel... geofenceModels) {
            geofenceDao.insertGeofence(geofenceModels[0]);
            return null;
        }
    }

    private static class deleteGeofenceTask extends AsyncTask<GeofenceModel,Void,Void> {

        private final GeofenceDao geofenceDao;

        public deleteGeofenceTask(GeofenceDao geofenceDao) {
            this.geofenceDao = geofenceDao;
        }

        @Override
        protected Void doInBackground(GeofenceModel... geofenceModels) {
            geofenceDao.deleteGeofence(geofenceModels[0]);
            return null;
        }
    }

}

