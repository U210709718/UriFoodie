package com.example.urifoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListRecyclerViewAdapter extends RecyclerView.Adapter<PostListRecyclerViewAdapter.PostViewHolder> {

    private final List<Post> postList;

    public PostListRecyclerViewAdapter(List<Post> postList) {
        this.postList = postList;
    }


    @Override
    public PostViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.postTextView.setText(post.getText());

        // Load the user profile picture
        if (post.getUserProfilePicUrl() != null && !post.getUserProfilePicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getUserProfilePicUrl())
                    .into(holder.userProfilePic);
        } else {
            // Optionally set a default image or clear the old image
            holder.userProfilePic.setImageResource(R.drawable.ic_userpic_foreground); // default or placeholder image
        }

        String imageUrl = post.getImageUrl();
        Picasso.get()
                .load(imageUrl)
                .into(holder.postImageView);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView postTextView;
        ImageView userProfilePic;
        ImageView postImageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            postImageView = itemView.findViewById(R.id.postImageView);
        }
    }
}
