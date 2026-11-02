package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Shows pantry items in a RecyclerView. Click = edit, long-press = delete. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> items = new ArrayList<>();
    private boolean highlightExpiring = true;
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<PantryItem> items, boolean highlightExpiring) {
        this.items = items;
        this.highlightExpiring = highlightExpiring;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        PantryItem item = items.get(position);
        StringBuilder sub = new StringBuilder(item.getQuantityText()).append(" ").append(item.getUnit());
        if (!item.getExpiryDate().isEmpty()) sub.append("  \u00B7  expires ").append(item.getExpiryDate());
        if (highlightExpiring && item.isExpiringSoon(3)) sub.append("  \u26A0 expiring soon");
        h.title.setText(item.getName());
        h.subtitle.setText(sub);
        h.itemView.setOnClickListener(v -> listener.onEdit(item));
        h.itemView.setOnLongClickListener(v -> { listener.onDelete(item); return true; });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView subtitle;
        ViewHolder(View v) {
            super(v);
            title = v.findViewById(android.R.id.text1);
            subtitle = v.findViewById(android.R.id.text2);
        }
    }
}
