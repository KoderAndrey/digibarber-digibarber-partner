package com.digibarber.app.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.StringRequest;
import com.digibarber.app.Beans.BookingListDayWise;
import com.digibarber.app.Beans.available_time;
import com.digibarber.app.CustomAdapters.LiveAvailavilityIndexRecyclerAdapter;
import com.digibarber.app.CustomAdapters.LiveAvailavilityRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.GridSpacingItemDecoration;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.digibarber.app.CustomClasses.Constants.isSameDay;

public class LiveAvailabilityActivity extends BaseActivity {

    TextView selected_date;
    RecyclerView live_avilty_recy;
    JSONArray ja_Open_Hours;
    ArrayList<String> availableHoursList = new ArrayList<>();
    ArrayList<String> availableHoursListForward = new ArrayList<>();
    ArrayList<String> available_hrsfinal = new ArrayList<>();
    int SelectedDay;
    ImageView iv_back;
    ArrayList<available_time> getTimeSlots = new ArrayList<>();
    String date = "";
    SimpleDateFormat formateDes;
    LinearLayout ll_closd_mesage_show;
    boolean isdayOpen = false;
    ArrayList<BookingListDayWise> parseData;

    ArrayList<String> alreadyBookedTime;
    SimpleDateFormat simpleDateFormat1;
    ArrayList<String> timeToaddcheck;
    ArrayList<String> timeToaddStart;
    ArrayList<String> timeToaddEnd;

