package com.example.xg.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class CompanyCreateTask extends AppCompatActivity {

    EditText taskPrice, taskCity, taskHours, taskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_create_task);

        Button createTask = findViewById(R.id.createTask);
        taskPrice = findViewById(R.id.taskPrice);
        taskCity = findViewById(R.id.taskCity);
        taskHours = findViewById(R.id.taskHours);
        taskDescription = findViewById(R.id.taskDescription);

        Bundle extra = getIntent().getExtras();
        final String companyName = extra.getString("companyName");
        //System.out.println(companyName + " the name of the company in company create task");
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject deal = new JSONObject();
                try {
                    deal.put("price", taskPrice.getText().toString());
                    deal.put("city", taskCity.getText().toString());
                    deal.put("hours", taskHours.getText().toString());
                    deal.put("description", taskDescription.getText().toString());
                    deal.put("companyName", companyName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String method = "addDeal";
                try {
                    new DataTransfer(deal,"post", method);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(CompanyCreateTask.this, "You created a task "+ deal.toString() , Toast.LENGTH_LONG).show();
            }
        });
    }
}
