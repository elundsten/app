package com.example.elund.tesat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    com.github.nkzawa.socketio.client.Socket socket;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            socket = IO.socket("http://10.0.2.2:8080");
            System.out.println("ASD");
        }catch (URISyntaxException e){
            Log.d("error", "onClick: " + e.toString());
        }
        MainFragment mf = new MainFragment();
        socket.on("updateExistingDeal", mf.onUpdateExistingDeal);
        socket.on("onNewDeal", mf.onNewDeal);
        //socket.off("newMessage");
        socket.connect();

        JSONObject json = new JSONObject();
        try {
            json.put("Erik", "Ollon");
            json.put("Yllka", "Rama");
            json.put("asd", "Knäpp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonValue = json.toString();
        Get myGet = new Get();
            myGet.execute("http://10.0.2.2:8080/API/get");
        Post myconnection = new Post();
            myconnection.execute("http://10.0.2.2:8080/API/post", jsonValue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText num1 = (EditText) findViewById(R.id.firstEditText);
                EditText num2 = (EditText) findViewById(R.id.secondEditText);
                TextView resultTextView = (TextView) findViewById(R.id.resultTextView);

                int number1 = Integer.parseInt(num1.getText().toString());
                int number2 = Integer.parseInt(num2.getText().toString());
                int res = number1 + number2;
                resultTextView.setText(res + "");
                socket.emit("join","I really have to iron");


            }
        });
        Button postBtn = findViewById(R.id.PostButton);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("YllkaRama_hotstuff", "hej123");
                    json.put("AuroraRama_youngstuff", "password123");
                    json.put("ErikDenMäktige", "hotstuff123");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonValue = json.toString();
                Post myconnection = new Post();
                myconnection.execute("http://10.0.2.2:8080/API/password", jsonValue);
            }
        });

        Button registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("userName", "YllkaRama");
                    json.put("password", "hej123");
                    json.put("userType", "cleaner");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonValue = json.toString();
                Post myconnection = new Post();
                myconnection.execute("http://10.0.2.2:8080/API/register", jsonValue);
            }
        });

        Button loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("userName", "YllkaRama");
                    json.put("password", "hej123");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonValue = json.toString();
                Post myconnection = new Post();
                myconnection.execute("http://10.0.2.2:8080/API/login", jsonValue);
            }
        });

        Button addDealBtn = findViewById(R.id.addDeal);
        addDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("price", "10000");
                    json.put("city", "Stockholm");
                    json.put("hours", "5");
                    json.put("description", "This is a description");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonValue = json.toString();
                Post myconnection = new Post();
                myconnection.execute("http://10.0.2.2:8080/API/addDeal", jsonValue);
            }
        });

        Button accpetDealbtn = findViewById(R.id.acceptDeal);
        accpetDealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("agreementID", "1");
                    json.put("price", "10000");
                    json.put("city", "Stockholm");
                    json.put("hours", "5");
                    json.put("description", "This is a description");
                    json.put("userName", "Yllka");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("acceptDeal",json);
            }
        });

        Button setTimeWorked = findViewById(R.id.acceptDeal);
        accpetDealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("agreementID", "1");
                    json.put("price", "10000");
                    json.put("city", "Stockholm");
                    json.put("hours", "5");
                    json.put("description", "This is a description");
                    json.put("userName", "Yllka");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("acceptDeal",json);
            }
        });

        Button timeWorkedBtn = findViewById(R.id.timeReport);
        timeWorkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("agreementID", "1");
                    json.put("userName", "Yllka");
                    json.put("hours", "5");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("timeUpdate",json);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
