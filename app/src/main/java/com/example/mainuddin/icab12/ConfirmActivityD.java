package com.example.mainuddin.icab12;

/**
 * Created by mainuddin on 5/26/2017.
 */

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ConfirmActivityD extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    List<driver> drivers = new ArrayList<>();
    private static final String find_url = "jdbc:mysql://192.168.1.5:3306/user_details";
    private  static  final String user = "test";
    private  static final String pass = "test123";
    public ConfirmActivityD(List<driver> drivers1){
        this.drivers = drivers1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_confirm);
        View view =  inflater.inflate(R.layout.activity_confirm,container,false);


        return view;
    }
    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map2);
        /*try {
            if (this == null) {
                //((MapView) getView().findViewById(R.id.map)).getMapAsync(this);
                mapFragment.getMapAsync(this.newInstance());
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        } */

        mapFragment.getMapAsync(this);
    }
    public void addDriver(String name,String type ,double latitute,double longitute){
       // List<driver> drivers = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            int resultSet = statement.executeUpdate("INSERT INTO `locations`(`name`, `type`, `latitute`, `longitute`) VALUES  ( + " + "\"" +name +"\""+" , "+ "\""+type +"\"" +"," + latitute + "," + longitute +" )");
           // ResultSetMetaData resultSetMetaData = resultSet.getMetaData();



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkDriver(String name){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM `locations` WHERE name = "+"\""+name+"\"");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            if(resultSet.next()){
                result+=resultSetMetaData.getColumnName(1) + ":" + resultSet.getString(1) + "\n";
                System.out.println(result);
                return true;}
            //else return false;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
    public void updateDriver(String name,String type ,double latitute,double longitute){
        // List<driver> drivers = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            int resultSet = statement.executeUpdate("INSERT INTO `locations`(`name`, `type`, `latitute`, `longitute`) VALUES  ( + " + "\"" +name +"\""+" , "+ "\""+type +"\"" +"," + latitute + "," + longitute +" )");
            // ResultSetMetaData resultSetMetaData = resultSet.getMetaData();



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        GPSTracker gps = new GPSTracker(getContext());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        //LatLng latLng = new LatLng(latitude,longitude);
        double latitudes[] = new double[10];

        double longitudes[] = new double[10];

        String names[] = new String[10];

        names[0] = "Baki-al-bahar";
        latitudes[0] = latitude;
        longitudes[0] = longitude;
        if(checkDriver(names[0])==false){
            addDriver(names[0], "driver", latitudes[0] + .0056, longitudes[0]);
        }else{
            System.out.println("Duplicate key");
        }
        int j=1;
        for(driver u : drivers){
            String s = u.latitute;
            Double d = Double.parseDouble(s);
            String s1 = u.longitute;
            Double d1 = Double.parseDouble(s1);
            latitudes[j] = d.doubleValue();
            longitudes[j] = d1.doubleValue();
            names[j] = u.name;
            j++;
        }
        //String result = backgroundWork.return_string();
        //System.out.println(result);
        for (int i = 0; i < j; i++) {

            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes[i], longitudes[i])).title(names[i]);
            // changing marker color
            if (i == 0) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            else marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            map.addMarker(marker);

            // Move the camera to last position with a zoom level
            if (i == 0) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude)).zoom(12).build();

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }



    }

}

