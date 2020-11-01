package com.digibarber.app.activities;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.apicalls.ApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by DIGIBARBER LTD on 5/27/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";
    public SharedPreferences prefs;

    @Override
    public void onNewToken(String s) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        prefs = getSharedPreferences(Constants.SHARED_PREFRENCE_DB_NAME, MODE_PRIVATE);
        Log.i("*notification_check", "onNewToken " + s);
        updateDeviceToken(refreshedToken);
    }

    private void updateDeviceToken(final String refreshedToken) {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.i("*notification_check", "Hitting");
            Log.i("notification_check",Constants.UpdateDeviceToken);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.UpdateDeviceToken,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("notification_check","RES " + response);
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                prefs.edit().putString(Constants.KEY_DEVICE_ID, refreshedToken).apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                prefs.edit().putString(Constants.KEY_DEVICE_ID, refreshedToken).apply();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("device_id", "" + refreshedToken.trim());
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {

        }
    }
}
