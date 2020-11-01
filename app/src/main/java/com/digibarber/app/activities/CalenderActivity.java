package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.Beans.AvailableDays;
import com.digibarber.app.Beans.BookingList;
import com.digibarber.app.Beans.BookingListDayWise;
import com.digibarber.app.CustomAdapters.CalenderRecyclerViewAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.CalenderListCallbacks;
import com.digibarber.app.R;
import com.digibarber.app.materialcalendarview.CalendarDay;
import com.digibarber.app.materialcalendarview.DayViewDecorator;
import com.digibarber.app.materialcalendarview.DayViewFacade;
import com.digibarber.app.materialcalendarview.MaterialCalendarView;
import com.digibarber.app.materialcalendarview.OnDateSelectedListener;
import com.digibarber.app.materialcalendarview.OnMonthChangedListener;
import com.digibarber.app.materialcalendarview.spans.DotSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.digibarber.app.CustomClasses.Constants.getDateFromString;


public class CalenderActivity extends BaseActivity implements DayViewDecorator, CalenderListCallbacks {

    TextView cur_dat;
    MaterialCalendarView mcv;
    ImageView previous, next;
    SimpleDateFormat formateDes;

    DateFormat FORMATTER;
    TextView showMonth, showYear;
    ArrayList<BookingListDayWise> parseData = new ArrayList<>();
    ArrayList<AvailableDays> availableDays;
    RecyclerView recyclerView;
    private ArrayList<BookingListDayWise> aryList = new ArrayList<>();
    FrameLayout fl_avail_unavail;
    FrameLayout live_available;
    ImageView iv_available_back;
    String selectedDate = null;
    String Cal_date_Month = "";
    String Cal_date_year = "";
    ImageView iv_back;
    int SelectedDay;
    String currentDate = "";
    ArrayList<String> addAbleHours = new ArrayList<>();
    ArrayList<String> ReschduleListTime = new ArrayList<>();
    ArrayList<String> removeableHours = new ArrayList<>();
    DateFormat Cal_Format;
    int black;
    int white;
    Drawable dawableRed;
    Drawable drawableBlack;

    HashSet<CalendarDay> setDotBookingsUnconfirmed = new HashSet<>();
    HashSet<CalendarDay> setDotBookingsConfirmed = new HashSet<>();


