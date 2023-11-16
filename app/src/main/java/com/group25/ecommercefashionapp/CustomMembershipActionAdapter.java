package com.group25.ecommercefashionapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomMembershipActionAdapter extends RecyclerView.Adapter<CustomMembershipActionAdapter.ViewHolder> {
    private List<ActionItem> items;
    private OnItemClickListener clickListener;

    // Constructor to set the data and click listener
    public CustomMembershipActionAdapter(List<ActionItem> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        private TextView actionName;
        // Add references to your CardView components here

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.membershipCardView);
            actionName = view.findViewById(R.id.actionNameTxt);
            // Initialize your UI components here
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.membership_action_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActionItem item = items.get(position);

        // Bind your data to the UI components of the CardView
        holder.actionName.setText(item.getName());
        // Set click listener on the card
        holder.cardView.setOnClickListener(v -> {

            clickListener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

