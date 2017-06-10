package com.example.mainuddin.icab12;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
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
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
    static double to_lat;
    static double to_long;
    double select_lat;
    double select_long;
    String drivername = " ";
    String s3;
    String s4;
    String stringLatitude;
    String stringLongitude;
    boolean isSelected = false;
    //ArrayList<passenger> passengers;
    private static final String find_url = "jdbc:mysql://192.168.1.6:3306/user_details";
    private  static  final String user = "test";
    private  static final String pass = "test123";
    private String country;
    private String city;
    private String postalCode;
    private String addressLine;
    private Stack<locateDriver> details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GPSTracker gps = new GPSTracker(getBaseContext());
        latitude = gps.getLatitude();
        s3 = Double.toString(latitude);
        longitude = gps.getLongitude();
        s4 = Double.toString(longitude);




        //Toast.makeText(getBaseContext(),stringLatitude+"\n"+stringLongitude+"\n"+country+"\n"+city+"\n"+addressLine,Toast.LENGTH_LONG).show();
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
        //searchView.setVisibilit;y(View.INVISIBLE);
        f.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                upDateLocation(username,s3,s4);
                final List<userInfo> locations = testDB();
                CoordinatorLayout mainLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
                view = getLayoutInflater().inflate(R.layout.search_layout, mainLayout, false);
                View view1;

                //searchView.setVisibility(View.VISIBLE
                final ConfirmActivity mapsActivity = new ConfirmActivity(isSelected,latitude,longitude,latitude,longitude,locations,username,curruser);
                final FragmentManager manager = getSupportFragmentManager();
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
                    final SearchView searchView = (SearchView) findViewById(R.id.search);
                    //searchView = (SearchView) searchView.getActionView();
                    searchView.setQueryHint("Search View Hint");
                    final String s = new String();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                           // Log.i("well", " this worked");
                            s.concat(query);

                            Toast.makeText(getBaseContext(), query,
                                    Toast.LENGTH_SHORT).show();
                            searchView.clearFocus();
                            getLatLongFromGivenAddress(query);
                            //mapsActivity.to_long = to_long;
                            //mapsActivity.to_lat = to_lat;
                            //isSelected = true;
                            final ConfirmActivity mapsActivity = new ConfirmActivity(isSelected,latitude,longitude,to_lat,to_long,locations,username,curruser);
                            final FragmentManager manager = getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.map_layout,mapsActivity).commit();
                            showAlertbox("Confirm to Start The Journey");
                            f.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(isSelected==true) {
                                        final ConfirmActivity mapsActivity1 = new ConfirmActivity(isSelected,select_lat,select_long, latitude, longitude, to_lat, to_long, locations, username, curruser);
                                        final FragmentManager manager1 = getSupportFragmentManager();
                                        manager1.beginTransaction().replace(R.id.map_layout, mapsActivity1).commit();
                                        Navication_bar2.isSelected = true;
                                        Navication_bar2.to_lat = latitude;
                                        Navication_bar2.to_long = longitude;
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        try {
                                            Class.forName("com.mysql.jdbc.Driver");
                                            Connection connection = DriverManager.getConnection(find_url,user,pass);
                                            String result = "Database connection is successful";
                                            Statement statement = connection.createStatement();
                                            int resultSet = statement.executeUpdate(" UPDATE `locations` SET `isSelect`= \"TRUE\",`to_lat` = "+ latitude +", `to_long` = "+longitude +"WHERE `name` = \""+ drivername +"\"");

                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        final Dialog dialog1 = new Dialog(Navigation_bar.this);
                                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog1.setContentView(R.layout.popupbar);
                                        dialog1.setCanceledOnTouchOutside(false);
                                        TextView alertbox_title1 = (TextView) dialog1
                                                .findViewById(R.id.alertbox_title);
                                        alertbox_title1.setText("IS JOURNEY STARTED");
                                        Button yes = (Button) dialog1.findViewById(R.id.alertbox_yes);
                                        Button no = (Button) dialog1.findViewById(R.id.alertbox_no);
                                        yes.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog1.dismiss();
                                                final Dialog dialog2 = new Dialog(Navigation_bar.this);
                                                dialog2.setContentView(R.layout.popupnobutton);
                                                dialog2.setCanceledOnTouchOutside(true);
                                                TextView alertbox_title2 = (TextView) dialog2
                                                        .findViewById(R.id.alertbox_title1);
                                                alertbox_title2.setText("Have a safe journey <^_^>");

                                                dialog2.show();

                                            }
                                             //dialog1.dismiss();
                                        });


                                        no.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog1.dismiss();
                                                dialog1.show();
                                            }
                                        });
                                        dialog1.show();
                                    }
                                }
                            });

                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            return false;
                        }
                    });

                    //editText.setText();

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
    public void showAlertbox(String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popupbar);
        dialog.setCanceledOnTouchOutside(false);
        TextView alertbox_title = (TextView) dialog
                .findViewById(R.id.alertbox_title);
        alertbox_title.setText(title);

        Button yes = (Button) dialog.findViewById(R.id.alertbox_yes);
        Button no = (Button) dialog.findViewById(R.id.alertbox_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notif;
                isSelected = true;
                selectDriverFromServerSide();
                notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    notify = new Notification.Builder
                            (getBaseContext()).setContentTitle("Driver is Selected").setContentText("Have a safe journey ^_^").
                            setContentTitle("Driver Selected : "+drivername).setSmallIcon(R.mipmap.ic_launcher).build();
                    //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    final Dialog dialog1 = new Dialog(Navigation_bar.this);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.popupnobutton);
                    dialog1.setCanceledOnTouchOutside(true);
                    TextView alertbox_title1 = (TextView) dialog1
                            .findViewById(R.id.alertbox_title1);
                    alertbox_title1.setText("Driver :"+drivername+"has been selected\n" +"Please press the arrow button on the right to see \n the location of the selected driver");
                    dialog1.show();
                }

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                isSelected = false;
            }
        });

        dialog.show();
    }
    class locateDriver{
        double lat;
        double lon;
        String dName;
        double distance;
        locateDriver(double d,double e,double dis,String n){
            lat = d;
            lon = e;
            distance = dis;
            dName = n;
        }
    }
    public void selectDriverFromServerSide(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(find_url,user,pass);
            String result = "Database connection is successful";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `latitute`,`longitute`,`driver_details`.`name` from `locations` ,`driver_details`  WHERE `locations`.`name`= `driver_details`.`name` AND `locations`.`isSelect` <> \"TRUE\"");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //Integer i = new Integer(0);
            //locate = new HashMap<>();
            details = new Stack<>();
            while(resultSet.next()){
                select_lat = Double.parseDouble(resultSet.getString(1));
                select_long = Double.parseDouble(resultSet.getString(2));
                drivername = resultSet.getString(3);
                LatLng drivers = new LatLng(select_lat,select_long);
                LatLng passengers = new LatLng(latitude,longitude);
                double distance = CalculationByDistance(drivers,passengers);
                //details.add(new Pair<Double, String>(distance,drivername));
                //isSelected = true;
                //details.add(distance);
                //locate.put(i,drivers);
                //i++;
               details.push(new locateDriver(select_lat,select_long,distance,drivername));
            }
            class MyComparator implements Comparator<locateDriver> {
                @Override
                public int compare(locateDriver o1, locateDriver o2) {
                    if (o1.distance > o2.distance) {
                        return 1;
                    } else if (o1.distance < o2.distance) {
                        return -1;
                    }
                    return 0;
                }}

            Collections.sort(details, new MyComparator());
            for(locateDriver l : details){
                System.out.println(l.distance+","+l.dName);
            }
            select_lat = details.get(0).lat;
            select_long = details.get(0).lon;
            drivername = details.get(0).dName;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
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
        }else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

