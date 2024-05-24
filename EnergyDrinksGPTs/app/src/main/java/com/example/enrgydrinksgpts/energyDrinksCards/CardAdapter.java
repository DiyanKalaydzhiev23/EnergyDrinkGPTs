package com.example.enrgydrinksgpts.energyDrinksCards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final List<CardItem> mItems;
    private final OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(CardItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView descriptionView;
        public TextView titleView;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image);
            titleView = v.findViewById(R.id.title);
            descriptionView = v.findViewById(R.id.descriptionText);
            cardView = v.findViewById(R.id.card_view);
        }
    }

    public CardAdapter(List<CardItem> items, OnItemClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem item = mItems.get(position);
        holder.imageView.setImageResource(item.getImageResourceId());
        holder.titleView.setText(item.getName());
        holder.descriptionView.setText(item.getDescription());
        holder.cardView.setCardBackgroundColor(item.getBackgroundColor());

        if (item.isUnlocked()) {
            holder.cardView.setOnClickListener(view -> mListener.onItemClick(item));
            holder.cardView.setAlpha(1.0f); // Fully opaque if unlocked
        } else {
            holder.cardView.setOnClickListener(null);
            holder.cardView.setAlpha(0.5f); // Semi-transparent if not unlocked
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}