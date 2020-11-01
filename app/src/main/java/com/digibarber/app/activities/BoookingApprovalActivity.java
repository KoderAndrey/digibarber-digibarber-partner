package com.digibarber.app.activities;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */

public class BoookingApprovalActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_first, ll_second, ll_third, ll_fourth;
    private TextView tv_firstno_hours, tv_secondno_hours, tv_thirdno_hours, tv_firsth1no_hours, tv_firsth2no_hours, tv_firsth3no_hours, tv_firsth4no_hours;
    private ImageView iv_back;
    private ImageView tv_done;
    String value = "0";
    String con_date, con_time;

    ImageView infinte;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_activity);

        /////////////id find method here///////////////
        IDFINDER();
        //api call here//////////
        BookingConfirmApi();
    }

    private void IDFINDER() {
        ll_first = (LinearLayout) findViewById(R.id.ll_first);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);
        ll_third = (LinearLayout) findViewById(R.id.ll_third);
        ll_fourth = (LinearLayout) findViewById(R.id.ll_fourth);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_done = (ImageView) findViewById(R.id.tv_done);
        tv_firstno_hours = (TextView) findViewById(R.id.tv_firstno_hours);
        tv_secondno_hours = (TextView) findViewById(R.id.tv_secondno_hours);
        tv_thirdno_hours = (TextView) findViewById(R.id.tv_thirdno_hours);
        tv_firsth1no_hours = (TextView) findViewById(R.id.tv_firsth1no_hours);
        tv_firsth2no_hours = (TextView) findViewById(R.id.tv_firsth2no_hours);
        tv_firsth3no_hours = (TextView) findViewById(R.id.tv_firsth3no_hours);
        tv_firsth4no_hours = (TextView) findViewById(R.id.tv_firsth4no_hours);
        infinte = (ImageView) findViewById(R.id.infinte);
        ////////////////////click listner///////////////
        ll_first.setOnClickListener(this);
        ll_second.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        ll_fourth.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        /////////////// get current date ////////////

//        String str = "Wednesday, September 4, 2013 at 5:07 PM";
//        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy 'at' hh:mm a", Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        SimpleDateFormat format1 = new SimpleDateFormat("kk:mm");
        con_date = format.format(new Date());
        con_time = format1.format(new Date());



      /*  DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        String inputDateStr="2013-06-24";
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);*/


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.ll_first:
                ll_first.setBackgroundResource(R.drawable.oval_shape);
                ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);
                tv_firsth1no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_firstno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                infinte.setColorFilter(Color.argb(17, 143, 235, 1));

                value = "2";

                break;


            case R.id.ll_second:

                ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_second.setBackgroundResource(R.drawable.oval_shape);
                ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);

                tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_secondno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_firsth2no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                infinte.setColorFilter(Color.argb(17, 143, 235, 1));

               // infinte.setColorFilter(getResources().getColor(R.color.bue));
                value = "4";


                break;

            case R.id.ll_third:

                ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_third.setBackgroundResource(R.drawable.oval_shape);
                ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);

                tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_thirdno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_firsth3no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                infinte.setVisibility(View.VISIBLE);
              //  infinte.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.bue), android.graphics.PorterDuff.Mode.MULTIPLY);
              //  rgba(17, 143, 235, 1)
                infinte.setColorFilter(Color.argb(17, 143, 235, 1));

                value = "8";


                break;

            case R.id.ll_fourth:

                ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                ll_fourth.setBackgroundResource(R.drawable.oval_shape);

                tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                tv_firsth4no_hours.setTextColor(Color.parseColor("#FFFFFF"));

                infinte.setColorFilter(Color.argb(255, 255, 255, 255));

               // infinte.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                value = "0";


                break;

            case R.id.tv_done:
                callStartBooking();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;

        }

    }


    ///////////on backpressed//////
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /////api  method////////
    private void callStartBooking() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(BoookingApprovalActivity.this);
            Log.d("api call post",Constants.BookingApproveApi);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.BookingApproveApi,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {

                                    finish();
                                } else {
                                    String message = jo.getString("message");
                                    Constants.showPopupFrozen(BoookingApprovalActivity.this,message);
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
                    Constants.showPopupServer(BoookingApprovalActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("con_hour", value);
                    params.put("con_time", con_time);
                    params.put("con_date", con_date);


                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(BoookingApprovalActivity.this);
        }
    }


    /////api cofirmBooking//////////
    private void BookingConfirmApi() {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(BoookingApprovalActivity.this);
            Log.d("api call post",Constants.BookingConfirmApi);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.BookingConfirmApi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String con_hour = jsonObject.getString("con_hour");
                                String con_time = jsonObject.getString("con_time");
                                String con_date = jsonObject.getString("con_date");

                                if (con_hour.equals("2")) {
                                    ll_first.setBackgroundResource(R.drawable.oval_shape);
                                    ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);
                                    tv_firsth1no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_firstno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    value = "2";
                                }
                                if (con_hour.equals("4")) {
                                    ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_second.setBackgroundResource(R.drawable.oval_shape);
                                    ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);
                                    tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_secondno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_firsth2no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    value = "4";
                                }
                                if (con_hour.equals("8")) {
                                    ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_third.setBackgroundResource(R.drawable.oval_shape);
                                    ll_fourth.setBackgroundResource(R.drawable.ovawhite_shape);
                                    tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_thirdno_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_firsth3no_hours.setTextColor(Color.parseColor("#FFFFFF"));
                                    tv_firsth4no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    value = "8";
                                }
                                if (con_hour.equalsIgnoreCase("empty")) {

                                    ll_first.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_second.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_third.setBackgroundResource(R.drawable.ovawhite_shape);
                                    ll_fourth.setBackgroundResource(R.drawable.oval_shape);

                                    tv_firsth1no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firstno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_secondno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth2no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_thirdno_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth3no_hours.setTextColor(Color.parseColor("#118FEB"));
                                    tv_firsth4no_hours.setTextColor(Color.parseColor("#FFFFFF"));

                                    infinte.setColorFilter(Color.argb(255, 255, 255, 255));

                                    // infinte.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                                    value = "0";
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
                    Constants.showPopupServer(BoookingApprovalActivity.this);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(BoookingApprovalActivity.this);
        }
    }

}