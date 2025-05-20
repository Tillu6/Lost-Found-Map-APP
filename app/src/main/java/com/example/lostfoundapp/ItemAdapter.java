package com.example.lostfoundapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    /** Callback interface for item clicks **/
    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    private List<LostFoundItem> items;
    private final OnItemClickListener listener;

    /** Constructor now takes your data *and* a click listener **/
    public ItemAdapter(List<LostFoundItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /** Swap in fresh data (e.g. after onResume in your Activity) **/
    public void setItems(List<LostFoundItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = items.get(position);

        // Use the correct getters from LostFoundItem
        holder.tvTitle      .setText(item.getName());
        holder.tvDescription.setText(item.getDescription());
        holder.tvLocation   .setText(item.getLocation());
        holder.tvType       .setText(item.getType());
        holder.tvContact    .setText(item.getPhone());

        // attach click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /** Simple ViewHolder that just grabs your five TextViews **/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDescription, tvLocation, tvType, tvContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle        = itemView.findViewById(R.id.tvTitle);
            tvDescription  = itemView.findViewById(R.id.tvDescription);
            tvLocation     = itemView.findViewById(R.id.tvLocation);
            tvType         = itemView.findViewById(R.id.tvType);
            tvContact      = itemView.findViewById(R.id.tvContact);
        }
    }
}
