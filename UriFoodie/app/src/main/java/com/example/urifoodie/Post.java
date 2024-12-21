package com.example.urifoodie;


public class Post {
    private String userId;
    private String username; // Display name of the user
    private String postText;
    private String imageUrl;
    private String userProfilePicUrl; // URL to user's profile picture
    private String imageBase64; // Base64 string for the image


    // Default constructor for Firebase
    public Post() {}

    // Constructor
    /*public Post(String userId, String username, String postText) {
        this.userId = userId;
        this.username = username;
        this.postText = postText;
    }*/
//    public Post(String userId, String username, String postText, String userProfilePicUrl) {
//        this.userId = userId;
//        this.username = username;
//        this.postText = postText;
//        this.userProfilePicUrl = userProfilePicUrl;
//    }

    public Post(String userId, String username, String postText, String imageBase64) {
        this.userId = userId;
        this.username = username;
        this.postText = postText;
        this.imageBase64 = imageBase64;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPostText() {
        return postText;
    }
    public String getUserProfilePicUrl() {
        return userProfilePicUrl;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
    public void setUserProfilePicUrl(String userProfilePicUrl) {
        this.userProfilePicUrl = userProfilePicUrl;
    }
    public String getImageBase64() {
        return imageBase64;
    }
    public void setImageBase64(String imageBase64 ) {
        this.imageBase64 = imageBase64;
    }
}