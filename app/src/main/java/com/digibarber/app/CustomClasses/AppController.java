package com.digibarber.app.CustomClasses;

/**
 * Created by DIGIBARBER LTD on 8/6/17.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.branch.referral.Branch;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    SharedPreferences prefs;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Foreground.init(this);
        prefs = getSharedPreferences("Digibarber", MODE_PRIVATE);

        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, Context ctx) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(ctx).add(req);
    }

    public RequestQueue getRequestQueue(Context ctx) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//			box = new ProgressDialog(ctx);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!box.isShowing()) {
//                        box.setMessage("Loading...");
//                        box.show();
//                    }
//                }
//            }, 1000);

//			mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//				@Override
//				public void onRequestFinished(Request<Object> request) {
//					if(box.isShowing()){
//						box.dismiss();
//					}
//				}
//			});
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            /*mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());*/
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /* Declaring service for making favorites/ unfavorites on button click */
    public void addToFavorites(final Context context, final String barber_id, final String follow_type, final String message) {
      /*  final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();*/
        Constants.showPorgess((Activity) context);

        Log.d("api call post",Constants.MakeFavorites);

        StringRequest req = new StringRequest(Request.Method.POST, Constants.MakeFavorites,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pDialog.dismiss();
                        Constants.dismissProgress();

                        Log.d("** RESPONSE **", response);

                        try {
                            JSONObject response_object = new JSONObject(response);

                            String success = response_object.getString("success");

                            if (success.equalsIgnoreCase("true")) {

                                /*JSONArray data_array = response_object.getJSONArray("data");
                                for(int i = 0; i < data_array.length(); i++)
                                {
                                    JSONObject sub_object = data_array.getJSONObject(i);

                                    String review = sub_object.getString("review");
                                    String service = sub_object.getString("service");
                                    String name = sub_object.getString("name");
                                    String rating = sub_object.getString("rating");
                                    String time = sub_object.getString("time");

                                    HashMap<String, String> hm = new HashMap<String, String>();

                                    hm.put("review", review);
                                    hm.put("service", service);
                                    hm.put("name", name);
                                    hm.put("rating", rating);
                                    hm.put("time", time);

                                }*/
                                Toast.makeText(context, "Successfully " + message + " favourite list.", Toast.LENGTH_LONG).show();

                            } else {
                                System.out.println("** false ** ");
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
//                pDialog.dismiss();
                Constants.dismissProgress();
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

                params.put("barber_id", barber_id);
                params.put("follow", follow_type);

                System.out.println(" ** PARAMS ** " + params);

                return params;
            }
        };

        req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
// Adding request to request queue
        addToRequestQueue(req, "Add/Update Favorite List");
    }
}
