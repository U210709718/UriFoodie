package com.example.urifoodie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.postTextView.setText(post.getPostText());
    }

//    @Override
//    public void onBindViewHolder(@NonNull PostListRecyclerViewAdapter.PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//        holder.usernameTextView.setText(post.getUsername());
//        holder.postTextView.setText(post.getPostText());
//
//        // Load the user profile picture
//        if (post.getUserProfilePicUrl() != null && !post.getUserProfilePicUrl().isEmpty()) {
//            Glide.with(holder.itemView.getContext())
//                    .load(post.getUserProfilePicUrl())
//                    .into(holder.userProfilePic);
//        } else {
//            // Optionally set a default image or clear the old image
//            holder.userProfilePic.setImageResource(R.drawable.ic_userpic_foreground); // default or placeholder image
//        }
////
////        String imageUrl = post.getImageUrl();
////        Picasso.get()
////                .load(imageUrl)
////                .into(holder.postImageView);
//    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView postTextView;
        Button recipeButton;
//        ImageView userProfilePic;
//        ImageView imageView;  // ImageView for the post image

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            recipeButton = itemView.findViewById(R.id.recipeButton);
//            userProfilePic = itemView.findViewById(R.id.userProfilePic);
//            imageView = itemView.findViewById(R.id.postImageView);  // Make sure this ID matches your layout
        }
    }
}