package com.example.urifoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final ArrayList<String> imageUrls;

    public ImageAdapter(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .centerCrop()  // Center crop to keep square shape
                .into(holder.imageView);

        // Set equal size for all images based on screen width
        int screenWidth = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        int imageSize = screenWidth / 3; // Divide screen width by 3 for 3 images per row
        holder.imageView.getLayoutParams().width = imageSize;
        holder.imageView.getLayoutParams().height = imageSize;
    }




    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.galleryImage);
        }
    }
}
