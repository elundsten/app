package com.example.xg.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class CleanerAssignedTasks extends AppCompatActivity {
    private LinearLayout linearList;
    private Button button;
    private DataTransfer dataTransfer;
    private JSONObject avaiTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_assigned_tasks);
        linearList = findViewById(R.id.cleanerAssignedTask);

        Bundle extra = getIntent().getExtras();
        final String userName = extra.getString("user");
        JSONObject username = new JSONObject();
        try {
            username.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String method = "mytasks";
        try {
            dataTransfer = new DataTransfer(username, "post", method);
            String tasks = dataTransfer.getData();
            //System.out.println(" test " + test);
            tasks = tasks.substring(1, tasks.length()-1);
            //System.out.println("test "+ test);
            String[] values = tasks.split("\\}");
            InfoButton createButton = new InfoButton(this);
            for (int i = 0; i<values.length;i++){
                if (i==0){
                    values[i] = values[i]+"}";
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList, "");
                } else {
                    values[i] = values[i]+"}";
                    values[i] = values[i].substring(1,values[i].length());
                    //System.out.println("ka " + values[i]);
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList, "");
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
