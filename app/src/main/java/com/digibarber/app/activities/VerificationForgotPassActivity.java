package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

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

import com.android.volley.misc.AsyncTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by DIGIBARBER LTD on 18/11/17.
 */

public class VerificationForgotPassActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    String request_id = "";
    ImageView iv_cross, iv_tick, iv_success, iv_resend, iv_call, iv_sad_face;
    TextView tv_phone, tv_header;
    EditText et1, et2, et3, et4, et5, et6;
    String phone = "";
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    RelativeLayout rl_verfication_main;
    RelativeLayout ll_click_popup;
    int count = 0;
    private String From;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_verification);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bd = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode("+"+getIntent().getStringExtra("phone"));
        if (bd != null) {
            phone = "+"+bd.getString("phone");
            Log.d("Phone>>>",""+phone);
            //  From = bd.getString("From");
            //   password = bd.getString("password");
            request_id = bd.getString("request_id");
            email = bd.getString("email");
        }


        // callSendOTP(phone, "firsttime");
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
                //setTextCodeVerified();
                //callCancelOTP();
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
                    Constants.hideSoftKeyboard(VerificationForgotPassActivity.this);
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
                    Constants.hideSoftKeyboard(VerificationForgotPassActivity.this);
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
                    Constants.hideSoftKeyboard(VerificationForgotPassActivity.this);
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
                    //Constants.hideSoftKeyboard(VerificationForgotPassActivity.this);
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
                    Constants.hideSoftKeyboard(VerificationForgotPassActivity.this);
                }

            }
        });

    }

    void setTextCodeVerified() {
        tv_header.setText("We've sent you a code to verify your mobile\n number");
        tv_header.setTextColor(Color.parseColor("#FF454545"));
    }

    private void locationServicesCheck() {


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gps_enabled && !network_enabled) {
            Intent it = new Intent(VerificationForgotPassActivity.this, EnableLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            startActivity(it);
        } else {
            Intent it = new Intent(VerificationForgotPassActivity.this, EnableLocationActivity.class);
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

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+"+number, 60,
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
            Toast.makeText(VerificationForgotPassActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        Log.d("VerificationId>>>",""+code+":::::"+verificationId);
       /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);*/
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
                            Intent it = new Intent(VerificationForgotPassActivity.this, ResetPassword.class);
                            it.putExtra("email", email);
                            startActivity(it);
                        } else {
                            et1.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et2.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et3.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et4.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et5.setBackgroundResource(R.mipmap.edit_verfi_red);
                            et6.setBackgroundResource(R.mipmap.edit_verfi_red);

                            iv_resend.setVisibility(View.VISIBLE);
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
                            Toast.makeText(VerificationForgotPassActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
