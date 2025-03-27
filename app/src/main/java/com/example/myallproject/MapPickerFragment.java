package com.example.myallproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPickerFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private Marker selectedMarker;
    private LatLng selectedLatLng;

    public static final String RESULT_KEY = "map_location_result";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_picker, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button btnSelect = view.findViewById(R.id.btnConfirmLocation);
        btnSelect.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                Bundle result = new Bundle();
                result.putDouble("latitude", selectedLatLng.latitude);
                result.putDouble("longitude", selectedLatLng.longitude);
                getParentFragmentManager().setFragmentResult(RESULT_KEY, result);
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Please select a location on the map", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng defaultLatLng = new LatLng(32.0853, 34.7818); // Tel Aviv for example
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 12f));

        map.setOnMapClickListener(latLng -> {
            selectedLatLng = latLng;
            if (selectedMarker != null) selectedMarker.remove();
            selectedMarker = map.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    // Map lifecycle management
    @Override public void onResume() { super.onResume(); mapView.onResume(); }
    @Override public void onPause() { super.onPause(); mapView.onPause(); }
    @Override public void onDestroy() { super.onDestroy(); mapView.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
}