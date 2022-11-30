package com.location.geofencesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.location.geofencesapp.R;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.List;

public class GeofencesAdapter extends RecyclerView.Adapter<GeofencesAdapter.ViewHolder> {

    List<GeofenceModel> geofences;
    Context context;

    GeofenceClickListener geofenceClickListener;

    public interface GeofenceClickListener {
        void onGeofenceClick(Double lat,Double lon);
    }

    public GeofencesAdapter(List<GeofenceModel> geofences, Context context,GeofenceClickListener geofenceClickListener) {
        this.geofences = geofences;
        this.context = context;
        this.geofenceClickListener = geofenceClickListener;
    }

    @NonNull
    @Override
    public GeofencesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.geofences,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeofencesAdapter.ViewHolder holder, int position) {
        holder.geofenceName.setText(geofences.get(position).getGeofenceName());
        holder.itemView.requestFocus();
    }

    @Override
    public int getItemCount() {
        if (geofences.isEmpty())
            Toast.makeText(context, "No geofences", Toast.LENGTH_SHORT).show();
        return geofences.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView geofenceName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            geofenceName = itemView.findViewById(R.id.geofenceName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            GeofenceModel model = geofences.get(position);
            geofenceClickListener.onGeofenceClick(model.getLatitude(), model.getLongitude());
        }
    }
}
