package com.example.mainuddin.icab12;

import android.app.Activity;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ConfirmActivity extends Fragment implements OnMapReadyCallback{
    GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    String username;
    String curruser;
    double latitute;
    double longitute;
    private static final String find_url = "jdbc:mysql://192.168.1.5.192:3306/user_details";
    private  static  final String user = "test";
    private  static final String pass = "test123";
    List<Navigation_bar.userInfo> userInfos = new ArrayList<>();
    public ConfirmActivity(double latitute ,double longitute,List<Navigation_bar.userInfo> list,String username,String curr){
        this.latitute = latitute;
        this.longitute = longitute;
        this.userInfos = list;
            this.username  = username;
            this.curruser = curr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
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



    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;

        //LatLng latLng = new LatLng(latitude,longitude);
        double latitudes[] = new double[10];

        double longitudes[] = new double[10];

        String names[] = new String[10];

        int f =0;
        int j=0;
        for(Navigation_bar.userInfo u : userInfos){
            String s = u.latitute;
            Double d = Double.parseDouble(s);
            String s1 = u.longitute;
            Double d1 = Double.parseDouble(s1);
            latitudes[j] = d.doubleValue();
            longitudes[j] = d1.doubleValue();
            names[j] = u.name;
            System.out.println(u.name);
            if(u.name== username){
                Toast.makeText(getContext(),username,Toast.LENGTH_LONG).show();
            }
            j++;
        }
        //String result = backgroundWork.return_string();
        //System.out.println(result);
        for (int i = 0; i < j; i++) {

            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes[i], longitudes[i])).title(names[i]);
            // changing marker color
            //Toast.makeText(getContext(),username,Toast.LENGTH_LONG).show();
            if (names[i].equals(username)) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            else marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            map.addMarker(marker);

            // Move the camera to last position with a zoom level
            if (names[i].equals(username)) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitute,longitute)).zoom(12).build();

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }




    }

}
