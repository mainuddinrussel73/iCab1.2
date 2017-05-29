package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/22/2017.
 */

public class passenger implements user {

    private String name;
    private String trips;
    private String comment;
    private String rating;

    passenger(){


    }
    passenger(String name,String trips, String comment ,String rating){
        this.name = name;
        this.trips = trips;
        this.comment = comment;
        this.rating = rating;
    }
    public String getNames() {
        return name;
    }

    public void setNames(String name) {
        this.name = name;
    }

    public String getTrips() {
        return trips;
    }

    public void setTrips(String trips) {
        this.trips = trips;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }




}
