package com.digibarber.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            prefs.edit().putString(Constants.KEY_DEVICE_ID, refreshedToken).apply();
            SharedPreferences.Editor editor = getSharedPreferences("Barber", MODE_PRIVATE).edit();
            editor.putString(Constants.KEY_DEVICE_ID, refreshedToken);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   Intent it = new Intent(SplashActivity.this, AddWorkPlaceLocationActivity.class);
        it.putExtra("From", "login");
        it.putExtra("first_name", "test");
        it.putExtra("last_name", "test");
        it.putExtra("email", "test@gmail.com");
        it.putExtra("password", "pass123");
        it.putExtra("phone", "07463656230");
        startActivity(it);*/


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        // return;
        waitThread();
        getKeyHash();
        TextView textVersion = findViewById(R.id.textVersion);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            textVersion.setText("Version "+pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // listener (within Main Activity's onStart)
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // option 1: log data


                    // option 2: save data to be used later

                    // option 3: navigate to page

                    // option 4: display data
                } else {

                }
            }
        }, this.getIntent().getData(), this);

    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.wa.digibarberapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(signature.toByteArray());

              /*  Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();*/
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    private void waitThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    String isON = prefs.getString(Constants.KEY_IS_SIGNUP_ON, "no");
                    if (isON.equalsIgnoreCase("yes")) {
                        String key = "";
                        String value = "";
                        String singupMissed = prefs.getString(Constants.KEY_IS_SIGNUP_MISSED, "");
                        if (singupMissed != null && !singupMissed.equalsIgnoreCase("")) {
                            ResumeSignupProcess(singupMissed);
                        } else {
                            normalFlow();
                        }
                    } else {
                        normalFlow();
                    }

                } catch (Exception e) {
                    normalFlow();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void ResumeSignupProcess(String key) {
        if (key.equalsIgnoreCase("signup")) {
            Intent it = new Intent(SplashActivity.this, CallSignupActivity.class);
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            startActivity(it);
        } else if (key.equalsIgnoreCase("birthday")) {
            Intent it = new Intent(SplashActivity.this, EnableLocationActivity.class);
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("location")) {
            Intent it = new Intent(SplashActivity.this, EnableNotficationActivity.class);
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("notification")) {
            Intent it = new Intent(SplashActivity.this, AddWorkPlaceLocationActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            startActivity(it);
        } else if (key.equalsIgnoreCase("add_workplace")) {
            Intent it = new Intent(SplashActivity.this, LocationOnMapActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("Latitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LAT, ""));
            it.putExtra("Longitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LOG, ""));
            it.putExtra("workplace", prefs.getString(Constants.KEY_IS_SIGNUP_ON_WORK_PLACE, ""));
            it.putExtra("address", prefs.getString(Constants.KEY_IS_SIGNUP_ON_ADDRESS, ""));
            it.putExtra("postcode", prefs.getString(Constants.KEY_IS_SIGNUP_ON_POSTCODE, ""));
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            startActivity(it);
        } else if (key.equalsIgnoreCase("location_map")) {
            Intent it = new Intent(SplashActivity.this, CustomCameraGalleryActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("Latitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LAT, ""));
            it.putExtra("Longitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LOG, ""));
            it.putExtra("workplace", prefs.getString(Constants.KEY_IS_SIGNUP_ON_WORK_PLACE, ""));
            it.putExtra("address", prefs.getString(Constants.KEY_IS_SIGNUP_ON_ADDRESS, ""));
            it.putExtra("postcode", prefs.getString(Constants.KEY_IS_SIGNUP_ON_POSTCODE, ""));
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            startActivity(it);
        } else if (key.equalsIgnoreCase("profile_image")) {
            Intent it = new Intent(SplashActivity.this, AddGalleryImagesActivity.class);
            it.putExtra("From", "SignUp");
            it.putExtra("Latitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LAT, ""));
            it.putExtra("Longitude", prefs.getString(Constants.KEY_IS_SIGNUP_ON_LOG, ""));
            it.putExtra("workplace", prefs.getString(Constants.KEY_IS_SIGNUP_ON_WORK_PLACE, ""));
            it.putExtra("address", prefs.getString(Constants.KEY_IS_SIGNUP_ON_ADDRESS, ""));
            it.putExtra("postcode", prefs.getString(Constants.KEY_IS_SIGNUP_ON_POSTCODE, ""));
            it.putExtra("first_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_F_NAME, ""));
            it.putExtra("last_name", prefs.getString(Constants.KEY_IS_SIGNUP_ON_L_NAME, ""));
            it.putExtra("email", prefs.getString(Constants.KEY_IS_SIGNUP_ON_EMAIl, ""));
            it.putExtra("password", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PASSWORD, ""));
            it.putExtra("phone", prefs.getString(Constants.KEY_IS_SIGNUP_ON_PHONE, ""));
            startActivity(it);
        } else if (key.equalsIgnoreCase("gallery_images")) {
            Intent it = new Intent(SplashActivity.this, PickServiceActivity.class);
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("select_services")) {
            Intent it = new Intent(SplashActivity.this, AddServicePriceTimeActivity.class);
            it.putExtra("joHair", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_HAIR, ""));
            it.putExtra("joBeard", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_BEARD, ""));
            it.putExtra("joPamper", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_PAMPER, ""));
            it.putExtra("joMiscellaneous", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_MISCELL, ""));
            it.putExtra("joMobile", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_MOBILE, ""));
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("add_service_price_time")) {
            Intent it = new Intent(SplashActivity.this, ServiceListActivity.class);
            it.putExtra("joHair", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_HAIR, ""));
            it.putExtra("joBeard", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_BEARD, ""));
            it.putExtra("joPamper", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_PAMPER, ""));
            it.putExtra("joMiscellaneous", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_MISCELL, ""));
            it.putExtra("joMobile", prefs.getString(Constants.KEY_IS_SIGNUP_ON_S_MOBILE, ""));
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("service_list")) {
            Intent it = new Intent(SplashActivity.this, AddOpenHoursActivity.class);
            it.putExtra("From", "SignUp");
            startActivity(it);
        } else if (key.equalsIgnoreCase("completed")) {
            Intent it = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(it);
        }
        finish();
    }


    public void normalFlow() {
        if (prefs.contains(Constants.KEY_IS_FIRST_TIME)) {
            Intent i = new Intent(SplashActivity.this, SignUpActivity.class);
            startActivity(i);
        } else {
            if (prefs.contains(Constants.KEY_TOKEN) && prefs.contains(Constants.KEY_USER_ID)) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(SplashActivity.this, SplashLandingActivity.class);
                startActivity(i);
            }
        }

        finish();
    }
   /* private void logout() {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.e("*updateDeviceToken*", "Hitting");
            StringRequest req = new StringRequest(Request.Method.POST, ZoomConst.Logout,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ClearLogoutPref();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    ClearLogoutPref();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }
            };
            int socketTimeout = 10000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

            req.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {

        }
    }
    void ClearLogoutPref() {
        prefs.edit().remove(ZoomConst.KEY_FULL_NAME).apply();
        prefs.edit().remove(ZoomConst.KEY_TOKEN).apply();
        prefs.edit().remove(ZoomConst.KEY_USER_ID).apply();
        prefs.edit().remove(ZoomConst.KEY_OPEN_HOURS).apply();
        prefs.edit().remove(ZoomConst.KEY_MANGOPAY_ID).apply();
        prefs.edit().remove(ZoomConst.KEY_WORKPLACE).apply();
        prefs.edit().remove(ZoomConst.KEY_ADDRESS).apply();
        prefs.edit().remove(ZoomConst.KEY_EMAIL).apply();
        prefs.edit().remove(ZoomConst.KEY_PHONE).apply();
        prefs.edit().remove(ZoomConst.KEY_BANK_DETAIL).apply();
        prefs.edit().remove(ZoomConst.KEY_PROFILE_IMAGE).apply();
        prefs.edit().remove(ZoomConst.KEY_SERVICES).apply();
        prefs.edit().remove(ZoomConst.KEY_SERVICES).apply();
        prefs.edit().remove(ZoomConst.KEY_PROOF_OF_IDENTY).apply();
        prefs.edit().remove(ZoomConst.KEY_PROOF_OF_ADDRESS).apply();
        prefs.edit().remove(ZoomConst.KEY_PROOF_OF_REGISTRATION).apply();
        prefs.edit().remove(ZoomConst.KEY_ACCOUNT_NUMBER).apply();
        prefs.edit().clear().commit();

        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }*/

}
