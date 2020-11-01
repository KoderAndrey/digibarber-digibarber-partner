package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

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
import com.digibarber.app.Interfaces.ApiCallback;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    private ImageView btn_edit_Services;
    private ImageView btn_gallery_images;
    private ImageView btn_change_open_hours;
    private ImageView btn_change_private_info;
    private ImageView view_text;

    private String mobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        setContentView(R.layout.edit_profile_activity);
        mobile = getIntent().getStringExtra("mobile");
        btn_change_private_info = (ImageView) findViewById(R.id.btn_change_private_info);
        btn_edit_Services = (ImageView) findViewById(R.id.btn_edit_Services);
        btn_gallery_images = (ImageView) findViewById(R.id.btn_gallery_images);
        btn_change_open_hours = (ImageView) findViewById(R.id.btn_change_open_hours);

        view_text = (ImageView) findViewById(R.id.view_text);

        btn_change_private_info.setOnClickListener(this);
        btn_edit_Services.setOnClickListener(this);
        btn_gallery_images.setOnClickListener(this);
        btn_change_open_hours.setOnClickListener(this);
        view_text.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /* Back click */
    public void backClick(View v) {

        Intent it = new Intent(EditProfileActivity.this, HomeActivity.class);
        startActivity(it);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent it = new Intent(EditProfileActivity.this, HomeActivity.class);
        startActivity(it);
        finish();
    }


    public void loadProfileData() {
        ApiClient.getInstance().getBarberProfile(this, prefs, new ApiCallback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_private_info:
                Intent it = new Intent(EditProfileActivity.this, ChangePrivateInformationActivity.class);
                startActivity(it);
                break;
            case R.id.btn_edit_Services:
                try {
                    String services = prefs.getString(Constants.KEY_SERVICES, "");
                    if (!services.equalsIgnoreCase("")) {
                        JSONArray jaService = new JSONArray(services);
                        JSONObject joHair = new JSONObject();
                        JSONObject joBeard = new JSONObject();
                        JSONObject joPamper = new JSONObject();
                        JSONObject joMiscellnous = new JSONObject();
                        JSONObject joMobile = new JSONObject();
                        for (int i = 0; i < jaService.length(); i++) {
                            if (i == 0) {
                                joHair = jaService.getJSONObject(i);
                            } else if (i == 1) {
                                joBeard = jaService.getJSONObject(i);
                            } else if (i == 2) {
                                joPamper = jaService.getJSONObject(i);
                            } else if (i == 3) {
                                joMiscellnous = jaService.getJSONObject(i);
                            } else if (i == 4) {
                                joMobile = jaService.getJSONObject(i);
                            }
                        }
                        it = new Intent(EditProfileActivity.this, ServiceListActivity.class);
                        it.putExtra("From", "EditProfile");
                        it.putExtra("joHair", joHair.toString());
                        it.putExtra("joBeard", joBeard.toString());
                        it.putExtra("joPamper", joPamper.toString());
                        it.putExtra("joMiscellaneous", joMiscellnous.toString());
                        it.putExtra("joMobile", joMobile.toString());
                        startActivityForResult(it, 1);
                    } else {
                        it = new Intent(EditProfileActivity.this, ServiceListActivity.class);
                        it.putExtra("From", "EditProfile");
                        startActivityForResult(it, 1);
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.btn_gallery_images:

                String gallery = prefs.getString(Constants.KEY_GALLERY_IMAGES, "");
                it = new Intent(EditProfileActivity.this, AddGalleryImagesActivity.class);
                it.putExtra("galleryImages", gallery);
                it.putExtra("From", "EditProfile");
                startActivity(it);
                break;
            case R.id.btn_change_open_hours:

                String openHours = prefs.getString(Constants.KEY_OPEN_HOURS, "");
                it = new Intent(EditProfileActivity.this, AddOpenHoursActivity.class);
                it.putExtra("openHours", openHours);
                it.putExtra("From", "EditProfile");
                startActivity(it);
                break;
            case R.id.view_text:
                Intent intent = new Intent(EditProfileActivity.this, ViewActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // getBarberProfile();
            }
        }

    }
}
