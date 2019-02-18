package com.example.xg.myapplication;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post extends AsyncTask<String, Void, String> {

    String response;

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            //System.out.println(url + " this is URL for POST");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type"
                    , "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
            //System.out.println("inside the post " + params[1]);
            writer.write(params[1]); // should be params[1]
            writer.flush();
            writer.close();

            connection.connect();
            //System.out.println("hit1");
            //String json_response = "";
            InputStreamReader in = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String result="";
            String text = "";
            //System.out.println("hit2");
            while ((text = br.readLine()) != null) {
                result += text;
            }
            System.out.println(" should be the result "+ result);
            // Response: 400
            Log.e("Response", result);
            //response = json_response;
            return result;

        } catch (Exception e) {
            Log.e(e.toString(), "Something with request");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        response = result;
        //System.out.println(result + " från post");
        //super.onPostExecute(result);
        //System.out.println("tessta post hääär "+ s);
        //System.out.println("tessta post 1 "+ response);
    }

    public String getResponse(){
        return response;
    }

}