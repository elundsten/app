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

import java.util.concurrent.ExecutionException;

public class RegisterME extends AppCompatActivity {
    EditText Username, Password, UserType, companyName;
    Button btn;
    String user, pass, type, company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_me);

        final String method  = "register";
        final JSONObject registerInfo = new JSONObject();
        Username  = findViewById(R.id.registerUserName);
        Password = findViewById(R.id.registerUserPass);
        UserType = findViewById(R.id.registerUserType);
        companyName = findViewById(R.id.registerCompanyName);
        btn = findViewById(R.id.registerButton);
        //System.out.println(user+ pass+ type);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                user = Username.getText().toString();
                pass = Password.getText().toString();
                type = UserType.getText().toString();
                company = companyName.getText().toString();
                System.out.println(user+ pass+ type+company);
                try {
                    registerInfo.put("userName", user);
                    registerInfo.put("password", pass);
                    registerInfo.put("userType", type);         // the type is either "cleaner" or "company"
                    registerInfo.put("companyName", company);
                    new DataTransfer(registerInfo,"post", method);

                    //Socket socket = IO.socket("http://192.168.1.105:8080");
                    //System.out.println("ASD");
                    //socket.connect();
                    //MainFragment mf = new MainFragment();
                    //socket.on("updateExistingDeal", mf.onNewMessage ); //lyssnar
                    //socket.emit("onNewDeal", acceptDeal);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
