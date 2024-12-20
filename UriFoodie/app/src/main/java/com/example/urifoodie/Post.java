package com.example.urifoodie;


import com.google.firebase.Timestamp;

public class Post {
    private String username;
    private String text;
    private String imageUrl;
    private Timestamp timestamp;


    public Post() {}

    public Post(String username, String text, String imageUrl, Timestamp timestamp) {
        this.username = username;
        this.text = text;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