    ArrayList<String> IndexList;
    RecyclerView recycler_num_index;
    LiveAvailavilityIndexRecyclerAdapter liveAvailavilityIndexRecyclerAdapter;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_live_avility);
        //formateDes = new SimpleDateFormat("dd MMMM yyyy");
        formateDes = new SimpleDateFormat("E, MMM d, yyyy");
        timeToaddcheck = new ArrayList<>();
        timeToaddcheck.add("00");
        timeToaddcheck.add("15");
        timeToaddcheck.add("30");
        timeToaddcheck.add("45");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        recycler_num_index = (RecyclerView) findViewById(R.id.recycler_num_index);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.VERTICAL, false);
        recycler_num_index.setLayoutManager(gridLayoutManager1);
        selected_date = (TextView) findViewById(R.id.selected_date);
        ll_closd_mesage_show = (LinearLayout) findViewById(R.id.ll_closd_mesage_show);
        live_avilty_recy = (RecyclerView) findViewById(R.id.live_avility_item_RecyclerView);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.VERTICAL, false);
        live_avilty_recy.setLayoutManager(gridLayoutManager3);
        bd = getIntent().getExtras();
        if (bd != null) {
            date = bd.getString("selected_date");
            SelectedDay = bd.getInt("SelectedDay");
            parseData = (ArrayList<BookingListDayWise>) bd.getSerializable("listbookings");
        }
        alreadyBookedTime = new ArrayList<>();
        timeToaddStart = new ArrayList<>();
        timeToaddEnd = new ArrayList<>();
        IndexList = new ArrayList<>();
        simpleDateFormat1 = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");

        try {
            Date formateDate = formateDes.parse(date);
            selected_date.setText(new SimpleDateFormat("dd MMMM yyyy").format(formateDate));
            //selected_date.setText(formateDate.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        String openHours = prefs.getString(Constants.KEY_OPEN_HOURS, "");
        if (openHours.equalsIgnoreCase("")) {
            getBarberProfile(date);
        } else {
            try {
                ja_Open_Hours = new JSONArray(openHours);
                checkIsDayclose(SelectedDay, "show");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isdayOpen) {
                    callRemoveMethod();
                } else {
                    finish();
                }

            }
        });


        try {
            if (parseData.size() > 0) {
                for (int i = 0; i < parseData.size(); i++) {
                    String time = parseData.get(i).booking_time;
                    String date = parseData.get(i).date;
                    String bookingtime[] = time.split("-");
                    String str_StartTime = bookingtime[0];
                    String str_EndTime = bookingtime[1];
                    Calendar startCalendar = Constants.getCalenderInstance();
                    Calendar endCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(simpleDateFormat1.parse(date.trim() + " " + bookingtime[0].trim()));
                    endCalendar.setTime(simpleDateFormat1.parse(date + " " + bookingtime[1].trim()));
                    SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
                    while (startCalendar.before(endCalendar)) {
                        String slotStartTime = slotTime.format(startCalendar.getTime());
                        alreadyBookedTime.add(slotStartTime);
                        startCalendar.add(Calendar.MINUTE, 15);
                    }

                }
            }
            Collections.sort(alreadyBookedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    private void callRemoveMethod() {

        AsyncTask<Void, Void, Void> asyc = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Constants.showPorgess(LiveAvailabilityActivity.this);

            }

            @Override
            protected Void doInBackground(Void... params) {

                for (int i = 0; i < getTimeSlots.size(); i++) {
                    if (getTimeSlots.get(i).status.equalsIgnoreCase("NotAvailble")) {
                        String notAvilaTime = getTimeSlots.get(i).time;
                        if (availableHoursListForward.contains(notAvilaTime)) {
                            availableHoursListForward.remove(notAvilaTime);
                        }

                    } else {
                        String notAvilaTime = getTimeSlots.get(i).time;
                        if (!availableHoursListForward.contains(notAvilaTime)) {
                            availableHoursListForward.add(notAvilaTime);
                        }

                    }
                }

                for (int i = 0; i < availableHoursListForward.size(); i++) {

                    available_hrsfinal.add("\"" + availableHoursListForward.get(i).toString().trim() + "\"");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                callsaveBarberHoursDaywise(date, available_hrsfinal);
            }
        };
        asyc.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void checkIsDayclose(int selectedDay, String Status) {
        try {
            switch (selectedDay) {
                case Calendar.SUNDAY:
                    JSONObject jo = ja_Open_Hours.getJSONObject(6);
                    if (jo.has("Sun")) {
                        JSONArray ja_Sun = jo.getJSONArray("Sun");
                        if (ja_Sun.length() > 0) {
                            isdayOpen = true;

                            recycler_num_index.setVisibility(View.VISIBLE);
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            live_avilty_recy.setVisibility(View.GONE);
                            recycler_num_index.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.MONDAY:

                    jo = ja_Open_Hours.getJSONObject(0);
                    if (jo.has("Mon")) {
                        JSONArray ja_Mon = jo.getJSONArray("Mon");
                        if (ja_Mon.length() > 0) {
                            isdayOpen = true;
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            recycler_num_index.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            recycler_num_index.setVisibility(View.GONE);
                            live_avilty_recy.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.TUESDAY:
                    jo = ja_Open_Hours.getJSONObject(1);
                    if (jo.has("Tue")) {
                        JSONArray ja_Tue = jo.getJSONArray("Tue");
                        if (ja_Tue.length() > 0) {
                            isdayOpen = true;
                            recycler_num_index.setVisibility(View.VISIBLE);
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            recycler_num_index.setVisibility(View.GONE);
                            live_avilty_recy.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.WEDNESDAY:
                    jo = ja_Open_Hours.getJSONObject(2);
                    if (jo.has("Wed")) {
                        JSONArray ja_Wed = jo.getJSONArray("Wed");
                        if (ja_Wed.length() > 0) {
                            isdayOpen = true;
                            recycler_num_index.setVisibility(View.VISIBLE);
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            recycler_num_index.setVisibility(View.GONE);
                            live_avilty_recy.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.THURSDAY:
                    jo = ja_Open_Hours.getJSONObject(3);
                    if (jo.has("Thu")) {
                        JSONArray ja_Thu = jo.getJSONArray("Thu");
                        if (ja_Thu.length() > 0) {
                            isdayOpen = true;
                            recycler_num_index.setVisibility(View.VISIBLE);
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            recycler_num_index.setVisibility(View.GONE);
                            live_avilty_recy.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.FRIDAY:
                    jo = ja_Open_Hours.getJSONObject(4);
                    if (jo.has("Fri")) {
                        JSONArray ja_Fri = jo.getJSONArray("Fri");
                        if (ja_Fri.length() > 0) {
                            isdayOpen = true;
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            recycler_num_index.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            live_avilty_recy.setVisibility(View.GONE);
                            recycler_num_index.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case Calendar.SATURDAY:
                    jo = ja_Open_Hours.getJSONObject(5);
                    if (jo.has("Sat")) {
                        JSONArray ja_Sat = jo.getJSONArray("Sat");
                        if (ja_Sat.length() > 0) {
                            isdayOpen = true;
                            live_avilty_recy.setVisibility(View.VISIBLE);
                            recycler_num_index.setVisibility(View.VISIBLE);
                            ll_closd_mesage_show.setVisibility(View.GONE);
                            getAvailableHours(date, SelectedDay, Status);
                        } else {
                            isdayOpen = false;
                            live_avilty_recy.setVisibility(View.GONE);
                            recycler_num_index.setVisibility(View.GONE);
                            ll_closd_mesage_show.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            isdayOpen = false;
            live_avilty_recy.setVisibility(View.GONE);
            recycler_num_index.setVisibility(View.GONE);
            ll_closd_mesage_show.setVisibility(View.VISIBLE);
        }
    }


    void addChart(String date, int selectedDay) {

        try {
            String startLunch = "";
            String endLunch = "";
            String startTime = "";
            String endTime = "";
            for (int i = 0; i < ja_Open_Hours.length(); i++) {
                JSONObject jo = ja_Open_Hours.getJSONObject(i);
                switch (selectedDay) {
                    case Calendar.SUNDAY:
                        if (jo.has("Sun")) {
                            JSONArray ja_Sun = jo.getJSONArray("Sun");
                            if (ja_Sun.length() >= 2) {

                                startTime = ja_Sun.getJSONObject(0).getString("start_time");
                                endTime = ja_Sun.getJSONObject(1).getString("end_time");

                                startLunch = ja_Sun.getJSONObject(0).getString("end_time");
                                endLunch = ja_Sun.getJSONObject(1).getString("end_time");
                            } else {

                                startTime = ja_Sun.getJSONObject(0).getString("start_time");
                                endTime = ja_Sun.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.MONDAY:

                        if (jo.has("Mon")) {

                            JSONArray ja_Mon = jo.getJSONArray("Mon");
                            if (ja_Mon.length() >= 2) {
                                startTime = ja_Mon.getJSONObject(0).getString("start_time");
                                endTime = ja_Mon.getJSONObject(1).getString("end_time");


                                startLunch = ja_Mon.getJSONObject(0).getString("end_time");
                                endLunch = ja_Mon.getJSONObject(1).getString("start_time");
                            } else {

                                startTime = ja_Mon.getJSONObject(0).getString("start_time");
                                endTime = ja_Mon.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.TUESDAY:

                        if (jo.has("Tue")) {

                            JSONArray ja_Tue = jo.getJSONArray("Tue");
                            if (ja_Tue.length() >= 2) {
                                startTime = ja_Tue.getJSONObject(0).getString("start_time");
                                endTime = ja_Tue.getJSONObject(1).getString("end_time");
                                startLunch = ja_Tue.getJSONObject(0).getString("end_time");
                                endLunch = ja_Tue.getJSONObject(1).getString("start_time");
                            } else {


                                startTime = ja_Tue.getJSONObject(0).getString("start_time");
                                endTime = ja_Tue.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.WEDNESDAY:

                        if (jo.has("Wed")) {

                            JSONArray ja_Wed = jo.getJSONArray("Wed");
                            if (ja_Wed.length() >= 2) {
                                startTime = ja_Wed.getJSONObject(0).getString("start_time");
                                endTime = ja_Wed.getJSONObject(1).getString("end_time");


                                startLunch = ja_Wed.getJSONObject(0).getString("end_time");
                                endLunch = ja_Wed.getJSONObject(1).getString("start_time");
                            } else {


                                startTime = ja_Wed.getJSONObject(0).getString("start_time");
                                endTime = ja_Wed.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.THURSDAY:

                        if (jo.has("Thu")) {

                            JSONArray ja_Thu = jo.getJSONArray("Thu");
                            if (ja_Thu.length() >= 2) {
                                startTime = ja_Thu.getJSONObject(0).getString("start_time");
                                endTime = ja_Thu.getJSONObject(1).getString("end_time");


                                startLunch = ja_Thu.getJSONObject(0).getString("end_time");
                                endLunch = ja_Thu.getJSONObject(1).getString("start_time");
                            } else {

                                startTime = ja_Thu.getJSONObject(0).getString("start_time");
                                endTime = ja_Thu.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.FRIDAY:

                        if (jo.has("Fri")) {

                            JSONArray ja_Fri = jo.getJSONArray("Fri");
                            if (ja_Fri.length() >= 2) {
                                startTime = ja_Fri.getJSONObject(0).getString("start_time");
                                endTime = ja_Fri.getJSONObject(1).getString("end_time");


                                startLunch = ja_Fri.getJSONObject(0).getString("end_time");
                                endLunch = ja_Fri.getJSONObject(1).getString("start_time");
                            } else {

                                startTime = ja_Fri.getJSONObject(0).getString("start_time");
                                endTime = ja_Fri.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }


                        break;
                    case Calendar.SATURDAY:

                        if (jo.has("Sat")) {
                            JSONArray ja_Sat = jo.getJSONArray("Sat");
                            if (ja_Sat.length() >= 2) {
                                startTime = ja_Sat.getJSONObject(0).getString("start_time");
                                endTime = ja_Sat.getJSONObject(1).getString("end_time");
                                startLunch = ja_Sat.getJSONObject(0).getString("end_time");
                                endLunch = ja_Sat.getJSONObject(1).getString("start_time");
                            } else {
                                startTime = ja_Sat.getJSONObject(0).getString("start_time");
                                endTime = ja_Sat.getJSONObject(0).getString("end_time");
                                startLunch = "0";
                                endLunch = "0";
                            }
                        }

                        break;

                }

            }

            String startT[] = startTime.split(":");
            String endT[] = endTime.split(":");
            int startIntT = 0;


            int endIntT = 0;
            int endIntM = 0;
            if (startT.length > 0) {
                startIntT = Integer.parseInt(startT[0]);
            }
            if (endT.length > 0) {
                endIntT = Integer.parseInt(endT[0]);
            }
            if (endT.length > 1) {
                endIntM = Integer.parseInt(endT[1]);
            }
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, endIntT);
            calendar.set(Calendar.MINUTE, endIntM);
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.MINUTE, -15);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");


            DecimalFormat formate = new DecimalFormat("00");

            int i = startIntT;
            try {
                endIntT = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

            } catch (Exception e) {

            }
            while (i <= endIntT) {


                Calendar currTime = Calendar.getInstance();


                String s1 = simpleDateFormat.format(currTime.getTime());
                try {

                    boolean isShowAl = true;
                    if (i >= Integer.valueOf(s1)) {
                        isShowAl = true;

                    } else
                        isShowAl = false;

                    try {
                        if (!isSameDay(new SimpleDateFormat("dd MMMM yyyy").parse(selected_date.getText().toString()), calendar.getTime())) {
                            isShowAl = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                   /* if ((!startLunch.equalsIgnoreCase("0") && !endLunch.equalsIgnoreCase("0") ) )
                    {
                        if ((!startLunch.equalsIgnoreCase("0") && !endLunch.equalsIgnoreCase("0")  ))
                        {
                            SimpleDateFormat lunchSimpleformat=new SimpleDateFormat("HH");

                            try {
                                Date currslotdate=lunchSimpleformat.parse(String.valueOf(i));
                                Date startLuchdate=lunchSimpleformat.parse(startLunch);
                                Date endLucnhdate=lunchSimpleformat.parse(endLunch);


                                if(currslotdate.after(startLuchdate) && currslotdate.before(endLucnhdate))
                                    isShowAl=false;


                            }
                            catch(Exception e)
                            {

                            }

                        }
                    }*/


                    if (isShowAl)


                        IndexList.add(formate.format(i) + ":00");
                } catch (NumberFormatException e) {

                }
                i = i + 1;
            }


            liveAvailavilityIndexRecyclerAdapter = new LiveAvailavilityIndexRecyclerAdapter(LiveAvailabilityActivity.this, IndexList);
            recycler_num_index.setAdapter(liveAvailavilityIndexRecyclerAdapter);
            liveAvailavilityIndexRecyclerAdapter.notifyDataSetChanged();

            getTimeSlots = getTimeSlots(date, startTime, endTime, startLunch, endLunch);

            if (getTimeSlots.size() == 0) {
                live_avilty_recy.setVisibility(View.GONE);
                recycler_num_index.setVisibility(View.GONE);
                ll_closd_mesage_show.setVisibility(View.VISIBLE);
            }

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacingLiveAvail);
            LiveAvailavilityRecyclerAdapter laca = new LiveAvailavilityRecyclerAdapter(LiveAvailabilityActivity.this, getTimeSlots);
            live_avilty_recy.addItemDecoration(new GridSpacingItemDecoration(4, spacingInPixels, false));
            recycler_num_index.addItemDecoration(new GridSpacingItemDecoration(1, spacingInPixels, false));
            live_avilty_recy.setAdapter(laca);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Constants.dismissProgress();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Constants.dismissProgress();
        }

    }


    ArrayList<available_time> getTimeSlots(String date, String startTime, String endTime, String startLuch, String endLucnh) {
        ArrayList<available_time> alLiveAvailaBiltyTime = new ArrayList<>();
        try {
            /*Normal all Slots*/
            // SimpleDateFormat sdf = ZoomConst.getSimpleDateFormate_yyyy_MM_dd_kk_mm();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
            Calendar startCalendar = Constants.getCalenderInstance();

            String starttime[] = startTime.split(":");
            String endtime[] = endTime.split(":");

            if (starttime[1].equalsIgnoreCase("00")) {
            } else {
                for (int i = 0; i < timeToaddcheck.size(); i++) {
                    if (timeToaddcheck.get(i).equalsIgnoreCase(starttime[1].toString().trim())) {
                        startTime = starttime[0] + ":00";
                        break;
                    } else {
                        timeToaddStart.add(starttime[0] + ":" + timeToaddcheck.get(i));
                    }
                }
            }

            if (endtime[1].equalsIgnoreCase("45")) {
            } else {
                int value = 0;
                if (endtime[1].equalsIgnoreCase("00")) {
                    value = 0;
                } else if (endtime[1].equalsIgnoreCase("15")) {
                    value = 1;
                } else if (endtime[1].equalsIgnoreCase("30")) {
                    value = 2;
                }
                for (int i = value; i < timeToaddcheck.size(); i++) {
                    if (timeToaddcheck.get(i).equalsIgnoreCase("45")) {
                        // endTime = endtime[0] + ":45";
                        timeToaddEnd.add(endtime[0] + ":" + timeToaddcheck.get(i));
                        break;
                    } else {
                        timeToaddEnd.add(endtime[0] + ":" + timeToaddcheck.get(i));
                    }
                }
            }

            for (int i = 0; i < timeToaddEnd.size(); i++) {
                //      Log.e("Deep End Array","POs:"+timeToaddEnd.get(i));
            }
            for (int i = 0; i < timeToaddStart.size(); i++) {
                //       Log.e("Deep Start Array","POs:"+timeToaddStart.get(i));
            }
            for (int i = 0; i < timeToaddcheck.size(); i++) {
                //    Log.e("Deep Check Array","POs:"+timeToaddcheck.get(i));
            }

            startCalendar.setTime(sdf.parse(date.trim() + " " + startTime.trim()));


            Calendar endCalendar = Constants.getCalenderInstance();
            endCalendar.setTime(sdf.parse(date + " " + endTime.trim()));
            SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");

            /*Lunch Time Slots*/
            Calendar startLunchCalendar = null;
            Calendar endLunchCalendar = null;

            if (!startLuch.equalsIgnoreCase("0") && !endLucnh.equalsIgnoreCase("0")) {
                startLunchCalendar = Constants.getCalenderInstance();
                startLunchCalendar.setTime(sdf.parse(date + " " + startLuch.trim()));
                endLunchCalendar = Constants.getCalenderInstance();
                endLunchCalendar.setTime(sdf.parse(date + " " + endLucnh.trim()));
            }
            /*While loop for opening hours slots , lunch time slots , Confirmation hours slots , Autoconfirmation hours slots*/
            int count = 0;
            Calendar calCurrentTime = Constants.getCalenderInstance();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");


            while (!startCalendar.after(endCalendar)) {
                String slotStartTime = slotTime.format(startCalendar.getTime());
                /*Condition to check if lunch start end times are equlas*/


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                String s = simpleDateFormat.format(simpleDateFormat.parse(slotStartTime));

                for (int i = 0; i < alreadyBookedTime.size(); i++) {


                }

                Calendar currTime = Calendar.getInstance();

                String s1 = simpleDateFormat.format(currTime.getTime());
                try {
                    boolean isShowAll = true;
                    if (Integer.valueOf(s) >= Integer.valueOf(s1)) {
                        isShowAll = true;
                    } else {
                        isShowAll = false;

                    }


                    if (currTime.after(endCalendar)) {
                        isShowAll = false;

                    }
                    if (!isSameDay(new SimpleDateFormat("dd MMMM yyyy").parse(selected_date.getText().toString()), currTime.getTime())) {
                        isShowAll = true;
                    }

                    // This Condition for check slot time lies between lunch Timing

                    if ((!startLuch.equalsIgnoreCase("0") && !endLucnh.equalsIgnoreCase("0"))) {
                        if ((!startLuch.equalsIgnoreCase("") && !endLucnh.equalsIgnoreCase(""))) {
                            SimpleDateFormat lunchSimpleformat = new SimpleDateFormat("HH:mm");

                            try {
                                Date currslotdate = lunchSimpleformat.parse(slotStartTime);
                                Date startLuchdate = lunchSimpleformat.parse(startLuch);
                                Date endLucnhdate = lunchSimpleformat.parse(endLucnh);

                                SimpleDateFormat lunchSimpleformat1 = new SimpleDateFormat("HH");
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(startLuchdate);

                                int unroundedMinutes = calendar.get(Calendar.MINUTE);
                                if (unroundedMinutes > 0) {
                                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                                    calendar.set(Calendar.MINUTE, 0);
                                }

                                if (currslotdate.after(calendar.getTime()) && currslotdate.before(endLucnhdate)) {
                                    alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "NotAvailble_InterValTime"));
                                    isShowAll = false;
                                }
                            } catch (Exception e) {
                            }

                        }
                    }


                    if (isShowAll) {


                        if (startCalendar.getTime().before(calCurrentTime.getTime()))
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "NotAvailble_CannotBooked"));

                        else if (timeToaddStart.size() > 0 && timeToaddStart.contains(Constants.getCurrentTime_hh_mm(startCalendar).trim())) {
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "TimeNotAvailble"));
                        } else if (timeToaddEnd.size() > 0 && timeToaddEnd.contains(Constants.getCurrentTime_hh_mm(startCalendar).trim())) {
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "TimeNotAvailble"));
                        } else if (startCalendar.equals(startLunchCalendar)) {
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "TimeNotAvailble"));
                        } else if ((startCalendar.after(startLunchCalendar) && startCalendar.before(endLunchCalendar))) {
                            /*Condition to add Lunch time slot and disabled before confirmation time */
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "TimeNotAvailble"));
                        } else if (alreadyBookedTime.size() > 0 && alreadyBookedTime.contains(Constants.getCurrentTime_hh_mm(startCalendar).trim())) {

                            /*Condition to add  already booked Time Slots*/
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "TimeNotAvailble"));
                        } else if (availableHoursList.size() > 0 && !availableHoursList.contains(Constants.getCurrentTime_hh_mm(startCalendar).trim())) {

                            /*Condition to add  Unavailable Time Slots*/
                            alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "NotAvailble"));
                        } else {

                            if (availableHoursList.size() <= 0) {
                                availableHoursListForward.add(slotStartTime);
                            }
                            if (slotStartTime.equalsIgnoreCase(endTime))
                                alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "NotAvailble"));
                            else

                                alLiveAvailaBiltyTime.add(new available_time(slotStartTime, "Availble"));
                        }
                    }

                } catch (NumberFormatException e) {

                }
              /*  if (count % 5 != 0) {
                    startCalendar.add(Calendar.MINUTE, 15);
                }*/
                startCalendar.add(Calendar.MINUTE, 15);
                count++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return alLiveAvailaBiltyTime;
    }

    public void callsaveBarberHoursDaywise(final String date, final ArrayList<String> unavailable_hrs) {

        Collections.sort(unavailable_hrs);
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.d("api call post", Constants.SaveBarberOpenHoursDayWise);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.SaveBarberOpenHoursDayWise,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    finish();
                                }
                            } catch (Exception e) {


                                e.printStackTrace();
                            }

                            /*-------Get Time Chat Code--------*/
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.showPopupServer(activity);
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
                    params.put("unavailable_hrs", unavailable_hrs.toString().replaceAll(" ", ""));
                    params.put("date", date);
                    System.out.println(" ** PARAMS ** " + params);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void getAvailableHours(final String date, final int selectedDay, String status) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            if (status.equalsIgnoreCase("show")) {
                Constants.showPorgess(LiveAvailabilityActivity.this);
            }
            Log.d("api call post", ApiUrls.GetAvailablehours);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetAvailablehours,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            availableHoursListForward = new ArrayList<>();
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    String unavailable_hrs = jo.getString("unavailable_hrs");
                                    availableHoursList = new ArrayList<>();
                                    availableHoursListForward = new ArrayList<>();
                                    if (unavailable_hrs != null && !unavailable_hrs.equalsIgnoreCase("")) {
                                        JSONArray jaUnavailable = new JSONArray(unavailable_hrs);
                                        for (int i = 0; i < jaUnavailable.length(); i++) {
                                            availableHoursList.add(jaUnavailable.getString(i));
                                            availableHoursListForward.add(jaUnavailable.getString(i));
                                        }

                                    }
                                    addChart(date, selectedDay);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            /*-------Get Time Chat Code--------*/
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.showPopupServer(activity);
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

                    params.put("barber_id", "0");
                    params.put("date", date);
                    System.out.println(" ** PARAMS ** " + params);

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    private void getBarberProfile(final String date) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(LiveAvailabilityActivity.this);
            Log.d("api call post", Constants.GetBarberProfile);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.GetBarberProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {

                                    JSONArray jaData = jo.getJSONArray("data");
                                    JSONObject jo0 = jaData.getJSONObject(0);
                                    String address = jo0.getString("address");
                                    String lon = jo0.getString("lon");
                                    String lat = jo0.getString("lat");
                                    String bank_detail = jo0.getString("bank_detail");
                                    String open_hours = jo0.getString("open_hours");
                                    String profile_image = jo0.getString("profile_image");
                                    String barber_id = jo0.getString("barber_id");
                                    String phone = jo0.getString("phone");
                                    String barber_name = jo0.getString("barber_name");
                                    String workplace = jo0.getString("workplace");
                                    String email = jo0.getString("email");

                                    JSONArray jaGallery = new JSONArray();
                                    if (jo0.has("gallery")) {
                                        jaGallery = jo0.getJSONArray("gallery");
                                    } else {

                                    }
                                    JSONArray Jaservices = new JSONArray();
                                    if (jo0.has("services")) {
                                        Jaservices = jo0.getJSONArray("services");
                                    } else {

                                    }
                                    prefs.edit().putString(Constants.KEY_USER_ID, barber_id).apply();
                                    prefs.edit().putString(Constants.KEY_FULL_NAME, barber_name).apply();
                                    prefs.edit().putString(Constants.KEY_EMAIL, email).apply();
                                    prefs.edit().putString(Constants.KEY_PHONE, phone).apply();
                                    prefs.edit().putString(Constants.KEY_BANK_DETAIL, bank_detail).apply();
                                    prefs.edit().putString(Constants.KEY_PROFILE_IMAGE, profile_image).apply();
                                    prefs.edit().putString(Constants.KEY_WORKPLACE, workplace).apply();
                                    prefs.edit().putString(Constants.KEY_ADDRESS, address).apply();
                                    prefs.edit().putString(Constants.KEY_FULL_NAME, barber_name).apply();
                                    prefs.edit().putString(Constants.KEY_SERVICES, Jaservices.toString()).apply();
                                    prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, jaGallery.toString()).apply();
                                    prefs.edit().putString(Constants.KEY_OPEN_HOURS, open_hours).apply();
                                    ja_Open_Hours = new JSONArray(open_hours);
                                    // getAvailableHours(date, SelectedDay, "DontShow");
                                    checkIsDayclose(SelectedDay, "DontShow");

                                } else {

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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", "35f5df37-d3c9-4ed9-ad77-68cc58db4088"));
                    headers.put("user_id", prefs.getString("user_id", "28"));
                    return headers;
                }

            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


}
