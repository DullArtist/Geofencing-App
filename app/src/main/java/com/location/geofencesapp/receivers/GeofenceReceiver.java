package com.location.geofencesapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.location.geofencesapp.MainActivity;
import com.location.geofencesapp.helpers.NotificationHelper;

import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceReceiver";
    private NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationHelper = new NotificationHelper(context);

        Toast.makeText(context, "Geofence entered", Toast.LENGTH_SHORT).show();

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "onReceive: Error receiving geofences" );
        }
        
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.i(TAG, "onReceive: "+geofence.getRequestId()+geofence.getLoiteringDelay());
        }
        
        int transitionTypes = geofencingEvent.getGeofenceTransition();
        
        switch (transitionTypes) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification("Geofence","You have entered the geofence", MainActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                notificationHelper.sendHighPriorityNotification("Geofence","You are inside the geofence", MainActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification("Geofence","You have exited the geofence", MainActivity.class);
                break;
            default:
                break;
        }

    }
}