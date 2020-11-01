package com.digibarber.app.apicalls;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.ApiCallback;
import com.digibarber.app.activities.EditProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static ApiClient client;
    private ApiClient(){
    }
    public static ApiClient getInstance(){
        if(client == null){
            client = new ApiClient();
        }
        return client;
    }

    public HashMap<String, String> getHeaders(SharedPreferences prefs){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authenticate", prefs.getString("token_value", ""));
        headers.put("user_id", prefs.getString("user_id", ""));
        return headers;
    }

    public RetryPolicy getRetryPolicy()
    {
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return policy;
    }




    public void getBarberProfile(final Activity activity, final SharedPreferences prefs, final ApiCallback callback) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(activity);
            Log.d("api call post",Constants.GetBarberProfile);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.GetBarberProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) { // this is working
                            Log.e("** GetBarberProfile **", response);
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
                                    boolean stripe_connected = jo0.getBoolean("stripe_connected");
                                    String phone = jo0.getString("phone");
                                    String barber_name = jo0.getString("barber_name");
                                    String workplace = jo0.getString("workplace");
                                    String email = jo0.getString("email");


                                    if (jo0.has("services")) {
                                        JSONArray Jaservices = jo0.getJSONArray("services");
                                        prefs.edit().putString(Constants.KEY_SERVICES, Jaservices.toString()).apply();
                                    } else {
                                        prefs.edit().putString(Constants.KEY_SERVICES, "").apply();

                                    }
                                    if (jo0.has("gallery")) {
                                        JSONArray jaGallery = jo0.getJSONArray("gallery");
                                        prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, jaGallery.toString()).apply();
                                    } else {
                                        prefs.edit().putString(Constants.KEY_GALLERY_IMAGES, "").apply();
                                    }
                                    prefs.edit().putString(Constants.KEY_USER_ID, barber_id).apply();
                                    prefs.edit().putString(Constants.KEY_EMAIL, email).apply();
                                    prefs.edit().putString(Constants.KEY_PHONE, phone).apply();
                                    prefs.edit().putString(Constants.KEY_BANK_DETAIL, bank_detail).apply();
                                    prefs.edit().putString(Constants.KEY_PROFILE_IMAGE, profile_image).apply();
                                    prefs.edit().putString(Constants.KEY_WORKPLACE, workplace).apply();
                                    prefs.edit().putString(Constants.KEY_ADDRESS, address).apply();
                                    prefs.edit().putString(Constants.KEY_OPEN_HOURS, open_hours).apply();
                                    prefs.edit().putBoolean(Constants.STRIPE_CONNECTED, stripe_connected).apply();

                                }
                                callback.onSuccess();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.onFailure();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Constants.dismissProgress();
                    callback.onFailure();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", "35f5df37-d3c9-4ed9-ad77-68cc58db4088"));
                    headers.put("user_id", prefs.getString("user_id", "28"));
                    Log.e("**GetBarberProfile**", "Headers: " + headers);

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
