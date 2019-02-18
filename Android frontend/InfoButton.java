package com.example.xg.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by xg on 2018-03-06.
 */

public class InfoButton {
    private Context context;
    DataTransfer dataTransfer;
    String city, description, companyName;
    public InfoButton(Context ctx) {
        this.context = ctx;
    }

    public void createTaskButton(JSONObject task, Button button, LinearLayout linearList, final String passedUserName){
        Iterator keys = task.keys();
        while (keys.hasNext()){
            String key = (String) keys.next();
            String value = null;
            try {
                value = task.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (key.equalsIgnoreCase("agreementID")){
                button = new Button(context);
                button.setId(Integer.parseInt(value));          // the value is the ID of the button ans the ID of the agreement
                button.setText("Titel " + value);
            }
            else if(key.equalsIgnoreCase("city")){
                city = value;
            }
            else if(key.equalsIgnoreCase("description")){
                description = value;
            }else if(key.equalsIgnoreCase("companyName")){
                companyName = value;
            }
            else if(key.equalsIgnoreCase("hours")){
                linearList.addView(button);
                final String hours = "hours " + value;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        offerButton(view, hours, passedUserName, city, description, companyName);
                    }
                });
            }
        }
    }

    public void offerButton(final View view, String hours, final String passedUserName, final String city, final String description, final String companyName) {
        //final String userName = passedUserName;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Uppdrag ID " + view.getId());
        dialogBuilder.setMessage("Beskrivning: " + description);
        // get the message of the idea
        dialogBuilder.setPositiveButton("Ta uppdraget", null);
        dialogBuilder.setNegativeButton("Stäng ner", null);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        // override the text color of positive button
        positiveButton.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        // provides custom implementation to positive button click
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onPositiveButtonClicked(alertDialog, view, passedUserName, city, description, companyName);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        // override the text color of negative button
        negativeButton.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        // provides custom implementation to negative button click
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeButtonClicked(alertDialog);
            }
        });
    }


    private void onPositiveButtonClicked(AlertDialog alertDialog, View view, String passedUserName, String city, String description, String companyName) throws JSONException, URISyntaxException {

        JSONObject acceptDeal = new JSONObject();
        acceptDeal.put("agreementID", view.getId());
        acceptDeal.put("city", city);
        acceptDeal.put("userName", passedUserName);
        acceptDeal.put("description", description);
        acceptDeal.put("companyName", companyName);

        Socket socket = IO.socket("http://192.168.1.105:8080");
        //System.out.println("ASD");
        socket.connect();
        //MainFragment mf = new MainFragment();
        //socket.on("updateExistingDeal", mf.onNewMessage ); //lyssnar
        socket.emit("acceptDeal", acceptDeal);


        //socket.off("newMessage"); //Ta bort för något du lyssnar på

        Toast.makeText(context, "Du är registrerad till uppdraget.", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    private void onNegativeButtonClicked(AlertDialog alertDialog) {
        Toast.makeText(context, "Du tog inte uppdraget", Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}
