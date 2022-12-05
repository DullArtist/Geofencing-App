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
    private  NotificationHelper notificationHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                Log.i(TAG, "onReceive: Error receiving geofencing event");
            }else {
                int transitionTypes = geofencingEvent.getGeofenceTransition();
                checkTransitionAndNotify(transitionTypes);
            }

        }else {
            Log.i(TAG, "onReceive: Geofencing Event is null");
        }

    }


    private void checkTransitionAndNotify(int transitionTypes) {
        switch (transitionTypes) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notificationHelper.sendHighPriorityNotification("Geofence","You have entered the geofence", MainActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notificationHelper.sendHighPriorityNotification("Geofence","You have exited the geofence", MainActivity.class);
                break;

        }
    }
}