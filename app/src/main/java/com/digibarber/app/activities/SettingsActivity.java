package com.digibarber.app.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends BaseActivity {

    Switch tb_noitifaction;
    ImageView iv_booking_arrow;
    private RelativeLayout help_layout;
    private RelativeLayout terms_layout;
    private RelativeLayout privacy_layout;
    private RelativeLayout rating_layout;
    // Unregister this account/device pair within the server.
    private void REgisterOrUnregisterUser(final String refreshedToken) {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.e("*updateDeviceToken*", "Hitting");
            Log.d("api call post",Constants.UpdateDeviceToken);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.UpdateDeviceToken,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("*DeviceToken RESPONSE *", response);
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
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        tb_noitifaction = (Switch) findViewById(R.id.tb_noitifaction);
        terms_layout = (RelativeLayout) findViewById(R.id.terms_layout);
        privacy_layout = (RelativeLayout) findViewById(R.id.privacy_layout);
        rating_layout = (RelativeLayout) findViewById(R.id.rating_layout);
        help_layout = (RelativeLayout) findViewById(R.id.help_layout);

        String status = prefs.getString(Constants.KEY_NOTIFICATION_STATUS, "on");
        if (status.equalsIgnoreCase("on")) {
            tb_noitifaction.setChecked(true);
        } else {
            tb_noitifaction.setChecked(false);
        }

        tb_noitifaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(800);
                    REgisterOrUnregisterUser(prefs.getString(Constants.KEY_DEVICE_ID,""));

                    prefs.edit().putString(Constants.KEY_NOTIFICATION_STATUS, "on").apply();
                } else {
                    prefs.edit().putString(Constants.KEY_NOTIFICATION_STATUS, "off").apply();
                    REgisterOrUnregisterUser("1234567890");

                }

            }
        });
        iv_booking_arrow = (ImageView) findViewById(R.id.iv_booking_arrow);
        iv_booking_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, BoookingApprovalActivity.class));
            }
        });



        terms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, TermConditionActiivty.class));
            }
        });
        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, TermConditionActiivty.class));
            }
        });
        privacy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, PrivacyPolicyActivity.class));
            }
        }); rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }            }
        });
    }

    /* Back click */
    public void backClick(View v) {
        this.finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /* Change Password click */
    public void changePasswordClick(View v) {
        startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
    }
}
