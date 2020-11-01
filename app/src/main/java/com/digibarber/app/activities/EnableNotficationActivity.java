package com.digibarber.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

public class EnableNotficationActivity extends BaseActivity {

    private  FrameLayout fl_get_started;

    private  ImageView iv_bell_icons;
    private   LinearLayout ll_overall;
    private  Animation shake;
    private Animation leftToRight;
    private String From = "SignUp";
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_enable_notfication);
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

        leftToRight = Constants.getLeftToRightAnimation(EnableNotficationActivity.this);
        shake = Constants.getShake(EnableNotficationActivity.this);
        ll_overall = (LinearLayout) findViewById(R.id.ll_overall);
        iv_bell_icons = (ImageView) findViewById(R.id.iv_bell_icons);
        fl_get_started = (FrameLayout) findViewById(R.id.fl_get_started);

        ll_overall.startAnimation(leftToRight);

        leftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_bell_icons.startAnimation(shake);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fl_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (From.equalsIgnoreCase("Login")) {
                    Intent it = new Intent(EnableNotficationActivity.this, HomeActivity.class);
                    startActivity(it);
                    finish();
                } else {

                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "notification").apply();
                    Intent it = new Intent(EnableNotficationActivity.this, AddWorkPlaceLocationActivity.class);
                    it.putExtra("From", From);
                    it.putExtra("first_name", firstname);
                    it.putExtra("last_name", lastName);
                    it.putExtra("email", email);
                    it.putExtra("password", password);
                    it.putExtra("phone", phone);
                    startActivity(it);
                    finish();
                }


            }
        });
    }



}
