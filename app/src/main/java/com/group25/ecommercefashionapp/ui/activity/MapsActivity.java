package com.group25.ecommercefashionapp.ui.activity;

import static com.google.android.libraries.places.api.model.Place.Field.ID;
import static com.google.android.libraries.places.api.model.Place.Field.LAT_LNG;
import static com.google.android.libraries.places.api.model.Place.Field.NAME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.group25.ecommercefashionapp.AddressCallback;
import com.group25.ecommercefashionapp.R;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 100;
    private AppCompatButton confirmBtn;
    private GoogleMap map;
    private Location currentLocation;
    private LatLng selectedLatLng;
    private Marker selectedMarker;
    private AutocompleteSupportFragment searchView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnMapLoadCompleteListener onMapLoadCompleteListener;
    private LatLng initLatLng;

    public interface OnMapLoadCompleteListener {
        void onMapLoadComplete();
    }
    public void setOnMapLoadCompleteListener(OnMapLoadCompleteListener onMapLoadCompleteListener) {
        this.onMapLoadCompleteListener = onMapLoadCompleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }

        confirmBtn = findViewById(R.id.btnConfirmLocation);
        confirmBtn.setOnClickListener(v -> {
            if (selectedLatLng != null) {
                Toast.makeText(this, convertLatLngToAddress(this, selectedLatLng), Toast.LENGTH_SHORT).show();
                Bundle getBundle = new Bundle();
                getBundle.putString("address", convertLatLngToAddress(this, selectedLatLng));
                Intent intent = new Intent();
                intent.putExtras(getBundle);
                setResult(RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            }
        });
        searchView = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.searchView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        searchView.setPlaceFields(Arrays.asList(ID, NAME, LAT_LNG));
        searchView.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                map.clear();
                selectedLatLng = place.getLatLng();
                map.addMarker(new MarkerOptions().position(selectedLatLng).title(place.getName()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
            }

            @Override
            public void onError(@NonNull Status e) {
                Toast.makeText(MapsActivity.this, "Error: " + e.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                selectedLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
                supportMapFragment.getMapAsync(MapsActivity.this);
            }
        });
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initLatLng = bundle.getParcelable("storeLocation");
            String storeName = bundle.getString("storeName");
            if (initLatLng != null) {
                selectedLatLng = initLatLng;
                selectedMarker = googleMap.addMarker(new MarkerOptions().position(initLatLng).title(storeName));
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(initLatLng, 17));
                confirmBtn.setText("Back");
                confirmBtn.setOnClickListener(v -> {
                    finish();
                });
            }
        }
        else {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            this.map.getUiSettings().setZoomControlsEnabled(true);
            this.map.getUiSettings().setZoomGesturesEnabled(true);
            this.map.getUiSettings().setScrollGesturesEnabled(true);
            this.map.getUiSettings().setRotateGesturesEnabled(true);
            this.map.getUiSettings().setTiltGesturesEnabled(true);
            this.map.getUiSettings().setCompassEnabled(true);
            this.map.getUiSettings().setMyLocationButtonEnabled(true);
            this.map.getUiSettings().setMapToolbarEnabled(true);
            this.map.getUiSettings().setIndoorLevelPickerEnabled(true);
            this.map.getUiSettings().setAllGesturesEnabled(true);
            this.map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.map.animateCamera(CameraUpdateFactory.zoomTo(17));
            if (onMapLoadCompleteListener != null) {
                onMapLoadCompleteListener.onMapLoadComplete();
            }
            googleMap.setOnCameraMoveListener(() -> {
                LatLng center = googleMap.getCameraPosition().target;
                if (selectedMarker == null) {
                    selectedMarker = googleMap.addMarker(new MarkerOptions().position(center).title("Selected Location"));
                } else {
                    selectedMarker.setPosition(center);
                    selectedLatLng = center;
                }
            });
            googleMap.setOnMapClickListener(latLng1 -> {
                if (selectedMarker == null) {
                    selectedMarker = googleMap.addMarker(new MarkerOptions().position(latLng1).title("Selected Location"));
                } else {
                    selectedMarker.setPosition(latLng1);
                    selectedLatLng = latLng1;
                }
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 17));
            });
        }
    }

    private static String convertLatLngToAddress(FragmentActivity activity, LatLng latLng) {
        String address = "";
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.e("MapsActivity", "getAddresFromLatLng: " + e.getMessage());
        }
        return address;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location Permission is denied, please allow it to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static void getCurrentAddress(FragmentActivity activity, AddressCallback callback) {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                callback.onAddressReceived("Location Permission is denied, please allow it to use this feature");
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    String address = convertLatLngToAddress(activity, latLng);
                    callback.onAddressReceived(address);
                } else {
                    callback.onAddressReceived("Location is null");
                }
            });
        } catch (Exception e) {
            Log.e("MapsActivity", "getCurrentAddress: " + e.getMessage());
            callback.onAddressReceived("Error getting address");
        }
    }
}
