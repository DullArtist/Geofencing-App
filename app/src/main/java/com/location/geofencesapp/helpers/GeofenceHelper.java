package com.location.geofencesapp.helpers;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.location.geofencesapp.receivers.GeofenceReceiver;

public class GeofenceHelper extends ContextWrapper {

    PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeofenceRequest(Geofence geofence) {

        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public Geofence getGeofence(String ID, LatLng latLng , float radius , int transitionTypes) {
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    public PendingIntent getPendingIntent() {

        if(pendingIntent!=null)
            return pendingIntent;

        Intent intent = new Intent(this, GeofenceReceiver.class);
        if (Build.VERSION.SDK_INT >=29)
            pendingIntent = PendingIntent.getBroadcast(this,432,intent,PendingIntent.FLAG_IMMUTABLE);
        else
            pendingIntent = PendingIntent.getBroadcast(this,432,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public String getError(Exception e) {
        if(e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "Geofence not available";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Too many geofences";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Too many PendingIntents";
            }
        }

        return e.getLocalizedMessage();
    }
}
