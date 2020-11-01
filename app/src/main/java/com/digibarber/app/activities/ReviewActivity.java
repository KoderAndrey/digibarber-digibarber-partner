package com.digibarber.app.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.CustomAdapters.ReviewRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.digibarber.app.CustomClasses.Constants.getDateFromString;

public class ReviewActivity extends BaseActivity {

    private RatingBar rating_stars;
    private TextView total_reviews_value, text_total_ratings;
    private ArrayList<HashMap<String, String>> reviews_listing = new ArrayList<HashMap<String, String>>();
    private ReviewRecyclerAdapter review_adapter;
    private RecyclerView rv_rating_list;
    private SharedPreferences prefs;
    ImageView iv_cross;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barber_profile_activity);
        prefs = getSharedPreferences("Digibarber", MODE_PRIVATE);
        iv_cross = (ImageView) findViewById(R.id.iv_cross);
        rating_stars = (RatingBar) findViewById(R.id.rating_stars);
        total_reviews_value = (TextView) findViewById(R.id.total_reviews_value);
        text_total_ratings = (TextView) findViewById(R.id.text_total_ratings);
        rv_rating_list = (RecyclerView) findViewById(R.id.rv_rating_list);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getReviewListing("0");

    }

    public void getReviewListing(final String barber_id) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(ReviewActivity.this);
            Log.d("api call post",Constants.BarberReviewsListing);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.BarberReviewsListing,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();

                            try {
                                JSONObject response_object = new JSONObject(response);
                                String success = response_object.getString("success");
                                if (success.equalsIgnoreCase("true")) {
                                    JSONArray data_array = response_object.getJSONArray("data");
                                    String overall_rating = response_object.getString("overall_rating");

                                    if (overall_rating != null && !overall_rating.equalsIgnoreCase(""))
                                    {
                                        DecimalFormat twoDForm = new DecimalFormat(".#");
                                        double ratings = Double.parseDouble(overall_rating);

                                        //ratings = Math.floor(ratings * 10) / 100;
                                        text_total_ratings.setText(twoDForm.format(ratings)+"");

                                        rating_stars.setRating((float) ratings);
                                        LayerDrawable stars = (LayerDrawable) rating_stars.getProgressDrawable();
                                        stars.getDrawable(2).setColorFilter(Color.parseColor("#FF9500"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                    total_reviews_value.setText("" + data_array.length());
                                    for (int i = 0; i < data_array.length(); i++) {
                                        JSONObject sub_object = data_array.getJSONObject(i);
                                        String profile_image = sub_object.getString("profile_image");
                                        String review = sub_object.getString("review");
                                        String service = sub_object.getString("service");
                                        String name = sub_object.getString("name");
                                        String rating = sub_object.getString("rating");
                                        String date_of_review = sub_object.getString("date_of_review");
                                        String time = sub_object.getString("time");
                                        HashMap<String, String> hm = new HashMap<String, String>();
                                        hm.put("profile_image", profile_image);
                                        hm.put("review", review);
                                        hm.put("service", service);
                                        hm.put("name", name);
                                        hm.put("rating", rating);
                                        hm.put("time", time);
                                        hm.put("date_of_review", date_of_review);
                                        reviews_listing.add(hm);
                                    }
//                                    Collections.sort(reviews_listing, new Comparator<HashMap< String,String >>() {
//
//                                        @Override
//                                        public int compare(HashMap<String, String> lhs,
//                                                           HashMap<String, String> rhs) {
//                                            // Do your comparison logic here and retrn accordingly.
//                                            return getDateFromString(lhs.get("date_of_review"),"dd/MM/yyyy").compareTo( getDateFromString(rhs.get("date_of_review"),"dd/MM/yyyy"));
//                                        }
//                                    });
//                                    Collections.sort(reviews_listing, Collections.reverseOrder());
                                    review_adapter = new ReviewRecyclerAdapter(ReviewActivity.this, reviews_listing);
                                    rv_rating_list.setLayoutManager(new LinearLayoutManager(ReviewActivity.this, LinearLayoutManager.VERTICAL, true));
                                    rv_rating_list.setAdapter(review_adapter);
                                    review_adapter.notifyDataSetChanged();
                                } else {
                                    System.out.println("** No reviews found ** ");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
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
                    params.put("barber_id", barber_id);
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


}