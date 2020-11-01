package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText first_name, last_name, email, password, phone;
    TextView tv_text_error_f_name, tv_text_error_l_name, tv_text_error_email, tv_text_error_password, tv_text_error_phone;
    ImageView iv_eye_icon, iv_line_f_name;
    ImageView iv_line_l_name;
    ImageView iv_line_email;
    ImageView img_terms;
    ImageView iv_line_password;
    ImageView iv_line_phone;
    FrameLayout fl_lets_go;
    ImageView google_icon;
    boolean showPassword = true;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    private String android_id = "";
    private String login_type = "";
    private String emailstr = "";
    String first_namestr = "";

    private String last_namestr = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);


        Toast.makeText(getApplicationContext(), "Signup Screen", Toast.LENGTH_LONG).show();
        iv_eye_icon = findViewById(R.id.iv_eye_icon);
        fl_lets_go = findViewById(R.id.fl_lets_go);
        tv_text_error_l_name = findViewById(R.id.tv_text_error_l_name);
        tv_text_error_f_name = findViewById(R.id.tv_text_error_f_name);
        tv_text_error_email = findViewById(R.id.tv_text_error_email);
        tv_text_error_password = findViewById(R.id.tv_text_error_password);
        tv_text_error_phone = findViewById(R.id.tv_text_error_phone);
        google_icon = (ImageView) findViewById(R.id.google_icon);

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.editTextPhone);

        iv_line_f_name = findViewById(R.id.iv_line_f_name);
        iv_line_l_name = findViewById(R.id.iv_line_l_name);
        iv_line_email = findViewById(R.id.iv_line_email);
        img_terms = findViewById(R.id.img_terms);
        iv_line_password = findViewById(R.id.iv_line_password);
        iv_line_phone = findViewById(R.id.iv_line_phone);


        first_name.addTextChangedListener(new CustomTextWatcher(first_name, tv_text_error_f_name, iv_line_f_name));
        last_name.addTextChangedListener(new CustomTextWatcher(last_name, tv_text_error_l_name, iv_line_l_name));
        email.addTextChangedListener(new CustomTextWatcher(email, tv_text_error_email, iv_line_email));
        password.addTextChangedListener(new CustomTextWatcher(password, tv_text_error_password, iv_line_password));
        phone.addTextChangedListener(new CustomTextWatcher(phone, tv_text_error_phone, iv_line_phone));

        iv_eye_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (showPassword) {
                    showPassword = false;
                    iv_eye_icon.setImageResource(R.drawable.close_eyes);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    showPassword = true;
                    password.setTransformationMethod(null);
                    iv_eye_icon.setImageResource(R.drawable.eyes_open);
                }
            }
        });
        img_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUpActivity.this, TermConditionActiivty.class));

            }
        });
        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(SignUpActivity.this)
                        .enableAutoManage(SignUpActivity.this, SignUpActivity.this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                login_type = "social";
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
        first_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL) {
                    if (Constants.EmptyText(SignUpActivity.this, first_name.getText().toString(), getResources().getString(R.string.empty_first_name), first_name)) {

                    } else if (first_name.getText().toString().length() < 3) {
                        Constants.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_first_name), first_name);
                    } else if (!Constants.namevalidation(first_name.getText().toString())) {
                        Constants.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_first_name), first_name);
                    }
                    //attemptLogin();
                    return true;
                }

                return false;
            }
        });
        last_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL) {
                   /* if (ZoomConst.Empty(SignUpActivity.this, last_name.getText().toString(), getResources().getString(R.string.empty_last_name), last_name)) {

                    } else if (last_name.getText().toString().length() < 3) {
                        ZoomConst.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_last_name), last_name);
                    } else if (!ZoomConst.namevalidation(last_name.getText().toString())) {
                        ZoomConst.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_last_name), last_name);
                    }*/
                    callValidation();

                    //attemptLogin();
                    return true;
                }

                return false;
            }
        });

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL) {
                  /*  if (ZoomConst.Empty(SignUpActivity.this, email.getText().toString(), getResources().getString(R.string.empty_email), email)) {

                    } else if (!ZoomConst.isEmailValid(email.getText().toString())) {
                        ZoomConst.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.email_valid), email);
                    }*/
                    callValidation();
                    //attemptLogin();
                    return true;
                }

                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    if (ZoomConst.Empty(SignUpActivity.this, password.getText().toString(), getResources().getString(R.string.empty_password), password)) {
//
//                    } else if (password.getText().toString().length() < 6) {
//                        ZoomConst.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_password), password);
//                    }
                    callValidation();

                    // attemptLogin();
                    return true;
                }
                return false;
            }
        });


        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                  /*  if (ZoomConst.Empty(SignUpActivity.this, phone.getText().toString(), getResources().getString(R.string.empty_phone), phone)) {

                    } else if (phone.getText().toString().length() < 10) {
                        ZoomConst.ErrorMessages(SignUpActivity.this, getResources().getString(R.string.minimum_phone), phone);
                    }*/
                    callValidation();

                    // attemptLogin();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("loginParams", requestCode + " !");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            Log.v("SSSSSS", statusCode + " !");
            Log.v("loginParams", statusCode + " !");
            // System.out.println(" ** Google Sign In ** "+statusCode);

            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("TAG", "handleSignInResult:" + result.isSuccess());
        Log.v("loginParams", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.


            GoogleSignInAccount acct = result.getSignInAccount();
            Log.v("loginParams", acct.getEmail() + " ---- " + acct.getDisplayName() + " ---- " + acct.getId() + " ---- " + acct.getIdToken());
            String personPhotoUrl = "", googleid = "";
            if (acct != null) {
                if (acct.getPhotoUrl() != null)
                    personPhotoUrl = acct.getPhotoUrl().toString();
                if (acct.getEmail() != null)
                    emailstr = acct.getEmail();
                if (acct.getId() != null)

                    googleid = acct.getId();
                if (acct.getGivenName() != null)

                    first_namestr = acct.getGivenName();
                if (acct.getFamilyName() != null)

                    last_namestr = acct.getFamilyName();
                SocialLogin("" + googleid);
                Log.e("TAG", "Name: " + first_name + ", email: " + email
                        + ", Image: " + personPhotoUrl);
            }
        }
        signOut();
    }

    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.stopAutoManage(this);

            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

    }

    private void SocialLogin(final String socialId) {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(SignUpActivity.this);

            Log.d("api call post", Constants.Social_Login);
            Log.d("loginParams", "-----------------");
            Log.d("loginParams", Constants.Social_Login);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Social_Login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("TestRes", "** Url **" + Constants.Social_Login);
                            Log.e("TestRes", "** RESPONSE **" + response);
                            Log.d("loginParams", "** RESPONSE **" + response);
                            Log.d("loginParams", "-----------------");
                            Constants.dismissProgress();
                            //   {"user_email":"sukhyoyo@gmail.com","birthdate":"12 December 1993","user_name":"sukh","is_freezed":"0","last_name":"yoyo","bank_detail":"","message":"Login Successfully","token":"fe2014c8-a2fb-4920-aebd-b06e6a637d7e","profile_status":"register","profile_image":"","balance":"£ 0.0","blocked":"0","user_id":"35","phone":"09530959517","success":"true","mangopayId":"39112014"}
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                String message = jsonobj.getString("message");
                                if (!response_values.equalsIgnoreCase("false")) {


                                    if (jsonobj.getString("blocked").equalsIgnoreCase("1")) {
                                        Constants.showPopupWithMsg(SignUpActivity.this, "You are blocked", "Alert!");
                                    } else if (jsonobj.getString("is_freezed").equalsIgnoreCase("1")) {

                                        // self.displayValidationMessage(alert: "", msg: "You are freezed")
                                        Constants.showPopupWithMsg(SignUpActivity.this, "You are freezed", "Alert!");


                                    } else {


                                        String user_id = jsonobj.getString("user_id");
                                        String token = jsonobj.getString("token");
                                        String balance = jsonobj.getString("balance");
                                        String user_name = jsonobj.getString("user_name");
                                        String last_name = jsonobj.getString("last_name");
                                        String email = jsonobj.getString("user_email");
                                        String phone = jsonobj.getString("phone");
                                        String bank_detail = "";
                                        String profile_image = jsonobj.getString("profile_image");
                                        String workplace = "";
                                        String address = "";
                                        String strlon = "";
                                        String strlat = "";
                                        String mangopayId = jsonobj.getString("mangopayId");
                                        try {
                                            workplace = jsonobj.getString("workplace");

                                        } catch (Exception e) {
                                            Log.e("TestRes", "** Exception **" + e);
                                        }
                                        try {
                                            address = jsonobj.getString("address");
                                        } catch (Exception e) {
                                            Log.e("TestRes", "** Exception **" + e);
                                        }
                                        try {
                                            strlon = jsonobj.getString("lon");
                                        } catch (Exception e) {
                                            Log.e("TestRes", "** Exception **" + e);
                                        }
                                        try {
                                            strlat = jsonobj.getString("lat");
                                        } catch (Exception e) {
                                            Log.e("TestRes", "** Exception **" + e);
                                        }


                                        String profile_status = "";
                                        try {
                                            profile_status = jsonobj.getString("profile_status");
                                        } catch (Exception e) {
                                            Log.e("TestRes", "** Exception **" + e);

                                        }


                                        String struser_name = "";
                                        String strlast_name = "";
                                        if (user_name != null && !user_name.equalsIgnoreCase("") && !user_name.equalsIgnoreCase("null")) {
                                            struser_name = user_name.substring(0, 1).toUpperCase() + user_name.substring(1).toLowerCase();
                                        }


                                        if (last_name != null && !last_name.equalsIgnoreCase("") && !last_name.equalsIgnoreCase("null")) {
                                            strlast_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1).toLowerCase();
                                        }


                                        if (jsonobj.has("services")) {
                                            JSONArray Jaservices = jsonobj.getJSONArray("services");
                                            prefs.edit().putString(Constants.KEY_SERVICES, Jaservices.toString()).apply();
                                        } else {
                                            prefs.edit().putString(Constants.KEY_SERVICES, "").apply();

                                        }

                                        if (jsonobj.has("gallery")) {
                                            JSONArray jaGallery = jsonobj.getJSONArray("gallery");
                                            prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, jaGallery.toString()).apply();
                                        } else {
                                            prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, "").apply();
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
                                        prefs.edit().putString(Constants.KEY_MANGOPAY_ID, mangopayId).apply();
                                        prefs.edit().putString(Constants.KEY_BALANCE, balance).apply();
                                        prefs.edit().putString(Constants.KEY_WORKPLACE, workplace).apply();
                                        prefs.edit().putString(Constants.KEY_ADDRESS, address).apply();

                                        prefs.edit().putString(Constants.KEY_LAT, strlat).apply();
                                        prefs.edit().putString(Constants.KEY_LOG, strlon).apply();
                                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                                        locationServicesCheck(profile_status);

                                    }
                                } else if (message.contains("User Already Exist As client")) {

                                    Constants.showPopupWithMsg(SignUpActivity.this, "You have already registered as a Client", "Register");

                                } else if (message.contains("There is a issue with your account.")) {


                                    Constants.showPopupFrozen(SignUpActivity.this, message);

                                } else {
                                    Intent it = new Intent(SignUpActivity.this, SocailLoginDetailActivity.class);
                                    it.putExtra("first_name", first_namestr);
                                    it.putExtra("last_name", last_namestr);
                                    it.putExtra("email", emailstr);
                                    it.putExtra("socialId", "" + socialId);
                                    startActivity(it);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Log.d("loginParams", "Error: " + error.getMessage());
                    Log.d("loginParams", "-----------------");
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    SharedPreferences prefs = getSharedPreferences("Barber", MODE_PRIVATE);
                    String restoredText = prefs.getString(Constants.KEY_DEVICE_ID, "");
                    String prevtoken = prefs.getString(Constants.KEY_DEVICE_ID, "");

                    if (prevtoken.equalsIgnoreCase("")) {
                        prefs.edit().putString(Constants.KEY_DEVICE_ID, restoredText).apply();

                    }
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("device_id", "" + prefs.getString(Constants.KEY_DEVICE_ID, "").trim());
                    params.put("device_type", "" + Constants.APP_PLATFORM);
                    params.put("user_type", "" + Constants.USER_TYPE);
                    params.put("social_id", socialId);
                    params.put("uniqueId", "" + android_id.trim());
                    Log.e("TestRes", "here params:" + params);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    private void locationServicesCheck(String profile_status) {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            //this.setContentView(R.layout.enable_location);
            if (profile_status != null && !profile_status.equalsIgnoreCase("")) {

                if (profile_status.equalsIgnoreCase("register")) {
                    Intent it = new Intent(SignUpActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(SignUpActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(SignUpActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(SignUpActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(SignUpActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(SignUpActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }

            } else {
                Intent it = new Intent(SignUpActivity.this, EnableLocationActivity.class);
                it.putExtra("From", "Login");
                startActivity(it);
                finish();
            }


        } else {

            if (profile_status != null && !profile_status.equalsIgnoreCase("")) {

                if (profile_status.equalsIgnoreCase("register")) {
                    Intent it = new Intent(SignUpActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(SignUpActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(SignUpActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(SignUpActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(SignUpActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(SignUpActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }
            } else {
                Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }

    private void callValidation() {

        iv_line_f_name = findViewById(R.id.iv_line_f_name);
        iv_line_l_name = findViewById(R.id.iv_line_l_name);
        iv_line_email = findViewById(R.id.iv_line_email);
        iv_line_password = findViewById(R.id.iv_line_password);
        iv_line_phone = findViewById(R.id.iv_line_phone);


        if (Constants.EmptyText(SignUpActivity.this, first_name.getText().toString(), getResources().getString(R.string.empty_email), first_name)) {

            iv_line_f_name.setImageResource(R.mipmap.red_line_first_name);
            tv_text_error_f_name.setVisibility(View.VISIBLE);
            tv_text_error_f_name.setText(getResources().getString(R.string.empty_first_name));

        } else if (first_name.getText().toString().length() < 3) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
            iv_line_f_name.setImageResource(R.mipmap.red_line_first_name);
            tv_text_error_f_name.setVisibility(View.VISIBLE);
            tv_text_error_f_name.setText(getResources().getString(R.string.minimum_first_name));

        } else if (Constants.EmptyText(SignUpActivity.this, last_name.getText().toString(), getResources().getString(R.string.empty_password), last_name)) {

            iv_line_l_name.setImageResource(R.mipmap.red_line_first_name);
            tv_text_error_l_name.setVisibility(View.VISIBLE);
            tv_text_error_l_name.setText(getResources().getString(R.string.empty_last_name));

        } else if (last_name.getText().toString().length() < 3) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
            iv_line_l_name.setImageResource(R.mipmap.red_line_first_name);
            tv_text_error_l_name.setVisibility(View.VISIBLE);
            tv_text_error_l_name.setText(getResources().getString(R.string.minimum_last_name));

        } else if (Constants.EmptyText(SignUpActivity.this, email.getText().toString(), getResources().getString(R.string.empty_password), email)) {

            iv_line_email.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_email.setVisibility(View.VISIBLE);
            tv_text_error_email.setText(getResources().getString(R.string.empty_email));

        } else if (!Constants.isEmailValid(email.getText().toString())) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.email_valid), mEmailView);

            iv_line_email.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_email.setVisibility(View.VISIBLE);
            tv_text_error_email.setText(getResources().getString(R.string.email_valid));

        } else if (Constants.EmptyText(SignUpActivity.this, password.getText().toString(), getResources().getString(R.string.empty_password), password)) {

            iv_line_password.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_password.setVisibility(View.VISIBLE);
            tv_text_error_password.setText(getResources().getString(R.string.empty_password));

        } else if (!Constants.isValidPasswordMinSixCharacter(password.getText().toString())) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
            iv_line_password.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_password.setVisibility(View.VISIBLE);
            tv_text_error_password.setText(getResources().getString(R.string.minimum_password));
        } else if (!Constants.isValidPasswordMinTwoNumber(password.getText().toString())) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
            iv_line_password.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_password.setVisibility(View.VISIBLE);
            tv_text_error_password.setText(getResources().getString(R.string.minimum_number));
        } else if (Constants.EmptyText(SignUpActivity.this, phone.getText().toString(), getResources().getString(R.string.empty_password), phone)) {

            iv_line_phone.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_phone.setVisibility(View.VISIBLE);
            tv_text_error_phone.setText(getResources().getString(R.string.empty_phone));

        } else if (phone.getText().toString().length() < 10) {

            iv_line_phone.setImageResource(R.mipmap.line_signup_red);
            tv_text_error_phone.setVisibility(View.VISIBLE);
            tv_text_error_phone.setText(getResources().getString(R.string.minimum_phone));
        } else {

            checkUserExsits(email.getText().toString());

        }
    }


    public void AlreadyResiterd() {
        final Dialog dialog_first = new Dialog(SignUpActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_signup_exists);
        dialog_first.setCancelable(false);

        LinearLayout ll_tryagin = dialog_first.findViewById(R.id.ll_tryagin);
        LinearLayout ll_recovery = dialog_first.findViewById(R.id.ll_recovery);
        LinearLayout ll_facebook = dialog_first.findViewById(R.id.ll_facebook);

        ll_tryagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_first.dismiss();
                iv_line_email.setImageResource(R.mipmap.line_signup_grey);
                tv_text_error_email.setVisibility(View.INVISIBLE);
            }
        });

        ll_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                dialog_first.dismiss();
            }
        });

        ll_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                dialog_first.dismiss();


            }
        });


        dialog_first.show();
    }


    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;
        ImageView ivLine;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror, ImageView ivLine) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
            this.ivLine = ivLine;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == View.VISIBLE) {
                tv_errror.setVisibility(View.INVISIBLE);

                if (mEditText.getId() == R.id.first_name || mEditText.getId() == R.id.last_name) {
                    ivLine.setImageResource(R.mipmap.first_name_line);
                } else {
                    ivLine.setImageResource(R.mipmap.line_signup_grey);
                }

            }
        }
    }


    /* Sign In layout click */
    public void signInClick(View v) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    /* Sign Up Button click */
    public void signUpClick(View v) {
        callValidation();
    }

    private void checkUserExsits(final String stremail) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(SignUpActivity.this);
            Log.e("Url", Constants.User_Exist_Validation);
            Log.d("api call post", Constants.User_Exist_Validation);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.User_Exist_Validation,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(" User_Exist_Validation", response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                String is_exist = jsonobj.getString("is_exist");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    if (is_exist.equalsIgnoreCase("true")) {
                                        AlreadyResiterd();
                                        iv_line_email.setImageResource(R.mipmap.line_signup_red);
                                        tv_text_error_email.setVisibility(View.VISIBLE);
                                        tv_text_error_email.setText(getResources().getString(R.string.user_email_exists));
                                    } else {
                                        String phoneNumber = getResources().getString(R.string.uk_code) + phone.getText().toString();
                                        sendVerificationCode(phoneNumber);
                                    }
                                } else {
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", "" + stremail.trim());
                    Log.e(" User_Exist_Validation", params + "");

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
            //System.out.println(" ** PLease connect to Internet **");
        }
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //verificationId = s;

            String countryName = "United Kingdom";

            String phoneNumber = phone.getText().toString();
            Intent it = new Intent(SignUpActivity.this, VerificationActivity.class);
            it.putExtra("first_name", first_name.getText().toString().replace(" ", ""));
            it.putExtra("last_name", last_name.getText().toString().replace(" ", ""));
            it.putExtra("email", email.getText().toString());
            it.putExtra("password", password.getText().toString());
            it.putExtra("phone", phoneNumber);
            it.putExtra("verificationId", s);
            it.putExtra("From", "Normal");
            it.putExtra("forceResendingToken", forceResendingToken);
            it.putExtra("country_name", "United KingdomF");
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "signup").apply();
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_F_NAME, first_name.getText().toString().replace(" ", "")).apply();
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_L_NAME, last_name.getText().toString().replace(" ", "")).apply();
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_EMAIl, email.getText().toString()).apply();
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, password.getText().toString()).apply();
            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_PHONE, phone.getText().toString()).apply();
            startActivity(it);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Log.i("test_test", "code " + code);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void sendVerificationCode(String number) {
        // progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 0, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }


    /* Back click */
    public void backClick(View v) {
        this.finish();
    }


}


