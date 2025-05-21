package com.example.lostfoundapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewLostItemActivity extends AppCompatActivity {

    public static final String EXTRA_LOST_ITEM_ID = "extra_lost_item_id";

    private TextView tvReportTypeTitle;
    private TextView tvItemName;
    private TextView tvDescription;
    private TextView tvDateReported;
    private TextView tvLocation;
    private TextView tvContactInformation;

    private Button btnViewAllPosts;
    private Button btnRemovePost;
    private Button btnViewOnMaps;

    private LostAndFoundDatabase lostAndFoundDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_lost_item);

        // handle edge‐to‐edge insets
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // bind views
        tvReportTypeTitle   = findViewById(R.id.tvReportType_Title);
        tvItemName          = findViewById(R.id.tvItemName);
        tvDescription       = findViewById(R.id.tvDescription);
        tvDateReported      = findViewById(R.id.tvDateReported);
        tvLocation          = findViewById(R.id.tvLocation);
        tvContactInformation= findViewById(R.id.tvContactInformation);

        btnViewAllPosts     = findViewById(R.id.btnViewAllPosts);
        btnRemovePost       = findViewById(R.id.btnRemovePost);
        btnViewOnMaps       = findViewById(R.id.btnViewOnMaps);

        // make sure we have a valid ID
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_LOST_ITEM_ID)) {
            navigateBack();
            return;
        }

        int lostItemId = intent.getIntExtra(EXTRA_LOST_ITEM_ID, -1);
        if (lostItemId < 0) {
            navigateBack();
            return;
        }

        // pull in the Room database
        lostAndFoundDatabase = DatabaseHelper
                .getInstance(this)
                .getDatabase();

        // fetch the item
        LostItem lostItem =
                lostAndFoundDatabase
                        .lostItemDao()
                        .getLostItemById(lostItemId);

        if (lostItem == null) {
            navigateBack();
            return;
        }

        // “View all posts” button
        btnViewAllPosts.setOnClickListener(v -> navigateBack());

        // “View on map” button
        btnViewOnMaps.setOnClickListener(v -> {
            startActivity(
                    new Intent(this, MapsActivity.class)
                            .putExtra(
                                    MapsActivity.EXTRA_FOCUS_LOST_ITEM_ID,
                                    lostItem.getId()
                            )
            );
            finish();
        });

        // “Delete” button
        btnRemovePost.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to delete this post?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        lostAndFoundDatabase
                                .lostItemDao()
                                .delete(lostItem);
                        Toast
                                .makeText(
                                        this,
                                        "Successfully deleted post",
                                        Toast.LENGTH_LONG
                                )
                                .show();
                        navigateBack();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // populate the fields
        if (lostItem.getReportType() == LostItem.REPORT_TYPE.REPORT_TYPE_LOST) {
            tvReportTypeTitle.setText("Missing item");
        } else {
            tvReportTypeTitle.setText("Found item");
        }
        tvItemName.setText(lostItem.getItemName());
        tvDescription.setText(lostItem.getDescription());
        tvDateReported.setText(lostItem.formatTimeAgo());
        tvLocation.setText(lostItem.getLocation().getLocationName());
        tvContactInformation.setText(
                lostItem.getPostersName()
                        + "\n"
                        + lostItem.getMobile()
        );
    }

    private void navigateBack() {
        startActivity(new Intent(this, ViewPostsActivity.class));
        finish();
    }
}
