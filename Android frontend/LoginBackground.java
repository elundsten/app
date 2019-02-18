package com.example.xg.myapplication;

//import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by xg on 2018-03-12.
 */

public class LoginBackground extends AsyncTask<String, Void, String> {

    Context context;
    private String user_name;

    public LoginBackground(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        if (type.equals("login")) {
            user_name = params[1];
            String password = params[2];
            String method = "login";
            JSONObject loginInfo = new JSONObject();
            try {
                loginInfo.put("userName", user_name);
                loginInfo.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                //String method = "login";
                String u = "http://192.168.1.105:8080/API/" + method;
                //String u = "http://10.110.88.33:8080/API/" + method;
                URL url = new URL(u);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type"
                        , "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(connection.getOutputStream()));
                writer.write(loginInfo.toString()); // should be params[1]
                writer.flush();
                writer.close();
                connection.connect();
                String json_response = "";
                InputStreamReader in = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String result = "";
                String text = "";
                while ((text = br.readLine()) != null) {
                    result += text;

                }
                // Response: 400
                Log.e("Response", json_response);
                //response = json_response;
                return result;

            } catch (Exception e) {
                Log.e(e.toString(), "Something with request");
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage("helllo");
        String[] results = result.split(":");
        result = result.substring(1,result.length()-1);
        String replaced = result.replaceAll("\"", "");
        replaced = replaced.replaceAll("\\\\", "");

        if (results.length > 2){
            String[] striped = replaced.split(",");
            String[] companyName = striped[0].split(":");
            String[] userType= striped[1].split(":");

            if (userType[1].equalsIgnoreCase("company")){
                super.onPostExecute(result);
                //MainActivity main = new MainActivity();
                Intent intent = new Intent(context, CompanyPageOne.class);
                intent.putExtra("user", user_name);
                intent.putExtra("companyName", companyName[1]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);}
        } else if (results.length == 2){
            String[] userType = replaced.split(":");
            if (userType[1].equalsIgnoreCase("cleaner")){
                super.onPostExecute(result);
                //MainActivity main = new MainActivity();
                Intent intent = new Intent(context, CleanerPageOne.class);
                intent.putExtra("user", user_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

}
