package com.example.xg.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class CompanyPageOne extends AppCompatActivity {

    private LinearLayout linearList;
    private Button button;
    private DataTransfer dataTransfer;
    private JSONObject avaiTasks;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page_one);
        //JSONObject pretend_json_input = new JSONObject();
        linearList = findViewById(R.id.companyTaskList);
        JSONObject company = new JSONObject();
        String method = "companyGetDeals";
        //System.out.println("we are here");
        Bundle extra = getIntent().getExtras();
        final String companyName = extra.getString("companyName");
        ctx = this;
        Socket socket = null;
        try {
            socket = IO.socket("http://192.168.1.105:8080");
            socket.connect();
            //MainFragment mf = new MainFragment();
            //socket.on("updateExistingDeal", mf.onNewMessage ); //lyssnar
            socket.on("onNewDeal", onNewMessage);
            //socket.emit("acceptDeal", acceptDeal);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //System.out.println("ASD");
        try {
            //System.out.println("this is the name of the company "+ companyName);
            company.put("companyName", companyName);
            dataTransfer = new DataTransfer(company,"post", method);
            String companyDeals = dataTransfer.getData();
            //System.out.println(" test snälla du "+test);
            companyDeals = companyDeals.substring(1, companyDeals.length()-1);
            //System.out.println("test "+ companyDeals);
            String[] values = companyDeals.split("\\}");
            InfoButton createButton = new InfoButton(this);
            for (int i = 0; i<values.length;i++){
                if (i==0){
                    values[i] = values[i]+"}";
                    //System.out.println(values[i]);
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList, "");
                } else {
                    values[i] = values[i]+"}";
                    values[i] = values[i].substring(1,values[i].length());
                    //System.out.println("ka " + values[i]);
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList,"");
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button createOneTasks = findViewById(R.id.createOneTask);
        createOneTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyPageOne.this, CompanyCreateTask.class);
                //Intent intent = new Intent(context, CleanerPageOne.class);
                // send to companyName to the next page
                intent.putExtra("companyName", companyName);
                startActivity(intent);
            }
        });
    }
    public Emitter.Listener onNewMessage = new Emitter.Listener() {



        @Override
        public void call(final Object... args) { //args är inkommande data
            Log.e("tag", args[0].toString());
            final JSONObject data = (JSONObject) args[0];
            final String[] agreementID = new String[1];
            String message;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //System.out.println("hit?");
                        agreementID[0] = data.getString("agreementID");
                        //message = data.getString("accepted");
                        int id = Integer.parseInt(agreementID[0]);
                        //System.out.println(agreementID[0] + " ID");
                        //System.out.println(data.toString());
                        InfoButton createButton = new InfoButton(ctx);
                        createButton.createTaskButton(data, button, linearList, "");
                    } catch (JSONException e) {
                        return;}
                }
            });



        }

    };
}