    HashSet<CalendarDay> setCurrentSelDays = new HashSet<>();
    HashSet<CalendarDay> setSelectedDays = new HashSet<>();
    int lessRound;
    SimpleDateFormat sdfddMMMM;
    RelativeLayout bookings;
    Typeface robotoRegular;
    String Cal_date;
    Date beforeThreeMontDate;
    Date selectedMonthDate;
    View bottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView tv_confirm;
    private TextView tv_reschdule;
    private TextView tv_delete;
    private TextView tv_back;
    private int Slecetdpos = 0;
    ArrayList<BookingList> alBookingLists;
    private SimpleDateFormat dotParseDate;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#F7F7F7"));
        }
        setContentView(R.layout.activity_calender);
        bd = getIntent().getExtras();
        if (bd != null) {
            alBookingLists = (ArrayList<BookingList>) bd.getSerializable("alBookingLists");
        }
        setDotBookingsUnconfirmed = new HashSet<>();
        setDotBookingsConfirmed = new HashSet<>();
        dotParseDate = new SimpleDateFormat("EEEE, dd MMM yyyy");

        if (alBookingLists != null && alBookingLists.size() > 0) {
            for (int i = 0; i < alBookingLists.size(); i++) {

                try {
                    String isconfrirm = alBookingLists.get(i).is_confirmed;
                    Date date = dotParseDate.parse(alBookingLists.get(i).date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    CalendarDay calDay = CalendarDay.from(cal);
                    if (isconfrirm.equalsIgnoreCase("0")) {
                        //grey color //unconfirmed
                        setDotBookingsUnconfirmed.add(calDay);
                    } else if (isconfrirm.equalsIgnoreCase("1")) {
                        //green color //Confimred
                        setDotBookingsConfirmed.add(calDay);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.e("bd", alBookingLists.size() + "");
        }


        bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_confirm = (TextView) bottomSheet.findViewById(R.id.tv_confirm);
        tv_reschdule = (TextView) bottomSheet.findViewById(R.id.tv_reschdule);
        tv_delete = (TextView) bottomSheet.findViewById(R.id.tv_delete);
        tv_back = (TextView) bottomSheet.findViewById(R.id.tv_back);


        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (parseData.get(Slecetdpos).is_confirmed.equalsIgnoreCase("1")) {
                    ShowPopupDialougCpmfrimed();
                } else {
                    callConfirmCancelUpcomingBooking("show", parseData.get(Slecetdpos).booking_id, "1", "", "");
                }
            }
        });
        tv_reschdule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parseData.get(Slecetdpos).is_confirmed.equalsIgnoreCase("1")) {
                    ShowPopupDialougCpmfrimed();
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    try {
                        String date = parseData.get(Slecetdpos).date;
                        String booking_time = parseData.get(Slecetdpos).booking_time;
                        String bookingtime[] = booking_time.split("-");
                        String str_StartTime = bookingtime[0];
                        String str_EndTime = bookingtime[1];
                        // SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
                        final ArrayList<String> unavailableHoursList = new ArrayList<>();
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                        Calendar startCalendar = Constants.getCalenderInstance();
                        Calendar endCalendar = Constants.getCalenderInstance();

                        startCalendar.setTime(simpleDateFormat1.parse(date.trim() + " " + bookingtime[0].trim()));

                        endCalendar.setTime(simpleDateFormat1.parse(date + " " + bookingtime[1].trim()));
                        SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
                        long mintues = Constants.getDateDiffernce(startCalendar.getTime(), endCalendar.getTime());

                        while (!startCalendar.after(endCalendar)) {
                            String slotStartTime = slotTime.format(startCalendar.getTime());
                            addAbleHours.add(slotStartTime);
                            startCalendar.add(Calendar.MINUTE, 15);
                        }
                        for (int i = 0; i < addAbleHours.size(); i++) {
                            if (i != addAbleHours.size() - 1) {
                                unavailableHoursList.add(addAbleHours.get(i));
                            }
                        }
                        callGetUnavailableHoursReschedule(parseData.get(Slecetdpos).booking_id, date, booking_time, unavailableHoursList, startCalendar.getTime(), endCalendar.getTime(), mintues);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showPopupDialogChecking(Slecetdpos);
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });


        fl_avail_unavail = (FrameLayout) findViewById(R.id.fl_avail_unavail);
        live_available = (FrameLayout) findViewById(R.id.live_available);
        iv_available_back = (ImageView) findViewById(R.id.iv_available_back);
        black = getResources().getColor(R.color.black);
        white = getResources().getColor(R.color.colorWhite);
        lessRound = getResources().getColor(R.color.lessRoundedGrey);
        dawableRed = getResources().getDrawable(R.drawable.back_notification);
        drawableBlack = getResources().getDrawable(R.drawable.black_back);
        cur_dat = (TextView) findViewById(R.id.currentDate);
        bookings = (RelativeLayout) findViewById(R.id.bookings);
        recyclerView = (RecyclerView) findViewById(R.id.Calendar_RecyclerView);
        formateDes = new SimpleDateFormat("EEEE/MMMM/yyyy");
        FORMATTER = formateDes;
        iv_back = (ImageView) findViewById(R.id.iv_back);
        previous = (ImageView) findViewById(R.id.previous_arrow);
        next = (ImageView) findViewById(R.id.next_arrow);
        mcv = (MaterialCalendarView) findViewById(R.id.calendarView);
        mcv.setTopbarVisible(false);
        Calendar calendar = Calendar.getInstance();
        mcv.setDateSelected(calendar.getTime(), true);
        CalendarDay cal = new CalendarDay();
        setCurrentSelDays.add(cal);
        mcv.addDecorator(new SelectedDayDecorator(setCurrentSelDays, white, dawableRed));
        mcv.addDecorator(new DisableAllPastDate(lessRound));
        showMonth = (TextView) findViewById(R.id.show_month);
        showYear = (TextView) findViewById(R.id.show_year);
        String dateSelc = FORMATTER.format(new Date());
        final String[] data = dateSelc.split("/");
        showMonth.setText(data[1]);
        showYear.setText(data[2]);
        //....................set current Date on Textview
        sdfddMMMM = new SimpleDateFormat("dd MMMM");
        DateFormat df = sdfddMMMM;
        String cdate = df.format(new Date());
        cur_dat.setText(cdate);

        //.........................hit api on Current Date

        Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Cal_date = Cal_Format.format(new Date());


        Calendar cals = Calendar.getInstance();
        SelectedDay = cals.get(Calendar.DAY_OF_WEEK);

        selectedDate = Cal_date;
        currentDate = Cal_date;
        DateFormat Cal_Format_Month = new SimpleDateFormat("MMMM");
        DateFormat Cal_Format__year = new SimpleDateFormat("yyyy");
        Cal_date_Month = Cal_Format_Month.format(new Date());
        Cal_date_year = Cal_Format__year.format(new Date());

        getBookingApi("show", Cal_date, Cal_date_year, Cal_date_Month);
        Calendar currentcal = Calendar.getInstance();
        currentcal.add(Calendar.MONTH, -3);
        int year = currentcal.get(Calendar.YEAR);
        int month = currentcal.get(Calendar.MONTH);
        int day = currentcal.get(Calendar.DAY_OF_MONTH);

        mcv.state().edit()
                .setMinimumDate(CalendarDay.from(year, month, day))
                .commit();

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String dateSelc = FORMATTER.format(date.getDate());
                String[] dat = dateSelc.split("/");
                showMonth.setText(dat[1]);
                showYear.setText(dat[2]);

            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mcv.goToPrevious();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcv.goToNext();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        live_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = 1;
                if (availableDays == null)
                    return;
                for (int i = 0; i < availableDays.size(); i++) {
                    String date = availableDays.get(i).date;
                    if (date.equals(selectedDate)) {
                        if (availableDays.get(i).is_day_on.equalsIgnoreCase("0")) {
                            status = 0;
                            break;
                        }
                    }
                }
                if (status == 1) {
                    Intent it = new Intent(CalenderActivity.this, LiveAvailabilityActivity.class);
                    it.putExtra("selected_date", selectedDate);
                    it.putExtra("SelectedDay", SelectedDay);
                    it.putExtra("listbookings", parseData);
                    startActivity(it);
                } else {
                    ShowPopupDialougUnavailble();
                }
            }
        });

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DateFormat Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                String Cal_dates = Cal_Format.format(date.getDate());
                Date selecteddate = null;
                Date curDates = null;
                try {
                    selecteddate = Cal_Format.parse(Cal_dates);
                    curDates = Cal_Format.parse(Cal_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cur_dat.setText(sdfddMMMM.format(date.getDate()));
                selectedDate = Cal_dates;
                widget.removeDecorators();
                setSelectedDays = new HashSet<>();
                setSelectedDays.add(date);
                CalendarDay currentdate = mcv.getCurrentDate();
                if (!selecteddate.before(curDates)) {
                    widget.addDecorator(new SelectedDayDecorator(setCurrentSelDays, white, dawableRed));
                    if (selectedDate.equalsIgnoreCase(currentDate)) {
                        widget.addDecorator(new SelectedDayDecorator(setSelectedDays, white, dawableRed));
                    } else {
                        widget.addDecorator(new SelectedDayDecorator(setSelectedDays, white, drawableBlack));
                    }
                } else {
                    mcv.addDecorator(new SelectedDayDecorator(setCurrentSelDays, white, dawableRed));
                }
//                mcv.setTileHeight(16);
                mcv.addDecorator(new DisableAllPastDate(lessRound));
                DateFormat Cal_Format_Month = new SimpleDateFormat("MMMM");
                DateFormat Cal_Format__year = new SimpleDateFormat("yyyy");
                Cal_date_Month = Cal_Format_Month.format(date.getDate());
                Cal_date_year = Cal_Format__year.format(date.getDate());

                Calendar cal = Calendar.getInstance();
                cal.setTime(date.getDate());
                SelectedDay = cal.get(Calendar.DAY_OF_WEEK);
                //SelectedDay = date.getDay();

                getBookingApi("show", Cal_dates, Cal_date_year, Cal_date_Month);

                mcv.addDecorator(new EventDecoratorConfirmed(getResources().getColor(R.color.colur_green), setDotBookingsConfirmed));
                mcv.addDecorator(new EventDecoratorUnconfirmed(getResources().getColor(R.color.grey), setDotBookingsUnconfirmed));

                //Monday, September 11, 2017
            }
        });
        fl_avail_unavail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<BookingListDayWise> arrayListBookedDate = new ArrayList<>();

                for (int i = 0; i < parseData.size(); i++) {
                    if (parseData.get(i).is_confirmed.equalsIgnoreCase("1")) {
                        arrayListBookedDate.add(parseData.get(i));
                    }
                }

                if (arrayListBookedDate.size() == 0) {
                    if (!currentDate.equalsIgnoreCase(selectedDate)) {
                        String staus = "1";
                        for (int i = 0; i < availableDays.size(); i++) {
                            if (selectedDate.equalsIgnoreCase(availableDays.get(i).date)) {
                                staus = availableDays.get(i).is_day_on;
                                break;
                            }
                        }
                        if (staus.equalsIgnoreCase("1")) {
                            staus = "0";
                        } else {
                            staus = "1";
                        }

                        setSelectedDays = new HashSet<CalendarDay>();
                        DateFormat Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                        Date Cal_date = null;
                        try {
                            Cal_date = Cal_Format.parse(selectedDate);
                            CalendarDay day = CalendarDay.from(Cal_date);
                            Log.e("Here cal", "" + day.getDay());
                            setSelectedDays.add(day);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mcv.addDecorator(new SelectedDayDecorator(setCurrentSelDays, white, dawableRed));
                        mcv.addDecorator(new SelectedDayDecorator(setSelectedDays, white, drawableBlack));
                        mcv.addDecorator(new DisableAllPastDate(lessRound));
                        make_day_Available_unAvailable(selectedDate, staus, Cal_date_year, Cal_date_Month);

                    }
                } else {
                    ShowPopupDialougForAlreadyBooked();
                }


            }
        });
        mcv.addDecorator(new EventDecoratorConfirmed(getResources().getColor(R.color.colur_green), setDotBookingsConfirmed));
        mcv.addDecorator(new EventDecoratorUnconfirmed(getResources().getColor(R.color.grey), setDotBookingsUnconfirmed));

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderActivity.this, UnconfermedBookingActivity.class);
                startActivity(intent);
            }
        });


    }

    public void ShowPopupDialougForAlreadyBooked() {
        final Dialog dialog_first = new Dialog(this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_time_not_avaiable);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView alertTitle = (TextView) dialog_first.findViewById(R.id.alertTitle);
        TextView tv_addrress1 = (TextView) dialog_first.findViewById(R.id.tv_addrress1);
        tv_addrress1.setText("Sorry, You cannnot make yourself unavailable you have booking on this day.");
        alertTitle.setText("Ooops!");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }

   /* void OpenBottomView(final int pos) {
        new BottomSheet.Builder(CalenderActivity.this).title("").sheet(R.menu.bottom_sheet).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.item_confirm:

                        break;
                    case R.id.item_res:


                        break;
                    case R.id.item_del:

                        break;
                    case R.id.item_cancel:

                        break;
                }
            }
        }).show();
    }*/

    private void callGetUnavailableHoursReschedule(final String bookingid, final String date, final String strBookingTime, final ArrayList<String> unavailableHoursList, final Date startDate, final Date dateEnd, final long mintues) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(CalenderActivity.this);
            Log.d("api call post", ApiUrls.GetAvailablehours);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetAvailablehours,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            ReschduleListTime = new ArrayList<>();
                            Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    String unavailable_hrs = jo.getString("unavailable_hrs");
                                    if (unavailable_hrs != null && !unavailable_hrs.equalsIgnoreCase("")) {
                                        JSONArray jaUnavailable = new JSONArray(unavailable_hrs);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                                        SimpleDateFormat simpleTimeFormate = new SimpleDateFormat("kk:mm");
                                        Date sloteddate = null;
                                        Calendar calender = Constants.getCalenderInstance();
                                        for (int i = 0; i < jaUnavailable.length(); i++) {
                                            String time = jaUnavailable.getString(i);
                                            unavailableHoursList.add(jaUnavailable.getString(i));
                                            String slotedate = date + " " + time;
                                            sloteddate = simpleDateFormat.parse(slotedate);
                                            calender.setTime(sloteddate);
                                            if (sloteddate.after(dateEnd) && ReschduleListTime.size() < 3) {
                                                calender.add(Calendar.MINUTE, (int) mintues);
                                                String checktime = simpleTimeFormate.format(calender.getTime());
                                                if (jaUnavailable.toString().contains(checktime)) {
                                                    ReschduleListTime.add(time);
                                                }
                                            }
                                        }
                                        ArrayList unavailable_hrsfinal = new ArrayList<>();
                                        for (int i = 0; i < unavailableHoursList.size(); i++) {
                                            unavailable_hrsfinal.add("\"" + unavailableHoursList.get(i).toString().trim() + "\"");
                                        }
                                        Collections.sort(unavailable_hrsfinal);
                                        Log.e("unavailable_hrs", "" + jaUnavailable.toString());
                                        showReScheduleNewTime(date, mintues, ReschduleListTime, unavailable_hrsfinal, bookingid);
                                    }
                                } else {
                                    String message = jo.getString("message");
                                    Constants.showToast(CalenderActivity.this, message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            } catch (ParseException e) {
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
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("date", date);
                    params.put("barber_id", "0");
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void showReScheduleNewTime(final String date, final long servicetime, final ArrayList<String> ReschduleListTime, final ArrayList<String> unavailable_hrsfinal, final String bookingid) {
        final Dialog dialog_first = new Dialog(CalenderActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_reschedule);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        final TextView tv_time_first = (TextView) dialog_first.findViewById(R.id.tv_time_first);
        final TextView tv_time_second = (TextView) dialog_first.findViewById(R.id.tv_time_second);
        final TextView tv_time_third = (TextView) dialog_first.findViewById(R.id.tv_time_third);
        final TextView tv_back = (TextView) dialog_first.findViewById(R.id.tv_back);

       /* if (ReschduleListTime.size() > 0) {
            tv_time_first.setText(ReschduleListTime.get(0));
            tv_time_second.setText(ReschduleListTime.get(1));
            tv_time_first.setVisibility(View.VISIBLE);
            tv_time_second.setVisibility(View.VISIBLE);
        } else {
            tv_time_first.setVisibility(View.GONE);
            tv_time_second.setVisibility(View.GONE);
        }

        if (ReschduleListTime.size() > 1) {
            tv_time_third.setText(ReschduleListTime.get(2));
            tv_time_third.setVisibility(View.VISIBLE);
        } else {
            tv_time_third.setVisibility(View.GONE);
        }*/

        if (ReschduleListTime.size() == 1) {
            tv_time_first.setText(ReschduleListTime.get(0));
            tv_time_first.setVisibility(View.VISIBLE);
        } else {
            tv_time_first.setVisibility(View.GONE);
        }

        if (ReschduleListTime.size() == 2) {
            tv_time_second.setText(ReschduleListTime.get(1));
            tv_time_second.setVisibility(View.VISIBLE);
        } else {
            tv_time_second.setVisibility(View.GONE);
        }

        if (ReschduleListTime.size() == 3) {
            tv_time_third.setText(ReschduleListTime.get(2));
            tv_time_third.setVisibility(View.VISIBLE);
        } else {
            tv_time_third.setVisibility(View.GONE);
        }

        tv_time_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTimeSel = tv_time_first.getText().toString().trim();
                removeableHours = new ArrayList<String>();
                Calendar calSelected = Constants.getCalenderInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));

                    SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
                    while (!startCalendar.after(endCalendar)) {
                        String slotStartTime = slotTime.format(startCalendar.getTime());
                        removeableHours.add(slotStartTime);
                        startCalendar.add(Calendar.MINUTE, 15);
                    }
                    for (int i = 0; i < removeableHours.size(); i++) {
                        if (i != removeableHours.size() - 1) {
                            if (unavailable_hrsfinal.contains(removeableHours.get(i))) {
                                unavailable_hrsfinal.remove(removeableHours.get(i));
                            }
                        }
                    }
                    String timeRes = startTimeSel + " - " + lasttime;

                    callReschduleBooking(bookingid, timeRes);
                    dialog_first.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        tv_time_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTimeSel = tv_time_second.getText().toString().trim();
                removeableHours = new ArrayList<String>();
                Calendar calSelected = Constants.getCalenderInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));

                    SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
                    while (!startCalendar.after(endCalendar)) {

                        String slotStartTime = slotTime.format(startCalendar.getTime());
                        removeableHours.add(slotStartTime);
                        startCalendar.add(Calendar.MINUTE, 15);

                    }


                    for (int i = 0; i < removeableHours.size(); i++) {

                        if (i != removeableHours.size() - 1) {

                            if (unavailable_hrsfinal.contains(removeableHours.get(i))) {
                                unavailable_hrsfinal.remove(removeableHours.get(i));
                            }

                        }
                    }
                    String timeRes = startTimeSel + " - " + lasttime;

                    callReschduleBooking(bookingid, timeRes);
                    dialog_first.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog_first.dismiss();
            }
        });

        tv_time_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTimeSel = tv_time_third.getText().toString().trim();
                removeableHours = new ArrayList<String>();
                Calendar calSelected = Constants.getCalenderInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));
                    SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
                    while (!startCalendar.after(endCalendar)) {

                        String slotStartTime = slotTime.format(startCalendar.getTime());
                        removeableHours.add(slotStartTime);
                        startCalendar.add(Calendar.MINUTE, 15);

                    }


                    for (int i = 0; i < removeableHours.size(); i++) {

                        if (i != removeableHours.size() - 1) {

                            if (unavailable_hrsfinal.contains(removeableHours.get(i))) {
                                unavailable_hrsfinal.remove(removeableHours.get(i));
                            }

                        }
                    }

                    String timeRes = startTimeSel + " - " + lasttime;

                    callReschduleBooking(bookingid, timeRes);
                    dialog_first.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog_first.dismiss();
            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_first.dismiss();
            }
        });

        dialog_first.show();
    }


    private void callReschduleBooking(final String booking_id, final String booking_time) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(CalenderActivity.this);
            Log.d("api call post",Constants.RescheduleBooking);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.RescheduleBooking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    getBookingApi("dontshow", selectedDate, Cal_date_year, Cal_date_Month);
                                } else {
                                    String message = jo.getString("message");
                                }
                                Constants.dismissProgress();
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
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("booking_time", booking_time);
                    params.put("booking_id", booking_id);
                    Log.e("Deeeeep", "ReschduleParams:" + params);

                    //params.put("unavailable_hrs", unavailable_hrs.toString().replaceAll(" ", ""));
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void showPopupDialogChecking(final int pos) {
        final Dialog dialog_first = new Dialog(CalenderActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_cancel_booking);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

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
                ShowPopupDialougReason(pos);
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }

    public void ShowPopupDialougReason(final int pos) {
        final Dialog dialog_first = new Dialog(CalenderActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_cancel_reason);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        final TextView tv_unable_offer_service_time = (TextView) dialog_first.findViewById(R.id.tv_unable_offer_service_time);
        final TextView tv_cust_no_shop = (TextView) dialog_first.findViewById(R.id.tv_cust_no_shop);
        final TextView tv_barber_busy = (TextView) dialog_first.findViewById(R.id.tv_barber_busy);
        final TextView tv_cust_made_error = (TextView) dialog_first.findViewById(R.id.tv_cust_made_error);
        final TextView tv_other = (TextView) dialog_first.findViewById(R.id.tv_other);
        final TextView tv_cancel = (TextView) dialog_first.findViewById(R.id.tv_cancel);

        String CurrentDate = Constants.getPresentDayName() + ", " + Constants.getCurrentMonthName() + " " + Constants.getCurrentDate() + ", " + Constants.getCurrentYear() + " " + Constants.getCurrentTime_hh_mm(Constants.getCalenderInstance());
        String bookingtime[] = parseData.get(pos).booking_time.split("-");
        String str_StatTime = bookingtime[0].trim();
        String str_EndTime = bookingtime[1].trim();
        String strdateBooking = parseData.get(pos).date + " " + str_EndTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
        Date dateBooking = null;
        Date dateCurrent = null;
        try {
            dateBooking = simpleDateFormat.parse(strdateBooking);
            dateCurrent = simpleDateFormat.parse(CurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mintues = Constants.getDateDiffernce(dateCurrent, dateBooking);

        final long hours = mintues / 60;
        final String isConfimred = parseData.get(pos).is_confirmed;
        final String booking_id = parseData.get(pos).booking_id;
        final String barber_id = "0";

        final String date = parseData.get(pos).date;
        final String addAbleTime = parseData.get(pos).booking_time;
        final ArrayList<String> unavailableHoursList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
        Calendar startCalendar = Constants.getCalenderInstance();
        Calendar endCalendar = Constants.getCalenderInstance();
        try {
            startCalendar.setTime(simpleDateFormat1.parse(date.trim() + " " + bookingtime[0].trim()));
            endCalendar.setTime(simpleDateFormat1.parse(date + " " + bookingtime[1].trim()));
            SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
            while (!startCalendar.after(endCalendar)) {
                String slotStartTime = slotTime.format(startCalendar.getTime());
                addAbleHours.add(slotStartTime);
                startCalendar.add(Calendar.MINUTE, 15);
            }

            for (int i = 0; i < addAbleHours.size(); i++) {

                if (i != addAbleHours.size() - 1) {
                    unavailableHoursList.add(addAbleHours.get(i));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        tv_unable_offer_service_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookingCode = "";
                dialog_first.dismiss();
                String code = getCode("B1", isConfimred, hours);
                //  callGetAvailableHours(booking_id, barber_id, date, tv_unable_offer_service_time.getText().toString(), code, unavailableHoursList);
                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_unable_offer_service_time.getText().toString(), code);

            }
        });

        tv_cust_no_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();

                String code = getCode("B2", isConfimred, hours);
                // callGetAvailableHours(booking_id, barber_id, date, tv_cust_no_shop.getText().toString(), code, unavailableHoursList);
                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_cust_no_shop.getText().toString(), code);
            }
        });
        tv_barber_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
                String code = getCode("B3", isConfimred, hours);
                //callGetAvailableHours(booking_id, barber_id, date, tv_barber_busy.getText().toString(), code, unavailableHoursList);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_barber_busy.getText().toString(), code);
            }
        });

        tv_cust_made_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
                String code = getCode("B4", isConfimred, hours);
                callGetAvailableHours(booking_id, barber_id, date, tv_cust_made_error.getText().toString(), code, unavailableHoursList);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_cust_made_error.getText().toString(), code);
            }
        });
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    private void callGetAvailableHours(final String booking_id, final String barber_id, final String date, final String reason, final String reason_code, final ArrayList<String> unavailableHoursList) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(CalenderActivity.this);
            Log.d("api call post",ApiUrls.GetAvailablehours);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetAvailablehours,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {

                                    String unavailable_hrs = jo.getString("unavailable_hrs");
                                    if (unavailable_hrs != null && !unavailable_hrs.equalsIgnoreCase("")) {
                                        JSONArray jaUnavailable = new JSONArray(unavailable_hrs);
                                        for (int i = 0; i < jaUnavailable.length(); i++) {
                                            unavailableHoursList.add(jaUnavailable.getString(i));
                                        }
                                        Log.e("unavailable_hrs", "" + jaUnavailable.toString());
                                    }

                                    ArrayList unavailable_hrsfinal = new ArrayList<>();
                                    for (int i = 0; i < unavailableHoursList.size(); i++) {
                                        unavailable_hrsfinal.add("\"" + unavailableHoursList.get(i).toString().trim() + "\"");
                                    }
                                    Collections.sort(unavailable_hrsfinal);
                                    callConfirmCancelUpcomingBooking("dontshow", booking_id, "0", reason, reason_code);
                                } else {
                                    String message = jo.getString("message");
                                    Constants.showToast(CalenderActivity.this, message);
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
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("date", date);
                    params.put("barber_id", barber_id);

                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void ShowPopupDialougUnavailble() {
        final Dialog dialog_first = new Dialog(CalenderActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_unavailable);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_heading = (TextView) dialog_first.findViewById(R.id.tv_heading);
        TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
        TextView tvOk = (TextView) dialog_first.findViewById(R.id.tvOk);


        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    public void ShowPopupDialougCpmfrimed() {
        final Dialog dialog_first = new Dialog(CalenderActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_already_confirmed);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });

        dialog_first.show();
    }


    private void callConfirmCancelUpcomingBooking(final String status, final String booking_id, final String isConfrim, final String reason, final String reason_code) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            if (!status.equalsIgnoreCase("dontshow")) {
                Constants.showPorgess(CalenderActivity.this);
            }
            Log.d("api call post",Constants.CancelUpcomingBooking);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.CancelUpcomingBooking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    getBookingApi("dontshow", selectedDate, Cal_date_year, Cal_date_Month);
                                } else {
                                    String message = jo.getString("message");
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
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("is_confirm", isConfrim);
                    params.put("booking_id", booking_id);
                    params.put("reason_code", reason_code);
                    params.put("reason", reason);
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    String getCode(String codeNuber, String isConfimred, long hours) {
        String bookingCode = "";
        if (isConfimred.equalsIgnoreCase("1")) {
            if (hours > 24) {
                bookingCode = codeNuber + "A";
            } else if (hours >= 1 && hours <= 24) {
                bookingCode = codeNuber + "B";
            } else if (hours < 1) {
                bookingCode = codeNuber + "C";
            }
        } else {
            bookingCode = codeNuber + "D";
        }
        return bookingCode;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return false;
    }


    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
    }

    //API Data..............................................
    private void getBookingApi(final String Status, final String Date, final String year, final String month) {
        Log.e("MangoPAyID", prefs.getString(Constants.KEY_MANGOPAY_ID, ""));
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            if (!Status.equalsIgnoreCase("dontshow")) {
                Constants.showPorgess(CalenderActivity.this);
            }
            Log.d("api call post",Constants.Get_Booking_List_Daywise);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Get_Booking_List_Daywise,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("TAG", response);
                            Get_BookingList_MonthWise(year, month);
                            parseData = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    JSONObject joInner = jsonArray.getJSONObject(0);
                                    String date = joInner.getString("date");
                                    String is_day_on = joInner.getString("is_day_on");
                                    JSONArray jaRequests = joInner.getJSONArray("Requests");
                                    for (int i = 0; i < jaRequests.length(); i++) {
                                        JSONObject joRequests = jaRequests.getJSONObject(i);
                                        BookingListDayWise bldw = new BookingListDayWise();
                                        String booking_id = joRequests.getString("booking_id");
                                        String user_id = joRequests.getString("user_id");
                                        String is_confirmed = joRequests.getString("is_confirmed");
                                        String user_name = joRequests.getString("user_name");
                                        String booking_time = joRequests.getString("booking_time");
                                        String services = joRequests.getString("services");
                                        String is_reschedule = joRequests.getString("is_reschedule");
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

                                        Date dateSelected = null;
                                        Date dateCurrent = null;
                                        SimpleDateFormat Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
                                        String currentTime = Constants.getCurrentTime_hh_mm(Constants.getCalenderInstance());


                                        String[] strTime = booking_time.split("-");
                                        try {
                                            dateSelected = Cal_Format.parse(selectedDate + " " + currentTime);

                                            dateCurrent = Cal_Format.parse(currentDate + " " + strTime[0].trim());
                                            if (dateSelected.after(dateCurrent)) {
                                                bldw.isEnableDisable = "1";
                                            } else {
                                                bldw.isEnableDisable = "0";
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String[] parts = booking_time.split("-");
                                        bldw.service_name = builderServices.toString();
                                        bldw.booking_id = booking_id;
                                        bldw.user_id = user_id;
                                        bldw.is_confirmed = is_confirmed;
                                        bldw.user_name = user_name;
                                        bldw.booking_time = booking_time;
                                        bldw.services = services;
                                        bldw.is_reschedule = is_reschedule;
                                        bldw.is_day_on = is_day_on;
                                        bldw.date = date;
                                        bldw.date_time = date + " " + parts[parts.length - 1];
                                        parseData.add(bldw);
                                    }


                                    Collections.sort(parseData, new Comparator<BookingListDayWise>() {

                                        @Override
                                        public int compare(BookingListDayWise lhs,
                                                           BookingListDayWise rhs) {
                                            // Do your comparison logic here and retrn accordingly.
                                            return getDateFromString(lhs.date_time, "EEEE, MMMM dd, yyyy hh:ss").compareTo(getDateFromString(rhs.date_time, "EEEE, MMMM dd, yyyy hh:ss"));
                                        }
                                    });

                                    ArrayList<BookingListDayWise> parseData_dupli = new ArrayList<>();
                                    ArrayList<BookingListDayWise> parseData_dupli_confirm = new ArrayList<>();


                                    for (int k = 0; k < parseData.size(); k++) {
                                        BookingListDayWise bookingListDayWise = parseData.get(k);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm");
                                        Date bookingdate = simpleDateFormat.parse(bookingListDayWise.date_time);
                                        Date curr = Calendar.getInstance().getTime();
                                        String strcurrdate = simpleDateFormat.format(curr);
                                        Date currdate = simpleDateFormat.parse(strcurrdate);


                                        if (currdate.before(bookingdate)) {
                                            parseData_dupli.add(bookingListDayWise);

                                        }

                                    }
                                    for (int j = 0; j < parseData.size(); j++) {
                                        BookingListDayWise bookingListDayWise = parseData.get(j);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm");
                                        Date bookingdate = simpleDateFormat.parse(bookingListDayWise.date_time);
                                        Date curr = Calendar.getInstance().getTime();
                                        String strcurrdate = simpleDateFormat.format(curr);
                                        Date currdate = simpleDateFormat.parse(strcurrdate);


                                        if (currdate.after(bookingdate)) {
                                            parseData_dupli.add(bookingListDayWise);


                                        }


                                    }


                                    for (int i = 0; i < parseData_dupli.size(); i++) {
                                        BookingListDayWise bookingListDayWise = parseData_dupli.get(i);

                                        if (bookingListDayWise.is_confirmed.equalsIgnoreCase("1")) {
                                            parseData_dupli_confirm.add(bookingListDayWise);
                                        }

                                    }
                                    for (int i = 0; i < parseData_dupli.size(); i++) {
                                        BookingListDayWise bookingListDayWise = parseData_dupli.get(i);

                                        if (bookingListDayWise.is_confirmed.equalsIgnoreCase("0")) {
                                            parseData_dupli_confirm.add(bookingListDayWise);
                                        }

                                    }
                                    for (int i = 0; i < parseData_dupli.size(); i++) {
                                        BookingListDayWise bookingListDayWise = parseData_dupli.get(i);

                                        if (!bookingListDayWise.is_confirmed.equalsIgnoreCase("0") & !bookingListDayWise.is_confirmed.equalsIgnoreCase("1")) {
                                            parseData_dupli_confirm.add(bookingListDayWise);
                                        }

                                    }
                                    Collections.sort(parseData_dupli_confirm, new Comparator<BookingListDayWise>() {

                                        @Override
                                        public int compare(BookingListDayWise lhs,
                                                           BookingListDayWise rhs) {
                                            // Do your comparison logic here and retrn accordingly.
                                            return rhs.is_confirmed.compareTo(lhs.is_confirmed);
                                        }
                                    });
                                    parseData = parseData_dupli_confirm;
                                }
                                Log.e("TAG.................", parseData.size() + "");
                                CalenderRecyclerViewAdapter rvca = new CalenderRecyclerViewAdapter(CalenderActivity.this, parseData, CalenderActivity.this, selectedDate, currentDate);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(rvca);
                                rvca.notifyDataSetChanged();

                            } catch (Exception e) {
                                Log.e("parseData_dupli1", "Exception" + e);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.dismissProgress(); //CalenderActivity.this
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    // Get_BookingList_MonthWise(year, month);
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();                     //date=\(strDate)&&barber_id=0"
                    params.put("date", selectedDate);
                    params.put("barber_id", "0");

                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    private void Get_BookingList_MonthWise(final String year, final String month) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.d("api call post",Constants.Get_BookingList_MonthWise);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Get_BookingList_MonthWise,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", "" + response);
                            Constants.dismissProgress();
                            HashSet<CalendarDay> setDays = new HashSet<>();
                            availableDays = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    JSONArray jaAvailable_days = jsonObject.getJSONArray("available_days");
                                    String status = "1";
                                    for (int i = 0; i < jaAvailable_days.length(); i++) {
                                        JSONObject jsonArray = jaAvailable_days.getJSONObject(i);
                                        AvailableDays ad = new AvailableDays();
                                        ad.date = jsonArray.getString("date");
                                        ad.is_day_on = jsonArray.getString("is_day_on");
                                        availableDays.add(ad);


                                        //yyyy-mm-dd
                                        if (jsonArray.getString("is_day_on").equalsIgnoreCase("0")) {
                                            DateFormat Cal_Format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                                            Date datee = Cal_Format.parse(jsonArray.getString("date"));
                                            Date currentDate = Cal_Format.parse(Cal_date);
                                            if (!datee.before(currentDate)) {
                                                Calendar cal = Calendar.getInstance();
                                                cal.setTime(datee);
                                                CalendarDay calDay = CalendarDay.from(cal);
                                                setDays.add(calDay);
                                            }
                                        }
                                        if (selectedDate.equalsIgnoreCase(jsonArray.getString("date"))) {
                                            if (jsonArray.getString("is_day_on").equalsIgnoreCase("0")) {
                                                status = "0";
                                            }
                                        }
                                    }
                                    if (status.equalsIgnoreCase("0")) {
                                        iv_available_back.setImageResource(R.mipmap.unavaila_grey);
                                    } else {
                                        iv_available_back.setImageResource(R.mipmap.availa_black);
                                    }

                                    ImageView iv_available_back;
                                    ImageView iv_available_text;
                                    int black = getResources().getColor(R.color.black);
                                    mcv.addDecorator(new BookingDecorator(black, setDays));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.showPorgess(CalenderActivity.this);
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();                     //date=\(strDate)&&barber_id=0"
                    params.put("year", year);
                    params.put("month", month);
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }

    }

    private void make_day_Available_unAvailable(final String date, final String status, final String year, final String month) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(CalenderActivity.this);
            Log.d("api call post",Constants.make_day_on_off);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.make_day_on_off,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    /*if(status.equalsIgnoreCase("0"))
                                    {
                                        availableImg.setImageResource(R.drawable.unavailable);
                                    }
                                    else
                                    {
                                        availableImg.setImageResource(R.drawable.available);
                                    }*/
                                    Get_BookingList_MonthWise(year, month);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();                     //date=\(strDate)&&barber_id=0"
                    params.put("date", date);
                    params.put("is_on", status);
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");

        } else {
            Constants.showPopupInternet(activity);
        }

    }

    @Override
    public void onclickAction(int pos) {
        // OpenBottomView(pos);
        Slecetdpos = pos;
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    private class BookingDecorator implements DayViewDecorator {
        private int mColor;
        private HashSet<CalendarDay> mCalendarDayCollection;

        public BookingDecorator(int color, HashSet<CalendarDay> calendarDayCollection) {
            mColor = color;
            mCalendarDayCollection = calendarDayCollection;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {

          /*  CalendarDay date = CalendarDay.today();
            if(day.isBefore(date))
            {
                return false;
            }
            else
            {
                return mCalendarDayCollection.contains(day);
            }*/

            return mCalendarDayCollection.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(mColor));
            view.setBackgroundDrawable(ContextCompat.getDrawable(CalenderActivity.this, R.drawable.unavailbledate));
        }
    }


    public class SelectedDayDecorator implements DayViewDecorator {
        private final int color;
        private final Drawable dawableRed;
        private HashSet<CalendarDay> mCalendarDayCollection;

        public SelectedDayDecorator(HashSet<CalendarDay> calendarDayCollection, int color, Drawable dawableRed) {
            mCalendarDayCollection = calendarDayCollection;
            this.color = color;
            this.dawableRed = dawableRed;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mCalendarDayCollection.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(color));
            view.setBackgroundDrawable(dawableRed);
        }
    }


    public class DisableAllPastDate implements DayViewDecorator {

        private final int color;

        public DisableAllPastDate(int color) {
            this.color = color;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            CalendarDay date = CalendarDay.today();
            return (day.isBefore(date)) ? true : false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(color));
        }
    }


    public class EventDecoratorConfirmed implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecoratorConfirmed(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }


    public class EventDecoratorUnconfirmed implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecoratorUnconfirmed(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }


    private boolean isPastDay(Date date) {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        // test your condition, if Date specified is before today
        if (date.before(today)) {
            return true;
        }
        return false;
    }
}
