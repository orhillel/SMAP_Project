package com.smap.reporter;


import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * Created by user on 01/01/2019.
 */

public class DataSender implements SendToAzure.AsyncResponse {
    public static final String urlReport = "addReport?code=bFpx4HXecwkh9yme0LhKChM2nSv2S27Ivpk0Y3TGTVaxGRpRc10zkw==";
    public static final String urlReportUser = "addUser?code=wckEGxTgDQiyWeEIZ1ab1Jam5I4ow0Tial80QfP58JYSo/QNy9m0OA==";
    public static final String urlGetUser = "getUser?code=ZUe9RumQiN7ur6an/gvZwKeN9p0z1HssUCaHqO4jZgTrs6aUGvgPxQ==";

    Timer timer;
    TimerTask timerTask;
    AppCompatActivity activity;
    BluetoothDevice myDevice = null;
    SendToAzure send;
    InputStream mmInputStream = null;
    LocationManager locationManager ;
    double a = 1 ;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public int emailExist = 0;

    public DataSender(AppCompatActivity activity) {
        this.activity = activity;
        startTimer();
    }

    public DataSender(AppCompatActivity activity,String stop) {
        this.activity = activity;
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//        timer.schedule(timerTask, 5000, 1800000); //
        timer.schedule(timerTask, 5000, 1000);
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            sendToAzure();
                        } catch (JSONException e) {

                        }

                    }
                });
            }
        };
    }

    public void sendToAzure() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        TextView v_co = (TextView) activity.findViewById(R.id.CO);
        TextView v_so2 = (TextView) activity.findViewById(R.id.SO_2);
        TextView v_o3 = (TextView) activity.findViewById(R.id.O_3);
        TextView v_no2 = (TextView) activity.findViewById(R.id.NO_2);
        TextView v_PM25 = (TextView) activity.findViewById(R.id.PM_2_5);
        TextView v_PM10 = (TextView) activity.findViewById(R.id.PM_10);
        TextView v_AQI = (TextView) activity.findViewById(R.id.valueAQI);
        SimpleDateFormat s = new SimpleDateFormat("dd.MM.yyyy hh-mm-ss");
        String currTime = s.format(new Date());

        Location location= getGpsData();
       if(location==null){
           location = new Location(LocationManager.NETWORK_PROVIDER);

       }
        jsonObject.put("CO",v_co.getText());
        jsonObject.put("SO2",v_so2.getText());
        jsonObject.put("O3",v_o3.getText());
        jsonObject.put("NO2",v_no2.getText());
        jsonObject.put("PM25",v_PM25.getText());
        jsonObject.put("PM10",v_PM10.getText());
        jsonObject.put("time",currTime);
        jsonObject.put("altitude",location.getLatitude());
        jsonObject.put("longitude",location.getLongitude());

        jsonObject.put("AQI",v_AQI.getText());
        TextView gpsLocation =  activity.findViewById(R.id.measureLocation);
        gpsLocation.setText(location.getLatitude() + "N/" + location.getLongitude() + "E");

        new SendToAzure(DataSender.this,jsonObject,urlReport).execute();
    }

    private Location getGpsData(){
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
        }else {
            return  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }
    @Override
    public void backFromCheck(String output) {
    }

    @Override
    public void processFinish(String output){

    }

}
