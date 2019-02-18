package com.example.elund.tesat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type"
                    , "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));

            writer.write(params[1]); // should be params[1]
            writer.close();

            connection.connect();
            String json_response = "";
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text = "";
            while ((text = br.readLine()) != null) {
                json_response += text;
            }
            // Response: 400
            Log.e("Response", json_response + "");


        } catch (Exception e) {
            Log.e(e.toString(), "Something with request");
        }



        return null;
    }

}