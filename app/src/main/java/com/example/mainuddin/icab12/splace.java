package com.example.mainuddin.icab12;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

//import static com.example.mainuddin.icab12.R.id.imageView;

public class splace extends AppCompatActivity {
    private Toolbar toolbar;
    private Button  passenger;
    private Button Driver;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.one,R.drawable.two,R.drawable.three};
    private static final String[] strings= {"waste of time","Suitable for the disabbled","Free from bargainning"};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private ArrayList<String> strings1 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splace);
        init();

    }
    private void init() {
        for (int i = 0; i < XMEN.length; i++){
            XMENArray.add(XMEN[i]);
            strings1.add(strings[i]);
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(splace.this,XMENArray,strings1));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.action_settings){
            Toast.makeText(this,"Hay you just hit"+item.getTitle(),Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id==R.id.navigate){

        }
        return super.onOptionsItemSelected(item);
    }
    public void onCreateDriverHome(View view){

        startActivity(new Intent(this,LoginActivity.class));

    }

    public void onCreatePassengerHome(View view){

        startActivity(new Intent(this,LoginActivity.class));
    }

}
