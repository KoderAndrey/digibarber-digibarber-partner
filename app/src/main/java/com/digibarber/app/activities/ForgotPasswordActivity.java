package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    EditText mEmailView;
    ImageView btn_remind_me;
    TextView tv_text_error_eamil;
    ImageView iv_line_email;
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 007;
    ImageView fb_icon, google_icon;
    private String email = "";
    String first_name = "";
    private GoogleApiClient mGoogleApiClient;
    private String login_type = "";
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private String android_id = "";

    private String last_name = "";

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {

        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            // System.out.println(" ** Google Sign In ** "+statusCode);

            handleSignInResult(result);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result);
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.


            GoogleSignInAccount acct = result.getSignInAccount();
            String personPhotoUrl="",googleid="";
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

                Log.e(TAG, "Name: " +first_name + ", email: " + email
                        + ", Image: " + personPhotoUrl);
            }
            else {
                // Signed out, show unauthenticated UI.
                updateUI(false);
            }
        }
        else {
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
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        } }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgot_password_activity);
        mCallbackManager = CallbackManager.Factory.create();
        google_icon = findViewById(R.id.google_icon);
        fb_icon = findViewById(R.id.fb_icon);
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

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
                        Toast.makeText(ForgotPasswordActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ForgotPasswordActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        mEmailView = (EditText) findViewById(R.id.email_forgot);
        iv_line_email = (ImageView) findViewById(R.id.iv_line_email);
        tv_text_error_eamil = (TextView) findViewById(R.id.tv_text_error_eamil);
        mEmailView.addTextChangedListener(new CustomTextWatcher(mEmailView, tv_text_error_eamil));

        btn_remind_me = (ImageView) findViewById(R.id.btn_remind_me);
        btn_remind_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constants.isEmailValid(mEmailView.getText().toString())) {

                    iv_line_email.setImageResource(R.mipmap.line_red_signup_login);
                    tv_text_error_eamil.setVisibility(View.VISIBLE);
                    tv_text_error_eamil.setText(getResources().getString(R.string.empty_email));
                } else {
                    callForgotPassword(mEmailView.getText().toString());
                }
            }
        });

        mEmailView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                mEmailView.setCursorVisible(true);
                return false;
            }
        });
        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(ForgotPasswordActivity.this)
                        .enableAutoManage(ForgotPasswordActivity.this, ForgotPasswordActivity.this)
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
                LoginManager.getInstance().logInWithReadPermissions(ForgotPasswordActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                login_type = "social";
            }
        });

    }

    private void SocialLogin(final String socialId) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(ForgotPasswordActivity.this);
            Log.d("api call post",Constants.Social_Login);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Social_Login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            Constants.dismissProgress();
                            //   {"user_email":"sukhyoyo@gmail.com","birthdate":"12 December 1993","user_name":"sukh","is_freezed":"0","last_name":"yoyo","bank_detail":"","message":"Login Successfully","token":"fe2014c8-a2fb-4920-aebd-b06e6a637d7e","profile_status":"register","profile_image":"","balance":"£ 0.0","blocked":"0","user_id":"35","phone":"09530959517","success":"true","mangopayId":"39112014"}
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
                                    String bank_detail = jsonobj.getString("bank_detail");
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


                                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                                    locationServicesCheck(profile_status);

                                } else {
                                    Intent it = new Intent(ForgotPasswordActivity.this, SocailLoginDetailActivity.class);
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
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);

                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("device_id", "" + prefs.getString(Constants.KEY_DEVICE_ID, "").trim());
                    params.put("device_type", "" + Constants.APP_PLATFORM);
                    params.put("user_type", "" + Constants.USER_TYPE);
                    params.put("social_id", socialId);
                    params.put("uniqueId", "" + android_id.trim());
                    Log.e("here params", "" + params);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == View.VISIBLE) {
                tv_errror.setVisibility(View.GONE);
                iv_line_email.setImageResource(R.mipmap.line_grey_login_signup);

            }
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
                    Intent it = new Intent(ForgotPasswordActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }

            } else {
                Intent it = new Intent(ForgotPasswordActivity.this, EnableLocationActivity.class);
                it.putExtra("From", "Login");
                startActivity(it);
                finish();
            }


        } else {

            if (profile_status != null && !profile_status.equalsIgnoreCase("")) {

                if (profile_status.equalsIgnoreCase("register")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, EnableLocationActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("address")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, CustomCameraGalleryActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("profile")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, AddGalleryImagesActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("gallery")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, PickServiceActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("service")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, AddOpenHoursActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                } else if (profile_status.equalsIgnoreCase("complete")) {
                    Intent it = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
                    it.putExtra("From", "Login");
                    startActivity(it);
                    finish();
                }
            } else {
                Intent i = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }


        }
    }

    /* Enable Location Services Button */
    public void locationServicesClick(View v) {

        Intent i = new Intent(ForgotPasswordActivity.this, HomeActivity.class);
        startActivity(i);
    }

    private void callForgotPassword(final String Email) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            email=mEmailView.getText().toString();
            Constants.showPorgess(ForgotPasswordActivity.this);
            Log.d("api call post",Constants.ForgotPasswordApi);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.ForgotPasswordApi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String success = jsonobj.getString("success");
                                if (success.equalsIgnoreCase("true")) {

                                    String strphone = jsonobj.getString("phone");
                                    /*if (strphone.length() > 0) {
                                        strphone = strphone.substring(2);
                                        strphone = "+"+strphone;
                                    }*/
                                    startActivity(new Intent(getApplicationContext(), VerificationForgotPassActivity.class)
                                            .putExtra("request_id", "")
                                            .putExtra("email", email)
                                            .putExtra("phone", strphone)
                                    );

                                } else {
                                    tv_text_error_eamil.setVisibility(View.VISIBLE);
                                    tv_text_error_eamil.setText(jsonobj.getString("msg"));
                                    Constants.showToast(ForgotPasswordActivity.this, "Failed to Recover Password");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    params.put("email", "" + Email);
                    Log.e("here params", "" + params);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    /* Back click */
    public void backClick(View v) {
        this.finish();
    }
}
