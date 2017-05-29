package com.example.mainuddin.icab12;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Navigation_bar extends AppCompatActivity
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
    String s3;
    String s4;
    //ArrayList<passenger> passengers;
    private static final String find_url = "jdbc:mysql://192.168.1.5:3306/user_details";
    private  static  final String user = "test";
    private  static final String pass = "test123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GPSTracker gps = new GPSTracker(getBaseContext());
        latitude = gps.getLatitude();
        s3 = Double.toString(latitude);
        longitude = gps.getLongitude();
        s4 = Double.toString(longitude);
        Toast.makeText(getBaseContext(),s3+s4,Toast.LENGTH_LONG).show();
        //currUser(username,latitude,longitude);
        curruser = username;


        Bundle bundle = getIntent().getExtras();
        total = bundle.getString("list");
        username = bundle.getString("name");
        email = bundle.getString("email");

        //passengers = (ArrayList<passenger>) getIntent().getSerializableExtra("list");

        //System.out.println(username);
        //Toast.makeText(getBaseContext(),passengers.get(0).getNames(),Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_navigation_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //findViewById(R.id.ham_button_example).setOnClickListener(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton f = (FloatingActionButton) findViewById(R.id.fab1);
        f.setBackgroundTintList(getResources().getColorStateList(R.color.colorPink));
        //searchView.setVisibility(View.INVISIBLE);
        f.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                upDateLocation(username,s3,s4);
                List<userInfo> locations = testDB();
                CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
                view = getLayoutInflater().inflate(R.layout.search_layout, mainLayout, false);
                View view1;

                //searchView.setVisibility(View.VISIBLE
                ConfirmActivity mapsActivity = new ConfirmActivity(latitude,longitude,locations,username,curruser);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.map_layout,mapsActivity).commit();
                if (isClicked == false) {
                    isClicked = true;

                    f.setVisibility(View.VISIBLE);

                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorFloat));
                    fab.setImageResource(R.drawable.ic_action_close);
                    view1 = mainLayout.findViewById(R.id.map_layout);
                    view.setVisibility(View.VISIBLE);
                    mainLayout.addView(view);
                    view1.setVisibility(View.VISIBLE);
                }
                else {
                    fab.setImageResource(R.drawable.ic_menu_search);
                    isClicked = false;

                    f.setVisibility(View.INVISIBLE);
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
                    view = mainLayout.findViewById(R.id.layout_search);
                    mainLayout.removeView(view);
                    view1 = mainLayout.findViewById(R.id.map_layout);
                    view1.setVisibility(View.INVISIBLE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
                alertDialog.setMessage("NULL ");
                alertDialog.show();
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
    public List<userInfo> testDB(){
        List<userInfo>  userInfos = new ArrayList<>();
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
                userInfos.add(i,new userInfo(resultSet.getString(1) ,resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
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
        textView1 = (TextView) findViewById(R.id.name);
        TextView textView2 = (TextView) findViewById(R.id.email_user);
        //System.out.println(passengers.get(0).getNames());
        textView1.setText(username);
        textView2.setText(email);
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
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
            //startActivity(new Intent(this,splace.class));
            NavUtils.navigateUpFromSameTask(this);
            //NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        else if(id==R.id.drawbar_layout){


        }
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
            startActivity(intent);
        }else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

