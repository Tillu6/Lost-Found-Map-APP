package com.example.lostfoundapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.lostfoundapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final String EXTRA_FOCUS_LOST_ITEM_ID = "extra_focus_lost_item_id";

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LostAndFoundDatabase lostAndFoundDatabase;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean cameraCentered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get your Room database
        lostAndFoundDatabase = DatabaseHelper
                .getInstance(this)
                .getDatabase();

        // prepare the location manager + listener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (mMap == null) return;
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                if (!cameraCentered) {
                    cameraCentered = true;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                }
            }
        };

        // hook up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // request GPS updates (or ask permission if needed)
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,    // 1s
                    0,
                    locationListener
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, locationListener
                );
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //--------------------------------------------
        // ENABLE ZOOM CONTROLS & GESTURES
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //--------------------------------------------

        // map settings
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // pull in all of your LostItems
        ArrayList<LostItem> lostItems =
                new ArrayList<>(lostAndFoundDatabase.lostItemDao().getAllLostItems());

        // see if we need to focus a specific one
        Intent intent = getIntent();
        int focusId = intent.getIntExtra(EXTRA_FOCUS_LOST_ITEM_ID, -1);
        if (focusId != -1) cameraCentered = true;

        // marker click → open detail screen
        mMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof Integer) {
                int itemId = (Integer) tag;
                startActivity(
                        new Intent(this, ViewLostItemActivity.class)
                                .putExtra(ViewLostItemActivity.EXTRA_LOST_ITEM_ID, itemId)
                );
                finish();
            }
            return true;
        });

        // add each lost/found marker
        for (LostItem item : lostItems) {
            LatLng pos = new LatLng(
                    item.getLocation().getLatitude(),
                    item.getLocation().getLongitude()
            );

            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title((item.getReportType() == LostItem.REPORT_TYPE.REPORT_TYPE_FOUND
                            ? "Found: "
                            : "Lost: ")
                            + item.getItemName())
                    .snippet(item.getLocation().getLocationName())
            );

            if (m != null) {
                float hue = (item.getReportType() == LostItem.REPORT_TYPE.REPORT_TYPE_FOUND
                        ? BitmapDescriptorFactory.HUE_YELLOW
                        : BitmapDescriptorFactory.HUE_RED);
                m.setIcon(BitmapDescriptorFactory.defaultMarker(hue));
                m.setTag(item.getId());

                if (item.getId() == focusId) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14));
                }
            }

            // just for your debug output
            item.getLocation().print();
        }
    }
}
