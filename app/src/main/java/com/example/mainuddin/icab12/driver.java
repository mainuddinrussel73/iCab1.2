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


}
