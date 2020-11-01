package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

public class EnableLocationActivity extends BaseActivity {


    FrameLayout fl_turn_on_location;

    final static int REQUEST_LOCATION = 199;
    private static final String TAG = "EditServiceActivity";
    LinearLayout ll_main_move;
    Animation shake;
    Animation leftToRight;
    private String From = "SignUp";
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_enable_location);
        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                From = bd.getString("From");
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
                phone = bd.getString("phone");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        leftToRight = Constants.getLeftToRightAnimation(EnableLocationActivity.this);
        shake = Constants.getShake(EnableLocationActivity.this);
        ll_main_move = (LinearLayout) findViewById(R.id.ll_main_move);
        ll_main_move.startAnimation(leftToRight);

        leftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll_main_move.startAnimation(shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fl_turn_on_location = (FrameLayout) findViewById(R.id.fl_turn_on_location);

        fl_turn_on_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayLocationSettingsRequest(EnableLocationActivity.this);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOCATION) {

                Intent it = new Intent(EnableLocationActivity.this, EnableNotficationActivity.class);
                it.putExtra("From", From);
                it.putExtra("first_name", firstname);
                it.putExtra("last_name", lastName);
                it.putExtra("email", email);
                it.putExtra("password", password);
                it.putExtra("phone", phone);
                startActivity(it);
                if (From.equalsIgnoreCase("Login")) {
                    finish();
                } else {

                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "").apply();
                }

            }
        }
    }

    private void displayLocationSettingsRequest(Context context) {


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Intent it = new Intent(EnableLocationActivity.this, EnableNotficationActivity.class);
                        it.putExtra("From", From);
                        it.putExtra("first_name", firstname);
                        it.putExtra("last_name", lastName);
                        it.putExtra("email", email);
                        it.putExtra("password", password);
                        it.putExtra("phone", phone);
                        startActivity(it);
                        if (From.equalsIgnoreCase("Login")) {
                            finish();
                        } else {

                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "location").apply();

                        }
                        Log.e(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(EnableLocationActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:


                        it = new Intent(EnableLocationActivity.this, EnableNotficationActivity.class);
                        it.putExtra("From", From);
                        it.putExtra("first_name", firstname);
                        it.putExtra("last_name", lastName);
                        it.putExtra("email", email);
                        it.putExtra("password", password);
                        it.putExtra("phone", phone);
                        startActivity(it);
                        if (From.equalsIgnoreCase("Login")) {
                            finish();
                        } else {
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "location").apply();
                        }
                        Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });

    }
}
