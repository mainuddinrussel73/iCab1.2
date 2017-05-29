package com.example.mainuddin.icab12;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by mainuddin on 5/27/2017.
 */

public class backgroundWork extends AsyncTask<String,String,String>{
    Context context;
    AlertDialog alertDialog;
    String result = "";
    String line = "";

    backgroundWork(Context cxt){
        context = cxt;

    }
    @Override
    protected String doInBackground(String... params) {
        String form = params[0];

        return null;
    }

    public String return_string(){

        return result;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Find");
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
