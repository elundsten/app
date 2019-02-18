package com.example.xg.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import java.util.concurrent.ExecutionException;

public class CleanerPageOne extends AppCompatActivity{
    private LinearLayout linearList;
    private Button button;
    private DataTransfer dataTransfer;
    private JSONObject avaiTasks;
    Context ctx = this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_page_one);
        linearList = findViewById(R.id.linear_list);
        JSONObject nothing = new JSONObject();
        String method = "getDeals";

        //Intent intent = new Intent(CleanerPageOne.this, MainFragment.class);
        //intent.putExtra("pageContext", );

        try {
            Socket socket = IO.socket("http://192.168.1.105:8080");
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //View test = findViewById(R.id.linear_list);
            //MainFragment mf = new MainFragment();
            System.out.println(R.id.linear_list+"lin");
            //fragmentTransaction.add(R.id.linear_list, mf);
            socket.on("updateExistingDeal", onNewMessage ); //lyssnar
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //System.out.println("we are here");
        Bundle extra = getIntent().getExtras();
        final String passedUserName = extra.getString("user");
        //System.out.println(passedUserName + " username");
        try {
            dataTransfer = new DataTransfer(nothing,"get", method);
            String allDeals = dataTransfer.getData();
            //System.out.println(" test "+test);
            //String tasks = test;
            allDeals = allDeals.substring(1, allDeals.length()-1);
            //System.out.println("test "+ allDeals);
            String[] values = allDeals.split("\\}");
            InfoButton createButton = new InfoButton(this);
            for (int i = 0; i<values.length;i++){
                if (i==0){
                    values[i] = values[i]+"}";
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList, passedUserName);
                } else {
                    values[i] = values[i]+"}";
                    values[i] = values[i].substring(1,values[i].length());
                    System.out.println("ka " + values[i]);
                    avaiTasks = new JSONObject(values[i]);
                    createButton.createTaskButton(avaiTasks, button, linearList, passedUserName);
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

        // Taking from this page to cleanerAssigned page
        Button cleanerTasks = findViewById(R.id.cleanerAssignedTask);
        cleanerTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleanerPageOne.this, CleanerAssignedTasks.class);
                intent.putExtra("user", passedUserName );
                startActivity(intent);
            }
        });
        Button timeReport = findViewById(R.id.tidsrapportering);
        timeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleanerPageOne.this, TimeReport.class);
                intent.putExtra("user", passedUserName );
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
                            System.out.println("hit?");
                            agreementID[0] = data.getString("agreementID");
                            //message = data.getString("accepted");
                            int id = Integer.parseInt(agreementID[0]);
                            System.out.println(agreementID[0] + " ID");

                            int count = linearList.getChildCount();
                            View v = null;
                            for(int i=0; i<count; i++) {
                                v = linearList.getChildAt(i);
                                //System.out.println("view id "+ v.getId());
                                if (v != null ){
                                    System.out.println("in hit?");
                                    if (v.getId() == id){

                                        //System.out.println();
                                        linearList.removeView(v);
                                        System.out.println("view id "+ v.getId());
                                    }
                                    System.out.println("passerat?");
                                }

                            }
                        } catch (JSONException e) {
                            return;}
                    }
                });



            }

        };
    }


