package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPostsActivity extends AppCompatActivity {

    private LostAndFoundDatabase lostAndFoundDatabase;
    private TextView tvNoPostsFound;
    private Button btnNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_posts);

        // edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        tvNoPostsFound = findViewById(R.id.tvNoPostsFound);
        btnNewPost    = findViewById(R.id.btnNewPost);

        btnNewPost.setOnClickListener(v -> {
            startActivity(new Intent(this, NewPostActivity.class));
            finish();
        });

        // initialize Room
        lostAndFoundDatabase = DatabaseHelper
                .getInstance(this)
                .getDatabase();

        // load all items
        ArrayList<LostItem> lostItems = new ArrayList<>();
        lostItems.addAll(lostAndFoundDatabase.lostItemDao().getAllLostItems());

        // show “no posts” message if empty
        if (lostItems.isEmpty()) {
            tvNoPostsFound.setVisibility(View.VISIBLE);
        } else {
            tvNoPostsFound.setVisibility(View.GONE);
        }

        // wire up RecyclerView
        RecyclerView rv = findViewById(R.id.lostItemsRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new LostItemAdapter(this, lostItems));
    }
}
