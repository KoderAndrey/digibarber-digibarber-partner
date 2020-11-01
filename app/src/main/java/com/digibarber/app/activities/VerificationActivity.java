package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

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
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by DIGIBARBER LTD on 18/11/17.
 */

public class VerificationActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private String verificationId = "";
    String request_id = "";
    ImageView iv_cross, iv_tick, iv_success, iv_resend, iv_call, iv_sad_face;
    TextView tv_phone;
    EditText et1, et2, et3, et4, et5, et6;
    TextView tv_header;
    String phone = "";
    String countryName = "";
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String socialId;
    private String birth_date;
    private String birthdate;
    private String android_id = "";

    RelativeLayout rl_verfication_main, ll_click_popup;
    int count = 0;
    private String From;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_verification);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bd = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(getIntent().getStringExtra("phone"));

        if (bd != null) {
            phone = bd.getString("phone");
            countryName = bd.getString("country_name");
            From = bd.getString("From");
            verificationId = bd.getString("verificationId");
            Log.i("loginParams",verificationId + "- - -- - ");
            //   forceResendToken = getIntent().getSerializableExtra("forceResendingToken");

            if (From.equalsIgnoreCase("SocialLogin")) {
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
                socialId = bd.getString("socialId");
                email = bd.getString("email");
                birth_date = bd.getString("birth_date");
                birthdate = bd.getString("birthdate");
            } else if (!From.equalsIgnoreCase("changeprivateinfo")) {
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
            }
        }
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
        et5 = (EditText) findViewById(R.id.et5);
        et6 = (EditText) findViewById(R.id.et6);
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
                et1.setText("");
                et2.setText("");
                et3.setText("");
                et4.setText("");
                et5.setText("");
                et6.setText("");
                et1.requestFocus();
                et1.setBackgroundResource(R.mipmap.edit_verfi_grey);
                et2.setBackgroundResource(R.mipmap.edit_verfi_grey);
                et3.setBackgroundResource(R.mipmap.edit_verfi_grey);
                et4.setBackgroundResource(R.mipmap.edit_verfi_grey);
                et5.setBackgroundResource(R.mipmap.edit_verfi_grey);
                et6.setBackgroundResource(R.mipmap.edit_verfi_grey);
                sendVerificationCode(getIntent().getStringExtra("phone"));

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
                Log.e("Value Ed1", et1.getText().toString().trim());
                setTextCodeVerified();
                if (et1.getText().toString().trim().isEmpty()) {
                    et1.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et2.requestFocus();
                    et1.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 1");
                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(VerificationActivity.this);
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
                Log.e("Value Ed2", et2.getText().toString().trim());
                if (et2.getText().toString().trim().isEmpty()) {
                    et2.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et3.requestFocus();
                    et2.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 2");
                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(VerificationActivity.this);
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
                Log.e("Value Ed3", et3.getText().toString().trim());
                if (et3.getText().toString().trim().isEmpty()) {
                    et3.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et4.requestFocus();
                    et3.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }

                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 3");
                    //callVerifyOTP();
                    Constants.hideSoftKeyboard(VerificationActivity.this);
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
                Log.e("Value Ed4", et4.getText().toString().trim());
                if (et4.getText().toString().trim().isEmpty()) {
                    et4.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et5.requestFocus();
                    et4.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }
                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 4");
                    //callVerifyOTP();
                    //Constants.hideSoftKeyboard(VerificationActivity.this);
                }

            }
        });
        et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();
                Log.e("Value Ed4", et5.getText().toString().trim());
                if (et5.getText().toString().trim().isEmpty()) {
                    et5.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et6.requestFocus();
                    et5.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }
                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty() && !et5.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 5");
                    //    callVerifyOTP();
                    // Constants.hideSoftKeyboard(VerificationActivity.this);
                }

            }
        });
        et6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {

                setTextCodeVerified();
                Log.e("Value Ed6", et6.getText().toString().trim());
                if (et6.getText().toString().trim().isEmpty()) {
                    et6.setBackgroundResource(R.mipmap.edit_verfi_grey);
                } else {
                    et6.setBackgroundResource(R.mipmap.edit_verfi_blue);
                }
                if (!et1.getText().toString().trim().isEmpty() && !et2.getText().toString().trim().isEmpty() && !et3.getText().toString().trim().isEmpty() && !et4.getText().toString().trim().isEmpty() && !et5.getText().toString().trim().isEmpty() && !et6.getText().toString().trim().isEmpty()) {
                    Log.e("All fill", "In 6");
                    //    callVerifyOTP();
                    verifyCode(et1.getText().toString() + "" + et2.getText().toString() + "" + et3.getText().toString() + "" + et4.getText().toString() + "" + et5.getText().toString() + "" + et6.getText().toString());
                    Constants.hideSoftKeyboard(VerificationActivity.this);
                }

            }
        });

    }
    void setTextCodeVerified() {
        tv_header.setText("We've sent you a code to verify your mobile\n number");
        tv_header.setTextColor(Color.parseColor("#FF454545"));
    }
    private void sendVerificationCode(String number) {
        // progressBar.setVisibility(View.VISIBLE);
        Log.i("loginParams",number);
        number = "+44"+number;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 0,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            //  verifyCode("123456");
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e(VerificationActivity.class.getCanonicalName(), "firebase", e);
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("loginParams",e.getMessage());
        }
    };
    private void verifyCode(String code) {
        PhoneAuthCredential credential = null;
        if (verificationId != null) {
            credential = PhoneAuthProvider.getCredential(verificationId, code);
            if (credential != null) {
                signInWithCredential(credential);
            } else {
                Toast.makeText(VerificationActivity.this, "failed to get verification Id", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(VerificationActivity.this, "failed to verify code", Toast.LENGTH_LONG).show();
        }
    }
    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            et1.setBackgroundResource(R.mipmap.edit_verfi_green);
                            et2.setBackgroundResource(R.mipmap.edit_verfi_green);
                            et3.setBackgroundResource(R.mipmap.edit_verfi_green);
                            et4.setBackgroundResource(R.mipmap.edit_verfi_green);
                            et5.setBackgroundResource(R.mipmap.edit_verfi_green);
                            et6.setBackgroundResource(R.mipmap.edit_verfi_green);
                            iv_resend.setVisibility(View.GONE);
                            iv_tick.setVisibility(View.VISIBLE);
                            iv_success.setVisibility(View.VISIBLE);
                            tv_header.setText("We have verified that number! Cant wait for\nyou to see the app!");
                            tv_header.setTextColor(Color.parseColor("#FF454545"));
                            callSignUp();

                        } else {
                            et1.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et2.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et3.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et4.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et5.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et6.setBackgroundResource(R.mipmap.edit_verfi_red);

                            iv_resend.setVisibility(View.VISIBLE);
                            // iv_call.setVisibility(View.VISIBLE);
                            iv_tick.setVisibility(View.GONE);
                            iv_success.setVisibility(View.GONE);

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
                                            et1.setText("");
                                            et2.setText("");
                                            et3.setText("");
                                            et4.setText("");
                                            et5.setText("");
                                            et6.setText("");
                                            et1.requestFocus();
                                            et1.setBackgroundResource(R.mipmap.edit_verfi_green);
                                            et2.setBackgroundResource(R.mipmap.edit_verfi_green);
                                            et3.setBackgroundResource(R.mipmap.edit_verfi_green);
                                            et4.setBackgroundResource(R.mipmap.edit_verfi_green);
                                            et5.setBackgroundResource(R.mipmap.edit_verfi_green);
                                            et6.setBackgroundResource(R.mipmap.edit_verfi_green);
                                        }
                                    });
                                }
                            });
                            th.start();
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void callSignUp() {
        signUpService();
    }
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
                            Log.e("Deep", "BirthDay response:" + response);
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
                                    String bank_detail = "";
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
                                    prefs.edit().putString(Constants.COUNTRY_NAME, "United Kingdom's").apply();
                                    Toast.makeText(getApplicationContext(), "SignUp successfully.", Toast.LENGTH_LONG).show();

                                    locationServicesCheck();
                                } else {
                                    if (jsonobj != null && jsonobj.has("message"))
                                        Constants.showPopupFrozen(VerificationActivity.this, jsonobj.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    params.put("birthdate", birthdate);
                    params.put("birth_date", birth_date);
                    params.put("phone", phone);
                    params.put("device_type", Constants.APP_PLATFORM);
                    params.put("device_id", prefs.getString(Constants.KEY_DEVICE_ID, ""));
                    params.put("user_type", Constants.USER_TYPE);
                    if (From.equalsIgnoreCase("SocialLogin")){
                        params.put("social_id",socialId);
                    }
                    params.put("login_type", (!From.equalsIgnoreCase("SocialLogin")) ? "normal" : "social");// Need to ask the type
                    params.put("uniqueId ", Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID));
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
            Intent it = new Intent(VerificationActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
            finish();
        } else {
            Intent it = new Intent(VerificationActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
            finish();
        }

    }
}