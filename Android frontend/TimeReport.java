package com.example.xg.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;

public class TimeReport extends AppCompatActivity {
    private EditText agreementID, workedHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_report);

        agreementID = findViewById(R.id.agreementID);
        workedHours = findViewById(R.id.workedHours);

        Button timeReport = findViewById(R.id.submitTime);
        timeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extra = getIntent().getExtras();
                final String passedUserName = extra.getString("user");
                String agreeID = agreementID.getText().toString();
                String hours = workedHours.getText().toString();

                JSONObject timeUpdate = new JSONObject();
                try {
                    timeUpdate.put("userName", passedUserName);
                    timeUpdate.put("agreementID", agreeID);
                    timeUpdate.put("hours", hours);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Socket socket = null;
                try {
                    socket = IO.socket("http://192.168.1.105:8080");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                socket.connect();
                socket.emit("timeUpdate", timeUpdate);
                System.out.println("time updated! ");
            }
        });
    }
}
