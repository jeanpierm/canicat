package com.jeanpier.canicat.ui.google_maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentSearchCanicatBinding;

public class SearchCanicatFragment extends Fragment {

    private FragmentSearchCanicatBinding binding;
    public static final int REQUEST_CODE_LOCATION = 1;
    public static GoogleMap map;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18f),
                    4000,
                    null);
            enableLocation();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
                binding = FragmentSearchCanicatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private boolean isLocationPermissionGranted() {
        if (getActivity() == null) return false;
        return ContextCompat.
                checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (map == null) return;
        if (!isLocationPermissionGranted()) {
            map.setMyLocationEnabled(false);
            Toast.makeText(getActivity(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableLocation() {
        if (map == null) return;
        if (isLocationPermissionGranted()) {
            map.setMyLocationEnabled(true);
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (getActivity() == null) return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_LONG).show();
        } else {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_LOCATION);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}