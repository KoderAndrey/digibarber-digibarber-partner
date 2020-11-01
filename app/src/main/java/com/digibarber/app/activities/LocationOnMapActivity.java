package com.digibarber.app.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.apicalls.ApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationOnMapActivity extends BaseActivity implements OnMapReadyCallback {

    EditText et_location;
    ImageView iv_cross;
    ImageView back_icon;
    private ImageView tv_next;
    private String Latitude = "0.0";
    private String Longitude = "0.0";
    private String address;
    private String From = "";
    private String workplace;
    private String postcode;
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_location_on_map);
        bd = getIntent().getExtras();
        if (bd != null) {
            Latitude = bd.getString("Latitude");
            Longitude = bd.getString("Longitude");
            workplace = bd.getString("workplace");
            address = bd.getString("address");
            postcode = bd.getString("postcode");
            From = bd.getString("From");
            firstname = bd.getString("first_name");
            lastName = bd.getString("last_name");
            email = bd.getString("email");
            password = bd.getString("password");
            phone = bd.getString("phone");
        }
        et_location = (EditText) findViewById(R.id.et_location);

        try {
            if (address != null && !TextUtils.isEmpty(address)) {

                String[] arrAddress = address.split(",");
                if (arrAddress.length >= 3) {
                    String ans = arrAddress[0] + ", " + arrAddress[1];
                    et_location.setText(ans);
                } else {
                    et_location.setText(address);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            et_location.setText(address);
        }


        iv_cross = (ImageView) findViewById(R.id.iv_cross);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        tv_next = (ImageView) findViewById(R.id.tv_next);
        if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
            tv_next.setImageResource(R.mipmap.done);
        } else {
            tv_next.setImageResource(R.mipmap.next);
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddBarberProfile(address, postcode, workplace);
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
                    Intent it = new Intent(LocationOnMapActivity.this, AddWorkPlaceLocationActivity.class);
                    it.putExtra("workplace", workplace);
                    it.putExtra("address", address);
                    it.putExtra("From", "EditPrfoile");
                    startActivity(it);
                } else {
                    Intent it = new Intent(LocationOnMapActivity.this, AddWorkPlaceLocationActivity.class);
                    it.putExtra("Latitude", Latitude);
                    it.putExtra("Longitude", Longitude);
                    it.putExtra("workplace", workplace);
                    it.putExtra("address", address);
                    it.putExtra("postcode", postcode);
                    it.putExtra("first_name", firstname);
                    it.putExtra("last_name", lastName);
                    it.putExtra("email", email);
                    it.putExtra("password", password);
                    it.putExtra("phone", phone);
                    it.putExtra("From", From);
                    startActivity(it);
                }
                finish();

            }
        });
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_location.setText("");
                if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
                    Intent it = new Intent(LocationOnMapActivity.this, ChangePrivateInformationActivity.class);
                    startActivity(it);
                    finish();
                } else {
                    Intent it = new Intent(LocationOnMapActivity.this, AddWorkPlaceLocationActivity.class);
                    it.putExtra("Latitude", Latitude);
                    it.putExtra("Longitude", Longitude);
                    it.putExtra("workplace", workplace);
                    it.putExtra("address", address);
                    it.putExtra("postcode", postcode);
                    it.putExtra("first_name", firstname);
                    it.putExtra("last_name", lastName);
                    it.putExtra("email", email);
                    it.putExtra("password", password);
                    it.putExtra("phone", phone);
                    it.putExtra("From", From);
                    startActivity(it);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLatLongFromAddress(address, googleMap);

    }


    @Override
    public void onBackPressed() {
        if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
            Intent it = new Intent(LocationOnMapActivity.this, AddWorkPlaceLocationActivity.class);
            it.putExtra("workplace", workplace);
            it.putExtra("address", address);
            it.putExtra("From", "EditPrfoile");
            startActivity(it);
        } else {
            Intent it = new Intent(LocationOnMapActivity.this, AddWorkPlaceLocationActivity.class);
            it.putExtra("Latitude", Latitude);
            it.putExtra("Longitude", Longitude);
            it.putExtra("workplace", workplace);
            it.putExtra("address", address);
            it.putExtra("postcode", postcode);
            it.putExtra("first_name", firstname);
            it.putExtra("last_name", lastName);
            it.putExtra("email", email);
            it.putExtra("password", password);
            it.putExtra("phone", phone);
            it.putExtra("From", From);
            startActivity(it);
        }
        finish();

    }

    protected void getLatLongFromAddress(final String address, final GoogleMap googleMap) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            final AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Constants.showPorgess(LocationOnMapActivity.this);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String temp = "Empty";
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    try {
                        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "%20") + "&key=AIzaSyCv0nOTO1Ii489Zc4VTmWBNyc3-7qLlVeI";
                        Log.e("Here Url", "" + url);
                        HttpGet get = new HttpGet(url);
                        response = client.execute(get);
                        /*Checking response */
                        if (response != null) {
                            // InputStream in = response.getEntity().getContent(); //Get the data in the entity
                            temp = EntityUtils.toString(response.getEntity());
                        } else {
                            Constants.dismissProgress();
                            Log.e("NUll", "response " + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Constants.dismissProgress();
                    }
                    return temp;
                }

                @Override
                protected void onPostExecute(String Resposne) {
                    super.onPostExecute(Resposne);
                    Log.e("Resposne googleapis", "" + Resposne);
                    Constants.dismissProgress();
                    try {
                        if (Resposne != null && !Resposne.equalsIgnoreCase("Empty")) {

                            JSONObject jaResponse = new JSONObject(Resposne);
                            JSONArray jaResults = jaResponse.getJSONArray("results");
                            if (jaResults.length() > 0) {
                                JSONObject jaZero = jaResults.getJSONObject(0);
                                if (jaZero.has("geometry")) {
                                    JSONObject joGemotry = jaZero.getJSONObject("geometry");
                                    if (joGemotry.has("location")) {
                                        Latitude = joGemotry.getJSONObject("location").getString("lat");
                                        Longitude = joGemotry.getJSONObject("location").getString("lng");
                                    } else {
                                        Latitude = "0.0";
                                        Longitude = "0.0";
                                    }
                                } else {
                                    Latitude = "0.0";
                                    Longitude = "0.0";
                                }
                            } else {

                            }
                        }
                        LatLng coordinate = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));
                        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                                coordinate, 14);
                        googleMap.addMarker(new MarkerOptions().title("Your Location").position(coordinate).icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 14.0f));
                        googleMap.animateCamera(location);
                        Log.e("getDefaultCardDetail", Resposne);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Latitude = "0.0";
                        Longitude = "0.0";
                    }
                }
            };
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Constants.showPopupInternet(activity);
        }
    }

    private void callAddBarberProfile(final String address, final String postcode, final String workplaces) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(LocationOnMapActivity.this);
            Log.e("Deep", "AddBarberProfile url:" + Constants.AddBarberProfile);

            Log.d("api call post",Constants.AddBarberProfile);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.AddBarberProfile,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Deep", "AddBarberProfile response:" + response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    if (From != null && From.equalsIgnoreCase("EditPrfoile")) {
                                        Intent it = new Intent(LocationOnMapActivity.this, ChangePrivateInformationActivity.class);
                                        startActivity(it);
                                        finish();
                                    } else {
                                        Intent it = new Intent(LocationOnMapActivity.this, CustomCameraGalleryActivity.class);
                                        it.putExtra("Latitude", Latitude);
                                        it.putExtra("Longitude", Longitude);
                                        it.putExtra("workplace", workplaces);
                                        it.putExtra("address", address);
                                        it.putExtra("postcode", postcode);
                                        it.putExtra("first_name", firstname);
                                        it.putExtra("last_name", lastName);
                                        it.putExtra("email", email);
                                        it.putExtra("password", password);
                                        it.putExtra("phone", phone);
                                        it.putExtra("From", From);
                                        startActivity(it);
                                        finish();
                                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "location_map").apply();
                                    }
                                    prefs.edit().putString(Constants.KEY_WORKPLACE, workplaces).apply();
                                    prefs.edit().putString(Constants.KEY_ADDRESS, address).apply();
                                    prefs.edit().putString(Constants.KEY_POSTCODE, postcode).apply();
                                    prefs.edit().putString(Constants.KEY_LAT, Latitude).apply();
                                    prefs.edit().putString(Constants.KEY_LOG, Longitude).apply();
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
            })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("authenticate", prefs.getString("token_value", ""));
                    headers.put("user_id", prefs.getString("user_id", ""));
                    Log.e("Deep", "AddBarberProfile headers:" + headers);

                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("lat", Latitude);
                    params.put("lon", Longitude);
                    params.put("address", address);
                    params.put("postcode", postcode);
                    params.put("workplace", workplaces);
                    params.put("phone", "");
                    params.put("open_hours", "");
                    Log.e("Deep", "AddBarberProfile params:" + params);

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
