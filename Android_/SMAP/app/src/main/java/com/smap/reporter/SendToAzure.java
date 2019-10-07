package com.smap.reporter;

import android.os.AsyncTask;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class SendToAzure extends AsyncTask<String, Void, String> {
    public interface AsyncResponse {
        void backFromCheck(String output);

        void processFinish(String output);
    }
    public JSONObject postData;

    public String uri;
    public AsyncResponse delegate = null;

    public SendToAzure(AsyncResponse delegate, JSONObject postData,String uri) {
        this.delegate = delegate;
        this.postData = postData;
        this.uri = uri;
    }
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder response = null;
        HttpURLConnection urlConnection;
        try {
            URL url = new URL("https://smapapp.azurewebsites.net/api/"+ uri );
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line);
                }
                Log.d("res",response.toString());
            } else {
                Log.d("res","error");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return "";
        }
        return response.toString(); //"Failed to fetch data!";
    }

    @Override
    protected void onPostExecute(String result) {

        delegate.processFinish(result);
    }
}
