package com.digibarber.app.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.digibarber.app.apicalls.ApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aigestudio.wheelpicker.WheelPicker;
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
import com.digibarber.app.CustomClasses.CustomTimePickerDialog;
import com.digibarber.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddOpenHoursActivity extends BaseActivity implements WheelPicker.OnItemSelectedListener {

    ImageView tv_done;
    private TextView tv_every_day_same;
    private TextView tv_clear_all;
    private ImageView back_icon;

    private LinearLayout ll_monday2;
    private TextInputEditText et_open_mon1;
    private TextInputEditText et_close_mon1;
    private TextView tv_open_mon1;
    private TextView tv_close_mon1;

    private ImageView iv_cancel_mon2;
    private ImageView iv_add_mon1;

    private LinearLayout ll_tuesday2;
    private ImageView iv_add_tues1;
    private TextInputEditText et_open_tues1;
    private TextInputEditText et_close_tues1;
    private ImageView iv_cancel_tues2;
    private TextView tv_open_tues1;
    private TextView tv_close_tues1;

    private LinearLayout ll_wednesday2;
    private ImageView iv_add_wed1;
    private TextInputEditText et_open_wed1;
    private TextInputEditText et_close_wed1;
    private ImageView iv_cancel_wed2;
    private TextView tv_open_wed1;
    private TextView tv_close_wed1;

    private LinearLayout ll_thursday2;
    private ImageView iv_add_thus1;
    private TextInputEditText et_open_thus1;
    private TextInputEditText et_close_thus1;
    private ImageView iv_cancel_thus2;

    private TextView tv_open_thus1;
    private TextView tv_close_thus1;


    private LinearLayout ll_friday2;
    private ImageView iv_add_fri1;
    private TextInputEditText et_open_fri1;
    private TextInputEditText et_close_fri1;
    private ImageView iv_cancel_fri2;
    private TextView tv_open_fri1;
    private TextView tv_close_fri1;


    private LinearLayout ll_saturday2;
    private ImageView iv_add_sat1;
    private TextInputEditText et_open_sat1;
    private TextInputEditText et_close_sat1;
    private ImageView iv_cancel_sat2;
    private TextView tv_open_sat1;
    private TextView tv_close_sat1;


    private LinearLayout ll_sunday2;
    private ImageView iv_add_sun1;
    private TextInputEditText et_open_sun1;
    private TextInputEditText et_close_sun1;

    private TextView tv_open_sun1;
    private TextView tv_close_sun1;

    private ImageView iv_cancel_sun2;
    private TextInputEditText et_open_mon2;
    private TextInputEditText et_close_mon2;

    private TextView tv_open_mon2;
    private TextView tv_close_mon2;

    private TextInputEditText et_open_tues2;
    private TextInputEditText et_close_tues2;

    private TextView tv_open_tues2;
    private TextView tv_close_tues2;

    private TextInputEditText et_open_wed2;
    private TextInputEditText et_close_wed2;
    private TextView tv_open_wed2;
    private TextView tv_close_wed2;

    private TextInputEditText et_open_thus2;
    private TextInputEditText et_close_thus2;

    private TextView tv_open_thus2;
    private TextView tv_close_thus2;

    private TextInputEditText et_open_fri2;
    private TextInputEditText et_close_fri2;

    private TextView tv_open_fri2;
    private TextView tv_close_fri2;


    private TextInputEditText et_open_sat2;
    private TextInputEditText et_close_sat2;

    private TextView tv_open_sat2;
    private TextView tv_close_sat2;

    private TextInputEditText et_open_sun2;
    private TextInputEditText et_close_sun2;


    private TextView tv_open_sun2;
    private TextView tv_close_sun2;

    private ImageView iv_clear_open_mon1;
    private ImageView iv_clear_close_mon1;


    private ImageView iv_clear_open_mon2;
    private ImageView iv_clear_close_mon2;

    private ImageView iv_clear_open_tues1;
    private ImageView iv_clear_close_tues1;


    private ImageView iv_clear_open_tues2;
    private ImageView iv_clear_close_tues2;


    private ImageView iv_clear_open_wed1;
    private ImageView iv_clear_close_wed1;


    private ImageView iv_clear_open_wed2;
    private ImageView iv_clear_close_wed2;


    private ImageView iv_clear_open_thus1;

    private ImageView iv_clear_close_thus1;

    private ImageView iv_clear_open_thus2;

    private ImageView iv_clear_close_thus2;


    private ImageView iv_clear_open_fri1;
    private ImageView iv_clear_close_fri1;
    private ImageView iv_clear_open_fri2;
    private ImageView iv_clear_close_fri2;

    private ImageView iv_clear_open_sat1;
    private ImageView iv_clear_close_sat1;
    private ImageView iv_clear_open_sat2;
    private ImageView iv_clear_close_sat2;


    private ImageView iv_clear_open_sun1;
    private ImageView iv_clear_close_sun1;
    private ImageView iv_clear_open_sun2;
    private ImageView iv_clear_close_sun2;


    JSONArray jaMain;
    private String From;
    private String openHours;

    private TextInputLayout text_input_open_mon1;
    private TextInputLayout text_input_close_mon1;
    private TextInputLayout text_input_open_mon2;
    private TextInputLayout text_input_close_mon2;
    private TextInputLayout text_input_open_tues1;
    private TextInputLayout text_input_close_tues1;
    private TextInputLayout text_input_open_tues2;
    private TextInputLayout text_input_close_tues2;
    private TextInputLayout text_input_open_wed1;
    private TextInputLayout text_input_close_wed1;
    private TextInputLayout text_input_open_wed2;
    private TextInputLayout text_input_close_wed2;
    private TextInputLayout text_input_open_thus1;
    private TextInputLayout text_input_close_thus1;
    private TextInputLayout text_input_open_thus2;
    private TextInputLayout text_input_close_thus2;
    private TextInputLayout text_input_open_fri1;
    private TextInputLayout text_input_close_fri1;
    private TextInputLayout text_input_open_fri2;
    private TextInputLayout text_input_close_fri2;

    private TextInputLayout text_input_open_sat1;
    private TextInputLayout text_input_close_sat1;
    private TextInputLayout text_input_open_sat2;
    private TextInputLayout text_input_close_sat2;


    private TextInputLayout text_input_open_sun1;
    private TextInputLayout text_input_close_sun1;
    private TextInputLayout text_input_open_sun2;
    private TextInputLayout text_input_close_sun2;


    private ImageView line_open_mon1;
    private ImageView line_close_mon1;
    private ImageView line_open_mon2;
    private ImageView line_close_mon2;
    private ImageView line_open_tues1;
    private ImageView line_close_tues1;
    private ImageView line_open_tues2;
    private ImageView line_close_tues2;
    private ImageView line_open_wed1;
    private ImageView line_close_wed1;
    private ImageView line_open_wed2;
    private ImageView line_close_wed2;
    private ImageView line_open_thus1;
    private ImageView line_close_thus1;
    private ImageView line_open_thus2;
    private ImageView line_close_thus2;
    private ImageView line_open_fri1;
    private ImageView line_close_fri1;
    private ImageView line_open_fri2;
    private ImageView line_close_fri2;

    private ImageView line_open_sat1;
    private ImageView line_close_sat1;
    private ImageView line_open_sat2;
    private ImageView line_close_sat2;


    private ImageView line_open_sun1;
    private ImageView line_close_sun1;
    private ImageView line_open_sun2;
    private ImageView line_close_sun2;


    int TIME_PICKER_INTERVAL = 15;
    boolean mIgnoreEvent = false;
    int mYear, mMonth, mDay, mHour, mMinute;
    //ColorStateList R.mipmap.line_red_signup_login;
    // ColorStateList colorStateListGrey;

    WheelPicker nv_hours;
    WheelPicker nv_mintues;
    WheelPicker nv_am_pm;
    private int hours = 00;
    private int mintues = 00;
    private String am_pm = "AM";

    String selectedTime = "";
    private String time;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_add_open_hours);
        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                openHours = bd.getString("openHours");
                From = bd.getString("From");
            }

            //   R.mipmap.line_red_signup_login = ColorStateList.valueOf(getResources().getColor(R.color.red_light));
            //  colorStateListGrey = ColorStateList.valueOf(getResources().getColor(R.color.colorGrey));
            text_input_open_mon1 = (TextInputLayout) findViewById(R.id.text_input_open_mon1);
            text_input_close_mon1 = (TextInputLayout) findViewById(R.id.text_input_close_mon1);
            text_input_open_mon2 = (TextInputLayout) findViewById(R.id.text_input_open_mon2);
            text_input_close_mon2 = (TextInputLayout) findViewById(R.id.text_input_close_mon2);

            text_input_open_tues1 = (TextInputLayout) findViewById(R.id.text_input_open_tues1);
            text_input_close_tues1 = (TextInputLayout) findViewById(R.id.text_input_close_tues1);
            text_input_open_tues2 = (TextInputLayout) findViewById(R.id.text_input_open_tues2);
            text_input_close_tues2 = (TextInputLayout) findViewById(R.id.text_input_close_tues2);

            text_input_open_wed1 = (TextInputLayout) findViewById(R.id.text_input_open_wed1);
            text_input_close_wed1 = (TextInputLayout) findViewById(R.id.text_input_close_wed1);
            text_input_open_wed2 = (TextInputLayout) findViewById(R.id.text_input_open_wed2);
            text_input_close_wed2 = (TextInputLayout) findViewById(R.id.text_input_close_wed2);

            text_input_open_thus1 = (TextInputLayout) findViewById(R.id.text_input_open_thus1);
            text_input_close_thus1 = (TextInputLayout) findViewById(R.id.text_input_close_thus1);
            text_input_open_thus2 = (TextInputLayout) findViewById(R.id.text_input_open_thus2);
            text_input_close_thus2 = (TextInputLayout) findViewById(R.id.text_input_close_thus2);

            text_input_open_fri1 = (TextInputLayout) findViewById(R.id.text_input_open_fri1);
            text_input_close_fri1 = (TextInputLayout) findViewById(R.id.text_input_close_fri1);
            text_input_open_fri2 = (TextInputLayout) findViewById(R.id.text_input_open_fri2);
            text_input_close_fri2 = (TextInputLayout) findViewById(R.id.text_input_close_fri2);

            text_input_open_sat1 = (TextInputLayout) findViewById(R.id.text_input_open_sat1);
            text_input_close_sat1 = (TextInputLayout) findViewById(R.id.text_input_close_sat1);
            text_input_open_sat2 = (TextInputLayout) findViewById(R.id.text_input_open_sat2);
            text_input_close_sat2 = (TextInputLayout) findViewById(R.id.text_input_close_sat2);

            text_input_open_sun1 = (TextInputLayout) findViewById(R.id.text_input_open_sun1);
            text_input_close_sun1 = (TextInputLayout) findViewById(R.id.text_input_close_sun1);
            text_input_open_sun2 = (TextInputLayout) findViewById(R.id.text_input_opne_sun2);
            text_input_close_sun2 = (TextInputLayout) findViewById(R.id.text_input_close_sun2);


            line_open_mon1 = (ImageView) findViewById(R.id.line_open_mon1);
            line_close_mon1 = (ImageView) findViewById(R.id.line_close_mon1);
            line_open_mon2 = (ImageView) findViewById(R.id.line_open_mon2);
            line_close_mon2 = (ImageView) findViewById(R.id.line_close_mon2);

            line_open_tues1 = (ImageView) findViewById(R.id.line_open_tues1);
            line_close_tues1 = (ImageView) findViewById(R.id.line_close_tues1);
            line_open_tues2 = (ImageView) findViewById(R.id.line_open_tues2);
            line_close_tues2 = (ImageView) findViewById(R.id.line_close_tues2);

            line_open_wed1 = (ImageView) findViewById(R.id.line_open_wed1);
            line_close_wed1 = (ImageView) findViewById(R.id.line_close_wed1);
            line_open_wed2 = (ImageView) findViewById(R.id.line_open_wed2);
            line_close_wed2 = (ImageView) findViewById(R.id.line_close_wed2);

            line_open_thus1 = (ImageView) findViewById(R.id.line_open_thus1);
            line_close_thus1 = (ImageView) findViewById(R.id.line_close_thus1);
            line_open_thus2 = (ImageView) findViewById(R.id.line_open_thus2);
            line_close_thus2 = (ImageView) findViewById(R.id.line_close_thus2);

            line_open_fri1 = (ImageView) findViewById(R.id.line_open_fri1);
            line_close_fri1 = (ImageView) findViewById(R.id.line_close_fri1);
            line_open_fri2 = (ImageView) findViewById(R.id.line_open_fri2);
            line_close_fri2 = (ImageView) findViewById(R.id.line_close_fri2);

            line_open_sat1 = (ImageView) findViewById(R.id.line_open_sat1);
            line_close_sat1 = (ImageView) findViewById(R.id.line_close_sat1);
            line_open_sat2 = (ImageView) findViewById(R.id.line_open_sat2);
            line_close_sat2 = (ImageView) findViewById(R.id.line_close_sat2);

            line_open_sun1 = (ImageView) findViewById(R.id.line_open_sun1);
            line_close_sun1 = (ImageView) findViewById(R.id.line_close_sun1);
            line_open_sun2 = (ImageView) findViewById(R.id.line_open_sun2);
            line_close_sun2 = (ImageView) findViewById(R.id.line_close_sun2);


            tv_done = (ImageView) findViewById(R.id.tv_done);
            tv_every_day_same = (TextView) findViewById(R.id.tv_every_day_same);
            tv_clear_all = (TextView) findViewById(R.id.tv_clear_all);
            back_icon = (ImageView) findViewById(R.id.back_icon);

            iv_add_mon1 = (ImageView) findViewById(R.id.iv_add_mon1);
            et_open_mon1 = (TextInputEditText) findViewById(R.id.et_open_mon1);
            et_close_mon1 = (TextInputEditText) findViewById(R.id.et_close_mon1);
            et_open_mon2 = (TextInputEditText) findViewById(R.id.et_open_mon2);
            et_close_mon2 = (TextInputEditText) findViewById(R.id.et_close_mon2);

            tv_open_mon1 = (TextView) findViewById(R.id.tv_open_mon1);
            tv_close_mon1 = (TextView) findViewById(R.id.tv_close_mon1);
            tv_open_mon2 = (TextView) findViewById(R.id.tv_open_mon2);
            tv_close_mon2 = (TextView) findViewById(R.id.tv_close_mon2);


            ll_monday2 = (LinearLayout) findViewById(R.id.ll_monday2);
            iv_cancel_mon2 = (ImageView) findViewById(R.id.iv_cancel_mon2);

            iv_add_tues1 = (ImageView) findViewById(R.id.iv_add_tues1);
            et_open_tues1 = (TextInputEditText) findViewById(R.id.et_open_tues1);
            et_close_tues1 = (TextInputEditText) findViewById(R.id.et_close_tues1);
            et_open_tues2 = (TextInputEditText) findViewById(R.id.et_open_tues2);
            et_close_tues2 = (TextInputEditText) findViewById(R.id.et_close_tues2);


            tv_open_tues1 = (TextView) findViewById(R.id.tv_open_tues1);
            tv_close_tues1 = (TextView) findViewById(R.id.tv_close_tues1);
            tv_open_tues2 = (TextView) findViewById(R.id.tv_open_tues2);
            tv_close_tues2 = (TextView) findViewById(R.id.tv_close_tues2);


            ll_tuesday2 = (LinearLayout) findViewById(R.id.ll_tuesday2);
            iv_cancel_tues2 = (ImageView) findViewById(R.id.iv_cancel_tues2);


            iv_add_wed1 = (ImageView) findViewById(R.id.iv_add_wed1);
            et_open_wed1 = (TextInputEditText) findViewById(R.id.et_open_wed1);
            et_close_wed1 = (TextInputEditText) findViewById(R.id.et_close_wed1);
            et_open_wed2 = (TextInputEditText) findViewById(R.id.et_open_wed2);
            et_close_wed2 = (TextInputEditText) findViewById(R.id.et_close_wed2);


            tv_open_wed1 = (TextView) findViewById(R.id.tv_open_wed1);
            tv_close_wed1 = (TextView) findViewById(R.id.tv_close_wed1);
            tv_open_wed2 = (TextView) findViewById(R.id.tv_open_wed2);
            tv_close_wed2 = (TextView) findViewById(R.id.tv_close_wed2);


            ll_wednesday2 = (LinearLayout) findViewById(R.id.ll_wednesday2);
            iv_cancel_wed2 = (ImageView) findViewById(R.id.iv_cancel_wed2);


            iv_add_thus1 = (ImageView) findViewById(R.id.iv_add_thus1);
            et_open_thus1 = (TextInputEditText) findViewById(R.id.et_open_thus1);
            et_close_thus1 = (TextInputEditText) findViewById(R.id.et_close_thus1);
            et_open_thus2 = (TextInputEditText) findViewById(R.id.et_open_thus2);
            et_close_thus2 = (TextInputEditText) findViewById(R.id.et_close_thus2);

            tv_open_thus1 = (TextView) findViewById(R.id.tv_open_thus1);
            tv_close_thus1 = (TextView) findViewById(R.id.tv_close_thus1);
            tv_open_thus2 = (TextView) findViewById(R.id.tv_open_thus2);
            tv_close_thus2 = (TextView) findViewById(R.id.tv_close_thus2);


            ll_thursday2 = (LinearLayout) findViewById(R.id.ll_thursday2);
            iv_cancel_thus2 = (ImageView) findViewById(R.id.iv_cancel_thus2);


            iv_add_fri1 = (ImageView) findViewById(R.id.iv_add_fri1);
            et_open_fri1 = (TextInputEditText) findViewById(R.id.et_open_fri1);
            et_close_fri1 = (TextInputEditText) findViewById(R.id.et_close_fri1);
            et_open_fri2 = (TextInputEditText) findViewById(R.id.et_open_fri2);
            et_close_fri2 = (TextInputEditText) findViewById(R.id.et_close_fri2);


            tv_open_fri1 = (TextView) findViewById(R.id.tv_open_fri1);
            tv_close_fri1 = (TextView) findViewById(R.id.tv_close_fri1);
            tv_open_fri2 = (TextView) findViewById(R.id.tv_open_fri2);
            tv_close_fri2 = (TextView) findViewById(R.id.tv_close_fri2);


            ll_friday2 = (LinearLayout) findViewById(R.id.ll_friday2);
            iv_cancel_fri2 = (ImageView) findViewById(R.id.iv_cancel_fri2);


            iv_add_sat1 = (ImageView) findViewById(R.id.iv_add_sat1);

            et_open_sat1 = (TextInputEditText) findViewById(R.id.et_open_sat1);
            et_close_sat1 = (TextInputEditText) findViewById(R.id.et_close_sat1);
            et_open_sat2 = (TextInputEditText) findViewById(R.id.et_open_sat2);
            et_close_sat2 = (TextInputEditText) findViewById(R.id.et_close_sat2);

            tv_open_sat1 = (TextView) findViewById(R.id.tv_open_sat1);
            tv_close_sat1 = (TextView) findViewById(R.id.tv_close_sat1);
            tv_open_sat2 = (TextView) findViewById(R.id.tv_open_sat2);
            tv_close_sat2 = (TextView) findViewById(R.id.tv_close_sat2);

            ll_saturday2 = (LinearLayout) findViewById(R.id.ll_saturday2);
            iv_cancel_sat2 = (ImageView) findViewById(R.id.iv_cancel_sat2);

            iv_add_sun1 = (ImageView) findViewById(R.id.iv_add_sun1);
            et_open_sun1 = (TextInputEditText) findViewById(R.id.et_open_sun1);
            et_close_sun1 = (TextInputEditText) findViewById(R.id.et_close_sun1);
            et_open_sun2 = (TextInputEditText) findViewById(R.id.et_open_sun2);
            et_close_sun2 = (TextInputEditText) findViewById(R.id.et_close_sun2);


            tv_open_sun1 = (TextView) findViewById(R.id.tv_open_sun1);
            tv_close_sun1 = (TextView) findViewById(R.id.tv_close_sun1);
            tv_open_sun2 = (TextView) findViewById(R.id.tv_open_sun2);
            tv_close_sun2 = (TextView) findViewById(R.id.tv_close_sun2);

            ll_sunday2 = (LinearLayout) findViewById(R.id.ll_sunday2);
            iv_cancel_sun2 = (ImageView) findViewById(R.id.iv_cancel_sun2);


            iv_clear_open_mon1 = (ImageView) findViewById(R.id.iv_clear_open_mon1);
            iv_clear_close_mon1 = (ImageView) findViewById(R.id.iv_clear_close_mon1);

            iv_clear_open_mon2 = (ImageView) findViewById(R.id.iv_clear_open_mon2);
            iv_clear_close_mon2 = (ImageView) findViewById(R.id.iv_clear_close_mon2);

            iv_clear_open_tues1 = (ImageView) findViewById(R.id.iv_clear_open_tues1);
            iv_clear_close_tues1 = (ImageView) findViewById(R.id.iv_clear_close_tues1);

            iv_clear_open_tues2 = (ImageView) findViewById(R.id.iv_clear_open_tues2);
            iv_clear_close_tues2 = (ImageView) findViewById(R.id.iv_clear_close_tues2);

            iv_clear_open_wed1 = (ImageView) findViewById(R.id.iv_clear_open_wed1);
            iv_clear_close_wed1 = (ImageView) findViewById(R.id.iv_clear_close_wed1);

            iv_clear_open_wed2 = (ImageView) findViewById(R.id.iv_clear_open_wed2);
            iv_clear_close_wed2 = (ImageView) findViewById(R.id.iv_clear_close_wed2);


            iv_clear_open_thus1 = (ImageView) findViewById(R.id.iv_clear_open_thus1);
            iv_clear_close_thus1 = (ImageView) findViewById(R.id.iv_clear_close_thus1);
            iv_clear_open_thus2 = (ImageView) findViewById(R.id.iv_clear_open_thus2);
            iv_clear_close_thus2 = (ImageView) findViewById(R.id.iv_clear_close_thus2);


            iv_clear_open_fri1 = (ImageView) findViewById(R.id.iv_clear_open_fri1);
            iv_clear_close_fri1 = (ImageView) findViewById(R.id.iv_clear_close_fri1);
            iv_clear_open_fri2 = (ImageView) findViewById(R.id.iv_clear_open_fri2);
            iv_clear_close_fri2 = (ImageView) findViewById(R.id.iv_clear_close_fri2);

            iv_clear_open_sat1 = (ImageView) findViewById(R.id.iv_clear_open_sat1);
            iv_clear_close_sat1 = (ImageView) findViewById(R.id.iv_clear_close_sat1);
            iv_clear_open_sat2 = (ImageView) findViewById(R.id.iv_clear_open_sat2);
            iv_clear_close_sat2 = (ImageView) findViewById(R.id.iv_clear_close_sat2);

            iv_clear_open_sun1 = (ImageView) findViewById(R.id.iv_clear_open_sun1);
            iv_clear_close_sun1 = (ImageView) findViewById(R.id.iv_clear_close_sun1);
            iv_clear_open_sun2 = (ImageView) findViewById(R.id.iv_clear_open_sun2);
            iv_clear_close_sun2 = (ImageView) findViewById(R.id.iv_clear_close_sun2);


            et_open_mon1.addTextChangedListener(new CustomTextWatcher(et_open_mon1, iv_clear_open_mon1, "Open", text_input_open_mon1, line_open_mon1, tv_open_mon1));
            et_close_mon1.addTextChangedListener(new CustomTextWatcher(et_close_mon1, iv_clear_close_mon1, "Close", text_input_close_mon1, line_close_mon1, tv_close_mon1));
            et_open_mon2.addTextChangedListener(new CustomTextWatcher(et_open_mon2, iv_clear_open_mon2, "Open", text_input_open_mon2, line_open_mon2, tv_open_mon2));
            et_close_mon2.addTextChangedListener(new CustomTextWatcher(et_close_mon2, iv_clear_close_mon2, "Close", text_input_close_mon2, line_close_mon2, tv_close_mon2));

            et_open_tues1.addTextChangedListener(new CustomTextWatcher(et_open_tues1, iv_clear_open_tues1, "Open", text_input_open_tues1, line_open_tues1, tv_open_tues1));
            et_close_tues1.addTextChangedListener(new CustomTextWatcher(et_close_tues1, iv_clear_close_tues1, "Close", text_input_close_tues1, line_close_tues1, tv_close_tues1));
            et_open_tues2.addTextChangedListener(new CustomTextWatcher(et_open_tues2, iv_clear_open_tues2, "Open", text_input_open_tues2, line_open_tues2, tv_open_tues2));
            et_close_tues2.addTextChangedListener(new CustomTextWatcher(et_close_tues2, iv_clear_close_tues2, "Close", text_input_close_tues2, line_close_tues2, tv_close_tues2));


            et_open_wed1.addTextChangedListener(new CustomTextWatcher(et_open_wed1, iv_clear_open_wed1, "Open", text_input_open_wed1, line_open_wed1, tv_open_wed1));
            et_close_wed1.addTextChangedListener(new CustomTextWatcher(et_close_wed1, iv_clear_close_wed1, "Close", text_input_close_wed1, line_close_wed1, tv_close_wed1));
            et_open_wed2.addTextChangedListener(new CustomTextWatcher(et_open_wed2, iv_clear_open_wed2, "Open", text_input_open_wed2, line_open_wed2, tv_open_wed2));
            et_close_wed2.addTextChangedListener(new CustomTextWatcher(et_close_wed2, iv_clear_close_wed2, "Close", text_input_close_wed2, line_close_wed2, tv_close_wed2));

            et_open_thus1.addTextChangedListener(new CustomTextWatcher(et_open_thus1, iv_clear_open_thus1, "Open", text_input_open_thus1, line_open_thus1, tv_open_thus1));
            et_close_thus1.addTextChangedListener(new CustomTextWatcher(et_close_thus1, iv_clear_close_thus1, "Close", text_input_close_thus1, line_close_thus1, tv_close_thus1));
            et_open_thus2.addTextChangedListener(new CustomTextWatcher(et_open_thus2, iv_clear_open_thus2, "Open", text_input_open_thus2, line_open_thus2, tv_open_thus2));
            et_close_thus2.addTextChangedListener(new CustomTextWatcher(et_close_thus2, iv_clear_close_thus2, "Close", text_input_close_thus2, line_close_thus2, tv_close_thus2));


            et_open_fri1.addTextChangedListener(new CustomTextWatcher(et_open_fri1, iv_clear_open_fri1, "Open", text_input_open_fri1, line_open_fri1, tv_open_fri1));
            et_close_fri1.addTextChangedListener(new CustomTextWatcher(et_close_fri1, iv_clear_close_fri1, "Close", text_input_close_fri1, line_close_fri1, tv_close_fri1));
            et_open_fri2.addTextChangedListener(new CustomTextWatcher(et_open_fri2, iv_clear_open_fri2, "Open", text_input_open_fri2, line_open_fri2, tv_open_fri2));
            et_close_fri2.addTextChangedListener(new CustomTextWatcher(et_close_fri2, iv_clear_close_fri2, "Close", text_input_close_fri2, line_close_fri2, tv_close_fri2));


            et_open_sat1.addTextChangedListener(new CustomTextWatcher(et_open_sat1, iv_clear_open_sat1, "Open", text_input_open_sat1, line_open_sat1, tv_open_sat1));
            et_close_sat1.addTextChangedListener(new CustomTextWatcher(et_close_sat1, iv_clear_close_sat1, "Close", text_input_close_sat1, line_close_sat1, tv_close_sat1));
            et_open_sat2.addTextChangedListener(new CustomTextWatcher(et_open_sat2, iv_clear_open_sat2, "Open", text_input_open_sat2, line_open_sat2, tv_open_sat2));
            et_close_sat2.addTextChangedListener(new CustomTextWatcher(et_close_sat2, iv_clear_close_sat2, "Close", text_input_close_sat2, line_close_sat2, tv_close_sat2));


            et_open_sun1.addTextChangedListener(new CustomTextWatcher(et_open_sun1, iv_clear_open_sun1, "Open", text_input_open_sun1, line_open_sun1, tv_open_sun1));
            et_close_sun1.addTextChangedListener(new CustomTextWatcher(et_close_sun1, iv_clear_close_sun1, "Close", text_input_close_sun1, line_close_sun1, tv_close_sun1));
            et_open_sun2.addTextChangedListener(new CustomTextWatcher(et_open_sun2, iv_clear_open_sun2, "Open", text_input_open_sun2, line_open_sun2, tv_open_sun2));
            et_close_sun2.addTextChangedListener(new CustomTextWatcher(et_close_sun2, iv_clear_close_sun2, "Close", text_input_close_sun2, line_close_sun2, tv_close_sun2));


            et_open_mon1.setOnClickListener(new CustomClickListener(line_open_mon1, et_open_mon1, et_open_mon1, et_close_mon1, et_open_mon2, et_close_mon2));
            et_close_mon1.setOnClickListener(new CustomClickListener(line_close_mon1, et_close_mon1, et_open_mon1, et_close_mon1, et_open_mon2, et_close_mon2));
            et_open_mon2.setOnClickListener(new CustomClickListener(line_open_mon2, et_open_mon2, et_open_mon1, et_close_mon1, et_open_mon2, et_close_mon2));
            et_close_mon2.setOnClickListener(new CustomClickListener(line_close_mon2, et_close_mon2, et_open_mon1, et_close_mon1, et_open_mon2, et_close_mon2));

            et_open_tues1.setOnClickListener(new CustomClickListener(line_open_tues1, et_open_tues1, et_open_tues1, et_close_tues1, et_open_tues2, et_close_tues2));
            et_close_tues1.setOnClickListener(new CustomClickListener(line_close_tues1, et_close_tues1, et_open_tues1, et_close_tues1, et_open_tues2, et_close_tues2));
            et_open_tues2.setOnClickListener(new CustomClickListener(line_open_tues2, et_open_tues2, et_open_tues1, et_close_tues1, et_open_tues2, et_close_tues2));
            et_close_tues2.setOnClickListener(new CustomClickListener(line_close_tues2, et_close_tues2, et_open_tues1, et_close_tues1, et_open_tues2, et_close_tues2));


            et_open_wed1.setOnClickListener(new CustomClickListener(line_open_wed1, et_open_wed1, et_open_wed1, et_close_wed1, et_open_wed2, et_close_wed2));
            et_close_wed1.setOnClickListener(new CustomClickListener(line_close_wed1, et_close_wed1, et_open_wed1, et_close_wed1, et_open_wed2, et_close_wed2));
            et_open_wed2.setOnClickListener(new CustomClickListener(line_open_wed2, et_open_wed2, et_open_wed1, et_close_wed1, et_open_wed2, et_close_wed2));
            et_close_wed2.setOnClickListener(new CustomClickListener(line_close_wed2, et_close_wed2, et_open_wed1, et_close_wed1, et_open_wed2, et_close_wed2));

            et_open_thus1.setOnClickListener(new CustomClickListener(line_open_thus1, et_open_thus1, et_open_thus1, et_close_thus1, et_open_thus2, et_close_thus2));
            et_close_thus1.setOnClickListener(new CustomClickListener(line_close_thus1, et_close_thus1, et_open_thus1, et_close_thus1, et_open_thus2, et_close_thus2));
            et_open_thus2.setOnClickListener(new CustomClickListener(line_open_thus2, et_open_thus2, et_open_thus1, et_close_thus1, et_open_thus2, et_close_thus2));
            et_close_thus2.setOnClickListener(new CustomClickListener(line_close_thus2, et_close_thus2, et_open_thus1, et_close_thus1, et_open_thus2, et_close_thus2));


            et_open_fri1.setOnClickListener(new CustomClickListener(line_open_fri1, et_open_fri1, et_open_fri1, et_close_fri1, et_open_fri2, et_close_fri2));
            et_close_fri1.setOnClickListener(new CustomClickListener(line_close_fri1, et_close_fri1, et_open_fri1, et_close_fri1, et_open_fri2, et_close_fri2));
            et_open_fri2.setOnClickListener(new CustomClickListener(line_open_fri2, et_open_fri2, et_open_fri1, et_close_fri1, et_open_fri2, et_close_fri2));
            et_close_fri2.setOnClickListener(new CustomClickListener(line_close_fri2, et_close_fri2, et_open_fri1, et_close_fri1, et_open_fri2, et_close_fri2));


            et_open_sat1.setOnClickListener(new CustomClickListener(line_open_sat1, et_open_sat1, et_open_sat1, et_close_sat1, et_open_sat2, et_close_sat2));
            et_close_sat1.setOnClickListener(new CustomClickListener(line_close_sat1, et_close_sat1, et_open_sat1, et_close_sat1, et_open_sat2, et_close_sat2));
            et_open_sat2.setOnClickListener(new CustomClickListener(line_open_sat2, et_open_sat2, et_open_sat1, et_close_sat1, et_open_sat2, et_close_sat2));
            et_close_sat2.setOnClickListener(new CustomClickListener(line_close_sat2, et_close_sat2, et_open_sat1, et_close_sat1, et_open_sat2, et_close_sat2));


            et_open_sun1.setOnClickListener(new CustomClickListener(line_open_sun1, et_open_sun1, et_open_sun1, et_close_sun1, et_open_sun2, et_close_sun2));
            et_close_sun1.setOnClickListener(new CustomClickListener(line_close_sun1, et_close_sun1, et_open_sun1, et_close_sun1, et_open_sun2, et_close_sun2));
            et_open_sun2.setOnClickListener(new CustomClickListener(line_open_sun2, et_open_sun2, et_open_sun1, et_close_sun1, et_open_sun2, et_close_sun2));
            et_close_sun2.setOnClickListener(new CustomClickListener(line_close_sun2, et_close_sun2, et_open_sun1, et_close_sun1, et_open_sun2, et_close_sun2));


            iv_add_mon1.setOnClickListener(new CustomClikAddCancelButton(et_open_mon1, et_close_mon1, ll_monday2, true));

            iv_cancel_mon2.setOnClickListener(new CustomClikAddCancelButton(et_open_mon1, et_close_mon1, ll_monday2, false));

            iv_add_tues1.setOnClickListener(new CustomClikAddCancelButton(et_open_tues1, et_close_tues1, ll_tuesday2, true));

            iv_cancel_tues2.setOnClickListener(new CustomClikAddCancelButton(et_open_tues1, et_close_tues1, ll_tuesday2, false));


            iv_add_wed1.setOnClickListener(new CustomClikAddCancelButton(et_open_wed1, et_close_wed1, ll_wednesday2, true));
            iv_cancel_wed2.setOnClickListener(new CustomClikAddCancelButton(et_open_wed1, et_close_wed1, ll_wednesday2, false));


            iv_add_thus1.setOnClickListener(new CustomClikAddCancelButton(et_open_thus1, et_close_thus1, ll_thursday2, true));
            iv_cancel_thus2.setOnClickListener(new CustomClikAddCancelButton(et_open_thus1, et_close_thus1, ll_thursday2, false));


            iv_add_fri1.setOnClickListener(new CustomClikAddCancelButton(et_open_fri1, et_close_fri1, ll_friday2, true));
            iv_cancel_fri2.setOnClickListener(new CustomClikAddCancelButton(et_open_fri1, et_close_fri1, ll_friday2, false));


            iv_add_sat1.setOnClickListener(new CustomClikAddCancelButton(et_open_sat1, et_close_sat1, ll_saturday2, true));
            iv_cancel_sat2.setOnClickListener(new CustomClikAddCancelButton(et_open_sat1, et_close_sat1, ll_saturday2, false));

            iv_add_sun1.setOnClickListener(new CustomClikAddCancelButton(et_open_sun1, et_close_sun1, ll_sunday2, true));
            iv_cancel_sun2.setOnClickListener(new CustomClikAddCancelButton(et_open_sun1, et_close_sun1, ll_sunday2, false));


            iv_clear_open_mon1.setOnClickListener(new CustomClearTextClickListener(et_open_mon1));
            iv_clear_close_mon1.setOnClickListener(new CustomClearTextClickListener(et_close_mon1));
            iv_clear_open_mon2.setOnClickListener(new CustomClearTextClickListener(et_open_mon2));
            iv_clear_close_mon2.setOnClickListener(new CustomClearTextClickListener(et_close_mon2));

            iv_clear_open_tues1.setOnClickListener(new CustomClearTextClickListener(et_open_tues1));
            iv_clear_close_tues1.setOnClickListener(new CustomClearTextClickListener(et_close_tues1));
            iv_clear_open_tues2.setOnClickListener(new CustomClearTextClickListener(et_open_tues2));
            iv_clear_close_tues2.setOnClickListener(new CustomClearTextClickListener(et_close_tues2));


            iv_clear_open_wed1.setOnClickListener(new CustomClearTextClickListener(et_open_wed1));
            iv_clear_close_wed1.setOnClickListener(new CustomClearTextClickListener(et_close_wed1));
            iv_clear_open_wed2.setOnClickListener(new CustomClearTextClickListener(et_open_wed2));
            iv_clear_close_wed2.setOnClickListener(new CustomClearTextClickListener(et_close_wed2));

            iv_clear_open_thus1.setOnClickListener(new CustomClearTextClickListener(et_open_thus1));
            iv_clear_close_thus1.setOnClickListener(new CustomClearTextClickListener(et_close_thus1));
            iv_clear_open_thus2.setOnClickListener(new CustomClearTextClickListener(et_open_thus2));
            iv_clear_close_thus2.setOnClickListener(new CustomClearTextClickListener(et_close_thus2));


            iv_clear_open_fri1.setOnClickListener(new CustomClearTextClickListener(et_open_fri1));
            iv_clear_close_fri1.setOnClickListener(new CustomClearTextClickListener(et_close_fri1));
            iv_clear_open_fri2.setOnClickListener(new CustomClearTextClickListener(et_open_fri2));
            iv_clear_close_fri2.setOnClickListener(new CustomClearTextClickListener(et_close_fri2));


            iv_clear_open_sat1.setOnClickListener(new CustomClearTextClickListener(et_open_sat1));
            iv_clear_close_sat1.setOnClickListener(new CustomClearTextClickListener(et_close_sat1));
            iv_clear_open_sat2.setOnClickListener(new CustomClearTextClickListener(et_open_sat2));
            iv_clear_close_sat2.setOnClickListener(new CustomClearTextClickListener(et_close_sat2));


            iv_clear_open_sun1.setOnClickListener(new CustomClearTextClickListener(et_open_sun1));
            iv_clear_close_sun1.setOnClickListener(new CustomClearTextClickListener(et_close_sun1));
            iv_clear_open_sun2.setOnClickListener(new CustomClearTextClickListener(et_open_sun2));
            iv_clear_close_sun2.setOnClickListener(new CustomClearTextClickListener(et_close_sun2));


            back_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            tv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callValidation();

                }
            });
            tv_clear_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callEveryDaySameAndClear("", "", "", "", "Clear");
                }
            });
            tv_every_day_same.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String open1 = et_open_mon1.getText().toString();
                    String close1 = et_close_mon1.getText().toString();
                    String open2 = et_open_mon2.getText().toString();
                    String close2 = et_close_mon2.getText().toString();

                    if (open1.length() > 0 && close1.length() > 0 && open2.length() > 0 && close2.length() > 0) {
                        callEveryDaySameAndClear(open1, close1, open2, close2, "Same");
                    } else if (open1.length() > 0 && close1.length() > 0) {
                        callEveryDaySameAndClear(open1, close1, open2, close2, "NotSame");
                    } else {
                        showDialogForSetMon();
                    }


                }
            });
            if (From != null && From.equalsIgnoreCase("EditProfile")) {
                JSONArray jaopenHours = new JSONArray(openHours);

                if (jaopenHours.length() > 0) {
                    setDataValues(jaopenHours);
                }

            }
        } catch (NullPointerException e) {

        } catch (JSONException e) {

        }
    }

    private void setDataValues(JSONArray jaopenHours) {
        try {
            for (int i = 0; i < jaopenHours.length(); i++) {
                JSONObject joInner = jaopenHours.getJSONObject(i);

                if (i == 0) {

                    if (joInner.has("Mon")) {

                        if (joInner.getJSONArray("Mon").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Mon").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Mon").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Mon").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Mon").getJSONObject(1).getString("end_time");

                            et_open_mon1.setText(start_time0);
                            et_close_mon1.setText(end_time0);
                            et_open_mon2.setText(start_time1);
                            et_close_mon2.setText(end_time1);

                            ll_monday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Mon").length() > 0) {
                                String start_time0 = joInner.getJSONArray("Mon").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Mon").getJSONObject(0).getString("end_time");
                                et_open_mon1.setText(start_time0);
                                et_close_mon1.setText(end_time0);
                            }


                        }
                    }


                } else if (i == 1) {
                    if (joInner.has("Tue")) {
                        if (joInner.getJSONArray("Tue").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Tue").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Tue").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Tue").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Tue").getJSONObject(1).getString("end_time");
                            et_open_tues1.setText(start_time0);
                            et_close_tues1.setText(end_time0);
                            et_open_tues2.setText(start_time1);
                            et_close_tues2.setText(end_time1);
                            ll_tuesday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Tue").length() > 0) {

                                String start_time0 = joInner.getJSONArray("Tue").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Tue").getJSONObject(0).getString("end_time");
                                et_open_tues1.setText(start_time0);
                                et_close_tues1.setText(end_time0);
                            }


                        }
                    }

                } else if (i == 2) {
                    if (joInner.has("Wed")) {
                        if (joInner.getJSONArray("Wed").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Wed").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Wed").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Wed").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Wed").getJSONObject(1).getString("end_time");
                            et_open_wed1.setText(start_time0);
                            et_close_wed1.setText(end_time0);
                            et_open_wed2.setText(start_time1);
                            et_close_wed2.setText(end_time1);
                            ll_wednesday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Wed").length() > 0) {

                                String start_time0 = joInner.getJSONArray("Wed").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Wed").getJSONObject(0).getString("end_time");
                                et_open_wed1.setText(start_time0);
                                et_close_wed1.setText(end_time0);

                            }

                        }
                    }
                } else if (i == 3) {


                    if (joInner.has("Thu")) {
                        if (joInner.getJSONArray("Thu").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Thu").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Thu").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Thu").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Thu").getJSONObject(1).getString("end_time");
                            et_open_thus1.setText(start_time0);
                            et_close_thus1.setText(end_time0);
                            et_open_thus2.setText(start_time1);
                            et_close_thus2.setText(end_time1);
                            ll_thursday2.setVisibility(View.VISIBLE);
                        } else {


                            if (joInner.getJSONArray("Thu").length() > 0) {
                                String start_time0 = joInner.getJSONArray("Thu").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Thu").getJSONObject(0).getString("end_time");
                                et_open_thus1.setText(start_time0);
                                et_close_thus1.setText(end_time0);

                            }

                        }
                    }
                } else if (i == 4) {
                    if (joInner.has("Fri")) {
                        if (joInner.getJSONArray("Fri").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Fri").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Fri").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Fri").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Fri").getJSONObject(1).getString("end_time");

                            et_open_fri1.setText(start_time0);
                            et_close_fri1.setText(end_time0);
                            et_open_fri2.setText(start_time1);
                            et_close_fri2.setText(end_time1);
                            ll_friday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Fri").length() > 0) {
                                String start_time0 = joInner.getJSONArray("Fri").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Fri").getJSONObject(0).getString("end_time");
                                et_open_fri1.setText(start_time0);
                                et_close_fri1.setText(end_time0);

                            }


                        }
                    }
                } else if (i == 5) {
                    if (joInner.has("Sat")) {
                        if (joInner.getJSONArray("Sat").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Sat").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Sat").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Sat").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Sat").getJSONObject(1).getString("end_time");
                            et_open_sat1.setText(start_time0);
                            et_close_sat1.setText(end_time0);
                            et_open_sat2.setText(start_time1);
                            et_close_sat2.setText(end_time1);
                            ll_saturday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Sat").length() > 0) {
                                String start_time0 = joInner.getJSONArray("Sat").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Sat").getJSONObject(0).getString("end_time");
                                et_open_sat1.setText(start_time0);
                                et_close_sat1.setText(end_time0);
                            }
                        }
                    }
                } else if (i == 6) {
                    if (joInner.has("Sun")) {
                        if (joInner.getJSONArray("Sun").length() == 2) {
                            String start_time0 = joInner.getJSONArray("Sun").getJSONObject(0).getString("start_time");
                            String end_time0 = joInner.getJSONArray("Sun").getJSONObject(0).getString("end_time");
                            String start_time1 = joInner.getJSONArray("Sun").getJSONObject(1).getString("start_time");
                            String end_time1 = joInner.getJSONArray("Sun").getJSONObject(1).getString("end_time");
                            et_open_sun1.setText(start_time0);
                            et_close_sun1.setText(end_time0);
                            et_open_sun2.setText(start_time1);
                            et_close_sun2.setText(end_time1);
                            ll_sunday2.setVisibility(View.VISIBLE);
                        } else {

                            if (joInner.getJSONArray("Sun").length() > 0) {

                                String start_time0 = joInner.getJSONArray("Sun").getJSONObject(0).getString("start_time");
                                String end_time0 = joInner.getJSONArray("Sun").getJSONObject(0).getString("end_time");
                                et_open_sun1.setText(start_time0);
                                et_close_sun1.setText(end_time0);
                            }


                        }
                    }
                } else {

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    private void callValidation() {
        try {
            boolean callApi = true;
            boolean everyDayFiled = true;
            jaMain = new JSONArray();
            JSONObject jo_mon = new JSONObject();
            JSONObject jo_tues = new JSONObject();
            JSONObject jo_wed = new JSONObject();
            JSONObject jo_thus = new JSONObject();
            JSONObject jo_fri = new JSONObject();
            JSONObject jo_sat = new JSONObject();
            JSONObject jo_sun = new JSONObject();
            StringBuilder builder = new StringBuilder();
            ArrayList<String> AlDays = new ArrayList<>();
            //etSelcted.setTag("Error");
            if (ll_monday2.getVisibility() == View.VISIBLE) {

                if (et_open_mon1.getText().length() <= 0 && et_close_mon1.getText().length() <= 0 && et_open_mon2.getText().length() <= 0 && et_close_mon2.getText().length() <= 0) {
                    everyDayFiled = false;
                    JSONArray jaMon = new JSONArray();

                    AlDays.add("Mon");
                    jo_mon.put("Mon", jaMon);

                } else if (et_open_mon1.getText().length() <= 0 && et_close_mon1.getText().length() <= 0) {

                    if (et_open_mon2.getText().length() > 0 && et_close_mon2.getText().length() <= 0) {

                        callApi = false;
                        et_close_mon2.setTag("Error");
                        line_close_mon2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_mon2.setVisibility(View.VISIBLE);

                    } else if (et_close_mon2.getText().length() > 0 && et_open_mon2.getText().length() <= 0) {

                        callApi = false;
                        et_open_mon2.setTag("Error");
                        line_open_mon2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_mon2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_mon1.getText().toString().trim());
                        jo1.put("end_time", et_close_mon1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        AlDays.add("Mon");
                        jo_mon.put("Mon", jaMon);

                    }

                } else if (et_open_mon2.getText().length() <= 0 && et_close_mon2.getText().length() <= 0) {

                    if (et_open_mon1.getText().length() > 0 && et_close_mon1.getText().length() <= 0) {

                        callApi = false;
                        et_close_mon1.setTag("Error");
                        line_close_mon1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_mon1.setVisibility(View.VISIBLE);

                    } else if (et_close_mon1.getText().length() > 0 && et_open_mon1.getText().length() <= 0) {

                        callApi = false;
                        et_open_mon1.setTag("Error");
                        line_open_mon1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_mon1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        JSONObject jo1 = new JSONObject();
                       /* jo1.put("start_time", et_open_mon2.getText().toString().trim());
                        jo1.put("end_time", et_close_mon2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        AlDays.add("Mon");
                        jo_mon.put("Mon", jaMon);

                    }

                } else if (et_open_mon1.getText().length() <= 0) {
                    callApi = false;

                    et_open_mon1.setTag("Error");
                    line_open_mon1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_mon1.setVisibility(View.VISIBLE);

                } else if (et_close_mon1.getText().length() <= 0) {
                    callApi = false;
                    et_close_mon1.setTag("Error");
                    line_close_mon1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_mon1.setVisibility(View.VISIBLE);

                } else if (et_open_mon2.getText().length() <= 0) {
                    callApi = false;
                    et_open_mon2.setTag("Error");
                    line_open_mon2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_mon2.setVisibility(View.VISIBLE);

                } else if (et_close_mon2.getText().length() <= 0) {
                    callApi = false;
                    et_close_mon2.setTag("Error");
                    line_close_mon2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_mon2.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jaMon = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_mon1.getText().toString().trim());
                    jo1.put("end_time", et_close_mon1.getText().toString().trim());
                    jo2.put("start_time", et_open_mon2.getText().toString().trim());
                    jo2.put("end_time", et_close_mon2.getText().toString().trim());
                    jaMon.put(jo1);
                    jaMon.put(jo2);
                    jo_mon.put("Mon", jaMon);
                }

            } else {
                if (et_open_mon1.getText().length() <= 0 && et_close_mon1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jaMon = new JSONArray();
                    AlDays.add("Mon");
                    /*JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_mon1.getText().toString().trim());
                    jo1.put("end_time", et_close_mon1.getText().toString().trim());
                    jaMon.put(jo1);*/
                    jo_mon.put("Mon", jaMon);

                } else if (et_close_mon1.getText().length() <= 0) {
                    callApi = false;
                    et_close_mon1.setTag("Error");
                    line_close_mon1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_mon1.setVisibility(View.VISIBLE);

                } else if (et_open_mon1.getText().length() <= 0) {
                    callApi = false;
                    et_open_mon1.setTag("Error");
                    line_open_mon1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_mon1.setVisibility(View.VISIBLE);
                } else {

                    JSONArray jaMon = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_mon1.getText().toString().trim());
                    jo1.put("end_time", et_close_mon1.getText().toString().trim());
                    jaMon.put(jo1);
                    jo_mon.put("Mon", jaMon);

                }
            }


            if (ll_tuesday2.getVisibility() == View.VISIBLE) {


                if (et_open_tues1.getText().length() <= 0 && et_close_tues1.getText().length() <= 0 && et_open_tues2.getText().length() <= 0 && et_close_tues2.getText().length() <= 0) {
                    everyDayFiled = false;
                    JSONArray jatues = new JSONArray();
                    AlDays.add("Tue");
                    /*JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_tues1.getText().toString().trim());
                    jo1.put("end_time", et_close_tues1.getText().toString().trim());
                    jo2.put("start_time", et_open_tues2.getText().toString().trim());
                    jo2.put("end_time", et_close_tues2.getText().toString().trim());
                    jatues.put(jo1);
                    jatues.put(jo2);*/
                    jo_tues.put("Tue", jatues);

                } else if (et_open_tues1.getText().length() <= 0 && et_close_tues1.getText().length() <= 0) {

                    if (et_open_tues2.getText().length() > 0 && et_close_tues2.getText().length() <= 0) {

                        callApi = false;
                        et_close_tues2.setTag("Error");
                        line_close_tues2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_tues2.setVisibility(View.VISIBLE);

                    } else if (et_close_tues2.getText().length() > 0 && et_open_tues2.getText().length() <= 0) {

                        callApi = false;
                        et_open_tues2.setTag("Error");
                        line_open_tues2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_tues2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Tue");
                     /*   JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_tues1.getText().toString().trim());
                        jo1.put("end_time", et_close_tues1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_tues.put("Tu", jaMon);

                    }

                } else if (et_open_tues2.getText().length() <= 0 && et_close_tues2.getText().length() <= 0) {

                    if (et_open_mon1.getText().length() > 0 && et_close_mon1.getText().length() <= 0) {

                        callApi = false;
                        et_close_tues1.setTag("Error");
                        line_close_tues1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_tues1.setVisibility(View.VISIBLE);

                    } else if (et_close_tues1.getText().length() > 0 && et_open_tues1.getText().length() <= 0) {

                        callApi = false;
                        et_open_tues1.setTag("Error");
                        line_open_tues1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_tues1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Tue");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_tues2.getText().toString().trim());
                        jo1.put("end_time", et_close_tues2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_tues.put("Tue", jaMon);

                    }

                } else if (et_open_tues1.getText().length() <= 0) {
                    callApi = false;

                    et_open_tues1.setTag("Error");
                    line_open_tues1.setImageResource(R.mipmap.line_red_signup_login);

                    tv_open_tues1.setVisibility(View.VISIBLE);

                } else if (et_close_tues1.getText().length() <= 0) {
                    callApi = false;

                    et_close_tues1.setTag("Error");
                    line_close_tues1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_tues1.setVisibility(View.VISIBLE);

                } else if (et_open_tues2.getText().length() <= 0) {
                    callApi = false;
                    et_open_tues2.setTag("Error");
                    line_open_tues2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_tues2.setVisibility(View.VISIBLE);

                } else if (et_close_tues2.getText().length() <= 0) {
                    callApi = false;
                    et_close_tues2.setBackgroundResource(R.drawable.background_error);

                    et_close_tues2.setTag("Error");
                    line_close_tues2.setImageResource(R.mipmap.line_red_signup_login);

                    tv_close_tues2.setVisibility(View.VISIBLE);

                } else {
                    JSONArray jatues = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_tues1.getText().toString().trim());
                    jo1.put("end_time", et_close_tues1.getText().toString().trim());
                    jo2.put("start_time", et_open_tues2.getText().toString().trim());
                    jo2.put("end_time", et_close_tues2.getText().toString().trim());
                    jatues.put(jo1);
                    jatues.put(jo2);
                    jo_tues.put("Tue", jatues);
                }

            } else {

                if (et_open_tues1.getText().length() <= 0 && et_close_tues1.getText().length() <= 0) {
                    everyDayFiled = false;
                    JSONArray jatues = new JSONArray();
                    AlDays.add("Tue");
                   /* JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_tues1.getText().toString().trim());
                    jo1.put("end_time", et_close_tues1.getText().toString().trim());
                    jatues.put(jo1);*/
                    jo_tues.put("Tue", jatues);
                } else if (et_open_tues1.getText().length() <= 0) {
                    callApi = false;

                    et_open_tues1.setTag("Error");
                    line_open_tues1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_tues1.setVisibility(View.VISIBLE);

                } else if (et_close_tues1.getText().length() <= 0) {
                    callApi = false;

                    et_close_tues1.setTag("Error");
                    line_close_tues1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_tues1.setVisibility(View.VISIBLE);

                } else {
                    JSONArray jatues = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_tues1.getText().toString().trim());
                    jo1.put("end_time", et_close_tues1.getText().toString().trim());
                    jatues.put(jo1);
                    jo_tues.put("Tue", jatues);
                }

            }


            if (ll_wednesday2.getVisibility() == View.VISIBLE) {


                if (et_open_wed1.getText().length() <= 0 && et_close_wed1.getText().length() <= 0 && et_open_wed2.getText().length() <= 0 && et_close_wed2.getText().length() <= 0) {
                    everyDayFiled = false;
                    JSONArray jawed = new JSONArray();
                    AlDays.add("Wed");
                   /* JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_wed1.getText().toString().trim());
                    jo1.put("end_time", et_close_wed1.getText().toString().trim());
                    jo2.put("start_time", et_open_wed2.getText().toString().trim());
                    jo2.put("end_time", et_close_wed2.getText().toString().trim());
                    jawed.put(jo1);
                    jawed.put(jo2);*/
                    jo_wed.put("Wed", jawed);

                } else if (et_open_wed1.getText().length() <= 0 && et_close_wed1.getText().length() <= 0) {

                    if (et_open_wed2.getText().length() > 0 && et_close_wed2.getText().length() <= 0) {

                        callApi = false;
                        et_close_wed2.setTag("Error");
                        line_close_wed2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_wed2.setVisibility(View.VISIBLE);

                    } else if (et_close_wed2.getText().length() > 0 && et_open_wed2.getText().length() <= 0) {

                        callApi = false;
                        et_open_wed2.setTag("Error");
                        line_open_wed2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_wed2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Wed");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_wed1.getText().toString().trim());
                        jo1.put("end_time", et_close_wed1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_wed.put("Wed", jaMon);

                    }

                } else if (et_open_wed2.getText().length() <= 0 && et_close_wed2.getText().length() <= 0) {

                    if (et_open_wed1.getText().length() > 0 && et_close_wed1.getText().length() <= 0) {

                        callApi = false;
                        et_close_wed1.setTag("Error");
                        line_close_wed1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_wed1.setVisibility(View.VISIBLE);

                    } else if (et_close_wed1.getText().length() > 0 && et_open_wed1.getText().length() <= 0) {

                        callApi = false;
                        et_open_wed1.setTag("Error");
                        line_open_wed1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_wed1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Wed");
                      /*  JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_wed2.getText().toString().trim());
                        jo1.put("end_time", et_close_wed2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_wed.put("Wed", jaMon);

                    }

                } else if (et_open_wed1.getText().length() <= 0) {
                    callApi = false;

                    et_open_wed1.setTag("Error");
                    line_open_wed1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_wed1.setVisibility(View.VISIBLE);

                } else if (et_close_wed1.getText().length() <= 0) {
                    callApi = false;
                    et_close_wed1.setTag("Error");
                    line_close_wed1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_wed1.setVisibility(View.VISIBLE);

                } else if (et_open_wed2.getText().length() <= 0) {
                    callApi = false;
                    et_open_wed2.setTag("Error");
                    line_open_wed2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_wed2.setVisibility(View.VISIBLE);

                } else if (et_close_wed2.getText().length() <= 0) {
                    callApi = false;
                    et_close_wed2.setTag("Error");
                    line_close_wed2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_wed2.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jawed = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_wed1.getText().toString().trim());
                    jo1.put("end_time", et_close_wed1.getText().toString().trim());
                    jo2.put("start_time", et_open_wed2.getText().toString().trim());
                    jo2.put("end_time", et_close_wed2.getText().toString().trim());
                    jawed.put(jo1);
                    jawed.put(jo2);
                    jo_wed.put("Wed", jawed);
                }

            } else {

                if (et_open_wed1.getText().length() <= 0 && et_close_wed1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatues = new JSONArray();
                    AlDays.add("Wed");
                   /* JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_wed1.getText().toString().trim());
                    jo1.put("end_time", et_close_wed1.getText().toString().trim());
                    jatues.put(jo1);*/
                    jo_wed.put("Wed", jatues);
                } else if (et_open_wed1.getText().length() <= 0) {
                    callApi = false;

                    et_open_wed1.setTag("Error");
                    line_open_wed1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_wed1.setVisibility(View.VISIBLE);

                } else if (et_close_wed1.getText().length() <= 0) {
                    callApi = false;
                    et_close_wed1.setTag("Error");
                    line_close_wed1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_wed1.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jawed = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_wed1.getText().toString().trim());
                    jo1.put("end_time", et_close_wed1.getText().toString().trim());
                    jawed.put(jo1);
                    jo_wed.put("Wed", jawed);
                }

            }


            if (ll_thursday2.getVisibility() == View.VISIBLE) {


                if (et_open_thus1.getText().length() <= 0 && et_close_thus1.getText().length() <= 0 && et_open_thus2.getText().length() <= 0 && et_close_thus2.getText().length() <= 0) {
                    everyDayFiled = false;
                    JSONArray jathus = new JSONArray();
                    AlDays.add("Thu");
                   /* JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_thus1.getText().toString().trim());
                    jo1.put("end_time", et_close_thus1.getText().toString().trim());
                    jo2.put("start_time", et_open_thus2.getText().toString().trim());
                    jo2.put("end_time", et_close_thus2.getText().toString().trim());
                    jathus.put(jo1);
                    jathus.put(jo2);*/
                    jo_thus.put("Thu", jathus);

                } else if (et_open_thus1.getText().length() <= 0 && et_close_thus1.getText().length() <= 0) {

                    if (et_open_thus2.getText().length() > 0 && et_close_thus2.getText().length() <= 0) {

                        callApi = false;
                        et_close_thus2.setTag("Error");
                        line_close_thus2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_thus2.setVisibility(View.VISIBLE);

                    } else if (et_close_thus2.getText().length() > 0 && et_open_thus2.getText().length() <= 0) {

                        callApi = false;
                        et_open_thus2.setTag("Error");
                        line_open_thus2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_thus2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Thu");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_thus1.getText().toString().trim());
                        jo1.put("end_time", et_close_thus1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_thus.put("Thu", jaMon);

                    }

                } else if (et_open_thus2.getText().length() <= 0 && et_close_thus2.getText().length() <= 0) {

                    if (et_open_thus1.getText().length() > 0 && et_close_thus1.getText().length() <= 0) {

                        callApi = false;

                        et_close_thus1.setTag("Error");
                        line_close_thus1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_thus1.setVisibility(View.VISIBLE);

                    } else if (et_close_thus1.getText().length() > 0 && et_open_thus1.getText().length() <= 0) {


                        callApi = false;
                        et_open_thus1.setTag("Error");
                        line_open_thus1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_thus1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Thu");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_thus2.getText().toString().trim());
                        jo1.put("end_time", et_close_thus2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_thus.put("Thu", jaMon);

                    }

                } else if (et_open_thus1.getText().length() <= 0) {
                    callApi = false;

                    et_open_thus1.setTag("Error");
                    line_open_thus1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_thus1.setVisibility(View.VISIBLE);

                } else if (et_close_thus1.getText().length() <= 0) {
                    callApi = false;
                    et_close_thus1.setTag("Error");
                    line_close_thus1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_thus1.setVisibility(View.VISIBLE);

                } else if (et_open_thus2.getText().length() <= 0) {
                    callApi = false;
                    et_open_thus2.setTag("Error");
                    line_open_thus2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_thus2.setVisibility(View.VISIBLE);
                } else if (et_close_thus2.getText().length() <= 0) {
                    callApi = false;
                    et_close_thus2.setTag("Error");
                    line_close_thus2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_thus2.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jathus = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_thus1.getText().toString().trim());
                    jo1.put("end_time", et_close_thus1.getText().toString().trim());
                    jo2.put("start_time", et_open_thus2.getText().toString().trim());
                    jo2.put("end_time", et_close_thus2.getText().toString().trim());
                    jathus.put(jo1);
                    jathus.put(jo2);
                    jo_thus.put("Thu", jathus);
                }

            } else {

                if (et_open_thus1.getText().length() <= 0 && et_close_thus1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jathus = new JSONArray();
                    AlDays.add("Thu");
                  /*  JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_thus1.getText().toString().trim());
                    jo1.put("end_time", et_close_thus1.getText().toString().trim());
                    jathus.put(jo1);*/
                    jo_thus.put("Thu", jathus);
                } else if (et_open_thus1.getText().length() <= 0) {
                    callApi = false;
                    et_open_thus1.setTag("Error");
                    line_open_thus1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_thus1.setVisibility(View.VISIBLE);

                } else if (et_close_thus1.getText().length() <= 0) {
                    callApi = false;
                    et_close_thus1.setTag("Error");
                    line_close_thus1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_thus1.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jathus = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_thus1.getText().toString().trim());
                    jo1.put("end_time", et_close_thus1.getText().toString().trim());
                    jathus.put(jo1);
                    jo_thus.put("Thu", jathus);
                }

            }


            if (ll_friday2.getVisibility() == View.VISIBLE) {


                if (et_open_fri1.getText().length() <= 0 && et_close_fri1.getText().length() <= 0 && et_open_fri2.getText().length() <= 0 && et_close_fri2.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatfri = new JSONArray();
                    AlDays.add("Fri");
                  /*  JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_fri1.getText().toString().trim());
                    jo1.put("end_time", et_close_fri1.getText().toString().trim());
                    jo2.put("start_time", et_open_fri2.getText().toString().trim());
                    jo2.put("end_time", et_close_fri2.getText().toString().trim());
                    jatfri.put(jo1);
                    jatfri.put(jo2);*/
                    jo_fri.put("Fri", jatfri);

                } else if (et_open_fri1.getText().length() <= 0 && et_close_fri1.getText().length() <= 0) {

                    if (et_open_fri2.getText().length() > 0 && et_close_fri2.getText().length() <= 0) {
                        callApi = false;

                        et_close_fri2.setTag("Error");
                        line_close_fri2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_fri2.setVisibility(View.VISIBLE);
                    } else if (et_close_fri2.getText().length() > 0 && et_open_fri2.getText().length() <= 0) {
                        callApi = false;
                        et_open_fri2.setTag("Error");
                        line_open_fri2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_fri2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Fri");
                      /*  JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_thus1.getText().toString().trim());
                        jo1.put("end_time", et_close_thus1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_fri.put("Fri", jaMon);

                    }

                } else if (et_open_fri2.getText().length() <= 0 && et_close_fri2.getText().length() <= 0) {

                    if (et_open_fri1.getText().length() > 0 && et_close_fri1.getText().length() <= 0) {

                        callApi = false;
                        et_close_fri1.setTag("Error");
                        line_close_fri1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_fri1.setVisibility(View.VISIBLE);

                    } else if (et_close_fri1.getText().length() > 0 && et_open_fri1.getText().length() <= 0) {

                        callApi = false;

                        et_open_fri1.setTag("Error");
                        line_open_fri1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_fri1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Fri");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_fri2.getText().toString().trim());
                        jo1.put("end_time", et_close_fri2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_fri.put("Fri", jaMon);

                    }

                } else if (et_open_fri1.getText().length() <= 0) {
                    callApi = false;
                    et_open_fri1.setTag("Error");
                    line_open_fri1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_fri1.setVisibility(View.VISIBLE);

                } else if (et_close_fri1.getText().length() <= 0) {
                    callApi = false;
                    et_close_fri1.setTag("Error");
                    line_close_fri1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_fri1.setVisibility(View.VISIBLE);

                } else if (et_open_fri2.getText().length() <= 0) {
                    callApi = false;

                    et_open_fri2.setTag("Error");
                    line_open_fri2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_fri2.setVisibility(View.VISIBLE);

                } else if (et_close_fri2.getText().length() <= 0) {
                    callApi = false;
                    et_close_fri2.setTag("Error");
                    line_close_fri2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_fri2.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jatfri = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_fri1.getText().toString().trim());
                    jo1.put("end_time", et_close_fri1.getText().toString().trim());
                    jo2.put("start_time", et_open_fri2.getText().toString().trim());
                    jo2.put("end_time", et_close_fri2.getText().toString().trim());
                    jatfri.put(jo1);
                    jatfri.put(jo2);
                    jo_fri.put("Fri", jatfri);
                }

            } else {

                if (et_open_fri1.getText().length() <= 0 && et_close_fri1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatfri = new JSONArray();
                    AlDays.add("Fri");
                    /*JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_fri1.getText().toString().trim());
                    jo1.put("end_time", et_close_fri1.getText().toString().trim());
                    jatfri.put(jo1);*/
                    jo_fri.put("Fri", jatfri);
                } else if (et_open_fri1.getText().length() <= 0) {
                    callApi = false;

                    et_open_fri1.setTag("Error");
                    line_open_fri1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_fri1.setVisibility(View.VISIBLE);

                } else if (et_close_fri1.getText().length() <= 0) {
                    callApi = false;
                    et_close_fri1.setTag("Error");
                    line_close_fri1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_fri1.setVisibility(View.VISIBLE);

                } else {
                    JSONArray jatfri = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_fri1.getText().toString().trim());
                    jo1.put("end_time", et_close_fri1.getText().toString().trim());
                    jatfri.put(jo1);
                    jo_fri.put("Fri", jatfri);
                }

            }


            if (ll_saturday2.getVisibility() == View.VISIBLE) {

                if (et_open_sat1.getText().length() <= 0 && et_close_sat1.getText().length() <= 0 && et_open_sat2.getText().length() <= 0 && et_close_sat2.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatsat = new JSONArray();
                    AlDays.add("Sat");
                /*    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jo2.put("start_time", et_open_sat2.getText().toString().trim());
                    jo2.put("end_time", et_close_sat2.getText().toString().trim());
                    jatsat.put(jo1);
                    jatsat.put(jo2);*/
                    jo_sat.put("Sat", jatsat);

                } else if (et_open_sat1.getText().length() <= 0 && et_close_sat1.getText().length() <= 0) {

                    if (et_open_sat2.getText().length() > 0 && et_close_sat2.getText().length() <= 0) {
                        callApi = false;

                        et_close_sat2.setTag("Error");
                        line_close_sat2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_sat2.setVisibility(View.VISIBLE);
                    } else if (et_close_sat2.getText().length() > 0 && et_open_sat2.getText().length() <= 0) {
                        callApi = false;
                        et_open_sat2.setTag("Error");
                        line_open_sat2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_sat2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Sat");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_sat1.getText().toString().trim());
                        jo1.put("end_time", et_close_sat1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_sat.put("Sat", jaMon);

                    }

                } else if (et_open_sat2.getText().length() <= 0 && et_close_sat2.getText().length() <= 0) {

                    if (et_open_sat1.getText().length() > 0 && et_close_sat1.getText().length() <= 0) {

                        callApi = false;
                        et_close_sat1.setTag("Error");
                        line_close_sat1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_sat1.setVisibility(View.VISIBLE);

                    } else if (et_close_sat1.getText().length() > 0 && et_open_sat1.getText().length() <= 0) {

                        callApi = false;
                        et_open_sat1.setTag("Error");
                        line_open_sat1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_sat1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Sat");
                        /*JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_sat2.getText().toString().trim());
                        jo1.put("end_time", et_close_sat2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_sat.put("Sat", jaMon);
                    }

                } else if (et_open_sat1.getText().length() <= 0) {
                    callApi = false;
                    et_open_sat1.setTag("Error");
                    line_open_sat1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sat1.setVisibility(View.VISIBLE);

                } else if (et_close_sat1.getText().length() <= 0) {
                    callApi = false;
                    et_close_sat1.setTag("Error");
                    line_close_sat1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sat1.setVisibility(View.VISIBLE);

                } else if (et_open_sat2.getText().length() <= 0) {
                    callApi = false;
                    et_open_sat2.setTag("Error");
                    line_open_sat2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sat2.setVisibility(View.VISIBLE);

                } else if (et_close_sat2.getText().length() <= 0) {
                    callApi = false;
                    et_close_sat2.setTag("Error");
                    line_close_sat2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sat2.setVisibility(View.VISIBLE);

                } else {
                    JSONArray jatsat = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jo2.put("start_time", et_open_sat2.getText().toString().trim());
                    jo2.put("end_time", et_close_sat2.getText().toString().trim());
                    jatsat.put(jo1);
                    jatsat.put(jo2);
                    jo_sat.put("Sat", jatsat);
                }
            } else {


                if (et_open_sat1.getText().length() <= 0 && et_close_sat1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatsat = new JSONArray();
                    AlDays.add("Sat");
                   /* JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jatsat.put(jo1);*/
                    jo_sat.put("Sat", jatsat);
                } else if (et_open_sat1.getText().length() <= 0) {
                    callApi = false;

                    et_open_sat1.setTag("Error");
                    line_open_sat1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sat1.setVisibility(View.VISIBLE);
                } else if (et_close_sat1.getText().length() <= 0) {
                    callApi = false;

                    et_close_sat1.setTag("Error");
                    line_close_sat1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sat1.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jatsat = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jatsat.put(jo1);
                    jo_sat.put("Sat", jatsat);
                }

            }


            if (ll_sunday2.getVisibility() == View.VISIBLE) {


                if (et_open_sun1.getText().length() <= 0 && et_close_sun1.getText().length() <= 0 && et_open_sun2.getText().length() <= 0 && et_close_sun2.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatsun = new JSONArray();
                    AlDays.add("Sun");
                   /* JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jo2.put("start_time", et_open_sat2.getText().toString().trim());
                    jo2.put("end_time", et_close_sat2.getText().toString().trim());
                    jatsun.put(jo1);
                    jatsun.put(jo2);*/
                    jo_sun.put("Sun", jatsun);

                } else if (et_open_sun1.getText().length() <= 0 && et_close_sun1.getText().length() <= 0) {

                    if (et_open_sun2.getText().length() > 0 && et_close_sun2.getText().length() <= 0) {
                        callApi = false;

                        et_close_sun2.setTag("Error");
                        line_close_sun2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_sun2.setVisibility(View.VISIBLE);
                    } else if (et_close_sun2.getText().length() > 0 && et_open_sun2.getText().length() <= 0) {
                        callApi = false;
                        et_open_sun2.setTag("Error");
                        line_open_sun2.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_sun2.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Sun");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_sun1.getText().toString().trim());
                        jo1.put("end_time", et_close_sun1.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_sun.put("Sun", jaMon);

                    }

                } else if (et_open_sun2.getText().length() <= 0 && et_close_sun2.getText().length() <= 0) {

                    if (et_open_sun1.getText().length() > 0 && et_close_sun1.getText().length() <= 0) {

                        callApi = false;
                        et_close_sun1.setTag("Error");
                        line_close_sun1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_close_sun1.setVisibility(View.VISIBLE);

                    } else if (et_close_sun1.getText().length() > 0 && et_open_sun1.getText().length() <= 0) {

                        callApi = false;
                        et_open_sun1.setTag("Error");
                        line_open_sun1.setImageResource(R.mipmap.line_red_signup_login);
                        tv_open_sun1.setVisibility(View.VISIBLE);

                    } else {
                        JSONArray jaMon = new JSONArray();
                        AlDays.add("Sun");
                       /* JSONObject jo1 = new JSONObject();
                        jo1.put("start_time", et_open_sun2.getText().toString().trim());
                        jo1.put("end_time", et_close_sun2.getText().toString().trim());
                        jaMon.put(jo1);*/
                        jo_sun.put("Sun", jaMon);
                    }

                } else if (et_open_sun1.getText().length() <= 0) {
                    callApi = false;
                    et_open_sun1.setTag("Error");
                    line_open_sun1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sun1.setVisibility(View.VISIBLE);

                } else if (et_close_sun1.getText().length() <= 0) {
                    callApi = false;
                    et_close_sun1.setTag("Error");
                    line_close_sun1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sun1.setVisibility(View.VISIBLE);

                } else if (et_open_sun2.getText().length() <= 0) {
                    callApi = false;
                    line_open_sun2.setTag("Error");
                    line_open_sun2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sun2.setVisibility(View.VISIBLE);

                } else if (et_close_sun2.getText().length() <= 0) {
                    callApi = false;
                    et_close_sun2.setTag("Error");
                    line_close_sun2.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sun2.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jatsun = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    jo1.put("start_time", et_open_sun1.getText().toString().trim());
                    jo1.put("end_time", et_close_sun2.getText().toString().trim());
                    jo2.put("start_time", et_open_sun2.getText().toString().trim());
                    jo2.put("end_time", et_close_sun2.getText().toString().trim());
                    jatsun.put(jo1);
                    jatsun.put(jo2);
                    jo_sun.put("Sun", jatsun);
                }
            } else {


                if (et_open_sun1.getText().length() <= 0 && et_close_sun1.getText().length() <= 0) {

                    everyDayFiled = false;
                    JSONArray jatsun = new JSONArray();
                    AlDays.add("Sun");
                    /*JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_sat1.getText().toString().trim());
                    jo1.put("end_time", et_close_sat1.getText().toString().trim());
                    jatsun.put(jo1);*/
                    jo_sun.put("Sun", jatsun);
                } else if (et_open_sun1.getText().length() <= 0) {
                    callApi = false;
                    et_open_sun1.setTag("Error");
                    line_open_sun1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_open_sun1.setVisibility(View.VISIBLE);

                } else if (et_close_sun1.getText().length() <= 0) {
                    callApi = false;
                    et_close_sun1.setTag("Error");
                    line_close_sun1.setImageResource(R.mipmap.line_red_signup_login);
                    tv_close_sun1.setVisibility(View.VISIBLE);
                } else {
                    JSONArray jatsun = new JSONArray();
                    JSONObject jo1 = new JSONObject();
                    jo1.put("start_time", et_open_sun1.getText().toString().trim());
                    jo1.put("end_time", et_close_sun1.getText().toString().trim());
                    jatsun.put(jo1);
                    jo_sun.put("Sun", jatsun);
                }

            }
            jaMain.put(jo_mon);
            jaMain.put(jo_tues);
            jaMain.put(jo_wed);
            jaMain.put(jo_thus);
            jaMain.put(jo_fri);
            jaMain.put(jo_sat);
            jaMain.put(jo_sun);

            if (callApi) {

                if (everyDayFiled) {
                    callAddBarberProfile(jaMain);
                } else {
                    showDialogCheck(callApi, AlDays);
                }


                //
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialogCheck(final boolean callApi, ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {

            if (i == list.size() - 2) {
                builder.append(list.get(i)).append(" and ");
            } else if (i == list.size() - 1) {
                builder.append(list.get(i));
            } else {
                builder.append(list.get(i)).append(", ");
            }

        }
        final Dialog dialog_first = new Dialog(AddOpenHoursActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_open_hours_confirm);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
        tv_message.setText(builder.toString());
        ImageView iv_yes = (ImageView) dialog_first.findViewById(R.id.iv_yes);
        ImageView iv_no = (ImageView) dialog_first.findViewById(R.id.iv_no);


        iv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });

        iv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (callApi) {
                    callAddBarberProfile(jaMain);
                } else {

                }*/
                callAddBarberProfile(jaMain);
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    private void showcantChange(String openhoursDay) {
        final Dialog dialog_first = new Dialog(AddOpenHoursActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_cannot_change_open_hours);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
        tv_message.setText("You can't change open hour for\n" + openhoursDay + " .Please delete booking first");
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    private void showDialogForSetMon() {

        final Dialog dialog_first = new Dialog(AddOpenHoursActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.poup_everyday_same);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });

        dialog_first.show();


    }


    /*private void showTimePIckerDialoug() {

        final Dialog dialog_first = new Dialog(AddOpenHoursActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_custom_time_picker);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) dialog_first.findViewById(R.id.tv_cancel);

        TimePicker timePicker = (TimePicker) dialog_first.findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(false);
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        TimePicker.OnTimeChangedListener mTimePickerListener = new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                if (mIgnoreEvent)
                    return;
                if (minute % TIME_PICKER_INTERVAL != 0) {

                    int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);

                    minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
                    if (minute == 60)
                        minute = 0;

                    mIgnoreEvent = true;
                    timePicker.setCurrentMinute(minute);
                    mIgnoreEvent = false;
                }


            }
        };

        timePicker.setOnTimeChangedListener(mTimePickerListener);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog_first.dismiss();

            }
        });


        dialog_first.show();


    }*/


    void callEveryDaySameAndClear(String open1, String close1, String open2, String close2, String status) {

        et_open_mon1.setText(open1);
        et_open_mon2.setText(open2);

        et_close_mon1.setText(close1);
        et_close_mon2.setText(close2);


        et_open_tues1.setText(open1);
        et_open_tues2.setText(open2);

        et_close_tues1.setText(close1);
        et_close_tues2.setText(close2);


        et_open_wed1.setText(open1);
        et_open_wed2.setText(open2);

        et_close_wed1.setText(close1);
        et_close_wed2.setText(close2);


        et_open_thus1.setText(open1);
        et_open_thus2.setText(open2);

        et_close_thus1.setText(close1);
        et_close_thus2.setText(close2);


        et_open_fri1.setText(open1);
        et_open_fri2.setText(open2);

        et_close_fri1.setText(close1);
        et_close_fri2.setText(close2);


        et_open_sat1.setText(open1);
        et_open_sat2.setText(open2);

        et_close_sat1.setText(close1);
        et_close_sat2.setText(close2);


        et_open_sun1.setText(open1);
        et_open_sun2.setText(open2);

        et_close_sun1.setText(close1);
        et_close_sun2.setText(close2);

        if (status.equalsIgnoreCase("Same")) {
            showVisiBility(ll_monday2);
            showVisiBility(ll_tuesday2);
            showVisiBility(ll_wednesday2);
            showVisiBility(ll_thursday2);
            showVisiBility(ll_friday2);
            showVisiBility(ll_saturday2);
            showVisiBility(ll_sunday2);
        }

    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.nv_hours:
                hours = Integer.parseInt(Constants.wheelHours[position]);
                Log.e("Here postion nv_hours ", "" + hours);
                break;
            case R.id.nv_mintues:
                mintues = Integer.parseInt(Constants.wheelMintues[position]);
                Log.e("Here postion nv_mintues", "" + mintues);
                break;
            case R.id.nv_am_pm:
                am_pm = Constants.wheelAm_PM[position];
                Log.e("Here postion nv_am_pm", "" + am_pm);
                break;
        }
        selectedTime = "" + hours + ":" + mintues + " " + am_pm;
    }


    private class CustomClickListener implements View.OnClickListener {
        private TextInputEditText mEditTextSelected1;
        private TextInputEditText mEditText1;
        private TextInputEditText mEditText2;
        private TextInputEditText mEditText3;
        private TextInputEditText mEditText4;
        private ImageView lineError;

        public CustomClickListener(ImageView lineError, TextInputEditText mEditTextSelected1, TextInputEditText mEditText1, TextInputEditText mEditText2, TextInputEditText mEditText3, TextInputEditText mEditText4) {
            this.mEditTextSelected1 = mEditTextSelected1;
            this.mEditText1 = mEditText1;
            this.mEditText2 = mEditText2;
            this.mEditText3 = mEditText3;
            this.mEditText4 = mEditText4;
            this.lineError = lineError;
        }

        @Override
        public void onClick(View view) {
            callSetDate(lineError, mEditTextSelected1, mEditText1, mEditText2, mEditText3, mEditText4);
        }
    }

    private void callSetDate(final ImageView lineError, final TextInputEditText etSelcted, final TextInputEditText mEditText1, final TextInputEditText mEditText2, final TextInputEditText mEditText3, final TextInputEditText mEditText4) {

       /* final Dialog dialog_first = new Dialog(AddOpenHoursActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_custom_time_picker);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) dialog_first.findViewById(R.id.tv_cancel);

        nv_hours = (WheelPicker) dialog_first.findViewById(R.id.nv_hours);
        nv_mintues = (WheelPicker) dialog_first.findViewById(R.id.nv_mintues);
        nv_am_pm = (WheelPicker) dialog_first.findViewById(R.id.nv_am_pm);
        nv_hours.setItemSpace(50);
        nv_mintues.setItemSpace(50);
        nv_am_pm.setItemSpace(50);

        nv_hours.setData(Arrays.asList(ZoomConst.wheelHours));
        nv_mintues.setData(Arrays.asList(ZoomConst.wheelMintues));
        nv_am_pm.setData(Arrays.asList(ZoomConst.wheelAm_PM));
        nv_hours.setOnItemSelectedListener(this);
        nv_mintues.setOnItemSelectedListener(this);
        nv_am_pm.setOnItemSelectedListener(this);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date dateSelected = null;
                    Date date1 = null;
                    Date date2 = null;
                    Date date3 = null;
                    Date date4 = null;
                    String strTime1 = mEditText1.getText().toString();
                    String strTime2 = mEditText2.getText().toString();
                    String strTime3 = mEditText3.getText().toString();
                    String strTime4 = mEditText4.getText().toString();
                    SimpleDateFormat dateFormate = new SimpleDateFormat("kk:mm");
                    time = ZoomConst.getCurrentTime_KK_mm_24Hours(selectedTime);
                    dateSelected = dateFormate.parse(time);
                    if (strTime1.length() > 0 || strTime2.length() > 0 || strTime3.length() > 0) {

                        if (etSelcted.getId() == mEditText1.getId()) {

                            if (strTime1.length() <= 0 && strTime2.length() > 0) {
                                date2 = dateFormate.parse(strTime2);
                                if (dateSelected.before(date2)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else {
                                etSelcted.setText(time);
                            }

                        } else if (etSelcted.getId() == mEditText2.getId()) {

                            if (strTime1.length() > 0) {
                                date1 = dateFormate.parse(strTime1);
                                if (dateSelected.after(date1)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else {
                                etSelcted.setText(time);
                            }

                        } else if (etSelcted.getId() == mEditText3.getId()) {

                            if (strTime2.length() > 0 && strTime4.length() > 0) {
                                date4 = dateFormate.parse(strTime4);
                                date2 = dateFormate.parse(strTime2);
                                if (dateSelected.after(date2) && dateSelected.before(date4)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else if (strTime2.length() > 0) {

                                date2 = dateFormate.parse(strTime2);
                                if (dateSelected.after(date2)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }

                            } else if (strTime4.length() > 0) {
                                date4 = dateFormate.parse(strTime4);
                                if (dateSelected.before(date4)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else {
                                etSelcted.setText(time);
                            }

                        } else if (etSelcted.getId() == mEditText4.getId()) {

                            if (strTime3.length() > 0) {
                                date3 = dateFormate.parse(strTime3);
                                if (dateSelected.after(date3)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else if (strTime2.length() > 0) {
                                date2 = dateFormate.parse(strTime2);
                                if (dateSelected.after(date2)) {
                                    etSelcted.setText(time);
                                } else {
                                    etSelcted.setTag("Error");
                                    lineError.setImageResource(R.mipmap.line_red_signup_login);
                                }
                            } else {
                                etSelcted.setText(time);
                            }

                        }
                    } else {
                        etSelcted.setText(time);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                    dialog_first.dismiss();
                }
                dialog_first.dismiss();

            }


        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_first.dismiss();

            }
        });


        dialog_first.show();*/

        try {
            final Calendar c = Calendar.getInstance();
            final int mHour = c.get(Calendar.HOUR_OF_DAY);
            final int mMinute = c.get(Calendar.MINUTE);
            CustomTimePickerDialog tpd = new CustomTimePickerDialog(
                    AddOpenHoursActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {
                            try {

                                int mHour = hourOfDay;
                                int mMinute = minute;

                                DecimalFormat formate = new DecimalFormat("00");
                                String strMintues = formate.format(minute);
                                String strHours = formate.format(mHour);

                                if (strHours.equalsIgnoreCase("00")) {
                                    strHours = "12";
                                }

                                String time = "" + strHours + ":" + strMintues;
                                Date dateSelected = null;
                                Date date1 = null;
                                Date date2 = null;
                                Date date3 = null;
                                Date date4 = null;
                                String strTime1 = mEditText1.getText().toString();
                                String strTime2 = mEditText2.getText().toString();
                                String strTime3 = mEditText3.getText().toString();
                                String strTime4 = mEditText4.getText().toString();
                                SimpleDateFormat dateFormate = new SimpleDateFormat("kk:mm");
                                dateSelected = dateFormate.parse(time);
                                if (strTime1.length() > 0 || strTime2.length() > 0 || strTime3.length() > 0) {


                                    if (etSelcted.getId() == mEditText1.getId()) {

                                        if (strTime1.length() <= 0 && strTime2.length() > 0) {
                                            date2 = dateFormate.parse(strTime2);
                                            if (dateSelected.before(date2)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else {
                                            etSelcted.setText(time);
                                        }

                                    } else if (etSelcted.getId() == mEditText2.getId()) {

                                        if (strTime1.length() > 0) {
                                            date1 = dateFormate.parse(strTime1);
                                            if (dateSelected.after(date1)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else {
                                            etSelcted.setText(time);
                                        }

                                    } else if (etSelcted.getId() == mEditText3.getId()) {

                                        if (strTime2.length() > 0 && strTime4.length() > 0) {
                                            date4 = dateFormate.parse(strTime4);
                                            date2 = dateFormate.parse(strTime2);
                                            if (dateSelected.after(date2) && dateSelected.before(date4)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else if (strTime2.length() > 0) {

                                            date2 = dateFormate.parse(strTime2);
                                            if (dateSelected.after(date2)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }

                                        } else if (strTime4.length() > 0) {
                                            date4 = dateFormate.parse(strTime4);
                                            if (dateSelected.before(date4)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else {
                                            etSelcted.setText(time);
                                        }

                                    } else if (etSelcted.getId() == mEditText4.getId()) {

                                        if (strTime3.length() > 0) {
                                            date3 = dateFormate.parse(strTime3);
                                            if (dateSelected.after(date3)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else if (strTime2.length() > 0) {
                                            date2 = dateFormate.parse(strTime2);
                                            if (dateSelected.after(date2)) {
                                                etSelcted.setText(time);
                                            } else {
                                                etSelcted.setTag("Error");
                                                lineError.setImageResource(R.mipmap.line_red_signup_login);
                                            }
                                        } else {
                                            etSelcted.setText(time);
                                        }

                                    }
                                } else {
                                    etSelcted.setText(time);
                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mHour, mMinute, true);
            tpd.show();

        } catch (NullPointerException e) {

        }
    }


    private class CustomClearTextClickListener implements View.OnClickListener {
        private TextInputEditText mEditText;

        public CustomClearTextClickListener(TextInputEditText etoprnclose) {
            mEditText = etoprnclose;
        }

        @Override
        public void onClick(View view) {

            clearDate(mEditText);
        }
    }

    private void clearDate(TextInputEditText etOpenClsoe) {
        etOpenClsoe.setText("");
    }


    private class CustomClikAddCancelButton implements View.OnClickListener {
        private TextInputEditText mEditText1;
        private TextInputEditText mEditText2;
        private LinearLayout mLinearLayout;
        boolean status;

        public CustomClikAddCancelButton(TextInputEditText mEditText1, TextInputEditText mEditText2, LinearLayout mLinearLayout, boolean status) {
            this.mEditText1 = mEditText1;
            this.mEditText2 = mEditText2;
            this.mLinearLayout = mLinearLayout;
            this.status = status;
        }

        @Override
        public void onClick(View view) {
            showGoneLinearVisiBility(mLinearLayout, status, mEditText1, mEditText2);
        }
    }


    private void showVisiBility(LinearLayout linear) {

        linear.setVisibility(View.VISIBLE);
    }

    private void showGoneLinearVisiBility(LinearLayout linear, boolean status, TextInputEditText et1, TextInputEditText et2) {
        if (status) {
            if (et1.getText().toString().length() > 0 && et2.getText().toString().length() > 0) {
                linear.setVisibility(View.VISIBLE);
            }
        } else {
            linear.setVisibility(View.GONE);
        }
    }


    private class CustomTextWatcher implements TextWatcher {
        private TextInputEditText mEditText;
        private ImageView mImageView;
        private String Text;
        TextInputLayout inputLayout;

        ImageView lineError;

        TextView mTvErrorText;

        public CustomTextWatcher(TextInputEditText etOpenClsoe, ImageView ivCancel, String Text, TextInputLayout inputLayoutl, ImageView lineError, TextView mTvErrorText) {
            mEditText = etOpenClsoe;
            mImageView = ivCancel;
            this.Text = Text;
            this.inputLayout = inputLayoutl;
            this.lineError = lineError;
            this.mTvErrorText = mTvErrorText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().trim().length() > 0) {
                inputLayout.setHint(Text);
                if (mEditText.getTag() != null && mEditText.getTag().toString().equalsIgnoreCase("Error")) {
                    // mEditText.setBackgroundResource(0);
                    //mEditText.setImageResource(colorStateListGrey);
                    mEditText.setTag("RemoveError");
                    mTvErrorText.setVisibility(View.GONE);
                    lineError.setImageResource(R.mipmap.open_hours_line_back);


                } else {

                }
                mEditText.setTextColor(Color.parseColor("#FF31363B"));
                mEditText.setAlpha(1);
                // mEditText.setImageResource(colorStateListGrey);
                hideVisibleCancelButton(mImageView, true);
            } else {
                mEditText.setTextColor(Color.parseColor("#FF31363B"));
                mEditText.setAlpha((float) 0.5);
                inputLayout.setHint("CLOSED");
                hideVisibleCancelButton(mImageView, false);
            }
        }
    }

    private void hideVisibleCancelButton(ImageView cancel, boolean status) {
        if (status) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void callAddBarberProfile(final JSONArray jaMain) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(AddOpenHoursActivity.this);
            Log.d("api call post",Constants.AddBarberProfile);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.AddBarberProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** AddBarberProfile **", response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");

                                if (!response_values.equalsIgnoreCase("false")) {
                                    String open_hours = jsonobj.getString("open_hours");
                                    String open_hours_status = jsonobj.getString("open_hours_status");
                                    if (open_hours_status != null && !open_hours_status.equalsIgnoreCase("")) {
                                        showcantChange(open_hours_status);
                                    } else {
                                        prefs.edit().putString(Constants.KEY_OPEN_HOURS, open_hours).apply();
                                        if (From != null && From.equalsIgnoreCase("EditProfile")) {
                                            finish();
                                        } else {
                                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "completed").apply();
                                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON, "no").apply();
                                            String message = jsonobj.getString("message");
                                            Intent it = new Intent(AddOpenHoursActivity.this, HomeActivity.class);
                                            startActivity(it);
                                        }
                                    }
                                } else {

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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("lat", prefs.getString(Constants.KEY_LAT, ""));
                    params.put("lon", prefs.getString(Constants.KEY_LOG, ""));
                    params.put("address", "");
                    params.put("postcode", "");
                    params.put("phone", "");
                    params.put("workplace", "");
                    params.put("open_hours", jaMain.toString());

                    //   Log.e("Deep_OpenHoursP",params.toString());
                    Log.e("** AddBarberProfile **", "params:" + params);

                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

}
