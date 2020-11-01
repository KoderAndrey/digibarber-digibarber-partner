package com.digibarber.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.apicalls.ApiClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private final int REQUEST_CODE_PERMISSION = 39;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private CallbackManager mCallbackManager;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView text1, current_location_text, location_white_text, noti_text2;

    private ImageView iv_forget_pass,iv_conatct_us;
    private ImageView iv_connect_with,do_account;

    Button location_services_on, enable_noti;
    private View mProgressView;
    private View mLoginFormView;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    ImageView fb_icon, google_icon;
    private String android_id = "";
    private String login_type = "";
    private String email = "";
    String first_name = "";

    private String last_name = "";
    TextView tv_text_error_eamil;
    TextView tv_text_error_password;

    ImageView iv_line_email;
    ImageView iv_line_pass;

    int count = 0;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Here refreshedToken", "" + refreshedToken);
        prefs.edit().putString(Constants.KEY_DEVICE_ID, refreshedToken).apply();
        SharedPreferences.Editor editor = getSharedPreferences("Barber", MODE_PRIVATE).edit();
        editor.putString(Constants.KEY_DEVICE_ID, refreshedToken);
        editor.apply();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("ApiResponse", "Success");

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            Log.e("Facebook onCompleted", object.toString());
                                            Log.e("Facebook GraphResponse", response.toString());
                                            String email_id = "";
                                            if (object.has("email")) {
                                                email_id = object.getString("email");
                                            }
                                            String gender = "";
                                            if (object.has("gender")) {
                                                gender = object.getString("gender");
                                            }


                                            String strFirst_name = "";
                                            if (object.has("first_name")) {
                                                strFirst_name = object.getString("first_name");
                                            }
                                            String strLast_name = "";
                                            if (object.has("last_name")) {
                                                strLast_name = object.getString("last_name");
                                            }

                                            long fb_id = object.getLong("id"); //use this for logout
                                            Log.e("AFter Fb Login", email_id + gender + strFirst_name + strLast_name);

                                            first_name = strFirst_name;
                                            last_name = strLast_name;
                                            email = email_id;

                                            //loginService();
                                            Log.i("kzr", fb_id + "");

                                            SocialLogin("" + fb_id);
                                            LoginManager.getInstance().logOut();

                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block

                                        }
                                        LoginManager.getInstance().logOut();


                                    }

                                });
                        Bundle bundle = request.getParameters();
                        bundle.putString("fields", "id,first_name,last_name,email");
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("ApiResponse", "Cancel");
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("ApiResponse", "Error");
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        setContentView(R.layout.login_activity);

        /* For getting Hash Keys */
        printHashKey();

        boolean request_result = isStoragePermissionGranted();
        System.out.println(" ** Permission ** " + request_result);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        iv_forget_pass = (ImageView) findViewById(R.id.iv_forget_pass);
        iv_conatct_us = (ImageView) findViewById(R.id.iv_conatct_us);
        iv_connect_with = (ImageView) findViewById(R.id.iv_connect_with);
        do_account = (ImageView) findViewById(R.id.do_account);

        tv_text_error_eamil = (TextView) findViewById(R.id.tv_text_error_eamil);
        tv_text_error_password = (TextView) findViewById(R.id.tv_text_error_password);

        iv_line_email = (ImageView) findViewById(R.id.iv_line_email);
        iv_line_pass = (ImageView) findViewById(R.id.iv_line_pass);

        mEmailView.addTextChangedListener(new CustomTextWatcher(mEmailView, tv_text_error_eamil, iv_line_email));
        mPasswordView.addTextChangedListener(new CustomTextWatcher(mPasswordView, tv_text_error_password, iv_line_pass));


        location_services_on = (Button) findViewById(R.id.location_services_on);
        fb_icon = (ImageView) findViewById(R.id.fb_icon);
        google_icon = (ImageView) findViewById(R.id.google_icon);

        // mEmailView.setTypeface(custom_font);
        // mPasswordView.setTypeface(custom_font);


        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_NULL) {
                  /*  if (ZoomConst.Empty(LoginActivity.this, mEmailView.getText().toString(), getResources().getString(R.string.empty_email), mEmailView)) {
                    } else if (!ZoomConst.isEmailValid(mEmailView.getText().toString())) {
                        ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.email_valid), mEmailView);
                    }*/
                    callValidation();
                    //attemptLogin();
                    return true;
                }

                return false;
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    /*if (ZoomConst.Empty(LoginActivity.this, mPasswordView.getText().toString(), getResources().getString(R.string.empty_password), mPasswordView)) {

                    } else if (mPasswordView.getText().toString().length() < 6) {
                        ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
                    }*/
                    callValidation();

                    // attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mEmailView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                mEmailView.setCursorVisible(true);
                return false;
            }
        });

        mPasswordView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                mPasswordView.setCursorVisible(true);
                return false;
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        google_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                        .enableAutoManage(LoginActivity.this, LoginActivity.this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                login_type = "social";
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        fb_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                login_type = "social";
            }
        });
        SharedPreferences prefs = getSharedPreferences("Barber", MODE_PRIVATE);
        String restoredText = prefs.getString(Constants.KEY_DEVICE_ID, "");
        String prevtoken = prefs.getString(Constants.KEY_DEVICE_ID, "");
        iv_conatct_us.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"barber@digibarber.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(LoginActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (prevtoken.equalsIgnoreCase("")) {
            prefs.edit().putString(Constants.KEY_DEVICE_ID, restoredText).apply();

        }
    }

    private void displayMessage(Profile profile) {
        if (profile != null) {
            Log.d(" NAME ", profile.getName());
            // textView.setText(profile_notification.getName());
        }
    }

    private void SocialLogin(final String socialId) {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(LoginActivity.this);

            Log.d("api call post",Constants.Social_Login);
            Log.d("loginParams","-----------------");
            Log.d("loginParams",Constants.Social_Login);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Social_Login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("TestRes", "** Url **" + Constants.Social_Login);
                            Log.e("TestRes", "** RESPONSE **" + response);
                            Log.d("loginParams","** RESPONSE **" + response);
                            Log.d("loginParams","-----------------");
                            Constants.dismissProgress();
                            //   {"user_email":"sukhyoyo@gmail.com","birthdate":"12 December 1993","user_name":"sukh","is_freezed":"0","last_name":"yoyo","bank_detail":"","message":"Login Successfully","token":"fe2014c8-a2fb-4920-aebd-b06e6a637d7e","profile_status":"register","profile_image":"","balance":"£ 0.0","blocked":"0","user_id":"35","phone":"09530959517","success":"true","mangopayId":"39112014"}
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                String message = jsonobj.getString("message");
                                if (!response_values.equalsIgnoreCase("false")) {


                                    if (jsonobj.getString("blocked").equalsIgnoreCase("1")) {
                                        Constants.showPopupWithMsg(LoginActivity.this, "You are blocked", "Alert!");
                                    } else if (jsonobj.getString("is_freezed").equalsIgnoreCase("1")) {

                                        // self.displayValidationMessage(alert: "", msg: "You are freezed")
                                        Constants.showPopupWithMsg(LoginActivity.this, "You are freezed", "Alert!");


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
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                        locationServicesCheck(profile_status);

                                    }
                                } else if (message.contains("User Already Exist As client")) {

                                    Constants.showPopupWithMsg(LoginActivity.this, "You have already registered as a Client", "Register");

                                } else if (message.contains("There is a issue with your account.")) {


                                    Constants.showPopupFrozen(LoginActivity.this, message);

                                } else {
                                    Intent it = new Intent(LoginActivity.this, SocailLoginDetailActivity.class);
                                    it.putExtra("first_name", first_name);
                                    it.putExtra("last_name", last_name);
                                    it.putExtra("email", email);
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
                    Log.d("loginParams","Error: " + error.getMessage());
                    Log.d("loginParams","-----------------");
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

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequest", "called");
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Constants.showToast(LoginActivity.this, "Location permission not granted");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    public void showDialogTryAgain() {
        final Dialog dialog_first = new Dialog(LoginActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.pop_login);
        dialog_first.setCancelable(false);

        LinearLayout ll_tryagin = (LinearLayout) dialog_first.findViewById(R.id.ll_tryagin);
        LinearLayout ll_recovery = (LinearLayout) dialog_first.findViewById(R.id.ll_recovery);
        LinearLayout ll_facebook = (LinearLayout) dialog_first.findViewById(R.id.ll_facebook);

        ll_tryagin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_first.dismiss();
                iv_line_email.setImageResource(R.mipmap.line_grey_login_signup);
                tv_text_error_eamil.setVisibility(View.GONE);
                tv_text_error_password.setVisibility(View.GONE);
                iv_line_pass.setImageResource(R.mipmap.line_grey_login_signup);

            }
        });

        ll_recovery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                dialog_first.dismiss();
            }
        });

        ll_facebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                login_type = "social";
                dialog_first.dismiss();


            }
        });


        dialog_first.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }


    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        Log.v("loginParams", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.


            GoogleSignInAccount acct = result.getSignInAccount();
            Log.v("loginParams", acct.getEmail() + " ---- " + acct.getDisplayName() + " ---- "+acct.getId() + " ---- "+acct.getIdToken());
            String personPhotoUrl = "", googleid = "";
            if (acct != null) {
                if (acct.getPhotoUrl() != null)
                    personPhotoUrl = acct.getPhotoUrl().toString();
                if (acct.getEmail() != null)
                    email = acct.getEmail();
                if (acct.getId() != null)

                    googleid = acct.getId();
                if (acct.getGivenName() != null)

                    first_name = acct.getGivenName();
                if (acct.getFamilyName() != null)

                    last_name = acct.getFamilyName();
                SocialLogin("" + googleid);
                updateUI(true);

                Log.e(TAG, "Name: " + first_name + ", email: " + email
                        + ", Image: " + personPhotoUrl);
            } else {
                // Signed out, show unauthenticated UI.
                updateUI(false);
            }
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
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
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

        } else {

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /* Sign Up Layout click */
    public void signUpClick(View v) {
        callSignup();
    }

    @Override
    public void onBackPressed() {
        callSignup();
    }

    private void callSignup() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
    }

    /* Forgot Password text click */
    public void forgotPasswordClick(View v) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    /* Sign In Button click */
    public void signInButtonClick(View v) {
        callValidation();


    }

    private void callValidation() {
        login_type = "normal";
        if (Constants.EmptyText(LoginActivity.this, mEmailView.getText().toString(), getResources().getString(R.string.empty_email), mEmailView)) {

            iv_line_email.setImageResource(R.mipmap.line_red_signup_login);
            tv_text_error_eamil.setVisibility(View.VISIBLE);
            tv_text_error_eamil.setText(getResources().getString(R.string.empty_email));

        } else if (!Constants.isValidEmail(mEmailView.getText().toString())) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.email_valid), mEmailView);
            iv_line_email.setImageResource(R.mipmap.line_red_signup_login);
            tv_text_error_eamil.setVisibility(View.VISIBLE);
            tv_text_error_eamil.setText(getResources().getString(R.string.email_valid));

        } else if (Constants.EmptyText(LoginActivity.this, mPasswordView.getText().toString(), getResources().getString(R.string.empty_password), mPasswordView)) {


            iv_line_pass.setImageResource(R.mipmap.line_red_signup_login);
            tv_text_error_password.setVisibility(View.VISIBLE);
            tv_text_error_password.setText(getResources().getString(R.string.empty_password));

        } else if (mPasswordView.getText().toString().length() < 6) {
            // ZoomConst.ErrorMessages(LoginActivity.this, getResources().getString(R.string.password_valid), mPasswordView);
            iv_line_pass.setImageResource(R.mipmap.line_red_signup_login);
            tv_text_error_password.setVisibility(View.VISIBLE);
            tv_text_error_password.setText(getResources().getString(R.string.password_valid));

        } else {
            email = mEmailView.getText().toString();
            loginService();
        }
    }

    /* Check location services enable or not */
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
                    Intent it = new Intent(LoginActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(LoginActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(LoginActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(LoginActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(LoginActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(LoginActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }

            } else {
                Intent it = new Intent(LoginActivity.this, EnableLocationActivity.class);
                it.putExtra("From", "Login");
                startActivity(it);
                finish();
            }


        } else {

            if (profile_status != null && !profile_status.equalsIgnoreCase("")) {

                if (profile_status.equalsIgnoreCase("register")) {
                    Intent it = new Intent(LoginActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(LoginActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(LoginActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(LoginActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(LoginActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(LoginActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }
            } else {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }


        }
    }

    /* Enable Location Services Button */
    public void locationServicesClick(View v) {
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
    }

    /* Getting debug Hash Key for Facebook integration */
    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wa.digibarber",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;
        private ImageView iv_line;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror, ImageView iv_line) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
            this.iv_line = iv_line;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {


            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == View.VISIBLE) {
                tv_errror.setVisibility(View.INVISIBLE);
                iv_line.setImageResource(R.mipmap.line_grey_login_signup);
            }
        }
    }


    /* Login service to check */
    private void loginService() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();

            Log.i("call_login_check",Constants.Login);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("call_login_check", response);
                            try {
                                JSONObject jsonobj = new JSONObject(response);

                                String response_values = jsonobj.getString("success");

                                String message = jsonobj.getString("message");

                                if (!response_values.equalsIgnoreCase("false")) {

                                    String user_id = jsonobj.getString("user_id");
                                    String token = jsonobj.getString("token");
                                    String balance = jsonobj.getString("balance");
                                    String user_name = jsonobj.getString("user_name");
                                    String last_name = jsonobj.getString("last_name");
                                    String email = jsonobj.getString("user_email");
                                    String phone = jsonobj.getString("phone");
                                    String bank_detail = "";
                                    String profile_image = jsonobj.getString("profile_image");
                                    String mangopayId = jsonobj.getString("mangopayId");
                                    String workplace = jsonobj.getString("workplace");
                                    String address = jsonobj.getString("address");

                                    String lon = jsonobj.getString("lon");
                                    String lat = jsonobj.getString("lat");

                                    String profile_status = jsonobj.getString("profile_status");

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


                                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                    locationServicesCheck(profile_status);
                                } else {
                                    if (count > 2) {
                                        showDialogTryAgain();
                                    }
                                    if (message.contains("Wrong Password!")) {
                                        iv_line_pass.setImageResource(R.mipmap.line_red_signup_login);
                                        tv_text_error_password.setVisibility(View.VISIBLE);
                                        tv_text_error_password.setText("Incorrect password - Please try again");
                                        count++;
                                    } else if (message.contains("Wrong Username!")) {

                                        iv_line_email.setImageResource(R.mipmap.line_red_signup_login);
                                        tv_text_error_eamil.setVisibility(View.VISIBLE);
                                        tv_text_error_eamil.setText("Incorrect email - Please check and try again");
                                        count++;
                                    } else if (message.contains("There is a issue with your account.")) {
                                        Constants.showPopupFrozen(LoginActivity.this, "There has been an issue with your account. Please contact barber@digibarber.com");

                                    } else if (message.contains("Login Failed! Your account has been deleted by admin!")) {
                                        Constants.showPopupFrozen(LoginActivity.this, "There has been an issue with your account. Please contact barber@digibarber.com");
                                    } else {
                                        iv_line_pass.setImageResource(R.mipmap.line_red_signup_login);
                                        iv_line_email.setImageResource(R.mipmap.line_red_signup_login);
                                        tv_text_error_password.setVisibility(View.VISIBLE);
                                        tv_text_error_password.setText("Incorrect password - Please try again");
                                        tv_text_error_eamil.setVisibility(View.VISIBLE);
                                        tv_text_error_eamil.setText("Incorrect email - Please check and try again");
                                        count++;
                                    }

                                    //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //checkConnection();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    // ZoomConst.showCustomAlert(MainActivity.this, getResources().getString(R.string.app_name), error.getMessage(), getResources().getString(R.string.ok));
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    pDialog.dismiss();
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
                    Log.i("Device Id",prefs.getString(Constants.KEY_DEVICE_ID, "").trim());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", "" + email.trim());
                    params.put("password", "" + mPasswordView.getText().toString().trim());
                    params.put("user_type", "" + Constants.USER_TYPE);
                    params.put("device_id", "" + prefs.getString(Constants.KEY_DEVICE_ID, "").trim());
                    params.put("device_type", "" + Constants.APP_PLATFORM);
                    params.put("first_name",first_name);
                    params.put("last_name", last_name);
                    params.put("login_type", "" + login_type.trim());
                    params.put("uniqueId", "" + android_id.trim());
                    Log.i("call_login_check", "" + params);
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

}

