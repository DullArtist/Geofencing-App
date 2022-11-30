package com.location.geofencesapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GeofenceModel.class},version = 1,exportSchema = false)
public abstract class GeofenceDatabase extends RoomDatabase {

    private static GeofenceDatabase instance;

    public static synchronized GeofenceDatabase getInstance(Context context) {

        if(instance == null){
            instance = Room.databaseBuilder(context,GeofenceDatabase.class,"GeofenceDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract GeofenceDao geofenceDao();
}
