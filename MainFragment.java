package com.example.elund.tesat;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elund on 2018-03-05.
 */

public class MainFragment extends Fragment {


    public Emitter.Listener onNewDeal = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("tag", "Sick message");
            Log.e("tag", args[0].toString());

            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            Log.e("Fail", "How far do i make it?");
            try {

                username = data.getString("username");
                message = data.getString("message");
            } catch (JSONException e) {
                return;
            }

            // add the message to view


        }
    };

    public Emitter.Listener onUpdateExistingDeal = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e("tag", "Sick message");
            Log.e("tag", args[0].toString());

            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            Log.e("Fail", "How far do i make it?");
            try {

                username = data.getString("agreementID");
                message = data.getString("accepted");
            } catch (JSONException e) {
                return;
            }

            // add the message to view


        }
    };
}


