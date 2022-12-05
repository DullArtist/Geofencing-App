package com.location.geofencesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.location.geofencesapp.adapters.GeofencesAdapter;
import com.location.geofencesapp.databinding.FragmentFenceListBinding;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.ArrayList;
import java.util.List;

public class fence_list extends Fragment implements GeofencesAdapter.GeofenceClickListener {

    private FragmentFenceListBinding binding;
    private GeofenceViewModel viewModel;
    private GeofencesAdapter adapter;
    private List<GeofenceModel> geofenceModelList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFenceListBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(GeofenceViewModel.class);

        viewModel.getAllGeofences().observe(getViewLifecycleOwner(), geofenceModels -> {

            adapter = new GeofencesAdapter(geofenceModels, requireActivity(),fence_list.this);
            binding.geofencesListRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.geofencesListRV.setAdapter(adapter);

            geofenceModelList = geofenceModels;

        });

        ItemTouchHelper();

        return binding.getRoot();
    }

    private void ItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                GeofenceModel geofence_to_be_deleted = geofenceModelList.get(viewHolder.getAdapterPosition());
                viewModel.deleteGeofence(geofence_to_be_deleted);

                Snackbar.make(binding.geofencesListRV,geofence_to_be_deleted.getGeofenceName(),Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> {
                            viewModel.insertGeofence(geofence_to_be_deleted);
                        }).show();

            }
        }).attachToRecyclerView(binding.geofencesListRV);
    }

    @Override
    public void onGeofenceClick(Double lat, Double lon) {
        Toast.makeText(requireActivity(), "clicked", Toast.LENGTH_SHORT).show();
    }
}