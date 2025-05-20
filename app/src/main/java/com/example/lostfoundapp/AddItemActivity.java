package com.example.lostfoundapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE   = 100;
    private static final int LOCATION_PERMISSION_REQUEST = 200;

    private LostFoundDBHelper db;
    private RadioButton    rbLost, rbFound;
    private EditText       etName, etPhone, etDescription, etDate, etLocation;
    private Button         btnCurrentLoc, btnSave;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng         selectedLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Initialize Places SDK once
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = new LostFoundDBHelper(this);

        // View bindings
        rbLost        = findViewById(R.id.rbLost);
        rbFound       = findViewById(R.id.rbFound);
        etName        = findViewById(R.id.etName);
        etPhone       = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate        = findViewById(R.id.etDate);
        etLocation    = findViewById(R.id.etLocation);
        btnCurrentLoc = findViewById(R.id.btnCurrentLoc);
        btnSave       = findViewById(R.id.btnSave);

        // Autocomplete on location field
        etLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
            );
            Intent intent = new Autocomplete
                    .IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // “Get Current Location” button
        btnCurrentLoc.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_PERMISSION_REQUEST
                );
            } else {
                fetchLastLocation();
            }
        });

        // Save button
        btnSave.setOnClickListener(v -> saveItem());
    }

    /**
     * Fetch device’s last known location, after ensuring permissions.
     */
    private void fetchLastLocation() {
        // explicit permission check to satisfy lint
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,
                    "Location permission not granted",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        selectedLatLng = new LatLng(
                                location.getLatitude(),
                                location.getLongitude()
                        );

                        // Reverse‐geocode to fill the address field
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> list = geocoder.getFromLocation(
                                    selectedLatLng.latitude,
                                    selectedLatLng.longitude,
                                    1
                            );
                            if (!list.isEmpty()) {
                                etLocation.setText(list.get(0).getAddressLine(0));
                            }
                        } catch (IOException e) {
                            Log.e("AddItemActivity", "Reverse geocode failed", e);
                        }
                    }
                });
    }

    private void saveItem() {
        String type  = rbLost.isChecked() ? "Lost" : "Found";
        String title = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String desc  = etDescription.getText().toString().trim();
        String date  = etDate.getText().toString().trim();
        String loc   = etLocation.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() ||
                phone.isEmpty() ||
                desc.isEmpty()  ||
                date.isEmpty()  ||
                loc.isEmpty()   ||
                selectedLatLng == null) {

            Toast.makeText(this,
                    "Please fill all fields and select a location",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String fullDesc = desc + "\nDate: " + date;

        // Pass all 9 fields in correct order
        LostFoundItem item = new LostFoundItem(
                0,                  // id (auto)
                title,              // name
                phone,              // phone
                fullDesc,           // description
                date,               // date
                loc,                // location
                selectedLatLng.latitude,
                selectedLatLng.longitude,
                type                // “Lost” or “Found”
        );

        db.addItem(item);
        Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation();
        } else {
            Toast.makeText(this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                selectedLatLng = place.getLatLng();
                etLocation.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("AddItemActivity",
                        "Autocomplete error: " + status.getStatusMessage());
            }
        }
    }
}
