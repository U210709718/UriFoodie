package com.example.urifoodie;


import com.google.firebase.Timestamp;

public class Post {
    private String username;
    private String postText;
    private String recipeText;
    private String imageUrl;
    private Timestamp timestamp;
    private String userProfilePicUrl;


    public Post() {}

    // New constructor for fewer parameters
    public Post (String username, String postText, String recipeText, Timestamp timestamp) {
        this.username = username;
        this.postText = postText;
        this.recipeText = recipeText;
//        this.imageUrl = imageUrl;
        this.timestamp = Timestamp.now(); // Automatically add a timestamp
    }

    // Existing full constructor
//    public Post(String username, String postText, String recipeText, String imageUrl, Timestamp timestamp, String userProfilePicUrl) {
//        this.username = username;
//        this.postText = postText;
//        this.recipeText = recipeText;
//        this.imageUrl = imageUrl;
//        this.timestamp = timestamp;
//        this.userProfilePicUrl = userProfilePicUrl;
//    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getRecipeText() {
        return recipeText;
    }

    public void setRecipeText(String recipeText) {
        this.recipeText = recipeText;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }

//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

//    public String getUserProfilePicUrl() {
//        return userProfilePicUrl;
//    }
//
//    public void setUserProfilePicUrl(String userProfilePicUrl) {
//        this.userProfilePicUrl = userProfilePicUrl;
//    }
}
