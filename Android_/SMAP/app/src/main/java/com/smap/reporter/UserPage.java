package com.smap.reporter;
import com.microsoft.windowsazure.mobileservices.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;


public class UserPage extends AppCompatActivity  {

    private MobileServiceClient mClient;
    public Connection con;
    private  DataSender ds ;
    private Button mapBtn;
    private Button reportBtn;
    private Collector collector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        initViews();

        collector = new Collector(this);
        ds = new DataSender(this);

        mapBtn = (Button) findViewById(R.id.map);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://smapwebapp.azurewebsites.net/maps.html"));
                startActivity(browserIntent);
            }
        });


    }


    private void initViews() {

        TextView userName = (TextView) findViewById(R.id.UserName);
        userName.setText( "Hello "+getIntent().getStringExtra("UserName"));
        TextView viewCO = (TextView) findViewById(R.id.CO);
        TextView viewNO_2 = (TextView) findViewById(R.id.NO_2);
        TextView viewSO_2 = (TextView) findViewById(R.id.SO_2);
        TextView viewPM_10 = (TextView) findViewById(R.id.PM_10);
        TextView viewPM_2_5 = (TextView) findViewById(R.id.PM_2_5);
        TextView viewO_3 = (TextView) findViewById(R.id.O_3);

        viewCO.setText("0.0");
        viewSO_2.setText("0.0");
        viewNO_2.setText("0.0");
        viewPM_2_5.setText("0.0");
        viewPM_10.setText("0.0");
        viewO_3.setText("0.0");
    }


}
