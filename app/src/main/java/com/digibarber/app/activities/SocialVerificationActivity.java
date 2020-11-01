package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.StringRequest;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class SocialVerificationActivity extends BaseActivity {

    String request_id = "";
    ImageView iv_cross;
    ImageView iv_tick;
    ImageView iv_success;
    ImageView iv_resend;
    ImageView iv_call;
    ImageView iv_sad_face;
    TextView tv_phone;
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    TextView tv_header;
    String phone = "";
    private String firstname;
    private String lastName;
    private String email;
    private String socialId;
    private String android_id = "";
    private String birth_date;
    private String birthdate;
    private String password;

    RelativeLayout rl_verfication_main;
    RelativeLayout ll_click_popup;
    int count = 0;
    private String From;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_social_verification);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bd = getIntent().getExtras();
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (bd != null) {
            phone = bd.getString("phone");

            From = bd.getString("From");
            if (From != null) {
                if (From.equalsIgnoreCase("SocialLogin")) {
                    firstname = bd.getString("first_name");
                    lastName = bd.getString("last_name");
                    email = bd.getString("email");
                    password = bd.getString("password");
                    socialId = bd.getString("socialId");
                    email = bd.getString("email");
                    birth_date = bd.getString("birth_date");
                    birthdate = bd.getString("birthdate");
                }
            } else {
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
            }


        }
        //callSendOTP(phone, "firsttime");
        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et1, InputMethodManager.SHOW_IMPLICIT);

            }
        }, 1000);
        rl_verfication_main = (RelativeLayout) findViewById(R.id.rl_verfication_main);
        ll_click_popup = (RelativeLayout) findViewById(R.id.ll_click_popup);


        ll_click_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        iv_tick = (ImageView) findViewById(R.id.iv_tick);
        iv_success = (ImageView) findViewById(R.id.iv_success);
        iv_cross = (ImageView) findViewById(R.id.iv_cross);
        iv_resend = (ImageView) findViewById(R.id.iv_resend);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_sad_face = (ImageView) findViewById(R.id.iv_sad_face);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_header = (TextView) findViewById(R.id.tv_header);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);
        tv_phone.setText(phone);
        et1.requestFocus();


        rl_verfication_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextCodeVerified();
                //callSendOTP(phone, "call");
            }
        });

        iv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextCodeVerified();
                //callCancelOTP();
            }
        });

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();
                if (et1.getText().toString().trim().isEmpty()) {
                    et1.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et2.requestFocus();
                    et1.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {

                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(SocialVerificationActivity.this);
                }

            }
        });

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();

                if (et2.getText().toString().trim().isEmpty()) {
                    et2.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et3.requestFocus();
                    et2.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {

                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(SocialVerificationActivity.this);
                }

            }
        });

        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();

                if (et3.getText().toString().trim().isEmpty()) {
                    et3.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et4.requestFocus();
                    et3.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {

                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(SocialVerificationActivity.this);
                }

            }
        });

        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();

                if (et4.getText().toString().trim().isEmpty()) {
                    et4.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et4.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }
                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {

                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(SocialVerificationActivity.this);
                }

            }
        });

    }


    void setTextCodeVerified() {
        tv_header.setText("We've sent you a code to verify your mobile\n number");
        tv_header.setTextColor(Color.parseColor("#FF454545"));
    }

    /*
    protected void callCancelOTP() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {

            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Constants.showPorgess(SocialVerificationActivity.this);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String temp = "Empty";
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    try {
                        HttpGet get = new HttpGet(Constants.CANCEL_OTP + request_id);
                        //get.setHeader("Authorization", ZoomConst.HeaderMangoPay);
                        response = client.execute(get);
                        if (response != null) {
                            // InputStream in = response.getEntity().getContent(); //Get the data in the entity
                            temp = EntityUtils.toString(response.getEntity());
                        } else {
                            Constants.dismissProgress();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Constants.dismissProgress();
                    }
                    return temp;
                }

                @Override
                protected void onPostExecute(String Resposne) {
                    super.onPostExecute(Resposne);
                    try {
                        JSONObject jo = new JSONObject(Resposne);

                        String success = null;
                        try {
                            success = jo.getString("status");
                            if (success.equals("0")) {
                                callSendOTP(phone, "message");
                            } else if (success.equals("4")) {
                                callSendOTP(phone, "message");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Constants.dismissProgress();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Constants.dismissProgress();
                    }
                    Constants.dismissProgress();

                }
            };
            asyncTask.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Constants.showPopupInternet(activity);
        }

    }
*/

    /*
    protected void callSendOTP(final String phone, final String Status) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            //final String number = "91" + phone.substring(1, phone.length());

            final String number = "44" + phone.substring(1, phone.length());
            final String url = Constants.SEND_OTP + number;

            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (Status.equalsIgnoreCase("firsttime") || Status.equalsIgnoreCase("message")) {


                    } else {
                        Constants.showPorgess(SocialVerificationActivity.this);
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String temp = "Empty";
                    String url=Constants.SEND_OTP + number;
                    Log.e("TestRes","URL:"+url);
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    try {
                        HttpGet get = new HttpGet(url);
                        //get.setHeader("Authorization", ZoomConst.HeaderMangoPay);
                        response = client.execute(get);
                      //  Checking response
                        if (response != null) {
                            // InputStream in = response.getEntity().getContent(); //Get the data in the entity
                            temp = EntityUtils.toString(response.getEntity());
                        } else {
                            Constants.dismissProgress();
                            Log.e("NUll", "response " + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Constants.dismissProgress();
                    }
                    return temp;
                }

                @Override
                protected void onPostExecute(String Resposne) {
                    super.onPostExecute(Resposne);
                    Constants.dismissProgress();
                    try {
                        JSONObject jo = new JSONObject(Resposne);
                        Log.e("SEnd Response", Resposne);
                        String success = null;
                        try {
                            success = jo.getString("status");
                            if (success.equals("0") || success.equals("10")) {
                                request_id = jo.getString("request_id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asyncTask.execute();

            Log.d("api call post",url);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response

                            try {
                                JSONObject jo = new JSONObject(response);

                                String success = null;
                                try {
                                    success = jo.getString("status");
                                    if (success.equals("0") || success.equals("10")) {
                                        request_id = jo.getString("request_id");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", "" + error);
                        }
                    }
            );

            postRequest.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(postRequest, "Verification");


        } else {
            Constants.showPopupInternet(activity);
        }
    }

     */

    private void signUpService(final String birthdate, final String birth_date) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result == true) {
            Constants.showPorgess(SocialVerificationActivity.this);
            Log.d("api call post",Constants.Register);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Register,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
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
                                    locationServicesCheck();
                                } else {
                                    String message = jsonobj.getString("message");
                                    if (message != null && message.contains("User Already Exist")) {
                                        /*tv_email_error.setText("This email address already been registered");
                                        tv_email_error.setVisibility(View.VISIBLE);
                                        et_email.setBackgroundResource(R.mipmap.box_1_r);*/
                                    }
                                    Log.e("signUpService else", "" + response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("first_name", "" + firstname);
                    params.put("last_name", "" + lastName);
                    params.put("email", "" + email);
                    params.put("password", "" + socialId);
                    params.put("social_id", "" + socialId);
                    params.put("phone", "" + phone);
                    params.put("birthdate", "" + birthdate);
                    params.put("birth_date", "" + birth_date);
                    params.put("device_type", Constants.APP_PLATFORM);
                    params.put("device_id", prefs.getString(Constants.KEY_DEVICE_ID, "").trim());
                    params.put("user_type", Constants.USER_TYPE);
                    params.put("login_type", "social");// Need to ask the type
                    params.put("uniqueId ", "" + android_id);

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "REGISTER");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    /*
    protected void callVerifyOTP() {


        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            final String code = et1.getText().toString().trim() + et2.getText().toString().trim() + et3.getText().toString().trim() + et4.getText().toString().trim();
            final String verifyurl = Constants.VERIFY_OTP + request_id + "&code=" + code;

            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String temp = "Empty";
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    try {
                        HttpGet get = new HttpGet(verifyurl);
                        //get.setHeader("Authorization", ZoomConst.HeaderMangoPay);
                        response = client.execute(get);
                        // Checking response
                        if (response != null) {
                            // InputStream in = response.getEntity().getContent(); //Get the data in the entity
                            temp = EntityUtils.toString(response.getEntity());
                        } else {
                            //  ZoomConst.dismissProgress();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //  ZoomConst.dismissProgress();
                    }
                    return temp;
                }

                @Override
                protected void onPostExecute(String Resposne) {
                    super.onPostExecute(Resposne);
                    //   ZoomConst.dismissProgress();
                    try {
                        JSONObject jo = new JSONObject(Resposne);

                        String success = null;
                        Constants.hideSoftKeyboard(SocialVerificationActivity.this);
                        try {
                            success = jo.getString("status");
                            if (success.equals("0")) {

                                et1.setBackgroundResource(R.mipmap.edit_verfi_green);
                                et2.setBackgroundResource(R.mipmap.edit_verfi_green);
                                et3.setBackgroundResource(R.mipmap.edit_verfi_green);
                                et4.setBackgroundResource(R.mipmap.edit_verfi_green);
                                iv_resend.setVisibility(View.GONE);
                                iv_call.setVisibility(View.GONE);
                                iv_tick.setVisibility(View.VISIBLE);
                                iv_success.setVisibility(View.VISIBLE);
                                tv_header.setText("We have verified that number! Cant wait for\nyou to see the app!");
                                tv_header.setTextColor(Color.parseColor("#FF454545"));

                                Thread th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(1500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }


                                        if (From != null && From.equalsIgnoreCase("SocialLogin")) {
                                            signUpService(birthdate, birth_date);
                                        } else if (From != null && From.equalsIgnoreCase("changeprivateinfo")) {
                                            //  locationServicesCheck();
                                            prefs.edit().putBoolean(Constants.ISMOBILEVERIFIED, true).apply();

                                            finish();
                                        } else {
                                            Intent it = new Intent(SocialVerificationActivity.this, CallSignupActivity.class);
                                            it.putExtra("first_name", firstname);
                                            it.putExtra("last_name", lastName);
                                            it.putExtra("email", email);
                                            it.putExtra("password", password);
                                            it.putExtra("phone", phone);
                                            startActivity(it);
                                        }


                                    }
                                });
                                th.start();
                            } else if (success.equals("16")) {
                                et1.requestFocus();
                                if (count > 2) {
                                    et1.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et2.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et3.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et4.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    iv_sad_face.setVisibility(View.VISIBLE);
                                    iv_resend.setVisibility(View.GONE);
                                    iv_call.setVisibility(View.GONE);
                                    iv_tick.setVisibility(View.GONE);
                                    iv_success.setVisibility(View.GONE);
                                    tv_header.setText("There has been a technical error.\n Please contact hello@digibarber.com");
                                    tv_header.setTextColor(Color.parseColor("#FFF94444"));

                                } else {

                                    et1.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et2.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et3.setBackgroundResource(R.mipmap.edit_verfi_red);
                                    et4.setBackgroundResource(R.mipmap.edit_verfi_red);

                                    iv_resend.setVisibility(View.VISIBLE);
                                    iv_call.setVisibility(View.VISIBLE);
                                    iv_tick.setVisibility(View.GONE);
                                    iv_success.setVisibility(View.GONE);
                                    count++;
                                    tv_header.setText("That code could not be verified \nPlease try again!");
                                    tv_header.setTextColor(Color.parseColor("#FFF94444"));
                                    Thread th = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //tv_header.setText("We've sent you a code to verify your mobile\n number");
                                                    //tv_header.setTextColor(getResources().getColor(R.color.colorGrey));
                                                    et1.setText("");
                                                    et2.setText("");
                                                    et3.setText("");
                                                    et4.setText("");
                                                    et1.requestFocus();
                                                    et1.setBackgroundResource(R.mipmap.edit_verfi_grey);
                                                    et2.setBackgroundResource(R.mipmap.edit_verfi_grey);
                                                    et3.setBackgroundResource(R.mipmap.edit_verfi_grey);
                                                    et4.setBackgroundResource(R.mipmap.edit_verfi_grey);
                                                }
                                            });
                                        }
                                    });
                                    th.start();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asyncTask.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);





        } else {
            Constants.showPopupInternet(activity);
        }

    }
*/

    private void locationServicesCheck() {


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps_enabled && !network_enabled) {
            Intent it = new Intent(SocialVerificationActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
        } else {
            Intent it = new Intent(SocialVerificationActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
        }
        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "birthday").apply();

    }


}
