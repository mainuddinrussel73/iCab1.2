package com.example.mainuddin.icab12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Navication_bar2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean isClicked = false;

    AlertDialog alertDialog;
    TextView textView1;
    String username ;
    String email;
    String total  ;
    String curruser;
    double latitude;
    double longitude;
    static double to_lat;
    static double to_long;
    String s3;
    String s4;

    private static final String find_url = "jdbc:mysql://192.168.1.6:3306/user_details";
    private  static  final String user = "test";
    private  static final String pass = "test123";

    private String country;
    private String city;
    private String postalCode;
    private String addressLine;
    static  boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navication_bar2);

        GPSTracker gps = new GPSTracker(getBaseContext());
        latitude = gps.getLatitude();
        s3 = Double.toString(latitude);
        longitude = gps.getLongitude();
        s4 = Double.toString(longitude);

        Bundle bundle = getIntent().getExtras();
        total = bundle.getString("list");
        username = bundle.getString("name");
        email = bundle.getString("email");

        curruser = username;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        final FloatingActionButton f = (FloatingActionButton) findViewById(R.id.fab2);
        f.setBackgroundTintList(getResources().getColorStateList(R.color.colorPink));
        //searchView.setVisibility(View.INVISIBLE);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Navication_bar2.userInfo> locations = testDB();
                CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.rootLayout1);
                // view = getLayoutInflater().inflate(R.layout.search_layout, mainLayout, false);
                View view1;
                //searchView.setVisibility(View.VISIBLE);
                ConfirmActivityD mapsActivity;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(find_url, user, pass);
                    String result = "Database connection is successful";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(" SELECT `isSelect`,`to_lat`,`to_long` FROM `locations` WHERE `name` = "+"\""+ username+"\"");
                    if(resultSet.next()) {
                        System.out.println(resultSet.getString(1));
                        if (resultSet.getString(1).equals("TRUE")) {
                            Double to_lat = Double.parseDouble(resultSet.getString(2));
                            Double to_long = Double.parseDouble(resultSet.getString(3));
                            mapsActivity = new ConfirmActivityD(latitude, longitude, to_lat, to_long, locations, username, curruser);
                            final Dialog dialog1 = new Dialog(Navication_bar2.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.popupnobutton);
                            dialog1.setCanceledOnTouchOutside(true);
                            TextView alertbox_title1 = (TextView) dialog1
                                    .findViewById(R.id.alertbox_title1);
                            alertbox_title1.setText("You are selected by a passenger go to the mapped location");
                            dialog1.show();
                            FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.map_layout1, mapsActivity).commit();
                            //isSelected = false;
                            //fab.setVisibility(View.INVISIBLE);
                            //toolbar.setVisibility(View.INVISIBLE);
                            //MenuItem menuItem = (MenuItem)findViewById(R.id.action_settings);

                        } else {
                            mapsActivity = new ConfirmActivityD(latitude, longitude, 23.7940, 90.4043, locations, username, curruser);

                            //fab.setVisibility(View.VISIBLE);
                            //toolbar.setVisibility(View.VISIBLE);
                            final Dialog dialog1 = new Dialog(Navication_bar2.this);
                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog1.setContentView(R.layout.popupnobutton);
                            dialog1.setCanceledOnTouchOutside(true);
                            TextView alertbox_title1 = (TextView) dialog1
                                    .findViewById(R.id.alertbox_title1);
                            alertbox_title1.setText("YOu are free now");
                            dialog1.show();
                            FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.map_layout1, mapsActivity).commit();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //List<driver> locations = searchUsers();
               // upDateLocation(username,s3,s4);
                final List<Navication_bar2.userInfo> locations = testDB();
                CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.rootLayout1);
               // view = getLayoutInflater().inflate(R.layout.search_layout, mainLayout, false);
                View view1;
                //searchView.setVisibility(View.VISIBLE);
                ConfirmActivityD mapsActivity ;
                mapsActivity = new ConfirmActivityD(latitude,longitude,23.7940,90.4043,locations,username,curruser);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.map_layout1,mapsActivity).commit();
                if (isClicked == false) {
                    isClicked = true;

                   // f.setVisibility(View.VISIBLE);

                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorFloat));
                    fab.setImageResource(R.drawable.ic_action_close);
                    view1 = mainLayout.findViewById(R.id.map_layout1);
                   // view.setVisibility(View.VISIBLE);
                    //mainLayout.addView(view);
                    view1.setVisibility(View.VISIBLE);

                }
                else {
                    fab.setImageResource(R.drawable.ic_menu_search);
                    isClicked = false;

                    //f.setVisibility(View.INVISIBLE);
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
                    //view = mainLayout.findViewById(R.id.layout_search);
                    //mainLayout.removeView(view);
                    view1 = mainLayout.findViewById(R.id.map_layout1);
                    view1.setVisibility(View.INVISIBLE);
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getPassengersLocation(double lat,double lon){



    }
    public List<driver> searchUsers(){
        List<driver>  drivers = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select latitute,longitute,type,name from locations");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int i =0;
            while(resultSet.next()){
                result+=resultSetMetaData.getColumnName(1) + ":" + resultSet.getString(1) + "\n";
                result+=resultSetMetaData.getColumnName(2) + ":" + resultSet.getString(2) + "\n";
                result+=resultSetMetaData.getColumnName(3) + ":" + resultSet.getString(3) + "\n";
                result+=resultSetMetaData.getColumnName(4) + ":" + resultSet.getString(4) + "\n";
                drivers.add(i,new driver(resultSet.getString(1) ,resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
                //System.out.println(locatios.get(i).first+",.,"+locatios.get(i).second);
                i++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drivers;
    }
    public static void getLatLongFromGivenAddress(String youraddress) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                youraddress + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            to_long = lng;
            double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            to_lat = lat;
            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void upDateLocation(String usern,String lati,String longi){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toast.makeText(getBaseContext(),"CALLED",Toast.LENGTH_LONG).show();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            if(lati==null || longi == null) {
                //alertDialog.setMessage("NULL ");
                //alertDialog.show();
            }
            int resultSet = statement.executeUpdate(" UPDATE `locations` SET `latitute` = " + lati  +" , `longitute` = "  + longi + " WHERE `name` = "+"\""+usern+"\"");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    class userInfo{
        userInfo(String latitute,String longitute,String type,String name){
            this.name = name;
            this.type = type;
            this.latitute = latitute;
            this.longitute = longitute;
        }
        userInfo(){

        }
        String name;
        String type;
        String latitute;
        String longitute;
    };
    public List<Navication_bar2.userInfo> testDB(){
        List<Navication_bar2.userInfo>  userInfos = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select latitute,longitute,type,name from locations");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int i =0;
            while(resultSet.next()){
                result+=resultSetMetaData.getColumnName(1) + ":" + resultSet.getString(1) + "\n";
                result+=resultSetMetaData.getColumnName(2) + ":" + resultSet.getString(2) + "\n";
                result+=resultSetMetaData.getColumnName(3) + ":" + resultSet.getString(3) + "\n";
                result+=resultSetMetaData.getColumnName(4) + ":" + resultSet.getString(4) + "\n";
                userInfos.add(i,new Navication_bar2.userInfo(resultSet.getString(1) ,resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
                //System.out.println(locatios.get(i).first+",.,"+locatios.get(i).second);
                i++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userInfos;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        textView1 = (TextView) findViewById(R.id.name1);
        TextView textView2 = (TextView) findViewById(R.id.email1);
        //System.out.println(passengers.get(0).getNames());
        textView1.setText(username);
        textView2.setText(email);
        getMenuInflater().inflate(R.menu.navication_bar2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                NavUtils.navigateUpFromSameTask(this);
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            }

        //else return true;
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(this,RecylerViewMain.class);
            intent.putExtra("list",total);
            intent.putExtra("name",username);
            intent.putExtra("email",email);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
