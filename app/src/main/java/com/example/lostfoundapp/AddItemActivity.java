package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {
    private LostFoundDBHelper db;

    // 1) View references
    private RadioGroup rgPostType;
    private RadioButton rbLost, rbFound;
    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // 2) Init DB helper
        db = new LostFoundDBHelper(this);

        // 3) Bind views
        rgPostType   = findViewById(R.id.rgPostType);
        rbLost       = findViewById(R.id.rbLost);
        rbFound      = findViewById(R.id.rbFound);
        etName       = findViewById(R.id.etName);
        etPhone      = findViewById(R.id.etPhone);
        etDescription= findViewById(R.id.etDescription);
        etDate       = findViewById(R.id.etDate);
        etLocation   = findViewById(R.id.etLocation);
        btnSave      = findViewById(R.id.btnSave);

        // 4) Handle Save click
        btnSave.setOnClickListener(v -> {
            // Read inputs
            String type        = rbLost.isChecked() ? "Lost" : "Found";
            String name        = etName.getText().toString().trim();
            String phone       = etPhone.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date        = etDate.getText().toString().trim();
            String location    = etLocation.getText().toString().trim();

            // Validate
            if (name.isEmpty() || phone.isEmpty() || description.isEmpty()
                    || date.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new LostFoundItem (id is auto-generated)
            LostFoundItem item = new LostFoundItem(
                    0,              // placeholder id
                    name,           // title
                    description + "\nDate: " + date,  // combine description + date
                    location,       // location
                    type,           // Lost or Found
                    phone           // contact
            );

            // Save and go back
            db.addItem(item);
            Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
