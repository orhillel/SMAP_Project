package com.smap.reporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity implements SendToAzure.AsyncResponse {
    public static final String urlGetUser = "getUser?code=ZUe9RumQiN7ur6an/gvZwKeN9p0z1HssUCaHqO4jZgTrs6aUGvgPxQ==";
    private Button b_signIn;
    private EditText m_email;
    private EditText m_password;

    //  private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b_signIn = findViewById(R.id.m_signIn);
        m_password = findViewById(R.id.m_password);
        m_email = findViewById(R.id.m_email);
        b_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = m_email.getText().toString().trim();
                String pass = m_password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches() || mail.isEmpty()) {
                    makeToast("Invalid Email");
                    return;
                }
                if (pass.isEmpty() || pass.length() < 6) {
                    makeToast("Invalid Password");
                    return;
                }
                try {
                    sendToAzureGetUser(m_email.getText().toString(), m_password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public String sendToAzureGetUser(String email, String pass) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email",email);
        jsonObject.put("password",pass);
        SendToAzure send = new SendToAzure(this,jsonObject,urlGetUser);
        send.delegate = this;
        try {
            send.execute().get();
            return "done";
        } catch (ExecutionException e) {
            return "";
        } catch (InterruptedException e) {
            return "";
        }
    }
    public void makeToast(String msg) {
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void openSignupActivity() {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

    public void openListActivity() {
        Intent intent = new Intent(this, UserPage.class);
        intent.putExtra("UserName", m_email.getText().toString().split("@")[0]);
        startActivity(intent);
        finish();
    }

    @Override
    public void backFromCheck(String output) {

    }

    @Override
    public void processFinish(String output) {
        Log.i("azure", output);
        JSONObject obj = null;
        try {
            obj = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (obj!= null && (obj.get("m_StringValue").toString()).equals("")){

                Toast.makeText(this, "WRONG EMAIL OR PASSWORD", Toast.LENGTH_SHORT).show();
            }
            else
            {
                    makeToast("SignIn Successfully ");
                    openListActivity();
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
