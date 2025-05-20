package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnCreateAdvert;
    private Button btnShowItems;
    private Button btnShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems    = findViewById(R.id.btnShowItems);
        btnShowMap      = findViewById(R.id.btnShowMap);

        btnCreateAdvert.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddItemActivity.class))
        );

        btnShowItems.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ShowItemsActivity.class))
        );

        btnShowMap.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ShowMapActivity.class))
        );
    }
}
