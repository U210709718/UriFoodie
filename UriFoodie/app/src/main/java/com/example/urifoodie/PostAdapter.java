package com.example.urifoodie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.urifoodie.Post;
import com.example.urifoodie.R;

import java.util.List;
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.postTextView.setText(post.getPostText());

        // Load the user profile picture
        if (post.getUserProfilePicUrl() != null && !post.getUserProfilePicUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getUserProfilePicUrl())
                    .into(holder.userProfilePic);
        } else {
            // Optionally set a default image or clear the old image
            holder.userProfilePic.setImageResource(R.drawable.ic_userpic_foreground); // default or placeholder image
        }

        // Load the post image from Base64 string
        if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
            byte[] decodedString = Base64.decode(post.getImageBase64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageView.setImageBitmap(decodedByte); // Make sure this ImageView is for the post image
        } else {
            // Optionally clear or set a default image if there's no post image
            holder.imageView.setImageDrawable(null);
        }
    }




    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView postTextView;
        ImageView userProfilePic;
        ImageView imageView;  // ImageView for the post image

        public PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            imageView = itemView.findViewById(R.id.postImageView);  // Make sure this ID matches your layout
        }
    }
}