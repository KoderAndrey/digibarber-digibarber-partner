package com.digibarber.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class SocailLoginDetailActivity extends BaseActivity {

    EditText et_phone;
    ImageView iv_back;
    ImageView iv_lest_go;
    private String login_type = "";
    private String android_id = "";

    TextView tv_phone_error;

    private String first_name="";
    private String last_name="";
    private String email;
    private String socialId;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        bd = getIntent().getExtras();
        if (bd != null) {
            first_name = bd.getString("first_name");
            last_name = bd.getString("last_name");
            email = bd.getString("email");
            socialId = bd.getString("socialId");
        }
        setContentView(R.layout.activity_socail_login_detail);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        tv_phone_error = (TextView) findViewById(R.id.et_phone_error);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_lest_go = (ImageView) findViewById(R.id.iv_lest_go);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_lest_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callValidation();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void callValidation() {

        if (Constants.EmptyText(SocailLoginDetailActivity.this, et_phone.getText().toString(), getResources().getString(R.string.empty_password), et_phone)) {
            et_phone.setBackgroundResource(R.mipmap.box_1_r);
            tv_phone_error.setVisibility(View.VISIBLE);
            tv_phone_error.setText(getResources().getString(R.string.empty_phone));
        } else if (et_phone.getText().toString().length() < 10) {
            et_phone.setBackgroundResource(R.mipmap.box_1_r);
            tv_phone_error.setVisibility(View.VISIBLE);
            Log.d("loginParams","errir -- " + et_phone.getText().length());
            tv_phone_error.setText(getResources().getString(R.string.minimum_phone));
        } else {
            Intent it = new Intent(SocailLoginDetailActivity.this, VerificationActivity.class);
            it.putExtra("phone", et_phone.getText().toString());
            it.putExtra("From", "SocialLogin");
            it.putExtra("first_name", first_name);
            it.putExtra("last_name", last_name);
            it.putExtra("password", "" + socialId);
            it.putExtra("socialId", "" + socialId);
            it.putExtra("email", email);
            it.putExtra("birthdate", "");
            it.putExtra("birth_date", "");
            startActivity(it);
        }
    }

}
