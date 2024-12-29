package com.example.urifoodie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.example.urifoodie.Post;
import com.example.urifoodie.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.PostViewHolder> {
    private List<Post> postList;

    public PostListRecyclerViewAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }


    //UPDATE METHOD HERE TO DISPLAY THE PICTURE TAKED BY THE CAMERA!!!!!!!
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set text fields
        holder.usernameTextView.setText(post.getUsername());
        holder.postTextView.setText(post.getPostText());

        // Load the image using Glide
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getImageUrl())
                    .placeholder(android.R.color.darker_gray) // Replace with built-in gray background
                    .error(android.R.drawable.ic_dialog_alert) // Replace with built-in alert icon
                    .into(holder.postImageView);
        } else {
            // Set a default image if no URL is available
            holder.postImageView.setImageResource(android.R.drawable.ic_dialog_alert); // Default alert icon

        }
    }


// OLD CODE !! update onBindViewHolder to display the picture taken by the camera!
//    @Override
//    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//        holder.usernameTextView.setText(post.getUsername());
//        holder.postTextView.setText(post.getPostText());
//    }

//    @Override
//    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//        holder.usernameTextView.setText(post.getUsername());
//        holder.postTextView.setText(post.getPostText());
//    }

    /* OLD METHOD VERSION
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        //set the username and text
        holder.usernameTextView.setText(post.getUsername());
        holder.postTextView.setText(post.getPostText());

        // Load the user profile picture
        if (post.getUserProfilePicUrl() != null && !post.getUserProfilePicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getUserProfilePicUrl())
                    .placeholder(R.drawable.ic_userpic_foreground)
                    .into(holder.userProfilePic);
        } else {
            // Optionally set a default image or clear the old image
            holder.userProfilePic.setImageResource(R.drawable.ic_userpic_foreground); // default or placeholder image
        }

        String imageUrl = post.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_userpic_foreground) // Placeholder for post images
                    //.error(R.drawable.ic_error_image) // Error image
                    .into(holder.postImageView);
        } else {
            holder.postImageView.setImageResource(R.drawable.ic_userpic_foreground); // Default image
        }

        // Handle recipe button click (if required)
        holder.recipeButton.setOnClickListener(v -> {
            // Add your recipe display logic here
        });

    }*/



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView postTextView;
        ImageView postImageView; //added ! for displaying post image
        Button recipeButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            recipeButton = itemView.findViewById(R.id.recipeButton);
            postImageView = itemView.findViewById(R.id.postImageView);

        }
    }
}