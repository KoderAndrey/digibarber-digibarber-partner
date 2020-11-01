package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.digibarber.app.Beans.BookingList;
import com.digibarber.app.Beans.DayWise;
import com.digibarber.app.CustomAdapters.UnconfirmedBookingAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UnconfermedBookingActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_calendar;
    ImageView iv_up;
    ImageView iv_down, back;
    LinearLayout rl_buttons;
    TextView tv_today;
    TextView tv_tomorrow;
    TextView tv_all;
    RecyclerView lvuncon_booking;
    SharedPreferences prefs;
    String date, booking_id, reschedule, user_id, is_confirmed, user_name, booking_time, services, rescdule_time, message;
    ArrayList<DayWise> daywise_array_list=new ArrayList<>();
    UnconfirmedBookingAdapter unconfirmedBookingAdapter;
    String date1, nextDate;
    String Today = "selected";
    String Tomorrow = "";
    String All = "";
    String all_bookings = "";
    String checkBooking_id = "";
    int count = 0;
    String type = "";
    private DateFormat formater;
    private DateFormat parseableDate;
    private ArrayList<BookingList> alBookingLists=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unconfermed_booking);
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            alBookingLists = (ArrayList<BookingList>) savedInstanceState.getSerializable("alBookingLists");
        }
        parseableDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        formater = new SimpleDateFormat("EEEE, dd MMM yyyy");

        prefs = getSharedPreferences(Constants.SHARED_PREFRENCE_DB_NAME, MODE_PRIVATE);
        back = (ImageView) findViewById(R.id.back);
        iv_calendar = (ImageView) findViewById(R.id.iv_calendar);
        lvuncon_booking = (RecyclerView) findViewById(R.id.lvuncon_booking);
        iv_down = (ImageView) findViewById(R.id.iv_down);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        rl_buttons = (LinearLayout) findViewById(R.id.rl_buttons);
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_tomorrow = (TextView) findViewById(R.id.tv_tomorrow);
        back.setOnClickListener(this);
        iv_calendar.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        tv_today.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_tomorrow.setOnClickListener(this);
        SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        date1 = parseFormat.format(new Date());



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        nextDate = parseFormat.format(calendar.getTime());

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UnconfermedBookingActivity.this, CalenderActivity.class).putExtra("alBookingLists", alBookingLists);
                startActivity(intent);
            }
        });
        callTodayUnconbokking();
    }

    private void callTodayUnconbokking() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(UnconfermedBookingActivity.this);
            String url = Constants.Path + Constants.BOOKING_LIST_DAYWISE;
            Log.d("api call post",url);
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Constants.dismissProgress();
                                Today = "selected";
                                Tomorrow = "";
                                All = "";
                                daywise_array_list = new ArrayList<>();
                                JSONObject jo = new JSONObject(response);

                                JSONArray jsonArray = jo.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //  Sunday, 22 Jun 2017 (today)
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String date = jsonObject.getString("date");

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Requests");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object = jsonArray1.getJSONObject(j);
                                        String is_confirmed = object.getString("is_confirmed");
                                        if (is_confirmed.equals("0")) {
                                            booking_id = object.getString("booking_id");
                                            if (all_bookings.contains(",")) {
                                                all_bookings = all_bookings + booking_id + ",";
                                            } else {
                                                all_bookings = booking_id + ",";
                                            }


                                            Date dateSelected = null;
                                            String datel = "";
                                            if (date != null && !date.equalsIgnoreCase("")) {
                                                try {
                                                    dateSelected = parseableDate.parse(date);
                                                    datel = formater.format(dateSelected);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                datel = date;
                                            }


                                            reschedule = object.getString("is_reschedule");
                                            user_id = object.getString("user_id");
                                            user_name = object.getString("user_name");
                                            booking_time = object.getString("booking_time");
                                            services = object.getString("services");
                                            count++;

                                            StringBuilder builderServices = new StringBuilder();
                                            if (services != null && !services.equalsIgnoreCase("")) {
                                                JSONArray ja = new JSONArray(services);
                                                for (int k = 0; k < ja.length(); k++) {
                                                    JSONObject joinner = ja.getJSONObject(k);
                                                    JSONArray jasub_services = joinner.getJSONArray("sub_services");
                                                    for (int l = 0; l < jasub_services.length(); l++) {
                                                        String service_name = jasub_services.getJSONObject(l).getString("service_name");
                                                        String sub_category_id = jasub_services.getJSONObject(l).getString("sub_category_id");
                                                        if (k == ja.length() - 1) {
                                                            builderServices.append(service_name);
                                                        } else {
                                                            builderServices.append(service_name).append(" & ");
                                                        }
                                                    }
                                                }
                                            } else {
                                                builderServices.append("");
                                            }

                                            String service_name = builderServices.toString();



                                            daywise_array_list.add(new DayWise(booking_id, reschedule, user_id, user_name, booking_time, service_name, rescdule_time, datel));
                                        }

                                        // lvuncon_booking.setAdapter(unconfirmedBookingAdapter);


                                    }


                                }


                            } catch (JSONException e) {
                                Constants.dismissProgress();
                                e.printStackTrace();
                            }

                            unconfirmedBookingAdapter = new UnconfirmedBookingAdapter(UnconfermedBookingActivity.this, daywise_array_list);
                            LinearLayoutManager llm = new LinearLayoutManager(UnconfermedBookingActivity.this);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            lvuncon_booking.setLayoutManager(llm);
                            lvuncon_booking.setAdapter(unconfirmedBookingAdapter);
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("date", date1);
                    params.put("barber_id", "0");



                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barbers Unconfirm Booking");

        } else {
            Constants.showPopupInternet(activity);
        }

    }


    private void callBookingDaywise() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(UnconfermedBookingActivity.this);
            String url = Constants.Path + Constants.BOOKING_LIST_DAYWISE;
            Log.d("api call post",url);
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Constants.dismissProgress();
                                Today = "";
                                Tomorrow = "selected";
                                All = "";
                                daywise_array_list = new ArrayList<>();
                                JSONObject jo = new JSONObject(response);

                                JSONArray jsonArray = jo.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String date1 = jsonObject.getString("date");

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Requests");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object = jsonArray1.getJSONObject(j);
                                        String is_confirmed = object.getString("is_confirmed");
                                        if (is_confirmed.equals("0")) {
                                            booking_id = object.getString("booking_id");

                                            if (all_bookings.contains(",")) {
                                                all_bookings = all_bookings + booking_id + ",";
                                            } else {
                                                all_bookings = booking_id + ",";
                                            }


                                            reschedule = object.getString("is_reschedule");
                                            user_id = object.getString("user_id");
                                            user_name = object.getString("user_name");
                                            booking_time = object.getString("booking_time");
                                            services = object.getString("services");
                                            count++;

                                            StringBuilder builderServices = new StringBuilder();
                                            if (services != "") {
                                                JSONArray ja = new JSONArray(services);
                                                for (int k = 0; k < ja.length(); k++) {
                                                    JSONObject joinner = ja.getJSONObject(k);
                                                    JSONArray jasub_services = joinner.getJSONArray("sub_services");
                                                    for (int l = 0; l < jasub_services.length(); l++) {
                                                        String service_name = jasub_services.getJSONObject(l).getString("service_name");
                                                        String sub_category_id = jasub_services.getJSONObject(l).getString("sub_category_id");
                                                        if (k == ja.length() - 1) {
                                                            builderServices.append(service_name);
                                                        } else {
                                                            builderServices.append(service_name).append(" & ");
                                                        }
                                                    }
                                                }
                                            } else {
                                                builderServices.append("");
                                            }

                                            String service_name = builderServices.toString();

                                            daywise_array_list.add(new DayWise(booking_id, reschedule, user_id, user_name, booking_time, service_name, rescdule_time, date1));
                                        }

                                        unconfirmedBookingAdapter = new UnconfirmedBookingAdapter(UnconfermedBookingActivity.this, daywise_array_list);
                                        LinearLayoutManager llm = new LinearLayoutManager(UnconfermedBookingActivity.this);
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        lvuncon_booking.setLayoutManager(llm);
                                        lvuncon_booking.setAdapter(unconfirmedBookingAdapter);
                                        // lvuncon_booking.setAdapter(unconfirmedBookingAdapter);


                                    }


                                }


                            } catch (JSONException e) {
                                Constants.dismissProgress();
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
           /*     headers.put("authenticate", " 5d64c95b-8fed-4a8d-8df7-6c5077aaaf81");
                headers.put("user_id", "7");*/
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("date", nextDate);
                    params.put("barber_id", "0");
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barbers Unconfirm Booking");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    private void callAllBooking() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {

            Constants.showPorgess(UnconfermedBookingActivity.this);
            String url = Constants.Path + Constants.BARBER_REQUESTS_LIST;
            Log.d("api call post",url);
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Constants.dismissProgress();
                                Today = "";
                                Tomorrow = "";
                                All = "selected";

                                daywise_array_list = new ArrayList<>();
                                JSONObject jo = new JSONObject(response);

                                JSONArray jsonArray = jo.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String date = jsonObject.getString("date");

                                    JSONArray jsonArray1 = jsonObject.getJSONArray("Requests");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject object = jsonArray1.getJSONObject(j);
                                        String is_confirmed = object.getString("is_confirmed");
                                        if (is_confirmed.equals("0")) {
                                            booking_id = object.getString("booking_id");
                                            if (all_bookings.contains(",")) {
                                                all_bookings = all_bookings + booking_id + ",";
                                            } else {
                                                all_bookings = booking_id + ",";
                                            }
                                            checkBooking_id = booking_id;
                                            reschedule = object.getString("reschedule");
                                            user_id = object.getString("user_id");
                                            user_name = object.getString("user_name");
                                            booking_time = object.getString("booking_time");
                                            services = object.getString("services");
                                            rescdule_time = object.getString("rescdule_time");

                                            count++;



                                            StringBuilder builderServices = new StringBuilder();
                                            if (services != "") {
                                                JSONArray ja = new JSONArray(services);
                                                for (int k = 0; k < ja.length(); k++) {
                                                    JSONObject joinner = ja.getJSONObject(k);
                                                    JSONArray jasub_services = joinner.getJSONArray("sub_services");
                                                    for (int l = 0; l < jasub_services.length(); l++) {
                                                        String service_name = jasub_services.getJSONObject(l).getString("service_name");
                                                        String sub_category_id = jasub_services.getJSONObject(l).getString("sub_category_id");
                                                        if (k == ja.length() - 1) {
                                                            builderServices.append(service_name);
                                                        } else {
                                                            builderServices.append(service_name).append(" & ");
                                                        }
                                                    }
                                                }
                                            } else {
                                                builderServices.append("");
                                            }

                                            String service_name = builderServices.toString();



                                            daywise_array_list.add(new DayWise(booking_id, reschedule, user_id, user_name, booking_time, service_name, rescdule_time, date));
                                        }
                                        unconfirmedBookingAdapter = new UnconfirmedBookingAdapter(UnconfermedBookingActivity.this, daywise_array_list);
                                        LinearLayoutManager llm = new LinearLayoutManager(UnconfermedBookingActivity.this);
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        lvuncon_booking.setLayoutManager(llm);
                                        lvuncon_booking.setAdapter(unconfirmedBookingAdapter);
                                        // lvuncon_booking.setAdapter(unconfirmedBookingAdapter);


                                    }


                                }


                            } catch (JSONException e) {
                                Constants.dismissProgress();
                                e.printStackTrace();
                            }


                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

            /*    headers.put("authenticate", " 5d64c95b-8fed-4a8d-8df7-6c5077aaaf81");
                headers.put("user_id", "7");
*/

                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barbers Unconfirm Booking");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.iv_calendar:

                break;

            case R.id.tv_all:
                if (All.equals("selected")) {

                    All = "";
                    if (count != 0) {
                        count = 0;

                        tv_all.setBackgroundResource(R.drawable.edit_custom_button_green);
                        tv_all.setTextColor(getResources().getColor(R.color.colorWhite));

                        tv_today.setBackgroundResource(R.mipmap.bgunselected);
                        tv_today.setTextColor(getResources().getColor(R.color.black));

                        tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                        tv_tomorrow.setTextColor(getResources().getColor(R.color.black));

                        showPopupAll("all");
                        break;
                    }

                } else {
                    tv_all.setBackgroundResource(R.mipmap.bg);
                    tv_all.setTextColor(getResources().getColor(R.color.colorWhite));

                    tv_today.setBackgroundResource(R.mipmap.bgunselected);
                    tv_today.setTextColor(getResources().getColor(R.color.black));

                    tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.black));

                    callAllBooking();
                    break;
                }
                break;

            case R.id.tv_today:
                if (Today.equals("selected")) {

                    Today = "";

                    if (count != 0) {
                        count = 0;


                        tv_all.setBackgroundResource(R.mipmap.bgunselected);
                        tv_all.setTextColor(getResources().getColor(R.color.black));

                        tv_today.setBackgroundResource(R.drawable.edit_custom_button_green);
                        tv_today.setTextColor(getResources().getColor(R.color.colorWhite));

                        tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                        tv_tomorrow.setTextColor(getResources().getColor(R.color.black));

                        showPopupAll("today");
                    }
                    break;
                } else {


                    tv_all.setBackgroundResource(R.mipmap.bgunselected);
                    tv_all.setTextColor(getResources().getColor(R.color.black));

                    tv_today.setBackgroundResource(R.mipmap.bg);
                    tv_today.setTextColor(getResources().getColor(R.color.colorWhite));

                    tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.black));

                    callTodayUnconbokking();

                    break;
                }



            case R.id.tv_tomorrow:
                if (Tomorrow.equals("selected")) {

                    Tomorrow = "";

                    if (count != 0) {
                        tv_all.setBackgroundResource(R.mipmap.bgunselected);
                        tv_all.setTextColor(getResources().getColor(R.color.black));

                        tv_today.setBackgroundResource(R.mipmap.bgunselected);
                        tv_today.setTextColor(getResources().getColor(R.color.black));

                        tv_tomorrow.setBackgroundResource(R.drawable.edit_custom_button_green);
                        tv_tomorrow.setTextColor(getResources().getColor(R.color.colorWhite));
                        count = 0;
                        showPopupAll("tomorrow");
                        break;
                    }

                } else {


                    tv_all.setBackgroundResource(R.mipmap.bgunselected);
                    tv_all.setTextColor(getResources().getColor(R.color.black));

                    tv_today.setBackgroundResource(R.mipmap.bgunselected);
                    tv_today.setTextColor(getResources().getColor(R.color.black));

                    tv_tomorrow.setBackgroundResource(R.mipmap.bg);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.colorWhite));

                    callBookingDaywise();
                    break;
                }

                break;

            case R.id.iv_up:

                iv_up.setVisibility(View.GONE);
                iv_down.setVisibility(View.VISIBLE);
                rl_buttons.setVisibility(View.GONE);
                break;

            case R.id.iv_down:
                iv_up.setVisibility(View.VISIBLE);
                iv_down.setVisibility(View.GONE);
                rl_buttons.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void showPopupAll(final String chheck_type) {
        final Dialog dialog_first = new Dialog(UnconfermedBookingActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.pop_confirm_all_booking);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_addrress1 = (TextView) dialog_first.findViewById(R.id.tv_addrress1);
        if (chheck_type.equals("tomorrow")) {
            tv_addrress1.setText("Are you sure you want to confirm all of tomorrows bookings?");
        } else if (chheck_type.equals("today")) {
            tv_addrress1.setText("Are you sure you want to confirm all of todays bookings?");
        } else if (chheck_type.equals("all")) {
            tv_addrress1.setText("Are you sure you want to confirm all of bookings?");
        } else {
            tv_addrress1.setText("Are you sure you want to confirm all of bookings?");
        }
        ImageView iv_no = (ImageView) dialog_first.findViewById(R.id.iv_no);
        ImageView iv_yes = (ImageView) dialog_first.findViewById(R.id.iv_yes);
        iv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chheck_type.equals("tomorrow")) {
                    tv_today.setBackgroundResource(R.mipmap.bgunselected);
                    tv_today.setTextColor(getResources().getColor(R.color.black));

                    tv_tomorrow.setBackgroundResource(R.mipmap.bg);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.colorWhite));

                    tv_all.setBackgroundResource(R.mipmap.bgunselected);
                    tv_all.setTextColor(getResources().getColor(R.color.black));
                }
                if (chheck_type.equals("today")) {
                    tv_today.setBackgroundResource(R.mipmap.bg);
                    tv_today.setTextColor(getResources().getColor(R.color.colorWhite));
                    tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.black));
                    tv_all.setBackgroundResource(R.mipmap.bgunselected);
                    tv_all.setTextColor(getResources().getColor(R.color.black));


                }
                if (chheck_type.equals("all")) {

                    tv_today.setBackgroundResource(R.mipmap.bgunselected);
                    tv_today.setTextColor(getResources().getColor(R.color.black));

                    tv_tomorrow.setBackgroundResource(R.mipmap.bgunselected);
                    tv_tomorrow.setTextColor(getResources().getColor(R.color.black));

                    tv_all.setBackgroundResource(R.mipmap.bg);
                    tv_all.setTextColor(getResources().getColor(R.color.colorWhite));


                }
                dialog_first.dismiss();
            }
        });

        iv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmAllBooking();
                dialog_first.dismiss();


            }
        });
        dialog_first.show();
    }

    private void callGetBooking() {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
         {
               // Constants.showPorgess(UnconfermedBookingActivity.this);
            }
            Log.d("api call post", ApiUrls.GetBookingList);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetBookingList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            alBookingLists = new ArrayList<>();

                       //     Constants.dismissProgress();

                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    JSONArray jadata = jo.getJSONArray("data");

                                    if (jadata.length() > 0) {

                                        for (int l = 0; l < jadata.length(); l++) {

                                            JSONObject joInner = jadata.getJSONObject(l);
                                            String date = joInner.getString("date");
                                            JSONArray jaRequests = joInner.getJSONArray("Requests");
                                            for (int i = 0; i < jaRequests.length(); i++) {
                                                JSONObject joRequests = jaRequests.getJSONObject(i);
                                                BookingList objBookingList = new BookingList();
                                                String booking_id = joRequests.getString("booking_id");
                                                String reschedule = joRequests.getString("reschedule");
                                                String user_id = joRequests.getString("user_id");
                                                String is_confirmed = joRequests.getString("is_confirmed");
                                                String user_name = joRequests.getString("user_name");
                                                String booking_time = joRequests.getString("booking_time");
                                                String services = joRequests.getString("services");
                                                String rescdule_time = joRequests.getString("rescdule_time");
                                                objBookingList.booking_id = booking_id;
                                                objBookingList.reschedule = reschedule;
                                                objBookingList.user_id = user_id;
                                                objBookingList.is_confirmed = is_confirmed;
                                                objBookingList.user_name = user_name;
                                                objBookingList.booking_time = booking_time;

                                                Date dateSelected = null;
                                                if (date != null && !date.equalsIgnoreCase("")) {
                                                    try {
                                                        dateSelected = parseableDate.parse(date);
                                                        String datel = formater.format(dateSelected);
                                                        objBookingList.date = datel;
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    objBookingList.date = date;
                                                }



                                                 {
                                                    if (is_confirmed.equalsIgnoreCase("1") || is_confirmed.equalsIgnoreCase("4")) {
                                                        objBookingList.isfirstConfBooking = "1";
                                                    } else {
                                                        objBookingList.isfirstConfBooking = "0";
                                                    }
                                                }


                                                StringBuilder builderServices = new StringBuilder();
                                                if (services != "") {
                                                    JSONArray ja = new JSONArray(services);
                                                    for (int j = 0; j < ja.length(); j++) {
                                                        JSONObject joinner = ja.getJSONObject(j);
                                                        JSONArray jasub_services = joinner.getJSONArray("sub_services");
                                                        for (int k = 0; k < jasub_services.length(); k++) {
                                                            String service_name = jasub_services.getJSONObject(k).getString("service_name");
                                                            String sub_category_id = jasub_services.getJSONObject(k).getString("sub_category_id");
                                                            if (j == ja.length() - 1) {
                                                                builderServices.append(service_name);
                                                            } else {
                                                                builderServices.append(service_name).append(" & ");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    builderServices.append("");
                                                }
                                                objBookingList.services = builderServices.toString();
                                                objBookingList.rescdule_time = rescdule_time;
                                                alBookingLists.add(objBookingList);
                                            }
                                        }


                                     /*   Collections.sort(alBookingLists, new Comparator<BookingList>() {

                                            @Override
                                            public int compare(BookingList lhs,
                                                               BookingList rhs) {
                                                // Do your comparison logic here and retrn accordingly.
                                                return getDateFromString(rhs.date,"EEEE, dd MMM yyyy").compareTo( getDateFromString(lhs.date,"EEEE, dd MMM yyyy"));
                                            }
                                        });*/



                                    } else {


                                    }

                                } else {

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            } catch (NullPointerException e) {

                            }
                            //    objHomeAdapter.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //mPtrFrame.refreshComplete();
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(UnconfermedBookingActivity.this);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
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
            // mPtrFrame.refreshComplete();
            Constants.showPopupInternet(UnconfermedBookingActivity.this);
        }

    }

    private void ConfirmAllBooking() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {

            Constants.showPorgess(UnconfermedBookingActivity.this);
            String url = Constants.Path + Constants.CONFIRM_BOOKING;
            Log.d("api call post",url);
            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            all_bookings = "";

                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if (jsonObject.getString("success").equalsIgnoreCase("true"))
                                {
                                    daywise_array_list.clear();
                                   alBookingLists.clear();
                                    unconfirmedBookingAdapter = new UnconfirmedBookingAdapter(UnconfermedBookingActivity.this, daywise_array_list);
                                    LinearLayoutManager llm = new LinearLayoutManager(UnconfermedBookingActivity.this);
                                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                                    lvuncon_booking.setLayoutManager(llm);
                                    lvuncon_booking.setAdapter(unconfirmedBookingAdapter);
                                    if (All.equalsIgnoreCase("selected"))
                                    {
                                        callAllBooking();
                                    }
                                    else
                                    {
                                        callBookingDaywise();
                                    }

                                    callGetBooking();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("bookings", all_bookings);

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barbers Unconfirm Booking");
        } else {
            Constants.showPopupInternet(activity);
        }

    }


}
