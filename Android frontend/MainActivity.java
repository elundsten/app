package com.example.xg.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
// implements AdapterView.OnItemClickListener
public class MainActivity extends AppCompatActivity{
    Socket socket;
    // ArrayAdapter<String> adapter;
    EditText UsernameEt, PasswordEt;
    // This is where we take care of core business logic...
    @Override
    protected void onCreate(Bundle savedInstanceState) {            // in this state, the activity is created
        super.onCreate(savedInstanceState);
        // Following line brings view on top of the activity ...
        setContentView(R.layout.activity_main);      // This is related to view
        UsernameEt = findViewById(R.id.etUserName);
        PasswordEt = findViewById(R.id.etPassword);

        Button cleanerTasks = findViewById(R.id.register);
        cleanerTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterME.class);
                startActivity(intent);
            }
        });

    }

    public void onLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        LoginBackground loginBackground = new LoginBackground(this);
        loginBackground.execute(type, username, password);

    }

}
