package com.digibarber.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digibarber.app.CustomAdapters.SplashViewPagerAdapter;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashLandingActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private SplashViewPagerAdapter mAdapter;
    protected View view;
    ImageView splashRelativeLayout;
    RelativeLayout fl_get_started;
    private String[] mTextValues = {"Welcome", "Let customers find you\n with a click of a button", "Set live availability using our\n DIGICALENDAR system ", "We work hard for you! Less\n queuing more cutting!", "Let customers know your \nservices. Everytime. "};
    TextView textView;
    Animation leftToRight;
    Animation shake;
    RelativeLayout Rl_viewPagerIndicator;
    private int previous;
    ImageView iv_imageView_GetStarted;
    ImageView iv_getstartted_text;

    RelativeLayout rl1;
    ImageView iv1, iv2, iv3, iv4;
    RelativeLayout rl2;
    ImageView iv21, iv22, iv23, iv24, iv25;

    Animation startAnimation;

    //TextView button_get_started;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_landing_activity);

        Rl_viewPagerIndicator = (RelativeLayout) findViewById(R.id.Rl_viewPagerIndicator);
        leftToRight = Constants.getLeftToRightAnimation(SplashLandingActivity.this);
        shake = Constants.getShake(SplashLandingActivity.this);
        textView = (TextView) findViewById(R.id.tv_text);
       // String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        // button_get_started = (TextView) findViewById(R.id.btn_get_started);
        fl_get_started = (RelativeLayout) findViewById(R.id.fl_get_started);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        splashRelativeLayout = (ImageView) findViewById(R.id.splashRelativeLayout);
        mAdapter = new SplashViewPagerAdapter(SplashLandingActivity.this, mTextValues);

        iv_imageView_GetStarted = (ImageView) findViewById(R.id.iv_imageView_GetStarted);
        iv_getstartted_text = (ImageView) findViewById(R.id.iv_getstartted_text);

        iv_imageView_GetStarted.setImageResource(R.mipmap.white);
        iv_getstartted_text.setVisibility(View.INVISIBLE);

        rl1 = (RelativeLayout) findViewById(R.id.rlminiguide1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);

        rl2 = (RelativeLayout) findViewById(R.id.rlminiguide2);
        iv21 = (ImageView) findViewById(R.id.iv21);
        iv22 = (ImageView) findViewById(R.id.iv22);
        iv23 = (ImageView) findViewById(R.id.iv23);
        iv24 = (ImageView) findViewById(R.id.iv24);
        iv25 = (ImageView) findViewById(R.id.iv25);

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl1.setVisibility(View.GONE);
                openMiniGuide2();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(Constants.KEY_IS_MINI_GUIDE_SHOWN, true).apply();
                prefs.edit().putString(Constants.KEY_IS_FIRST_TIME, "true");
                startActivity(new Intent(SplashLandingActivity.this, SignUpActivity.class));
                finish();
            }
        });

        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        fl_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*prefs.edit().putString(Constants.KEY_IS_FIRST_TIME, "true");
                startActivity(new Intent(SplashLandingActivity.this, LoginActivity.class));
                finish();*/

                if (!prefs.getBoolean(Constants.KEY_IS_MINI_GUIDE_SHOWN, false)) {
                    openMiniGuide1();
                } else {
                    prefs.edit().putString(Constants.KEY_IS_FIRST_TIME, "true");
                    startActivity(new Intent(SplashLandingActivity.this, SignUpActivity.class));
                    finish();
                }

            }
        });

       /* button_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString(ZoomConst.KEY_IS_FIRST_TIME, "true");
                startActivity(new Intent(SplashLandingActivity.this, LoginActivity.class));
                finish();
            }
        });*/


        setUiPageViewController();
    }

    int i = 1;

    private void openMiniGuide1() {
        rl1.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (++i) {
                        case 1:
                         /*   iv1.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv1.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);*/
                            break;
                        case 2:
                            iv2.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv2.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);
                            break;
                        case 3:
                            iv3.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv3.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);
                            break;
                        case 4:
                            iv4.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv4.startAnimation(startAnimation);
                            handler.removeCallbacks(this);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }, 800);
    }

    int j = 1;

    private void openMiniGuide2() {
        rl2.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (++j) {
                        case 1:
                            /*iv21.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv21.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);*/
                            break;
                        case 2:
                            iv22.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv22.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);
                            break;
                        case 3:
                            iv23.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv23.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);
                            break;
                        case 4:
                            iv24.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv24.startAnimation(startAnimation);
                            handler.postDelayed(this, 1300);
                            break;
                        case 5:
                            iv25.setVisibility(View.VISIBLE);
                            startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_guide);
                            iv25.startAnimation(startAnimation);
                            handler.removeCallbacks(this);
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }, 800);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);//Left, top, right, bottom
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_dot_red));

        if (position == 0) {

//            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            splashRelativeLayout.setImageResource(R.mipmap.splashlandingback);
            iv_imageView_GetStarted.setImageResource(R.mipmap.back_white_getstarted);
            //button_get_started.setTextColor(getResources().getColor(R.color.black));
            iv_getstartted_text.setVisibility(View.VISIBLE);
            // button_get_started.setVisibility(View.GONE);

            textView.setVisibility(View.GONE);
          /*  Rl_viewPagerIndicator.set
                    button_get_started.setre*/
        } else {

            if (position > previous) {
                splashRelativeLayout.setImageResource(0);
                splashRelativeLayout.setBackgroundResource(R.drawable.drawable_splash_back);

                iv_imageView_GetStarted.setImageResource(R.mipmap.white);
                iv_getstartted_text.setVisibility(View.INVISIBLE);
                // button_get_started.setTextColor(getResources().getColor(R.color.colorWhite));
                //  button_get_started.setVisibility(View.VISIBLE);
                //  button_get_started.setBackgroundResource(R.drawable.blue_rounded_button);
                //  button_get_started.setTextColor(getResources().getColor(R.color.colorWhite));


                textView.setVisibility(View.VISIBLE);
                textView.startAnimation(leftToRight);
                leftToRight.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.startAnimation(shake);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                textView.setText(mTextValues[position]);
            }
        }

        iv_imageView_GetStarted.setImageResource(R.mipmap.white);
        iv_getstartted_text.setVisibility(View.INVISIBLE);

        previous = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}