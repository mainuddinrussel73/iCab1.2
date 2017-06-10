package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/22/2017.
 */

public class driver implements user{
    String name;
    String type;
    String latitute;
    String longitute;

        driver(String latitute,String longitute,String type,String name){
            this.name = name;
            this.type = type;
            this.latitute = latitute;
            this.longitute = longitute;
        }
        driver(){

        }
    public String getNames() {
        return name;
    }

    public void setNames(String name) {
        this.name = name;
    }

    public String getTrips() {
        return type;
    }

    public void setTrips(String trips) {
        this.type = trips;
    }

    public String getComment() {
        return longitute;
    }

    public void setComment(String comment) {
        this.longitute = comment;
    }

    public String getRating() {
        return latitute;
    }

    public void setRating(String rating) {
        this.latitute = rating;
    }


}
