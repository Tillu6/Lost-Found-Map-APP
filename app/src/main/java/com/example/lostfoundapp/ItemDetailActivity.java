package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {
    private LostFoundDBHelper db;
    private int itemId;

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvContact;
    private Button   btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // 1. Wire up views
        tvTitle       = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation    = findViewById(R.id.tvLocation);
        tvContact     = findViewById(R.id.tvContact);
        btnRemove     = findViewById(R.id.btnRemove);

        // 2. Init DB helper and fetch the passed ID
        db     = new LostFoundDBHelper(this);
        itemId = getIntent().getIntExtra("item_id", -1);

        // 3. Load the item from the database
        LostFoundItem item = db.getItemById(itemId);
        if (item != null) {
            tvTitle      .setText(item.getTitle());
            tvDescription.setText(item.getDescription());
            tvLocation   .setText(item.getLocation());
            tvContact    .setText(item.getContact());
        }

        // 4. When Remove is tapped, delete and go back
        btnRemove.setOnClickListener(v -> {
            db.deleteItem(itemId);
            finish();
        });
    }
}
