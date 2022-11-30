package com.location.geofencesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.location.geofencesapp.adapters.GeofencesAdapter;
import com.location.geofencesapp.databinding.FragmentFenceListBinding;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.List;

public class fence_list extends Fragment implements GeofencesAdapter.GeofenceClickListener {

    private FragmentFenceListBinding binding;
    private GeofencesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFenceListBinding.inflate(inflater, container, false);

        GeofenceViewModel viewModel = new ViewModelProvider(requireActivity()).get(GeofenceViewModel.class);

        viewModel.getAllGeofences().observe(getViewLifecycleOwner(), geofenceModels -> {
            adapter = new GeofencesAdapter(geofenceModels, requireActivity(),fence_list.this);
            binding.geofencesListRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.geofencesListRV.setAdapter(adapter);
        });
        

        return binding.getRoot();
    }

    @Override
    public void onGeofenceClick(Double lat, Double lon) {
        Toast.makeText(requireActivity(), "clicked", Toast.LENGTH_SHORT).show();
    }
}