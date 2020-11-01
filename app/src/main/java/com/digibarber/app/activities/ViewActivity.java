package com.digibarber.app.activities;

/**
 * Created by DIGIBARBER LTD on 9/10/17.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.apicalls.ApiClient;
import com.squareup.picasso.Picasso;
import com.digibarber.app.CustomAdapters.CustomGalleryAdapter;
import com.digibarber.app.CustomAdapters.ReviewRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;

import static com.digibarber.app.CustomClasses.Constants.downloadFileFromServer;

public class ViewActivity extends BaseActivity {

    private RatingBar rating_stars;
    private FrameLayout imageLayout;
    private RelativeLayout reviewsLayout;
    private CircleImageView barber_profile_image;
    private TextView textViewName, workplaceName, address, timing_text, review_text, photos_text, total_reviews_value, text_total_ratings, reviews_text;
    private GridView gallery_grid;
    private ArrayList<HashMap<String, String>> gallery_image_listing = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> reviews_listing = new ArrayList<HashMap<String, String>>();
    private CustomGalleryAdapter gallery_adapter;
    //    private CustomReviewAdapter review_adapter;
    private ReviewRecyclerAdapter review_adapter;
    private RecyclerView review_list_view;
    private ScrollView scroll_view;
    private Button book_button;
    String start_time, end_time;
    private DateFormat formater;
    private DateFormat parseableDate;

    LinearLayout middleLayout;
    RelativeLayout rl_top;

    LinearLayout ll_grid_linear;
    int SelectedDay;

    String overallRating = "";
    String barber_profile_details = "";

    int day;

    private final int REQUEST_PERMISSION_STORAGE_GROUP = 235;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#F8F8F8"));
        }

        formater = new SimpleDateFormat("dd MMM yy");
        parseableDate = new SimpleDateFormat("dd/mm/yyyy");

        day = Constants.getPresentDay();

        rating_stars = (RatingBar) findViewById(R.id.rating_stars);
        imageLayout = (FrameLayout) findViewById(R.id.imageLayout);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        middleLayout = (LinearLayout) findViewById(R.id.middleLayout);
        barber_profile_image = (CircleImageView) findViewById(R.id.barber_profile_image);
        textViewName = (TextView) findViewById(R.id.textViewName);
        workplaceName = (TextView) findViewById(R.id.workplaceName);
        address = (TextView) findViewById(R.id.address);
        timing_text = (TextView) findViewById(R.id.timing_text);
        review_text = (TextView) findViewById(R.id.review_text);
        photos_text = (TextView) findViewById(R.id.photos_text);
        total_reviews_value = (TextView) findViewById(R.id.total_reviews_value);
        text_total_ratings = (TextView) findViewById(R.id.text_total_ratings);
        reviews_text = (TextView) findViewById(R.id.reviews_text);
        book_button = (Button) findViewById(R.id.book_button);

        ll_grid_linear = (LinearLayout) findViewById(R.id.ll_grid_linear);
        gallery_grid = (GridView) findViewById(R.id.gallery_grid);
        review_list_view = (RecyclerView) findViewById(R.id.review_list_view);
        reviewsLayout = (RelativeLayout) findViewById(R.id.reviewsLayout);

        getReviewListing();

        String barber_id = prefs.getString(Constants.KEY_USER_ID, "");
        String Full_Name = prefs.getString(Constants.KEY_FULL_NAME, "");
        String Emai = prefs.getString(Constants.KEY_EMAIL, "");
        String Phone = prefs.getString(Constants.KEY_PHONE, "");
        String Bankdetails = prefs.getString(Constants.KEY_BANK_DETAIL, "");
        String User_Image = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
        String Work_place = prefs.getString(Constants.KEY_WORKPLACE, "");
        String strAddress = prefs.getString(Constants.KEY_ADDRESS, "");
        String Services = prefs.getString(Constants.KEY_SERVICES, "");
        String Gallery_image = prefs.getString(Constants.KEY_GALLERY_IMAGES, "");
        String Open_Hours = prefs.getString(Constants.KEY_OPEN_HOURS, "");

        Calendar calendar = Calendar.getInstance();
        SelectedDay = calendar.get(Calendar.DAY_OF_WEEK);

        //scroll_view.smoothScrollTo(0, rl_top.getTop());
        //imageLayout.bringToFront();
        total_reviews_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, ReviewActivity.class));
            }
        });
        if (Gallery_image != null && !Gallery_image.equals("")) {


            JSONArray gallery_array = null;
            try {
                gallery_array = new JSONArray(Gallery_image);
                Log.e("gallerylenth", String.valueOf(gallery_array.length()));

                photos_text.setText(gallery_array.length() + " photos");
                Log.e("gallery_array", gallery_array.toString());
                gallery_image_listing = new ArrayList<HashMap<String, String>>();

                for (int gallery_count = 0; gallery_count < gallery_array.length(); gallery_count++) {
                    JSONObject gallery_object = gallery_array.getJSONObject(gallery_count);

                    String gallery_image = gallery_object.getString("gallery_image");
                    Log.e("Gallery_image", gallery_image);
                    HashMap<String, String> hm = new HashMap<String, String>();

                    hm.put("gallery_image", gallery_image);


                    gallery_image_listing.add(hm);
                }


                int modOfGalleryArrayLength = gallery_array.length() % 3;
                if (modOfGalleryArrayLength > 0) {
                    modOfGalleryArrayLength = modOfGalleryArrayLength == 1 ? 2 : 1;
                    for (int gallery_count = 0; gallery_count < modOfGalleryArrayLength; gallery_count++) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("gallery_image", "");
                        gallery_image_listing.add(hm);
                    }
                }

                gallery_adapter = new CustomGalleryAdapter(this, gallery_image_listing);
                gallery_grid.setAdapter(gallery_adapter);
                //  setGridViewHeightBasedOnChildren(gallery_grid, 3);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            photos_text.setText("0 photos");
        }


        try {
            if (!Open_Hours.trim().equalsIgnoreCase("")) {
                Log.e("open_hours", "" + Open_Hours.trim());
                try {
                    JSONArray jsonArray = new JSONArray(Open_Hours.trim());
                    for (int k = 0; k < jsonArray.length(); k++) {
                        JSONObject days_object = jsonArray.getJSONObject(k);
                        switch (SelectedDay) {
                            case Calendar.SUNDAY:

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

                                        } else {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }


                                }

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

                                        } else {
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

                                        } else {
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

                                        } else {
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

                                        } else {
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

                                        } else {
                                            start_time = sub_object.getString("start_time");
                                            end_time = sub_object.getString("end_time");
                                        }
                                    }
                                }

                                break;
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {


        }


        Log.e("id", barber_id);
        Log.e("Open_Hours", Open_Hours);
        Log.e("User_Image", User_Image);
        Log.e("Work_place", Work_place);


        if (Full_Name != null && !Full_Name.equalsIgnoreCase("")) {
            textViewName.setText(Full_Name);
        } else {
            textViewName.setText("");
        }


        if (Work_place != null && !Work_place.equalsIgnoreCase("")) {
            workplaceName.setText(Work_place);
        } else {
            workplaceName.setText("");
        }

        if (strAddress != null && !strAddress.equalsIgnoreCase("")) {
            address.setText(strAddress);
        } else {
            address.setText("");
        }


        if (start_time != null && end_time != null && !start_time.equalsIgnoreCase("") && !end_time.equalsIgnoreCase("")) {
            timing_text.setText(start_time + "" + " - " + "" + end_time);
        } else {
            timing_text.setText("CLOSED");
        }


        if (barber_profile_image != null && !barber_profile_image.equals("")) {
            Picasso.with(this).load(User_Image).placeholder(R.color.colorGrey)
                    .fit().into(barber_profile_image);
        }

        photos_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" ** Photos scrool ** ");

                scroll_view.smoothScrollTo(0, ll_grid_linear.getBottom());
            }
        });

        review_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(" ** Reviews scrool ** ");
                scroll_view.smoothScrollTo(0, middleLayout.getBottom());
            }
        });


    }


    /* Back click */
    public void backClick(View v) {
        finish();
    }

    /* Share click */
    public void shareClick(View v) {
        if (ContextCompat.checkSelfPermission(ViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE_GROUP);
        } else {
            shareOperation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE_GROUP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareOperation();
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

    private void shareOperation() {
        try {
            Constants.showPorgess(ViewActivity.this);

            String barber_id = prefs.getString(Constants.KEY_USER_ID, "");
            String Full_Name = prefs.getString(Constants.KEY_FULL_NAME, "");
            String Emai = prefs.getString(Constants.KEY_EMAIL, "");
            String Phone = prefs.getString(Constants.KEY_PHONE, "");
            String Bankdetails = prefs.getString(Constants.KEY_BANK_DETAIL, "");
            final String profile_image = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
            String Work_place = prefs.getString(Constants.KEY_WORKPLACE, "");
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

            Log.e("profile_object", profile_object.toString());

            barber_profile_details = profile_object.toString();

            BranchUniversalObject buo = new BranchUniversalObject()
                    .setCanonicalIdentifier("content/12345")
                    .setTitle("DigiBarber")
                    .setContentDescription("DigiBarber")
//                    .setContentImageUrl(profile_image)
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(new ContentMetadata().addCustomMetadata("barber_profile_details", barber_profile_details));

            LinkProperties lp = new LinkProperties()
                    .setChannel("facebook")
                    .setFeature("sharing")
                    .setCampaign("content 123 launch")
                    .setStage("new user")
                    .addControlParameter("$desktop_url", "http://example.com/home")
                    .addControlParameter("custom", "data")
                    .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

            buo.generateShortUrl(this, lp, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (error == null) {
                        Log.i("BRANCH SDK", "got my Branch link to share: " + url);
                        shareLink(url, profile_image);
                    }
                }
            });


            /*ShareSheetStyle ss = new ShareSheetStyle(ViewActivity.this, "Check this out!", "Barber Profile Details: ")
                .setCopyUrlStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        buo.showShareSheet(this, lp, ss, new Branch.BranchLinkShareListener() {
            @Override
            public void onShareLinkDialogLaunched() {
            }

            @Override
            public void onShareLinkDialogDismissed() {
            }

            @Override
            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
            }

            @Override
            public void onChannelSelected(String channelName) {
            }
        });*/

        } catch (Exception e) {
            Constants.dismissProgress();
            Log.e("Error", e.getMessage());
        }
    }

    private void shareLink(final String url, final String profile_image) {

        new AsyncTask<Void, Void, String>() {

            File file;

            @Override
            protected String doInBackground(Void... voids) {
                file = downloadFileFromServer(profile_image);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Constants.dismissProgress();

                shareLink(file, url);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void shareLink(File file, String url) {
        String text = textViewName.getText().toString() + " - " + workplaceName.getText().toString()
                + "\n" + "You can now book your haircut with me on the DIGIBARBER app!"
                + "\n" + "DigiBarber"
                + "\n\n" + url;

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, "DigiBarber");
            i.putExtra(Intent.EXTRA_TEXT, text);

            i.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    getApplicationContext(),
                    getApplicationContext().getPackageName() + ".provider", file));

            i.setType("*/*");
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i, "Share"));
        } catch (Exception e) {
            Log.e("Share Error", e.getMessage());
        }
    }

    /* Getting reviews listing for a particular Barber */
    public void getReviewListing() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
           /* final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();*/
            Constants.showPorgess(this);

            Log.d("api call post",Constants.BarberReviewsListing);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.BarberReviewsListing,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            pDialog.dismiss();
                            Constants.dismissProgress();

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
                                        rating_stars.setRating((float) ratings);
                                        LayerDrawable stars = (LayerDrawable) rating_stars.getProgressDrawable();
                                        stars.getDrawable(2).setColorFilter(Color.parseColor("#FF9500"), PorterDuff.Mode.SRC_ATOP);
                                        String date = formate.format(ratings);
                                        overallRating = date;
                                        text_total_ratings.setText(date);
                                    }

                                    review_text.setText(data_array.length() + " Reviews");
                                    total_reviews_value.setText("(view all " + data_array.length() + " reviews)");

                                    for (int i = 0; i < data_array.length(); i++) {
                                        JSONObject sub_object = data_array.getJSONObject(i);

                                        String profile_image = sub_object.getString("profile_image");
                                        String review = sub_object.getString("review");
                                        String service = sub_object.getString("service");
                                        String name = sub_object.getString("name");
                                        String rating = sub_object.getString("rating");
                                        String date_of_review = sub_object.getString("date_of_review");

//                                        Date dateSelected = null;
//                                        String datel = "";
//                                        if (date_of_review != null && !date_of_review.equalsIgnoreCase("")) {
//                                            try {
//                                                dateSelected = parseableDate.parse(date_of_review);
//                                                datel = formater.format(dateSelected);
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                                datel = date_of_review;
//                                            }
//                                        } else {
//                                            datel = date_of_review;
//                                        }

                                        String time = sub_object.getString("time");

                                        HashMap<String, String> hm = new HashMap<String, String>();
                                        hm.put("profile_image", profile_image);
                                        hm.put("review", review);
                                        hm.put("service", service);
                                        hm.put("rating", rating);
                                        hm.put("time", time);
                                        hm.put("name", name);
                                        hm.put("date_of_review", date_of_review);
                                        hm.put("Activity", "view");

                                        reviews_listing.add(hm);

                                    }


                                    review_adapter = new ReviewRecyclerAdapter(ViewActivity.this, reviews_listing);
                                    review_list_view.setLayoutManager(new LinearLayoutManager(ViewActivity.this, LinearLayoutManager.VERTICAL, true));
                                    review_list_view.setNestedScrollingEnabled(false);
                                    review_list_view.setAdapter(review_adapter);
                                    review_adapter.notifyDataSetChanged();

                                } else {
                                    review_text.setText("0 reviews");
                                    total_reviews_value.setText("(view all 0 reviews)");
                                    System.out.println("** No reviews found ** ");
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
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
                    Constants.showPopupServer(activity);
                }
            }) {

                /**
                 * Passing some request headers
                 */
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

                    System.out.println(" ** PARAMS ** " + params);

                    return params;
                }
            };

            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
// Adding request to request queue
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
            //System.out.println(" ** PLease connect to Internet **");
        }
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        gallery_adapter = (CustomGalleryAdapter) gridView.getAdapter();
        if (gallery_adapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gallery_adapter.getCount();
        int rows = 0;

        View listItem = gallery_adapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }
}

