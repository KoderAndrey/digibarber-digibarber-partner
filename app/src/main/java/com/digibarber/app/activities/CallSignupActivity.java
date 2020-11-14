package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
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

public class CallSignupActivity extends BaseActivity  {


    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String country_name = "";
    private String android_id;


    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.callsignup);

        bd = getIntent().getExtras();
        if (bd != null) {
            firstname = bd.getString("first_name");
            lastName = bd.getString("last_name");
            email = bd.getString("email");
            password = bd.getString("password");
            phone = bd.getString("phone");
            country_name = bd.getString("country_name");
        }
        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON, "yes").apply();
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        signupNow();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void signupNow() {

        signUpService();

    }

    /* Register new user */
    private void signUpService() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result == true) {

            Constants.showPorgess(this);
            Log.e("Deep", "BirthDay Url:" + Constants.Register);

            Log.d("api call post",Constants.Register);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Register,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                if (!response_values.equalsIgnoreCase("false")) {

                                    String message = jsonobj.getString("message");
                                    String user_id = jsonobj.getString("user_id");
                                    String token = jsonobj.getString("token");
                                    String user_name = jsonobj.getString("user_name");
                                    String last_name = jsonobj.getString("last_name");
                                    String email = jsonobj.getString("user_email");
                                    String phone = jsonobj.getString("phone");
                                    String bank_detail = jsonobj.getString("bank_detail");
                                    String profile_image = jsonobj.getString("profile_image");

                                    if (jsonobj.has("mangopayId")) {
                                        String mangopayId = jsonobj.getString("mangopayId");
                                        prefs.edit().putString(Constants.KEY_MANGOPAY_ID, mangopayId).apply();
                                    }

                                    String struser_name = "";
                                    String strlast_name = "";
                                    if (user_name != null && !user_name.equalsIgnoreCase("") && !user_name.equalsIgnoreCase("null")) {
                                        struser_name = user_name.substring(0, 1).toUpperCase() + user_name.substring(1).toLowerCase();
                                    }
                                    if (last_name != null && !last_name.equalsIgnoreCase("") && !last_name.equalsIgnoreCase("null")) {
                                        strlast_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1).toLowerCase();
                                    }
                                    prefs.edit().putString(Constants.KEY_USER_ID, user_id).apply();
                                    prefs.edit().putString(Constants.KEY_TOKEN, token).apply();
                                    prefs.edit().putString(Constants.KEY_FULL_NAME, struser_name + " " + strlast_name).apply();
                                    prefs.edit().putString(Constants.KEY_FIRST_NAME, struser_name).apply();
                                    prefs.edit().putString(Constants.KEY_LAST_NAME, strlast_name).apply();
                                    prefs.edit().putString(Constants.KEY_EMAIL, email).apply();
                                    prefs.edit().putString(Constants.KEY_PHONE, phone).apply();
                                    prefs.edit().putString(Constants.KEY_BANK_DETAIL, bank_detail).apply();
                                    prefs.edit().putString(Constants.KEY_PROFILE_IMAGE, profile_image).apply();
                                    prefs.edit().putString(Constants.COUNTRY_NAME, country_name).apply();
                                    Toast.makeText(CallSignupActivity.this, "SignUp successfully.", Toast.LENGTH_LONG).show();

                                    locationServicesCheck();
                                } else {
                                    if (jsonobj != null && jsonobj.has("message"))
                                        Constants.showPopupFrozen(CallSignupActivity.this, jsonobj.getString("message"));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                finish();
                            }

//                            pDialog.dismiss();
                            Constants.dismissProgress();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //checkConnection();
                    // ZoomConst.showCustomAlert(MainActivity.this, getResources().getString(R.string.app_name), error.getMessage(), getResources().getString(R.string.ok));
                    VolleyLog.d("** ERROR **", "Error: " + error.getMessage());
//                    pDialog.dismiss();
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    SharedPreferences prefs = getSharedPreferences("Barber", MODE_PRIVATE);
                    String restoredText = prefs.getString(Constants.KEY_DEVICE_ID, "");
                    if (restoredText != null) {
                        String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                        int idName = prefs.getInt("idName", 0); //0 is the default value.
                    }

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("first_name", firstname);
                    params.put("last_name", lastName);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("phone", phone);
                    params.put("device_type", Constants.APP_PLATFORM);
                    params.put("device_id", prefs.getString(Constants.KEY_DEVICE_ID, ""));
                    params.put("user_type", Constants.USER_TYPE);
                    params.put("login_type", "normal");// Need to ask the type
                    params.put("uniqueId ", android_id);

                    return params;
                }
            };


            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(req, "REGISTER");
        } else {
            Constants.showPopupInternet(activity);
            //System.out.println(" ** PLease connect to Internet **");
        }
    }

    private void locationServicesCheck() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enabled) {
            Intent it = new Intent(CallSignupActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
            finish();
        } else {
            Intent it = new Intent(CallSignupActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
            finish();
        }
        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "birthday").apply();

    }

}