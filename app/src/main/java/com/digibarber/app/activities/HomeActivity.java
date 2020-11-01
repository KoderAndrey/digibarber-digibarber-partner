package com.digibarber.app.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.joooonho.SelectableRoundedImageView;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.digibarber.app.Beans.BookingList;
import com.digibarber.app.Beans.NotificationServerSide;
import com.digibarber.app.Beans.UpcomingBookingNotification;
import com.digibarber.app.CustomAdapters.HomeRecyclerAdapter;
import com.digibarber.app.CustomAdapters.UpcomingBookingNotificationRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BothSideCoordinatorLayout;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.BookingListCallbacks;
import com.digibarber.app.Interfaces.InnerNotificationClickCallback;
import com.digibarber.app.R;
import com.xenione.libs.swipemaker.SwipeLayout;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

import static com.digibarber.app.CustomClasses.Constants.downloadFileFromServer;

public class HomeActivity extends FragmentActivity implements BookingListCallbacks, InnerNotificationClickCallback {

    SimpleSideDrawer slide_me;
    SharedPreferences prefs;
    private ImageView menu_button, calender_icon, barber_profile_image;
    private TextView tv_time, tv_user_name;
    RecyclerView rv_booking_List;
    View bottomSheet;
    private CustomBottomSheetBehavior mBottomSheetBehavior;
    private TextView tv_notification_number;
    private RecyclerView rv_notification;

    public String android_id;
    ArrayList<BookingList> alBookingLists = new ArrayList<>();
    public ArrayList<UpcomingBookingNotification> bookingNotificationArrayList = new ArrayList<>();
    HomeRecyclerAdapter objHomeAdapter;
    UpcomingBookingNotificationRecyclerAdapter objNotificationAdapter;
    private Paint p = new Paint();

    ArrayList<String> ReschduleListTime = new ArrayList<>();
    ArrayList<String> removeableHours = new ArrayList<>();

    TextView tv_notification_text;
    int unconfirmedBooking = 0;
    boolean isFirstConBooking;
    private Animator mCurrentAnimator;
    LinearLayout ll_unconfirm;
    private ImageView expanded_image;
    private DateFormat formater;
    private DateFormat parseableDate;
    private int mShortAnimationDuration;
    ImageView iv_image_collapsed;
    //  ImageView iv_image_expanded;
    SelectableRoundedImageView logo_img;
    String openlink = "";
    TextView flashmsg;
    JellyRefreshLayout objJellyRefreshLayoutRecycler;
    JellyRefreshLayout objJellyRefreshLayoutScroll;
    RelativeLayout ll_back_panel, innerContainer;
    CoordinatorLayout main_content;
    RelativeLayout fl_back;
    RelativeLayout container_ofbanner;
    //    LinearLayout ll_touch;
    private NotificationServerSide objNotificationServerSide;
    Handler mHandler = new Handler();
    private FirebaseAnalytics mFirebaseAnalytics;

    LinearLayoutManager linearLayoutManager;
    int page = 0;
    boolean isLoading = false;

    String overallRating = "";
    int day;

    private final int REQUEST_PERMISSION_STORAGE_GROUP = 235;


    @Override
    protected void onCreate(Bundle bd) {
        super.onCreate(bd);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        bd = getIntent().getExtras();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        day = Constants.getPresentDay();

        if (bd != null) {
            objNotificationServerSide = (NotificationServerSide) bd.getSerializable("objNotificationServerSide");

            if (objNotificationServerSide != null) {
                String From = objNotificationServerSide.type;
                String date = objNotificationServerSide.booking_date;
                String name = objNotificationServerSide.barber_name;
                String service_name = objNotificationServerSide.service_name;
                String time = objNotificationServerSide.booking_time;

                String notify_id = objNotificationServerSide.notify_id;
                String message = objNotificationServerSide.message;
                if (From.equalsIgnoreCase("reschedule_customer_confirm") || From.equalsIgnoreCase("reschedule_confirm") || From.equalsIgnoreCase("reschedule_cancel") || From.equalsIgnoreCase("cancel_by_user")) {
                    showNotficationPopup(name, service_name, time, date, From, notify_id);
                }

                if (From.equalsIgnoreCase("book_barber") && message.equalsIgnoreCase("You have a new booking")) {
                    showNotficationPopup(name, service_name, time, date, From, notify_id);
                } else if (From.equalsIgnoreCase("auto_cancel")) {
                    //Take  to UpcomingBooking
                    showNotficationPopupForgot(name, service_name, time, date, From, notify_id, "You did not confirm this booking in time.This booking has been cancelled", "Where were you?");

                } else if (From.equalsIgnoreCase("auto_confirm_barber")) {
                    //Take  to UpcomingBooking
                    showNotficationPopupForgot(name, service_name, time, date, From, notify_id, "We know you are busy, but all bookings need to be closed by the barber", "Booking Closed!");

                    //  callClearNotfication(id);
                }
            }
        }


        main_content = (CoordinatorLayout) findViewById(R.id.main_content);
        fl_back = findViewById(R.id.fl_back);
        container_ofbanner = findViewById(R.id.container_ofbanner);
        container_ofbanner.setPadding(0, (int) (35), 0, 0);


//        ll_touch = (LinearLayout) findViewById(R.id.ll_touch);
        //callGetBooking("Dont Show");
        expanded_image = (ImageView) findViewById(R.id.expanded_image);
        View loadingView = LayoutInflater.from(this).inflate(R.layout.header_elastic_layout, null);
        View loadingView1 = LayoutInflater.from(this).inflate(R.layout.header_elastic_layout1, null);

        objJellyRefreshLayoutRecycler = (JellyRefreshLayout) findViewById(R.id.objJellyRefreshLayoutRecycler);
        objJellyRefreshLayoutScroll = (JellyRefreshLayout) findViewById(R.id.objJellyRefreshLayoutScroll);
        objJellyRefreshLayoutRecycler.setLoadingView(loadingView);
        objJellyRefreshLayoutScroll.setLoadingView(loadingView1);
        //   objJellyRefreshLayout.setHeaderView(headerView);
        objJellyRefreshLayoutRecycler.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                callGetBooking("Dont Show");
            }
        });
        objJellyRefreshLayoutScroll.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                callGetBooking("Dont Show");
            }
        });

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        formater = new SimpleDateFormat("EEEE, dd MMM yyyy");
        parseableDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        ll_unconfirm = findViewById(R.id.ll_unconfirm);
        tv_notification_text = findViewById(R.id.tv_notification_text);
        rv_booking_List = findViewById(R.id.rv_booking_List);
        barber_profile_image = findViewById(R.id.barber_profile_image);
        menu_button = findViewById(R.id.menu_button);
        calender_icon = findViewById(R.id.calender_icon);

        tv_time = findViewById(R.id.tv_time);
        tv_user_name = findViewById(R.id.tv_user_name);
        prefs = getSharedPreferences(Constants.SHARED_PREFRENCE_DB_NAME, MODE_PRIVATE);
        slide_me = new SimpleSideDrawer(this);

        bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = (CustomBottomSheetBehavior) BottomSheetBehavior.from(bottomSheet);
        iv_image_collapsed = bottomSheet.findViewById(R.id.iv_image_collapsed);
        //iv_image_expanded =  bottomSheet.findViewById(R.id.iv_image_expanded);
        logo_img = bottomSheet.findViewById(R.id.logo_img);
        flashmsg = bottomSheet.findViewById(R.id.flashmsg);
        logo_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!openlink.equalsIgnoreCase(""))
                    startActivity(new Intent(HomeActivity.this, TermConditionActiivty.class).putExtra("booking_id", openlink).putExtra("headername", "DigiBarber"));

            }
        });
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.tut_work1_book_now_w);
        mBottomSheetBehavior.setPeekHeight(spacingInPixels);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        tv_notification_number = bottomSheet.findViewById(R.id.tv_notification_number);

        rv_notification = bottomSheet.findViewById(R.id.rv_notification);

        linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        rv_notification.setLayoutManager(linearLayoutManager);

        objNotificationAdapter = new UpcomingBookingNotificationRecyclerAdapter(bookingNotificationArrayList, HomeActivity.this, HomeActivity.this);
