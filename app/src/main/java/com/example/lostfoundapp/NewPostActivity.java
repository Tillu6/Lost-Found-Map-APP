package com.example.lostfoundapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class NewPostActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private EditText etItemName,
            etDescription,
            etLocation,
            etDate,
            etPosterName,
            etMobile;

    private Button btnSavePost,
            btnGetCurrentLocation;

    private TabLayout tlReportType;
    private LostAndFoundDatabase lostAndFoundDatabase;
    private LocationInfo itemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_post);

        // Apply window insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // Prepare a holder for the chosen location
        itemLocation = new LocationInfo();

        // Initialize the Places SDK with your API key from strings.xml
        if (!Places.isInitialized()) {
            String apiKey = getString(R.string.google_api_key);
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Prevent layout resize when keyboard is shown
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Grab our Room database instance
        lostAndFoundDatabase = DatabaseHelper
                .getInstance(this)
                .getDatabase();

        // Bind all views
        etItemName            = findViewById(R.id.etItemName);
        etDescription         = findViewById(R.id.etDescription);
        etLocation            = findViewById(R.id.etLocation);
        etDate                = findViewById(R.id.etDate);
        etPosterName          = findViewById(R.id.etPosterName);
        etMobile              = findViewById(R.id.etMobile);
        btnSavePost           = findViewById(R.id.btnSavePost);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);
        tlReportType          = findViewById(R.id.tlReportType);

        // “Use my current location” logic
        btnGetCurrentLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                        REQUEST_LOCATION_PERMISSION
                );
                return;
            }

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                lm.getCurrentLocation(
                        LocationManager.GPS_PROVIDER,
                        null,
                        getMainExecutor(),
                        new Consumer<Location>() {
                            @Override
                            public void accept(Location loc) {
                                if (loc == null) return;
                                double lat = loc.getLatitude();
                                double lng = loc.getLongitude();

                                Geocoder geocoder = new Geocoder(
                                        getBaseContext(), Locale.getDefault()
                                );
                                try {
                                    List<Address> addrs = geocoder.getFromLocation(lat, lng, 1);
                                    if (addrs != null && !addrs.isEmpty()) {
                                        Address a = addrs.get(0);
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i <= a.getMaxAddressLineIndex(); i++) {
                                            sb.append(a.getAddressLine(i));
                                            if (i < a.getMaxAddressLineIndex()) {
                                                sb.append(", ");
                                            }
                                        }
                                        etLocation.setText(sb.toString());
                                    }
                                } catch (IOException e) {
                                    Log.e("NewPostActivity", "Geocoder failed", e);
                                }
                            }
                        }
                );
            }
        });

        // Launch Google Places Autocomplete
        etLocation.setFocusable(false);
        etLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG
            );
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields
            ).build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        // Save button handler
        btnSavePost.setOnClickListener(v -> {
            LostItem.REPORT_TYPE type =
                    LostItem.REPORT_TYPE.values()[
                            tlReportType.getSelectedTabPosition()
                            ];

            String name    = etItemName.getText().toString().trim();
            String desc    = etDescription.getText().toString().trim();
            String locText = etLocation.getText().toString().trim();
            String dateStr = etDate.getText().toString().trim();
            String poster  = etPosterName.getText().toString().trim();
            String mobile  = etMobile.getText().toString().trim();

            // Validate date format yyyy-MM-dd
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    LocalDate.parse(dateStr,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    );
                } catch (Exception ex) {
                    Toast.makeText(
                            this,
                            "Date must be yyyy-MM-dd",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }
            }

            // Basic field checks
            if (name.isEmpty() || desc.isEmpty() ||
                    locText.isEmpty() || dateStr.isEmpty() ||
                    poster.isEmpty() || mobile.isEmpty())
            {
                Toast.makeText(
                        this,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            if (itemLocation.getLocationName().isEmpty()) {
                Toast.makeText(
                        this,
                        "Please select a location",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // Insert into Room
            LostItem item = new LostItem(
                    type,
                    name,
                    desc,
                    itemLocation,
                    dateStr,
                    poster,
                    mobile
            );
            lostAndFoundDatabase.lostItemDao().insert(item);

            Toast.makeText(this, "Post created!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewPostsActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etLocation.setText(place.getName());
                itemLocation.setLocationName(place.getName());

                LatLng ll = place.getLatLng();
                if (ll != null) {
                    itemLocation.setLatitude(ll.latitude);
                    itemLocation.setLongitude(ll.longitude);
                    itemLocation.print();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.w("NewPostActivity", "Places error: " + status.getStatusMessage());
            }
        }
    }
}
