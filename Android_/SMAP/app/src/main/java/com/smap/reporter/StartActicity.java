package com.smap.reporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StartActicity extends AppCompatActivity {

    private Button b_signup;
    private Button b_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_acticity);
        b_signup = findViewById(R.id.signup);
        b_login = findViewById(R.id.login);

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupActivity();
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openloginActivity();
            }
        });
    }
    public void openSignupActivity() {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
    public void openloginActivity() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
