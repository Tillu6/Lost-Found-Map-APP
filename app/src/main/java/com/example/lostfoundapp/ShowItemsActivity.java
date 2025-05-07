package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private LostFoundDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        db = new LostFoundDBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<LostFoundItem> items = db.getAllItems();
        adapter = new ItemAdapter(items, item -> {
            // onClick → show detail
            Intent i = new Intent(ShowItemsActivity.this, ItemDetailActivity.class);
            i.putExtra("item_id", item.getId());
            startActivity(i);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh after possible delete
        adapter.setItems(db.getAllItems());
        adapter.notifyDataSetChanged();
    }
}
