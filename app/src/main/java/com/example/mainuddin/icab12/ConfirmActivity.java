package com.example.mainuddin.icab12;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.util.Log;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import android.app.Notification;
import android.app.NotificationManager;
import static java.util.Locale.getDefault;


public class ConfirmActivity extends Fragment implements OnMapReadyCallback{
    GoogleMap map;
    GoogleApiClient mGoogleApiClient;
    String username;
    String curruser;
    double latitute;
    double longitute;
    double to_lat;
    double to_long;
    double select_lat;
    double select_long;
    boolean isSelected = false;
    boolean getSelected = false;
    String cityname;
    ArrayList<Pair<String,String>> locatiosOftheDrivers = new ArrayList<>();
    private static final String find_url = "jdbc:mysql://192.168.1.6:3306/user_details";
    private static  final String user = "test";
    private static final String pass = "test123";
    List<Navigation_bar.userInfo> userInfos = new ArrayList<>();
    public ConfirmActivity(boolean option , double latitute ,double longitute,double to_lat,double to_long,List<Navigation_bar.userInfo> list,String username,String curr){
        this.latitute = latitute;
        this.longitute = longitute;
        this.userInfos = list;
            this.username  = username;
            this.curruser = curr;
        this.to_lat = to_lat;
        this.to_long = to_long;
        this.getSelected = option;
    }
    public ConfirmActivity(boolean option ,double select_lat,double select_long, double latitute ,double longitute,double to_lat,double to_long,List<Navigation_bar.userInfo> list,String username,String curr){
        this.latitute = latitute;
        this.longitute = longitute;
        this.userInfos = list;
        this.username  = username;
        this.curruser = curr;
        this.to_lat = to_lat;
        this.to_long = to_long;
        this.getSelected = option;
        this.select_lat = select_lat;
        this.select_long = select_long;
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

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        Locale locale = Locale.getDefault();
        try {
            Geocoder geocoder = new Geocoder(getContext(),locale);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName()).append("\n");
                result.append(address.getAddressLine(0));
                cityname = address.getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
    public void findDrivers(String sername){
        //List<Navigation_bar.userInfo>  userInfos = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `latitute`, `longitute` FROM `locations` WHERE `name` <> " + "\"" + sername + "\"" + " and `type` <> " + "\"" + "passenger" + "\"");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while(resultSet.next()){
                result+=resultSetMetaData.getColumnName(1) + ":" + resultSet.getString(1) + "\n";
                result+=resultSetMetaData.getColumnName(2) + ":" + resultSet.getString(2) + "\n";
                Log.d("locations names : ",resultSet.getString(1)+":"+resultSet.getString(2));
                locatiosOftheDrivers.add(new Pair<String, String>(resultSet.getString(1),resultSet.getString(2)));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap){
        map = googleMap;
        String address = getAddress(latitute,longitute);
        //Toast.makeText(getContext(),address,Toast.LENGTH_LONG);
        //System.out.println("////////////........" + address+".....///////////////////");
        LatLng origin = new LatLng(latitute,longitute);
        LatLng dest = new LatLng(to_lat,to_long);
        findDrivers(username);
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        if(getSelected==true){
            LatLng origin1 = new LatLng(latitute,longitute);
            LatLng dest1 = new LatLng(select_lat,select_long);
            //findDrivers(username);
            String url1 = getDirectionsUrl(origin1, dest1);

            DownloadTask downloadTask1 = new DownloadTask();
            downloadTask1.execute(url1);
        }
        // Start downloading json data from Google Directions API

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
        MarkerOptions marker1 = new MarkerOptions().position(new LatLng(to_lat,to_long));
        marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        map.addMarker(marker1);
        for (int i = 0; i < j; i++) {

            // Adding a marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudes[i], longitudes[i])).title(names[i]);
            // changing marker color
            //Toast.makeText(getContext(),username,Toast.LENGTH_LONG).show();
            if (names[i].equals(username)) {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.title(names[i]+"Place : "+cityname);
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
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
                //Toast.makeText(getContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
            //Toast.makeText(getContext(),"Distance:"+distance + ", Duration:"+duration,Toast.LENGTH_LONG).show();

            // Drawing polyline in the Google Map for the i-th route
            map.addPolyline(lineOptions);
        }
    }

}
