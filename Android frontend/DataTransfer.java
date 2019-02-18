package com.example.xg.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by xg on 2018-03-06.
 */

// Retrive and insert the value from the database
public class DataTransfer {

    String retrievedData = null;
    public DataTransfer(JSONObject jsonData, String getOrPost, String method) throws ExecutionException, InterruptedException {

        String url = "http://192.168.1.105:8080/API/"+method;
        //String url = "http://192.168.0.26:8080/API/"+method;
        //String url = "http://10.33.62.240:8080/API/"+method;
        //String url = "http://10.110.88.33:8080/API/"+method;
        //String retrievedData = null;
        if (getOrPost.equalsIgnoreCase("post")){
            //System.out.println(" this is the jsonData " + jsonData.toString());
            retrievedData = insertData(url, jsonData.toString());
        } else if (getOrPost.equalsIgnoreCase("get")){
            retrievedData = retrievData(url);
        }
    }

    public String insertData(String url, String jsonData) throws ExecutionException, InterruptedException {
        Post myPostConnection = new Post();
        String data = myPostConnection.execute(url, jsonData).get();
        return data;
    }

    public String retrievData(String url) throws ExecutionException, InterruptedException {
        Get myGetConnection = new Get();
        String retrievedData = myGetConnection.execute(url).get();
        return retrievedData;
    }

    public String getData(){
        return retrievedData;
    }
}
