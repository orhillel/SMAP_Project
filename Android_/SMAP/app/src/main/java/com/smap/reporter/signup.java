package com.smap.reporter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity  implements SendToAzure.AsyncResponse {
    public static final String urlReport = "addReport?code=bFpx4HXecwkh9yme0LhKChM2nSv2S27Ivpk0Y3TGTVaxGRpRc10zkw==";
    public static final String urlReportUser = "addUser?code=wckEGxTgDQiyWeEIZ1ab1Jam5I4ow0Tial80QfP58JYSo/QNy9m0OA==";
    public static final String urlGetUser = "getUser?code=ZUe9RumQiN7ur6an/gvZwKeN9p0z1HssUCaHqO4jZgTrs6aUGvgPxQ==";
    public static final String urlGetUserByMail = "getUserByMail?code=kBUGuBXubjofc4KKGWxD6R768hmFtzIHLFhIJEtwXfae1ZbqG1dAew==";
    private Button b_signup;
    private EditText m_email;
    private EditText m_password;
    private int insertUser = 0 ;
  //  private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    //    mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        insertUser = 0 ;
        b_signup = findViewById(R.id.m_signup);
        m_password = findViewById(R.id.m_Password2);
        m_email = findViewById(R.id.m_Email2);
        b_signup.setOnClickListener(new View.OnClickListener() {
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

    public void sendToAzureUser(String email, String pass) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        insertUser = 1;
        jsonObject.put("email",email);
        jsonObject.put("password",pass);
        new SendToAzure(this,jsonObject,urlReportUser).execute();
    }
    public String sendToAzureGetUser(String email, String pass) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email",email);
        jsonObject.put("password",pass);
        SendToAzure send = new SendToAzure(this,jsonObject,urlGetUserByMail);
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

    public void makeToast(String msg)
    {
        Toast.makeText(signup.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backFromCheck(String output) {

    }

    @Override
    public void processFinish(String output) {
        if(insertUser == 1)
            return;
        Log.i("azure", output);
        JSONObject obj = null;
        try {
            obj = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (obj!= null && !(obj.get("m_StringValue")).toString().equals("")) {
                Toast.makeText(this, "Email already exist", Toast.LENGTH_SHORT).show();
            }
            else
            {
                sendToAzureUser(m_email.getText().toString(), m_password.getText().toString());
                makeToast("Signup Complete");
                Intent intent = new Intent(signup.this, Login.class);
                startActivity(intent);
                this.finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}