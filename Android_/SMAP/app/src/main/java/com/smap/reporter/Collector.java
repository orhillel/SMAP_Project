package com.smap.reporter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Collections;
import java.util.ArrayList;


public class Collector {
    private TimerTask timerTask;
    private AppCompatActivity activity;
    private BluetoothDevice myDevice = null;
    private InputStream mmInputStream = null;
    //we are going to use a handler to be able to run in our TimerTask
    private final Handler handler = new Handler();

    public class Contaminant{
        String type;
        double value;

        public Contaminant (String type , double value) {
            this.type = type;
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

    public Collector(AppCompatActivity activity) {
        this.activity = activity;
        startTimer();
    }

    private void startTimer() {
        //set a new Timer
        Timer timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 1000ms the TimerTask will run every 1000ms
        timer.schedule(timerTask, 1000, 1000); //
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        while( myDevice == null){
                            try {
                                initDevice();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        try {
                            Data data = getData();
                            if (data == null){
                                return;
                            }
                            updateValues(data);

                            TextView lastUpdate = activity.findViewById(R.id.lastUpdate);
                            Calendar calendar = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                                    new SimpleDateFormat("dd.MM.yyyy  hh-mm-ss");
                            final String curTime = simpleDateFormat.format(calendar.getTime());
                            lastUpdate.setText(curTime);

                            TextView AQI = activity.findViewById(R.id.valueAQI);
                            TextView AQIText = activity.findViewById(R.id.textAQI);
                            Contaminant con = calculateAPI();
                            AQI.setText(String.valueOf(100 - (int)(con.value)));
                            setColorAPI(AQIText , AQI,100 - (int)(con.value));

                            Location sLocation= getGpsData();
                            if(sLocation==null){
                                sLocation = new Location(LocationManager.NETWORK_PROVIDER);
                            }
                            TextView location = activity.findViewById(R.id.measureLocation);
                            //location.setText(sLocation.toString());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
    }
    private  void setColorAPI(TextView AQI, TextView AQINumber, double value)
    {   int Brown       = 0x9a6324;
        if(value >= 51 && value <= 100) {
            AQI.setTextColor(Color.GREEN);
            AQINumber.setTextColor(Color.GREEN);
        }
        else if(value >=0  && value <= 50) {
            AQI.setTextColor(Color.YELLOW);
            AQINumber.setTextColor(Color.YELLOW);
        }
        else if(value <=-1  && value >= -200) {
            AQI.setTextColor(Color.RED);
            AQINumber.setTextColor(Color.RED);
        }
        else if(value <=-201  && value >= -400){
            AQI.setTextColor(Brown);
            AQINumber.setTextColor(Brown);
        }



    }
    private double calculateAQI(double BPhi, double BPlow, double IChi, double IClow, double cp) {
        return (BPhi - BPlow)*((cp-IClow)/(IChi-IClow))+BPlow;
    }

    private Contaminant calculateAPI() {

        double BPhi, BPlow, IChi, IClow;
        ArrayList<Contaminant> ContaminantList = new ArrayList<Contaminant>();

        TextView viewCO = activity.findViewById(R.id.CO);
        TextView viewNO_2 = activity.findViewById(R.id.NO_2);
        TextView viewSO_2 = activity.findViewById(R.id.SO_2);
        TextView viewPM_10 = activity.findViewById(R.id.PM_10);
        TextView viewPM_2_5 = activity.findViewById(R.id.PM_2_5);
        TextView viewO_3 = activity.findViewById(R.id.O_3);

        double CpCO =      Double.parseDouble(viewCO.getText().toString());
        double CpNO_2 =    Double.parseDouble(viewNO_2.getText().toString());
        double CpSO_2 =    Double.parseDouble(viewSO_2.getText().toString());
        double CpPM_10 =   Double.parseDouble(viewPM_10.getText().toString());
        double CpPM_2_5 =  Double.parseDouble(viewPM_2_5.getText().toString());
        double CpO_3 =     Double.parseDouble(viewO_3.getText().toString());

        // CO
        if (CpCO < 27){
            BPhi = 49;
            BPlow = 0;
            IChi = 26;
            IClow = 0;
        } else if (CpCO < 52) {
            BPhi = 100;
            BPlow = 50;
            IChi = 51;
            IClow = 27;
        } else if (CpCO < 79) {
            BPhi = 200;
            BPlow = 101;
            IChi = 78;
            IClow = 52;
        } else if (CpCO < 105) {
            BPhi = 300;
            BPlow = 201;
            IChi = 104;
            IClow = 79;
        } else if (CpCO < 131) {
            BPhi = 400;
            BPlow = 301;
            IChi = 130;
            IClow = 105;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 156;
            IClow = 131;
        }
        double AQI_CO = calculateAQI(BPhi, BPlow, IChi, IClow, CpCO);

        // NO_2
        if (CpNO_2 < 54){
            BPhi = 49;
            BPlow = 0;
            IChi = 53;
            IClow = 0;
        } else if (CpNO_2 < 106) {
            BPhi = 100;
            BPlow = 50;
            IChi = 105;
            IClow = 54;
        } else if (CpNO_2 < 161) {
            BPhi = 200;
            BPlow = 101;
            IChi = 160;
            IClow = 106;
        } else if (CpNO_2 < 214) {
            BPhi = 300;
            BPlow = 201;
            IChi = 213;
            IClow = 161;
        } else if (CpNO_2 < 261) {
            BPhi = 400;
            BPlow = 301;
            IChi = 260;
            IClow = 214;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 316;
            IClow = 261;
        }
        double AQI_NO_2 = calculateAQI(BPhi, BPlow, IChi, IClow, CpNO_2);

        // SO_2
        if (CpSO_2 < 68){
            BPhi = 49;
            BPlow = 0;
            IChi = 676;
            IClow = 0;
        } else if (CpSO_2 < 134) {
            BPhi = 100;
            BPlow = 50;
            IChi = 133;
            IClow = 68;
        } else if (CpSO_2 < 164) {
            BPhi = 200;
            BPlow = 101;
            IChi = 163;
            IClow = 134;
        } else if (CpSO_2 < 192) {
            BPhi = 300;
            BPlow = 201;
            IChi = 191;
            IClow = 164;
        } else if (CpSO_2 < 254) {
            BPhi = 400;
            BPlow = 301;
            IChi = 253;
            IClow = 192;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 303;
            IClow = 254;
        }
        double AQI_SO_2 = calculateAQI(BPhi, BPlow, IChi, IClow, CpSO_2);

        // PM_10
        if (CpPM_10 < 66){
            BPhi = 49;
            BPlow = 0;
            IChi = 65;
            IClow = 0;
        } else if (CpPM_10 < 130) {
            BPhi = 100;
            BPlow = 50;
            IChi = 129;
            IClow = 66;
        } else if (CpPM_10 < 216) {
            BPhi = 200;
            BPlow = 101;
            IChi = 215;
            IClow = 130;
        } else if (CpPM_10 < 301) {
            BPhi = 300;
            BPlow = 201;
            IChi = 300;
            IClow = 216;
        } else if (CpPM_10 < 356) {
            BPhi = 400;
            BPlow = 301;
            IChi = 355;
            IClow = 301;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 430;
            IClow = 356;
        }
        double AQI_PM_10 = calculateAQI(BPhi, BPlow, IChi, IClow, CpPM_10);

        // PM_2_5
        if (CpPM_2_5 < 18.6){
            BPhi = 49;
            BPlow = 0;
            IChi = 18.5;
            IClow = 0;
        } else if (CpPM_2_5 < 37.5) {
            BPhi = 100;
            BPlow = 50;
            IChi = 37;
            IClow = 18.6;
        } else if (CpPM_2_5 < 84.5) {
            BPhi = 200;
            BPlow = 101;
            IChi = 84;
            IClow = 37.5;
        } else if (CpPM_2_5 < 130.5) {
            BPhi = 300;
            BPlow = 201;
            IChi = 130;
            IClow = 84.5;
        } else if (CpPM_2_5 < 165.5) {
            BPhi = 400;
            BPlow = 301;
            IChi = 165;
            IClow = 130.5;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 200;
            IClow = 165.5;
        }
        double AQI_PM_2_5 = calculateAQI(BPhi, BPlow, IChi, IClow, CpPM_2_5);

        // O_3
        if (CpO_3 < 36){
            BPhi = 49;
            BPlow = 0;
            IChi = 35;
            IClow = 0;
        } else if (CpO_3 < 71) {
            BPhi = 100;
            BPlow = 50;
            IChi = 70;
            IClow = 36;
        } else if (CpO_3 < 98) {
            BPhi = 200;
            BPlow = 101;
            IChi = 97;
            IClow = 71;
        } else if (CpO_3 < 118) {
            BPhi = 300;
            BPlow = 201;
            IChi = 117;
            IClow = 98;
        } else if (CpO_3 < 156) {
            BPhi = 400;
            BPlow = 301;
            IChi = 155;
            IClow = 118;
        } else {
            BPhi = 500;
            BPlow = 401;
            IChi = 188;
            IClow = 156;
        }
        double AQI_O_3 = calculateAQI(BPhi, BPlow, IChi, IClow, CpO_3);

        ContaminantList.add(new Contaminant("CO", AQI_CO));
        ContaminantList.add(new Contaminant("NO_2", AQI_NO_2));
        ContaminantList.add(new Contaminant("SO_2", AQI_SO_2));
        ContaminantList.add(new Contaminant("PM_10", AQI_PM_10));
        ContaminantList.add(new Contaminant("PM_2_5", AQI_PM_2_5));
        ContaminantList.add(new Contaminant("O_3", AQI_O_3));

        Collections.sort(ContaminantList, new Comparator<Contaminant>() {
            @Override
            public int compare(Contaminant cp1, Contaminant cp2) {
                return Double.compare(cp1.getValue(), cp2.getValue());
            }
        });
        return ContaminantList.get(5);
    }

    private void updateValues(Data data) {
        TextView viewCO = activity.findViewById(R.id.CO);
        TextView viewNO_2 = activity.findViewById(R.id.NO_2);
        TextView viewSO_2 = activity.findViewById(R.id.SO_2);
        TextView viewPM_10 = activity.findViewById(R.id.PM_10);
        TextView viewPM_2_5 = activity.findViewById(R.id.PM_2_5);
        TextView viewO_3 = activity.findViewById(R.id.O_3);
        viewCO.setText(data.getCO());
        viewSO_2.setText(data.getSO_2());
        viewNO_2.setText(data.getNO_2());
        viewPM_2_5.setText(data.getPM_2_5());
        viewPM_10.setText(data.getPM_10());
        viewO_3.setText(data.getO_3());

    }


    private void initDevice() throws IOException {
        // Get the default adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Log.i("BT", "No bluetooth adapter available");
            Toast.makeText(activity.getApplicationContext(),"No Bluetooth device ",Toast.LENGTH_SHORT).show();
            //this.activity.finish();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("HC-05")) {
                        Log.i("BT", "BT found");
                        myDevice = device;
                        break;
                    }
                }
            }

            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            BluetoothSocket mmSocket = myDevice.createRfcommSocketToServiceRecord(uuid);
            mBluetoothAdapter.cancelDiscovery();
            mmSocket.connect();
            mmInputStream = mmSocket.getInputStream();
        }
    }

