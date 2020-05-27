package com.example.instaimage.Objects;

public class Comment {
    private String name;
    private String image;
    private String commentText;



    public Comment() {
    }


    public Comment(String commentText) {
        this.commentText = commentText;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