//        rv_notification.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_notification.setHasFixedSize(true);
        rv_notification.setAdapter(objNotificationAdapter);

        rv_notification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1) && !isLoading) {
//                    isLoading = true;
//                    //page++;
//                    callUpcomingBookingNotificationApi("NotfiApi");
//                }

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    isLoading = true;
                    page++;
                    callUpcomingBookingNotificationApi("Show");
                }
            }
        });

        ll_back_panel = bottomSheet.findViewById(R.id.ll_back_panel);
        innerContainer = bottomSheet.findViewById(R.id.innerContainer);
        mBottomSheetBehavior.setHideable(false);

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bookingNotificationArrayList = new ArrayList<>();
                    //page = 0;
                    if (page == 0) {
                        // callUpcomingBookingNotificationApi("NotfiApi");
                    }
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

                Log.e("Here SlideOffset", slideOffset + "");
                float revOffset = 1 - slideOffset;
                revOffset = revOffset * 100;
                double imgPadding = revOffset / 2.85;
                double imgMargin = revOffset / 2.65;

               /* RelativeLayout.LayoutParams parameter  =  (RelativeLayout.LayoutParams) fl_back_container1.getLayoutParams();
                parameter.setMargins(parameter.leftMargin,(int)imgMargin, parameter.rightMargin, parameter.bottomMargin); // left, top, right, bottom
              //  fl_back_container1.setLayoutParams(parameter);*/

                container_ofbanner.setPadding(0, (int) (imgPadding), 0, 0);

                //       innerContainer.setPadding(0,(int)(imgPadding),0,0);
                Log.e("Heree Padding", (int) (imgPadding) + "");
                if (logo_img.getVisibility() == View.GONE) {
                    iv_image_collapsed.setVisibility(View.VISIBLE);
                    iv_image_collapsed.setImageResource(R.mipmap.top_panel);
                }

                if (slideOffset > 0.9) {
                    if (logo_img.getVisibility() == View.GONE) {
                        //   fl_back.setBackgroundResource(0);
                        //    fl_back.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    if (logo_img.getVisibility() == View.GONE) {
                        //   fl_back.setBackgroundResource(0);
                        //   fl_back.setBackgroundColor(Color.TRANSPARENT);
                    }
                }

                if (slideOffset > 0.3) {
                    ll_back_panel.setBackgroundColor(0);
                    ll_back_panel.setBackgroundColor(Color.TRANSPARENT);

                    tv_notification_number.setVisibility(View.GONE);

                } else {
                    mBottomSheetBehavior.setAllowDragging(true);
                    tv_notification_number.setVisibility(View.VISIBLE);
                    ll_back_panel.setBackgroundColor(0);
                    ll_back_panel.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ll_back_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ll_unconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UnconfermedBookingActivity.class).putExtra("alBookingLists", alBookingLists);
                startActivity(intent);
            }
        });

        slide_me.setLeftBehindContentView(R.layout.side_menu_layout);

        calender_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CalenderActivity.class).putExtra("alBookingLists", alBookingLists));
            }
        });
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                slide_me.toggleLeftDrawer();
            /*    if (slide_me.getVisibility()==View.VISIBLE) {
                    main_content.setFitsSystemWindows(false);
                    main_content.requestFitSystemWindows();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }else {
                    main_content.setFitsSystemWindows(true);
                    main_content.requestFitSystemWindows();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }*/


            }

        });

        barber_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(HomeActivity.this, BarberProfileImageActivity.class));
                String imagePath = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
                if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
                    zoomImageFromThumb(barber_profile_image);
                    if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
                        //   new PhotoFullPopupWindow(HomeActivity.this, R.layout.popup_photo_full, view, imagePath, null);

                    }

                }
            }
        });


        fl_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        sideMenuLayouts();
        setValues();
    }


    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationServerSide event) {
        if (event != null) {
            callGetBooking("Dont Show");
            Log.e("On Event Subscribe Yess", "==Type" + event.type + " == booking_date" + event.booking_date);
            String From = event.type;
            String date = event.booking_date;
            String name = event.barber_name;
            String service_name = event.service_name;
            String time = event.booking_time;
            String message = event.message;
            String Notificationid = event.notify_id;

            if (From.equalsIgnoreCase("reschedule_confirm") || From.equalsIgnoreCase("reschedule_cancel") || From.equalsIgnoreCase("cancel_by_user")) {
                showNotficationPopup(name, service_name, time, date, From, Notificationid);
            }
            if (From.equalsIgnoreCase("book_barber") && message.equalsIgnoreCase("You have a new booking")) {
                showNotficationPopup(name, service_name, time, date, From, Notificationid);
            } else if (From.equalsIgnoreCase("auto_cancel")) {
                //Take  to UpcomingBooking
                showNotficationPopupForgot(name, service_name, time, date, From, Notificationid, "You did not confirm this booking in time.This booking has been cancelled", "Where were you?");

            } else if (From.equalsIgnoreCase("auto_confirm_barber")) {
                //Take  to UpcomingBooking
                showNotficationPopupForgot(name, service_name, time, date, From, Notificationid, "We know you are busy, but all bookings need to be closed by the barber", "Booking Closed!");

                //  callClearNotfication(id);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetBooking("Show");
        profileDetail();

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        final RelativeLayout expandedimg_container = (RelativeLayout) findViewById(
                R.id.expandedimg_container);

        String imagePath = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
        if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
            Picasso.with(HomeActivity.this).load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).skipMemoryCache().error(R.mipmap.thick_search_default_pic).into(expandedImageView, new Callback() {
                @Override
                public void onSuccess() {
                    ViewTreeObserver vto = expandedImageView.getViewTreeObserver();
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        public boolean onPreDraw() {
                            expandedImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            int heightofimg = expandedImageView.getMeasuredHeight();
                            int widthofimg = expandedImageView.getMeasuredWidth();


                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int height = displayMetrics.heightPixels;
                            int width = displayMetrics.widthPixels;

                            float widthratio = heightofimg / widthofimg;
                            heightofimg = (int) (width * widthratio);

                            expandedImageView.getLayoutParams().width = width;
                            expandedImageView.getLayoutParams().height = heightofimg;
                            // expandedimg_container.setVisibility(View.GONE);
                            return true;
                        }
                    });


                }

                @Override
                public void onError() {
                    expandedimg_container.setVisibility(View.GONE);
                }
            });
        }
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.main_content)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedimg_container.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedimg_container.setPivotX(0f);
        expandedimg_container.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedimg_container, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedimg_container, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedimg_container, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedimg_container,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedimg_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedimg_container, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedimg_container,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedimg_container,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedimg_container,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedimg_container.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedimg_container.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    public void ShowPopupDialougReason(final int pos) {
        final Dialog dialog_first = new Dialog(HomeActivity.this);
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
        String bookingtime[] = alBookingLists.get(pos).booking_time.split("-");
        String str_StatTime = bookingtime[0].trim();
        String str_EndTime = bookingtime[1].trim();
        String strdateBooking = alBookingLists.get(pos).date + " " + str_EndTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");
        SimpleDateFormat simpleDateFormatdata = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");

        Date dateBooking = null;
        Date dateCurrent = null;
        try {
            dateBooking = simpleDateFormatdata.parse(strdateBooking);
            dateCurrent = simpleDateFormat.parse(CurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mintues = Constants.getDateDiffernce(dateCurrent, dateBooking);

        final long hours = mintues / 60;
        final String isConfimred = alBookingLists.get(pos).is_confirmed;
        final String booking_id = alBookingLists.get(pos).booking_id;
        final String barber_id = "0";

        final String date = alBookingLists.get(pos).date;
        final String addAbleTime = alBookingLists.get(pos).booking_time;
        // final ArrayList<String> unavailableHoursList = new ArrayList<>();
        //  SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, MMMM dd, yyyy kk:mm");

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
        Calendar startCalendar = Constants.getCalenderInstance();
        Calendar endCalendar = Constants.getCalenderInstance();


        tv_unable_offer_service_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookingCode = "";
                dialog_first.dismiss();
                String code = getCode("B1", isConfimred, hours);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_unable_offer_service_time.getText().toString(), code);

                //  callGetAvailableHours(booking_id, barber_id, date, tv_unable_offer_service_time.getText().toString(), code/*, unavailableHoursList*/);
            }
        });

        tv_cust_no_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();

                String code = getCode("B2", isConfimred, hours);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_cust_no_shop.getText().toString(), code);

                // callGetAvailableHours(booking_id, barber_id, date, tv_cust_no_shop.getText().toString(), code/*, unavailableHoursList*/);
            }
        });
        tv_barber_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
                String code = getCode("B3", isConfimred, hours);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_barber_busy.getText().toString(), code);

                //callGetAvailableHours(booking_id, barber_id, date, tv_barber_busy.getText().toString(), code/*, unavailableHoursList*/);
            }
        });

        tv_cust_made_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();
                String code = getCode("B4", isConfimred, hours);

                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_cust_made_error.getText().toString(), code);

                // callGetAvailableHours(booking_id, barber_id, date, tv_cust_made_error.getText().toString(), code/*, unavailableHoursList*/);
            }
        });
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callConfirmCancelUpcomingBooking("show", booking_id, "0", tv_other.getText().toString(), "Other");
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

    private void callGetUnavailableHoursReschedule(final String bookingid, final String date, final String strBookingTime, /*final ArrayList<String> unavailableHoursList,*/ final Date startDate, final Date dateEnd, final long mintues) {
        boolean con_result = ConnectivityReceiver.isConnected();

        //Sunday, November 05, 2017

        //Sunday, 05 Nov 2017
        //  formater = new SimpleDateFormat("EEEE, dd MMM yyyy");
        //  parseableDate = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Date dateSelected = null;
        String passDate = "";
        if (date != null && !date.equalsIgnoreCase("")) {
            try {
                dateSelected = formater.parse(date);
                passDate = parseableDate.format(dateSelected);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
        }
        if (con_result) {
            Constants.showPorgess(HomeActivity.this);
            final String finalPassDate = passDate;
            Log.d("api call post", ApiUrls.GetAvailablehours);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetAvailablehours,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            ReschduleListTime = new ArrayList<>();
                            Log.e("** GetAvailablehours **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                Calendar calender = Constants.getCalenderInstance();
                                Date sloteddate = null;
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
                                SimpleDateFormat simpleTimeFormate = new SimpleDateFormat("kk:mm");
                                if (success.equalsIgnoreCase("true")) {

                                    String unavailable_hrs = jo.getString("unavailable_hrs");
                                    if (unavailable_hrs != null && !unavailable_hrs.equalsIgnoreCase("")) {
                                        JSONArray jaUnavailable = new JSONArray(unavailable_hrs);
                                        // Log.e("unavailableHoursList",""+unavailableHoursList);
                                        //Log.e("unavailableHoursList",""+jaUnavailable);

                                        for (int i = 0; i < jaUnavailable.length(); i++) {
                                            String time = jaUnavailable.getString(i);
                                            // unavailableHoursList.add(jaUnavailable.getString(i));
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

                                        showReScheduleNewTime(date, mintues, ReschduleListTime,/* unavailable_hrsfinal,*/ bookingid);
                                    } else {
                                    }
                                } else {
                                    String message = jo.getString("message");
                                    Constants.showToast(HomeActivity.this, message);
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
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("date", finalPassDate);
                    params.put("barber_id", "0");

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
        }
    }


    private void callConfirmCancelUpcomingBooking(final String status, final String booking_id, final String isConfrim, final String reason, final String reason_code) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            if (!status.equalsIgnoreCase("dontshow")) {
                Constants.showPorgess(HomeActivity.this);
            }
            Log.d("api call post", Constants.CancelUpcomingBooking);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.CancelUpcomingBooking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            //   Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    callGetBooking("Dont Show");
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
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
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
            Constants.showPopupInternet(HomeActivity.this);
        }
    }


    void disableRefrshing() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (objJellyRefreshLayoutRecycler.isRefreshing()) {
                    objJellyRefreshLayoutRecycler.setRefreshing(false);
                }
                if (objJellyRefreshLayoutScroll.isRefreshing()) {
                    objJellyRefreshLayoutScroll.setRefreshing(false);
                }
            }
        }, 500);
    }

    private void callGetBooking(final String status) {
        unconfirmedBooking = 0;
        isFirstConBooking = false;
        Log.e("token_value", prefs.getString("token_value", ""));
        Log.e("user_id", prefs.getString("user_id", ""));
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            if (!status.equalsIgnoreCase("Dont Show")) {
                Constants.showPorgess(HomeActivity.this);
            }
            Log.d("api call post", ApiUrls.GetBookingList);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.GetBookingList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            disableRefrshing();

                            alBookingLists = new ArrayList<>();
                            Log.e("callGetBooking", response);
                            Constants.dismissProgress();
                            if (!status.equalsIgnoreCase("Dont Show")) {
                                callUpcomingBookingNotificationApi("Api");
                            }
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
                                                if (joRequests.has("customer_postcode")) {
                                                    String customerPostCode = joRequests.getString("customer_postcode");
                                                    objBookingList.customerPostCode = customerPostCode;
                                                }

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


                                                if (is_confirmed.equalsIgnoreCase("0")) {
                                                    unconfirmedBooking++;
                                                }
                                                if (!isFirstConBooking) {
                                                    if (is_confirmed.equalsIgnoreCase("1") || is_confirmed.equalsIgnoreCase("4")) {
                                                        isFirstConBooking = true;
                                                        objBookingList.isfirstConfBooking = "1";
                                                    } else {
                                                        objBookingList.isfirstConfBooking = "0";
                                                    }
                                                } else {
                                                    objBookingList.isfirstConfBooking = "0";
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
                                        callSetNotifiationCount();
                                        objJellyRefreshLayoutRecycler.setVisibility(View.VISIBLE);
                                        objJellyRefreshLayoutScroll.setVisibility(View.GONE);


                                        objHomeAdapter = new HomeRecyclerAdapter(alBookingLists, HomeActivity.this, HomeActivity.this);
                                        rv_booking_List.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                        rv_booking_List.setAdapter(objHomeAdapter);
                                        objHomeAdapter.notifyDataSetChanged();
                                    } else {

                                        callSetNotifiationCount();
                                        objJellyRefreshLayoutRecycler.setVisibility(View.GONE);
                                        objJellyRefreshLayoutScroll.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    callSetNotifiationCount();
                                    objHomeAdapter = new HomeRecyclerAdapter(alBookingLists, HomeActivity.this, HomeActivity.this);
                                    rv_booking_List.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                                    rv_booking_List.setAdapter(objHomeAdapter);
                                    objHomeAdapter.notifyDataSetChanged();
                                    objJellyRefreshLayoutRecycler.setVisibility(View.GONE);
                                    objJellyRefreshLayoutScroll.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                                objJellyRefreshLayoutRecycler.setVisibility(View.GONE);
                                objJellyRefreshLayoutScroll.setVisibility(View.VISIBLE);
                                disableRefrshing();
                            } catch (NullPointerException e) {
                                disableRefrshing();
                            }
                            //    objHomeAdapter.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    disableRefrshing();
                    //mPtrFrame.refreshComplete();
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    objJellyRefreshLayoutRecycler.setVisibility(View.GONE);
                    objJellyRefreshLayoutScroll.setVisibility(View.VISIBLE);
                    Constants.showPopupServer(HomeActivity.this);

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
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
            Constants.showPopupInternet(HomeActivity.this);
        }

    }

    private void callSetNotifiationCount() {

        if (unconfirmedBooking == 1) {
            tv_notification_text.setText(unconfirmedBooking + " unconfirmed booking");
        } else {
            tv_notification_text.setText(unconfirmedBooking + " unconfirmed bookings");
        }
    }


    public void showNotficationPopup(String name, String service_name, String time, String date, String type, String notificationid) {

        final Dialog dialog_first = new Dialog(HomeActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_notification_clicked);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_date = (TextView) dialog_first.findViewById(R.id.tv_date);
        TextView tv_service_name = (TextView) dialog_first.findViewById(R.id.tv_service_name);
        TextView tv_time = (TextView) dialog_first.findViewById(R.id.tv_time);
        TextView tv_barber_name = (TextView) dialog_first.findViewById(R.id.tv_barber_name);
        LinearLayout ll_message_first = (LinearLayout) dialog_first.findViewById(R.id.ll_message_first);

        SimpleDateFormat parseableDate = new SimpleDateFormat("dd MMM yyyy");
        Date dateSelected = null;
        if (date != null && !date.equalsIgnoreCase("")) {
            try {
                dateSelected = parseableDate.parse(date);
                String datel = formater.format(dateSelected);
                tv_date.setText(datel);
            } catch (ParseException e) {
                e.printStackTrace();
                tv_date.setText(date);
            }
        } else {

            tv_date.setText(date);
        }

        tv_service_name.setText(service_name);
        tv_time.setText(time);
        tv_barber_name.setText(name);


        ImageView iv_header = (ImageView) dialog_first.findViewById(R.id.iv_header);
        TextView tv_mesages = (TextView) dialog_first.findViewById(R.id.tv_mesages);
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);


        if (type.equalsIgnoreCase("confirm_reschedule") || type.equalsIgnoreCase("reschedule_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            iv_header.setImageResource(R.mipmap.permissions_goodnews);
            tv_mesages.setText(name + "  has accepted your rescheduled request.");
        } else if (type.equalsIgnoreCase("reschedule_customer_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            iv_header.setImageResource(R.mipmap.permissions_goodnews);
            tv_mesages.setText(name + "  has accepted your rescheduled request.");
        } else if (type.equalsIgnoreCase("cancel_reschedule") || type.equalsIgnoreCase("reschedule_cancel")) {

            //canMake it
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.permissionscanmakeit);
            tv_mesages.setText(name + " could not make that time! He has decided to cancel this booking");
        } else if (type.equalsIgnoreCase("auto_cancel")) {
            //where are you
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.where_are_you);
            tv_mesages.setText(name + " You did not confirm this booking in time. This booking has been cancelled");
        } else if (type.equalsIgnoreCase("customer_cancel") || type.equalsIgnoreCase("cancel_by_user")) {
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.permissionsdarn_it);
            tv_mesages.setText(name + " has had to cancel this upcoming booking. We are working to get you another!");

        } else if (type.equalsIgnoreCase("book_barber")) {
            ll_message_first.setBackgroundResource(R.mipmap.new_booking);
            iv_header.setImageResource(R.mipmap.panel_green);
            tv_mesages.setText("You have a confirmed booking.See details above");

        } else {

        }

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();

            }
        });
        dialog_first.show();
        callClearNotfication(notificationid);
    }

    public void showNotficationPopupMsg(String name, String service_name, String time, String date, String type, String notificationid, String msg) {

        final Dialog dialog_first = new Dialog(HomeActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_notification_clicked);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_date = (TextView) dialog_first.findViewById(R.id.tv_date);
        TextView tv_service_name = (TextView) dialog_first.findViewById(R.id.tv_service_name);
        TextView tv_time = (TextView) dialog_first.findViewById(R.id.tv_time);
        TextView tv_barber_name = (TextView) dialog_first.findViewById(R.id.tv_barber_name);
        LinearLayout ll_message_first = (LinearLayout) dialog_first.findViewById(R.id.ll_message_first);

        SimpleDateFormat parseableDate = new SimpleDateFormat("dd MMM yyyy");
        Date dateSelected = null;
        if (date != null && !date.equalsIgnoreCase("")) {
            try {
                dateSelected = parseableDate.parse(date);
                String datel = formater.format(dateSelected);
                tv_date.setText(datel);
            } catch (ParseException e) {
                e.printStackTrace();
                tv_date.setText(date);
            }
        } else {

            tv_date.setText(date);
        }

        tv_service_name.setText(service_name);
        tv_time.setText(time);
        tv_barber_name.setText(name);


        ImageView iv_header = (ImageView) dialog_first.findViewById(R.id.iv_header);
        TextView tv_mesages = (TextView) dialog_first.findViewById(R.id.tv_mesages);
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);


        if (type.equalsIgnoreCase("confirm_reschedule") || type.equalsIgnoreCase("reschedule_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            iv_header.setImageResource(R.mipmap.permissions_goodnews);
            tv_mesages.setText(name + " is happy to attend at the rescheduled time. See updated booking above!");
        } else if (type.equalsIgnoreCase("cancel_reschedule") || type.equalsIgnoreCase("reschedule_cancel")) {

            //canMake it
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.permissionscanmakeit);
            tv_mesages.setText(name + " could not make that time! He has decided to cancel this booking");
        } else if (type.contains("reschedule")) {

            //canMake it
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.permissionscanmakeit);
            tv_mesages.setText(msg);
        } else if (type.equalsIgnoreCase("auto_cancel")) {
            //where are you
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.where_are_you);
            tv_mesages.setText(name + " You did not confirm this booking in time. This booking has been cancelled");
        } else if (type.contains("cancel")) {
            //where are you
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.where_are_you);
            tv_mesages.setText(msg);
        } else if (type.equalsIgnoreCase("customer_cancel") || type.equalsIgnoreCase("cancel_by_user")) {
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            iv_header.setImageResource(R.mipmap.permissionsdarn_it);
            tv_mesages.setText(name + " has had to cancel this upcoming booking. We are working to get you another!");

        } else if (type.equalsIgnoreCase("book_barber")) {
            ll_message_first.setBackgroundResource(R.mipmap.new_booking);
            iv_header.setImageResource(R.mipmap.panel_green);
            tv_mesages.setText("You have a confirmed booking.See details above");

        } else {
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            iv_header.setImageResource(R.mipmap.permissions_goodnews);
            tv_mesages.setText(msg);
        }

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();

            }
        });
        dialog_first.show();
        callClearNotfication(notificationid);
    }

    public void showNotficationPopupForgot(String name, String service_name, String time, String date, String type, String notificationid, String msg, String headerstr) {

        final Dialog dialog_first = new Dialog(HomeActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_notification_clicked_);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_date = (TextView) dialog_first.findViewById(R.id.tv_date);
        TextView iv_header = (TextView) dialog_first.findViewById(R.id.iv_header);
        TextView tv_mesages = (TextView) dialog_first.findViewById(R.id.tv_mesages);
        TextView tv_service_name = (TextView) dialog_first.findViewById(R.id.tv_service_name);
        TextView tv_time = (TextView) dialog_first.findViewById(R.id.tv_time);
        TextView tv_barber_name = (TextView) dialog_first.findViewById(R.id.tv_barber_name);
        LinearLayout ll_message_first = (LinearLayout) dialog_first.findViewById(R.id.ll_message_first);

        SimpleDateFormat parseableDate = new SimpleDateFormat("dd MMM yyyy");
        Date dateSelected = null;
        if (date != null && !date.equalsIgnoreCase("")) {
            try {
                dateSelected = parseableDate.parse(date);
                String datel = formater.format(dateSelected);
                tv_date.setText(datel);
            } catch (ParseException e) {
                e.printStackTrace();
                tv_date.setText(date);
            }
        } else {
            tv_date.setText(date);
        }

        tv_service_name.setText(service_name);

        tv_time.setText(time);
        tv_barber_name.setText(name);

        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);

        if (type.equalsIgnoreCase("confirm_reschedule") || type.equalsIgnoreCase("reschedule_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            tv_mesages.setText(name + " is happy to attend at the rescheduled time. See updated booking above!");
        } else if (type.equalsIgnoreCase("cancel_reschedule") || type.equalsIgnoreCase("reschedule_cancel")) {

            //canMake it
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            tv_mesages.setText(name + " could not make that time! He has decided to cancel this booking");
        } else if (type.equalsIgnoreCase("auto_cancel")) {
            //where are you
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            tv_mesages.setText(name + " You did not confirm this booking in time. This booking has been cancelled");
        } else if (type.equalsIgnoreCase("customer_cancel") || type.equalsIgnoreCase("cancel_by_user")) {
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_can_makeit);
            tv_mesages.setText(name + " has had to cancel this upcoming booking. We are working to get you another!");

        } else if (type.equalsIgnoreCase("book_barber")) {
            ll_message_first.setBackgroundResource(R.mipmap.new_booking);
            tv_mesages.setText("You have a confirmed booking.See details above");

        } else if (type.contains("confirm") || type.equalsIgnoreCase("reschedule_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            tv_mesages.setText(name + " is happy to attend at the rescheduled time. See updated booking above!");
        } else if (type.contains("reschedule") || type.equalsIgnoreCase("reschedule_confirm")) {
            //good news
            ll_message_first.setBackgroundResource(R.mipmap.rectangle_goodnews);
            tv_mesages.setText(msg);
        } else {

        }
        tv_mesages.setText(msg);
        iv_header.setText(headerstr);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();

            }
        });
        dialog_first.show();
        callClearNotfication(notificationid);
    }


    private void callUpcomingBookingNotificationApi(final String From) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            //if (!From.equalsIgnoreCase("Api")) {
            Constants.showPorgess(HomeActivity.this);
            // }
            Log.d("notif api", "notif api " + ApiUrls.UpcomigBookingNotification);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.UpcomigBookingNotification,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
