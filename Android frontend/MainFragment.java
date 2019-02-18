package com.example.xg.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xg on 2018-03-12.
 */

//Main fragment klassen
public class MainFragment extends Fragment {
    LinearLayout list;
    Context ctx;
    private Activity a;
/*
    public MainFragment(LinearLayout list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Activity a;
        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    public Emitter.Listener onNewMessage = new Emitter.Listener() {



        @Override
        public void call(final Object... args) { //args är inkommande data
            Log.e("tag", args[0].toString());
            final JSONObject data = (JSONObject) args[0];
            final String[] agreementID = new String[1];
            String message;

            //onAttach();
            /*if(getActivity() == null){
                System.out.println("getActivity är tom");
            }*/

            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("hit?");
                        agreementID[0] = data.getString("agreementID");
                        //message = data.getString("accepted");
                        int id = Integer.parseInt(agreementID[0]);
                        System.out.println(agreementID[0] + " ID");
                        int count = list.getChildCount();
                        View v = null;
                        for(int i=0; i<count; i++) {
                            v = list.getChildAt(i);
                            if (v.getId() == id){
                                list.removeView(v);
                            }
                        }
                    } catch (JSONException e) {
                        return;}
                }
            });

            //runOnUiThread(new Runnable())


            //TextView notes = (TextView) findViewById(getResources().getIdentifier(agreementID, "id", getPackageName()));


            //Button removeButton = findViewById(R.id.erik);

            //System.out.println(username);
            //System.out.println(message + " här ");
            // add the message to view


        }

    };
}
