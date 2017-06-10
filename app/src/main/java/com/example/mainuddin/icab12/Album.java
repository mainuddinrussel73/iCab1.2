package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/29/2017.
 */

public class Album {
    private String destination;
    private String rating;
    private String comments;
    private int thumbnail;

    public Album() {

    }

    public Album(String destination, String rating,String comments, int thumbnail) {
        this.destination = destination;
        this.rating = rating;
        this.comments = comments;
        this.thumbnail = thumbnail;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