//                            bookingNotificationArrayList = new ArrayList<>();

                            page++;
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    Log.e("pagex", "pagex success" + page);
                                } else {
                                    Log.e("pagex", "pagex fail" + page);
                                }

                                if (success.equalsIgnoreCase("true")) {
                                    // bookingNotificationArrayList.clear();
                                    String notification_count = jo.getString("notification_unread_count");
                                    tv_notification_number.setText(notification_count);
                                    JSONArray jadata = jo.getJSONArray("data");
                                    for (int j = 0; j < jadata.length(); j++) {
                                        JSONObject joDate = jadata.getJSONObject(j);
                                        UpcomingBookingNotification objUpcomingBookingNotification = new UpcomingBookingNotification();
                                        objUpcomingBookingNotification.msg = joDate.getString("msg");
                                        objUpcomingBookingNotification.date = joDate.getString("date");

                                        String strNotifydate = joDate.getString("date");
                                        objUpcomingBookingNotification.dates = joDate.getString("time");

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

                                        try {
                                            Date notifydate = simpleDateFormat.parse(strNotifydate);
                                            Calendar cal = Calendar.getInstance();
                                            String currdatestr = simpleDateFormat.format(cal.getTime());

                                            Date currDate = simpleDateFormat.parse(currdatestr);
                                            if (notifydate.before(currDate)) {
                                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE");
                                                String dayOfTheWeek = simpleDateFormat1.format(notifydate);
                                                objUpcomingBookingNotification.dates = dayOfTheWeek;
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        objUpcomingBookingNotification.date = joDate.getString("date");


                                        objUpcomingBookingNotification.image = joDate.getString("image");
                                        objUpcomingBookingNotification.reason = joDate.getString("reason");
                                        objUpcomingBookingNotification.booking_date = joDate.getString("booking_date");
                                        objUpcomingBookingNotification.service_name = joDate.getString("service_name");
                                        objUpcomingBookingNotification.booking_time = joDate.getString("booking_time");
                                        objUpcomingBookingNotification.type = joDate.getString("type");
                                        objUpcomingBookingNotification.is_read = joDate.getString("is_read");
                                        objUpcomingBookingNotification.booking_id = joDate.getString("booking_id");
                                        objUpcomingBookingNotification.new_time = joDate.getString("new_time");
                                        try {
                                            objUpcomingBookingNotification.barber_id = joDate.getString("barber_id");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e("exception", "Exception:" + e);
                                        }
                                        objUpcomingBookingNotification.price = joDate.getString("price");
                                        try {
                                            objUpcomingBookingNotification.percentage = joDate.getString("percentage");
                                        } catch (Exception e) {
                                            Log.e("exception", "Exception:" + e);
                                        }
                                        objUpcomingBookingNotification.name = joDate.getString("name");
                                        objUpcomingBookingNotification.id = joDate.getString("id");
                                        objUpcomingBookingNotification.time = joDate.getString("time");
                                        objUpcomingBookingNotification.workplace = joDate.getString("workplace");
                                        objUpcomingBookingNotification.address = joDate.getString("address");
                                        objUpcomingBookingNotification.lon = joDate.getString("lon");
                                        objUpcomingBookingNotification.lat = joDate.getString("lat");
                                        //            Log.e("Deeep","Time:"+objUpcomingBookingNotification.time);
                                        bookingNotificationArrayList.add(objUpcomingBookingNotification);
                                    }
                                    JSONArray flashArry = jo.getJSONArray("flash_data");
                                    if (flashArry.length() > 0) {
                                        JSONObject jsonObjectFlash = flashArry.getJSONObject(0);
                                        Log.e("Deep", "Shadow:" + jsonObjectFlash.getString("flash_image1"));

                                        if (!HomeActivity.this.isFinishing())
                                            if (jsonObjectFlash.has("flash_image1")) {
                                                String flashURL = jsonObjectFlash.getString("flash_image1");
                                                Glide.with(HomeActivity.this)

                                                        .asBitmap()
                                                        .load(flashURL)
                                                        .into(logo_img);
                                            }

                                        logo_img.setBackground(null);
                                        logo_img.setVisibility(View.VISIBLE);
                                        flashmsg.setVisibility(View.GONE);
                                        if (jsonObjectFlash.has("message"))
                                            flashmsg.setText(jsonObjectFlash.getString("message"));

                                        if (jsonObjectFlash.has("url_link"))
                                            openlink = jsonObjectFlash.getString("url_link");
                                        logo_img.setBackgroundColor(getResources().getColor(R.color.transaprent));
                                    } else {
                                        logo_img.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    }

                                    objNotificationAdapter.setItems(bookingNotificationArrayList);

                                } else {
                                    if (bookingNotificationArrayList.size() == 0) {
                                        String message = jo.getString("message");
                                        Constants.dismissProgress();
                                        //Constants.showToast(HomeActivity.this, message);
                                    }
                                }

                                isLoading = false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("error", e.getMessage());
                                Constants.dismissProgress();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("page", String.valueOf(page));
                    params.put("page_size", String.valueOf(10));
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
        }
    }

    public long getDays(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        return elapsedDays;
    }

    private void setValues() {

        tv_user_name.setText("Hello " + prefs.getString(Constants.KEY_FIRST_NAME, ""));
        String imagePath = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
        if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
            Picasso.with(HomeActivity.this).load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).skipMemoryCache().placeholder(R.mipmap.thick_search_default_pic).error(R.mipmap.thick_search_default_pic).into(barber_profile_image);


            final RelativeLayout expandedimg_container = (RelativeLayout) findViewById(R.id.expandedimg_container);

            expanded_image.setVisibility(View.VISIBLE);
            expandedimg_container.setVisibility(View.INVISIBLE);
            Picasso.with(HomeActivity.this)
                    .load(imagePath)
                    .error(R.mipmap.home_default_profile_pic)
                    .noFade()
                    .into(expanded_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            ViewTreeObserver vto = expanded_image.getViewTreeObserver();
                            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                public boolean onPreDraw() {
                                    expanded_image.getViewTreeObserver().removeOnPreDrawListener(this);
                                    int heightofimg = expanded_image.getMeasuredHeight();
                                    int widthofimg = expanded_image.getMeasuredWidth();
                                    expandedimg_container.setVisibility(View.GONE);


                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                    try {


                                        int height = displayMetrics.heightPixels;
                                        int width = displayMetrics.widthPixels;

                                        float widthratio = heightofimg / widthofimg;
                                        float heightratio = height / heightofimg;
                                        heightofimg = (int) (width * widthratio);

                                        expanded_image.getLayoutParams().width = width;
                                        expanded_image.getLayoutParams().height = heightofimg;
                                        expandedimg_container.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        Log.e("Test", "Exception:" + e);
                                    }
                                    return true;
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            expandedimg_container.setVisibility(View.GONE);
                        }


                    });


        }

        tv_time.setText(Constants.getPresentDayName() + ", " + Constants.getCurrentMonthNameMMM() + " " + Constants.getCurrentDate());
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void sideMenuLayouts() {
        ImageView tv_edit_profile_text = (ImageView) slide_me.findViewById(R.id.tv_edit_profile_text);
        final ImageView tv_home = (ImageView) slide_me.findViewById(R.id.tv_home);
        final ImageView tv_calender = (ImageView) slide_me.findViewById(R.id.tv_calender);
        final ImageView iv_review = (ImageView) slide_me.findViewById(R.id.iv_review);
        final ImageView tv_wallet = (ImageView) slide_me.findViewById(R.id.tv_wallet);
        final ImageView tv_settings = (ImageView) slide_me.findViewById(R.id.tv_settings);
        final ImageView tv_promote = (ImageView) slide_me.findViewById(R.id.tv_promote);
        final ImageView tv_sign_out = (ImageView) slide_me.findViewById(R.id.tv_sign_out);
        final TextView tv_user_name_menu = (TextView) slide_me.findViewById(R.id.tv_user_name_menu);
        String fullname = prefs.getString(Constants.KEY_FULL_NAME, "");
        Log.e("Full name", fullname);
        tv_user_name_menu.setText(fullname);
        ImageView babrber_slider_profile_image = (ImageView) findViewById(R.id.babrber_slider_profile_image);

        String imagePath = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
        if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
            Picasso.with(HomeActivity.this).load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).skipMemoryCache().placeholder(R.mipmap.thick_search_default_pic).error(R.mipmap.thick_search_default_pic).into(babrber_slider_profile_image);
        }

        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
            }
        });
        tv_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, CalenderActivity.class));
            }
        });

        iv_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, ReviewActivity.class));
            }
        });
        tv_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, WalletActivity.class));

            }
        });


 /*       if (prefs.contains("full_name")) {
            name.setText(prefs.getString("full_name", ""));
        }*/

        tv_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        tv_edit_profile_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                finish();
            }
        });

        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        tv_promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide_me.close();
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_GROUP);
                } else {
                    getReviewListing("Show");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE_GROUP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getReviewListing("Dont Show");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Sorry! We can't continue without storage permission");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
            }
        }
    }

    /* Getting reviews listing for a particular Barber */
    public void getReviewListing(String status) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {

            if (!status.equalsIgnoreCase("Dont Show")) {
                Constants.showPorgess(this);
            }

            Log.d("api call post", Constants.BarberReviewsListing);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.BarberReviewsListing,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            pDialog.dismiss();
//                            Constants.dismissProgress();

                            Log.e("** RESPONSE **", response);

                            try {
                                JSONObject response_object = new JSONObject(response);

                                String success = response_object.getString("success");

                                if (success.equalsIgnoreCase("true")) {
                                    JSONArray data_array = response_object.getJSONArray("data");
                                    String overall_rating = response_object.getString("overall_rating");
                                    if (overall_rating != null && !overall_rating.equalsIgnoreCase("")) {
                                        DecimalFormat formate = new DecimalFormat("0.0");
                                        double ratings = Double.parseDouble(overall_rating);
                                        String date = formate.format(ratings);
                                        overallRating = date;
                                    }
                                } else {
                                    System.out.println("** No reviews found ** ");
                                }

                                shareClick();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //checkConnection();
                    // ZoomConst.showCustomAlert(MainActivity.this, getResources().getString(R.string.app_name), error.getMessage(), getResources().getString(R.string.ok));
                    VolleyLog.d("** ERROR **", "Error: " + error.getMessage());
//                    pDialog.dismiss();
                    Constants.dismissProgress();
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("barber_id", "0");
                    System.out.println(" ** PARAMS ** " + params);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
            //System.out.println(" ** PLease connect to Internet **");
        }
    }


    public void shareClick() {
        try {
//            Constants.showPorgess(HomeActivity.this);

            String barber_id = prefs.getString(Constants.KEY_USER_ID, "");
            final String Full_Name = prefs.getString(Constants.KEY_FULL_NAME, "");
            String Emai = prefs.getString(Constants.KEY_EMAIL, "");
            String Phone = prefs.getString(Constants.KEY_PHONE, "");
            String Bankdetails = prefs.getString(Constants.KEY_BANK_DETAIL, "");
            final String profile_image = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
            final String Work_place = prefs.getString(Constants.KEY_WORKPLACE, "");
            String strAddress = prefs.getString(Constants.KEY_ADDRESS, "");
            String Gallery_image = prefs.getString(Constants.KEY_GALLERY_IMAGES, "");
            String Open_Hours = prefs.getString(Constants.KEY_OPEN_HOURS, "");
            String post_code = prefs.getString(Constants.KEY_POSTCODE, "");
            String services = prefs.getString(Constants.KEY_SERVICES, "");

            JSONObject profile_object = new JSONObject();
            if (Gallery_image != null && !Gallery_image.equals("")) {
                JSONArray gallery_array = new JSONArray(Gallery_image);//gallery
                Log.d("Gallery", gallery_array.toString());
                profile_object.put("barber_gallery", gallery_array);
            }
            if (services != null && !services.equals("")) {
                JSONArray service_array = new JSONArray(services);//gallery
                Log.d("services", service_array.toString());
                profile_object.put("services_array", service_array);
            }

            String start_time = "", end_time = "";
            if (!Open_Hours.trim().equalsIgnoreCase("")) {
                Log.e("open_hours", "" + Open_Hours.trim());
                try {
                    JSONArray jsonArray = new JSONArray(Open_Hours.trim());

                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject days_object = jsonArray.getJSONObject(k);
                        //  Log.e("JsonArray",days_object.toString());
                        switch (day) {
                            case Calendar.SUNDAY:
                                // Current day is Sunday
                                if (days_object.has("Sun")) {
                                    JSONArray sub_json_array7 = days_object.getJSONArray("Sun");
                                    for (int j = 0; j < sub_json_array7.length(); j++) {
                                        JSONObject sub_object = sub_json_array7.getJSONObject(j);
                                        if (sub_json_array7.length() >= 2) {

                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }

                                        } else if (sub_json_array7.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                // Log.e("Sat Array", start_time + end_time);
                                break;

                            case Calendar.MONDAY:
                                // Current day is Monday
                                if (days_object.has("Mon")) {
                                    JSONArray sub_json_array1 = days_object.getJSONArray("Mon");
                                    for (int j = 0; j < sub_json_array1.length(); j++) {
                                        JSONObject sub_object = sub_json_array1.getJSONObject(j);
                                        if (sub_json_array1.length() >= 2) {
                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }
                                        } else if (sub_json_array1.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                break;

                            case Calendar.TUESDAY:

                                if (days_object.has("Tue")) {
                                    JSONArray sub_json_array2 = days_object.getJSONArray("Tue");
                                    for (int j = 0; j < sub_json_array2.length(); j++) {
                                        JSONObject sub_object = sub_json_array2.getJSONObject(j);

                                        if (sub_json_array2.length() >= 2) {

                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }

                                        } else if (sub_json_array2.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }

                                    }
                                }
                                break;
                            case Calendar.WEDNESDAY:

                                if (days_object.has("Wed")) {
                                    JSONArray sub_json_array3 = days_object.getJSONArray("Wed");

                                    for (int j = 0; j < sub_json_array3.length(); j++) {
                                        JSONObject sub_object = sub_json_array3.getJSONObject(j);
                                        if (sub_json_array3.length() >= 2) {

                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }

                                        } else if (sub_json_array3.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                break;
                            case Calendar.THURSDAY:

                                if (days_object.has("Thu")) {
                                    JSONArray sub_json_array4 = days_object.getJSONArray("Thu");
                                    for (int j = 0; j < sub_json_array4.length(); j++) {
                                        JSONObject sub_object = sub_json_array4.getJSONObject(j);
                                        if (sub_json_array4.length() >= 2) {

                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }

                                        } else if (sub_json_array4.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                break;
                            case Calendar.FRIDAY:

                                if (days_object.has("Fri")) {
                                    JSONArray sub_json_array5 = days_object.getJSONArray("Fri");
                                    for (int j = 0; j < sub_json_array5.length(); j++) {
                                        JSONObject sub_object = sub_json_array5.getJSONObject(j);
                                        if (sub_json_array5.length() >= 2) {

                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }

                                        } else if (sub_json_array5.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                break;
                            case Calendar.SATURDAY:

                                if (days_object.has("Sat")) {
                                    JSONArray sub_json_array6 = days_object.getJSONArray("Sat");
                                    for (int j = 0; j < sub_json_array6.length(); j++) {
                                        JSONObject sub_object = sub_json_array6.getJSONObject(j);
                                        if (sub_json_array6.length() >= 2) {
                                            if (j == 0) {
                                                start_time = sub_object.getString("start_time");
                                            } else {
                                                end_time = sub_object.getString("end_time");
                                            }
                                        } else if (sub_json_array6.length() > 0) {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }
                                //  Log.e("Sat Array",start_time+end_time);
                                break;
                        }

                    }
                    //Log.e("Json Array",jsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JsonException", "" + e.getMessage());
                }
            }

            String hours_value = "";
            if (!start_time.equalsIgnoreCase("") && !end_time.equalsIgnoreCase("")) {
                hours_value = start_time + " - " + end_time;
            } else {
                hours_value = "Closed";
            }

            profile_object.put("barber_name", Full_Name);
            profile_object.put("barber_address", strAddress);
            profile_object.put("postcode", post_code);
            profile_object.put("barber_workplace", Work_place);
            profile_object.put("barber_open_hours", hours_value);
            profile_object.put("barber_profile_image", profile_image);
            profile_object.put("barber_open_hours_full", Open_Hours);
            profile_object.put("barber_id", barber_id);
            profile_object.put("average_ratings", overallRating);
            profile_object.put("is_follow", "");
            profile_object.put("con_hours", "");
            profile_object.put("isBarberService","1");

            String barber_profile_details = profile_object.toString();
            String deepLink         = encodeData(barber_profile_details);
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://app.digibarber.com/data="+deepLink))
                    .setDomainUriPrefix("https://digibarber.page.link")
                    .setIosParameters(new DynamicLink.IosParameters.Builder("com.webastral.DIGICLIENT").build())
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.digipartner.app").build())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {

                                Uri shortLink = task.getResult().getShortLink();
                                shareLink(null,shortLink.toString(),Full_Name,Work_place);
                            }
                        }
                    });

        }
        catch (Exception e) {
            Constants.dismissProgress();
            Log.e("Error", e.getMessage());
        }

    }
    public static String encodeData(String stringCondificado){
        try {
            return new String(android.util.Base64.encode(stringCondificado.getBytes("UTF-8"),android.util.Base64.DEFAULT)).replace("\n","");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
    private void shareLink(File file, String url, String Full_Name, String Work_place) {
        String text = Full_Name + " - " + Work_place
                + "\n" + "You can now book your haircut with me on the DIGIBARBER app!"
                + "\n" + "DigiBarber"
                + "\n\n" + url;

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, "DigiBarber");
            i.putExtra(Intent.EXTRA_TEXT, text);
            i.setType("text/plain");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i, "Share"));
        } catch (Exception e) {
            Log.e("Share Error", e.getMessage());
        }
    }


    void ClearLogoutPref() {
        slide_me.close();
        prefs.edit().clear().commit();


        startActivity(new Intent(HomeActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void profileDetail() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.d("api call post", Constants.Profile + "&uniqueId=" + android_id.trim());
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Profile + "&uniqueId=" + android_id.trim(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {

                                    if (jo.getString("is_freezed").equalsIgnoreCase("1")) {
                                        logout();
                                    } else if (jo.getString("blocked").equalsIgnoreCase("1")) {
                                        logout();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
                            } catch (NullPointerException e) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
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
        }
    }

    private void logout() {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(HomeActivity.this);
            Log.e("*updateDeviceToken*", "Hitting");
            Log.d("api call post", Constants.Logout);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.Logout,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            ClearLogoutPref();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    ClearLogoutPref();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    SharedPreferences prefs = getSharedPreferences("Barber", MODE_PRIVATE);
                    String keyid = prefs.getString(Constants.KEY_DEVICE_ID, "");
                    Log.e("Testtt", "keyid:" + keyid);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("device_id", "" + keyid);
                    Log.e("here params", "" + params);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {

        }
    }


    private void callStartCloseBooking(final String booking_id, final String status, final String arrive_time) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(HomeActivity.this);
            Log.d("api call post", Constants.StartCloseBookingApi);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.StartCloseBookingApi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** CB  RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    callGetBooking("Dont Show");
                                } else {
                                    Constants.dismissProgress();
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
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("arrive_time", arrive_time);
                    params.put("booking_id", booking_id);
                    params.put("status", status);
                    Log.e("** CB  params **", params.toString());
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
        }
    }

    public void ShowPopupDialougCpmfrimed(String heading, String message) {
        final Dialog dialog_first = new Dialog(HomeActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_already_confirmed);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
        TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
        TextView tv_heading = (TextView) dialog_first.findViewById(R.id.tv_heading);
        tv_message.setText(message);
        tv_heading.setText(heading);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });

        dialog_first.show();
    }


    public void showPopupDialogChecking(final int pos) {
        final Dialog dialog_first = new Dialog(HomeActivity.this);
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


    public void showReScheduleNewTime(final String date, final long servicetime, final ArrayList<String> ReschduleListTime,/*final ArrayList<String> unavailable_hrsfinal,*/ final String bookingid) {
        final Dialog dialog_first = new Dialog(HomeActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_reschedule);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        final TextView tv_time_first = (TextView) dialog_first.findViewById(R.id.tv_time_first);
        final TextView tv_time_second = (TextView) dialog_first.findViewById(R.id.tv_time_second);
        final TextView tv_time_third = (TextView) dialog_first.findViewById(R.id.tv_time_third);
        final TextView tv_back = (TextView) dialog_first.findViewById(R.id.tv_back);

        if (ReschduleListTime.size() > 0) {
            tv_time_first.setText(ReschduleListTime.get(0));
            tv_time_first.setVisibility(View.VISIBLE);
        } else {
            tv_time_first.setVisibility(View.GONE);
        }

        if (ReschduleListTime.size() > 1) {
            tv_time_second.setText(ReschduleListTime.get(1));
            tv_time_second.setVisibility(View.VISIBLE);
        } else {
            tv_time_second.setVisibility(View.GONE);
        }

        if (ReschduleListTime.size() > 2) {
            tv_time_third.setText(ReschduleListTime.get(2));
            tv_time_third.setVisibility(View.VISIBLE);
        } else {
            tv_time_third.setVisibility(View.GONE);
        }


        tv_time_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTimeSel = tv_time_first.getText().toString().trim();
                // removeableHours = new ArrayList<String>();
                Calendar calSelected = Constants.getCalenderInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));

                    String timeRes = startTimeSel + " - " + lasttime;
                    callReschduleBooking(bookingid, timeRes/*, unavailable_hrsfinal*/);
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
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));

                    String timeRes = startTimeSel + " - " + lasttime;
                    callReschduleBooking(bookingid, timeRes/*, unavailable_hrsfinal*/);
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
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
                try {
                    calSelected.setTime(sdf.parse(date + " " + startTimeSel));
                    calSelected.add(Calendar.MINUTE, (int) servicetime);
                    String lasttime = Constants.getCurrentTime_hh_mm(calSelected);
                    Calendar startCalendar = Constants.getCalenderInstance();
                    startCalendar.setTime(sdf.parse(date.trim() + " " + startTimeSel));

                    Calendar endCalendar = Constants.getCalenderInstance();
                    endCalendar.setTime(sdf.parse(date + " " + lasttime));

                    String timeRes = startTimeSel + " - " + lasttime;
                    callReschduleBooking(bookingid, timeRes/*, unavailable_hrsfinal*/);
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

    private void callReschduleBooking(final String booking_id, final String booking_time/*, final ArrayList<String> unavailable_hrs*/) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(HomeActivity.this);
            Log.d("api call post", Constants.RescheduleBooking);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.RescheduleBooking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    callGetBooking("Dont Show");
                                } else {
                                    String message = jo.getString("message");
                                    Constants.dismissProgress();
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
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("booking_time", booking_time);
                    params.put("booking_id", booking_id);
                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
        }
    }


    @Override
    public void confirmBookingListener(int pos) {
        if (alBookingLists.get(pos).reschedule.equalsIgnoreCase("1")) {
            ShowPopupDialougCpmfrimed("Pending request", "You are unable to confirm this booking as we are still awaiting your customers response");
        } else {
            if (alBookingLists.get(pos).is_confirmed.equalsIgnoreCase("1")) {
                ShowPopupDialougCpmfrimed("Confirmed!", "This booking already confirmed");
            } else {
                callConfirmCancelUpcomingBooking("show", alBookingLists.get(pos).booking_id, "1", "", "");
            }
        }
    }

    @Override
    public void startCloseBookingListener(int pos) {

        String time = Constants.getCurrentTime_hh_mm(Constants.getCalenderInstance());
        Calendar cal = Calendar.getInstance();
        String datel = formater.format(cal.getTime());
        if (alBookingLists.get(pos).is_confirmed.equalsIgnoreCase("1")) {
            callStartCloseBooking(alBookingLists.get(pos).booking_id, "4", time);
        } else if (alBookingLists.get(pos).is_confirmed.equalsIgnoreCase("4")) {
            //Close Booking
            callStartCloseBooking(alBookingLists.get(pos).booking_id, "3", time);
        }
    }

    @Override
    public void rescheduleBookingListener(int pos) {
        try {
            String date = alBookingLists.get(pos).date;
            String booking_time = alBookingLists.get(pos).booking_time;

            String bookingtime[] = booking_time.split("-");
            String str_StartTime = bookingtime[0];
            String str_EndTime = bookingtime[1];

            //   final ArrayList<String> unavailableHoursList = new ArrayList<>();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEEE, dd MMM yyyy kk:mm");
            Calendar startCalendar = Constants.getCalenderInstance();
            Calendar endCalendar = Constants.getCalenderInstance();
            //"Sunday, 05 Nov 2017 06:30" (at offset 8)
            startCalendar.setTime(simpleDateFormat1.parse(date.trim() + " " + bookingtime[0].trim()));
            endCalendar.setTime(simpleDateFormat1.parse(date + " " + bookingtime[1].trim()));
            long mintues = Constants.getDateDiffernce(startCalendar.getTime(), endCalendar.getTime());
            callGetUnavailableHoursReschedule(alBookingLists.get(pos).booking_id, date, booking_time, /*unavailableHoursList,*/ startCalendar.getTime(), endCalendar.getTime(), mintues);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cancelDeclineBookingListener(int pos, String type) {
        showPopupDialogChecking(pos);
    }

    @Override
    public void onPostCodeClicked(int position) {
        onBookingDetail(position);
    }

    void onBookingDetail(int position) {
        Intent it = new Intent(HomeActivity.this, BookingDetailsActivity.class).putExtra("bookingNotificationArrayList", bookingNotificationArrayList.get(position));
        startActivity(it);
    }

    public void translationDone(int position) {

        for (int i = 0; i < objHomeAdapter.getItemCount(); i++) {
            if (i != position) {
                RecyclerView.ViewHolder viewHolder = rv_booking_List.findViewHolderForAdapterPosition(i);
                //RecyclerView.ViewHolder viewHolder = rv_booking_List.findViewHolderForItemId(objHomeAdapter.getItemId(i));
                if (viewHolder != null) {
                    BothSideCoordinatorLayout bothSideCoordinatorLayout = (BothSideCoordinatorLayout) viewHolder.itemView;
                    SwipeLayout sp = (SwipeLayout) bothSideCoordinatorLayout.findViewById(R.id.foregroundView);
                    sp.translateTo(0);
                }
            }
        }
    }

    @Override
    public void notificationClickListerner(int pos) {
        if (bookingNotificationArrayList != null
                && bookingNotificationArrayList.size() > 0) {

            Log.e("DeepX", "notificationClickListerner type:" + bookingNotificationArrayList.get(pos).type);
            String From = bookingNotificationArrayList.get(pos).type;
            String id = bookingNotificationArrayList.get(pos).id;

            String date = bookingNotificationArrayList.get(pos).date;
            String name = bookingNotificationArrayList.get(pos).name;
            String strmsg = bookingNotificationArrayList.get(pos).msg;
            String service_name = bookingNotificationArrayList.get(pos).service_name;
            String time = bookingNotificationArrayList.get(pos).booking_time;
            String type = bookingNotificationArrayList.get(pos).type;
            String bookingid = bookingNotificationArrayList.get(pos).booking_id;


            setStatusRead(pos);
            if (!From.equalsIgnoreCase("Api")) {
                if (From.equalsIgnoreCase("signup_customer") || From.equalsIgnoreCase("signup_complete") || From.equalsIgnoreCase("today_booking") || From.equalsIgnoreCase("new_user")
                        || From.equalsIgnoreCase("new_user_profile") || From.equalsIgnoreCase("reschedule_customer_confirm") || From.equalsIgnoreCase("customer_reschedule") || From.equalsIgnoreCase("reschedule_customer") || From.equalsIgnoreCase("barber_reschedule_confirm") ||
                        From.equalsIgnoreCase("barber_confirm_reschedule") || From.equalsIgnoreCase("barber_reschedule") ||
                        From.equalsIgnoreCase("confirm_reschedule") || From.equalsIgnoreCase("cancel_reschedule") || From.equalsIgnoreCase("customer_cancel")) {
                    /*good news , cantmakeit, where are you ,Darn it */
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showNotficationPopupMsg(name, service_name, time, date, type, id, strmsg);
                } else if (From.equalsIgnoreCase("addIn_whitelist") || From.equalsIgnoreCase("new_barber_uncon_booking") || From.equalsIgnoreCase("new_user")) {
                    //Take  to UpcomingBooking
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showNotficationPopupMsg(name, service_name, time, date, type, id, strmsg);

                } else if (From.equalsIgnoreCase("add_favourite")) {
                    //Take  to UpcomingBooking
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showNotficationPopupMsg(name, service_name, time, date, type, id, strmsg);

                } else if (From.equalsIgnoreCase("auto_cancel")) {
                    //Take  to UpcomingBooking
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showNotficationPopupForgot(name, service_name, time, date, type, id, "You did not confirm this booking in time.This booking has been cancelled", "Where were you?");

                } else if (From.equalsIgnoreCase("barber_cancel")) {
                    //Take  to UpcomingBooking
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showNotficationPopupMsg(name, service_name, time, date, type, id, strmsg);

                } else if (From.equalsIgnoreCase("auto_confirm_barber")) {
                    //Take  to UpcomingBooking
                    showNotficationPopupForgot(name, service_name, time, date, From, id, "We know you are busy, but all bookings need to be closed by the barber", "Booking Closed!");

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //  callClearNotfication(id);
                } else if (From.equalsIgnoreCase("barber_confirm")) {
                    //Take  to UpcomingBooking
                    showNotficationPopupForgot(name, service_name, time, date, From, id, "We know you are busy, but all bookings need to be closed by the barber", "Booking Closed!");

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //  callClearNotfication(id);
                } else if (From.equalsIgnoreCase("activate_soon") || From.equalsIgnoreCase("questions")) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Intent it = new Intent(HomeActivity.this, HelpActivity.class);
                    startActivity(it);
                    //Take  to BarberHelp page
                } else if (From.equalsIgnoreCase("first_review_barber")) {
                    //Takes	you	to	the review	screen
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Intent it = new Intent(HomeActivity.this, ReviewActivity.class);
                    startActivity(it);
                } else if (From.equalsIgnoreCase("view_profile")) {
                    //Takes	you	to	Barber	profile
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Intent it = new Intent(HomeActivity.this, ViewActivity.class);
                    startActivity(it);
                } else if (From.equalsIgnoreCase("direct_message")) {
                    //Takes	you	to	Barber	profile
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Intent it = new Intent(HomeActivity.this, TermConditionActiivty.class);
                    it.putExtra("booking_id", bookingid);
                    startActivity(it);
                } else {

                }
                if (!id.equalsIgnoreCase(""))
                    callClearNotfication(id);
            }
        }
    }

    private void callClearNotfication(final String notification_id) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Log.d("api call post", Constants.ClearNotfication);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.ClearNotfication,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            callUpcomingBookingNotificationApi("Api");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(HomeActivity.this);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return ApiClient.getInstance().getHeaders(prefs);
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("notification_id", notification_id);
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(HomeActivity.this);
        }
    }

    private void setStatusRead(int pos) {
        objNotificationAdapter.Service_list.get(pos).is_read = "1";
        objNotificationAdapter.notifyDataSetChanged();
    }
    public byte[] encodeBase64(String encodeMe){
        byte[] encodedBytes = Base64.encodeBase64(encodeMe.getBytes());
        return encodedBytes ;
     }
    public String bytetostring(byte[] encodedBytes)
    {
        String encoded = Base64Utils.encode(encodedBytes);
        return encoded;
    }

}