    private Location getGpsData(){
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }else {
            return  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
    }

    private Data getData() throws IOException {
        try {
            int bytes = 0;
            Data data;
            byte[] readBuffer = new byte[1024];
            bytes = mmInputStream.read(readBuffer);     // Get number of bytes and message in "buffer"
            data = parseData(readBuffer);
            return data;
        }
        catch (Exception e){
            return null;
        }
    }

    private Data parseData(byte[] packetBytes){
        Data data;
        if (packetBytes==null){
            Log.i("BT-data", "No data received");
            return null;
        }
        String[] splitData = new String(packetBytes).split(";");
        if(splitData.length<1){
          Log.i("BT-data",  new String(packetBytes));
          Log.i("BT-data", "splitData empty");
          return null;
        }
        String[] sensorsValues = splitData[0].split(",");
        if(sensorsValues.length<6){
          Log.i("BT-data", "Missing data");
          return null;
        }

        data = new Data(String.valueOf(Double.parseDouble(sensorsValues[0])/10),
                String.valueOf(Double.parseDouble(sensorsValues[1])/5),
                String.valueOf(Double.parseDouble(sensorsValues[2])/4),
                String.valueOf(Double.parseDouble(sensorsValues[3])/5),
                sensorsValues[4],
                sensorsValues[5]);
        return data;
    }
}
