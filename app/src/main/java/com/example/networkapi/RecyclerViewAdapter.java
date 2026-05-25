package com.example.networkapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.networkapi.databinding.GridItemBinding;
import com.example.networkapi.databinding.RowItemBinding;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Meal> meals;
    private final Context context;
    private boolean isGrid = false;

    private static final int TYPE_LIST = 0;
    private static final int TYPE_GRID = 1;

    public RecyclerViewAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;
    }

    public void setViewType(boolean isGrid) {
        this.isGrid = isGrid;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isGrid ? TYPE_GRID : TYPE_LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_GRID) {
            GridItemBinding binding = GridItemBinding.inflate(inflater, parent, false);
            return new GridViewHolder(binding);
        } else {
            RowItemBinding binding = RowItemBinding.inflate(inflater, parent, false);
            return new ListViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        if (holder instanceof GridViewHolder) {
            GridViewHolder gridHolder = (GridViewHolder) holder;
            gridHolder.binding.tvItemName.setText(meal.getStrMeal());
            gridHolder.binding.tvItemDesc.setText(meal.getStrArea());
            Glide.with(context)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.broken_image_24)
                    .into(gridHolder.binding.imgItemPhoto);
        } else if (holder instanceof ListViewHolder) {
            ListViewHolder listHolder = (ListViewHolder) holder;
            listHolder.binding.tvItemName.setText(meal.getStrMeal());
            listHolder.binding.tvItemDesc.setText(meal.getStrArea());
            Glide.with(context)
                    .load(meal.getStrMealThumb())
                    .placeholder(R.drawable.broken_image_24)
                    .into(listHolder.binding.imgItemPhoto);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("i_idMeal", meal.getIdMeal());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        final RowItemBinding binding;
        ListViewHolder(RowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {
        final GridItemBinding binding;
        GridViewHolder(GridItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}