package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

// Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdaptor extends RecyclerView.Adapter<ItemsAdaptor.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClick(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdaptor(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position);
        // Bind the items into the specified view holder
        holder.bind(item);
    }

    // responsible for binding data to a particular view holder
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each flow of the list
    class  ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItems = itemView.findViewById(android.R.id.text1);
        }
        // Update the view inside of the view holder with this data
        public void bind(String item) {
            tvItems.setText(item);
            tvItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View V) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItems.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View V) {
                    // Notify thw listener which position was long pressed
                    longClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}


