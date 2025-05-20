package com.example.lostfoundapp;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LostFoundDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        db = new LostFoundDBHelper(this);

        // Dynamically add the map fragment into our FrameLayout
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFrag = (SupportMapFragment)
                fm.findFragmentByTag("map_fragment_tag");

        if (mapFrag == null) {
            mapFrag = SupportMapFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.mapContainer, mapFrag, "map_fragment_tag")
                    .commit();
        }

        // Once it’s in place, request the GoogleMap object
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<LostFoundItem> items = db.getAllItems();
        if (items == null || items.isEmpty()) {
            return; // nothing to show
        }

        // Build a LatLngBounds to include all markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LostFoundItem it : items) {
            LatLng pos = new LatLng(it.getLatitude(), it.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(it.getName())
                    .snippet(it.getDescription()));
            builder.include(pos);
        }

        // Move camera so all markers are visible
        int padding = 100; // px
        mMap.moveCamera(
                CameraUpdateFactory.newLatLngBounds(builder.build(), padding)
        );
    }
}
