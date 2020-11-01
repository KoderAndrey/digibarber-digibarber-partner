package com.digibarber.app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.CustomAdapters.ServiceListRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.EditServiceCallback;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServiceListActivity extends BaseActivity implements EditServiceCallback {
    JSONObject joHair = new JSONObject();
    JSONObject joBeard = new JSONObject();
    JSONObject joPamper = new JSONObject();
    JSONObject joMiscellaneous = new JSONObject();
    JSONObject joMobile = new JSONObject();
    JSONObject joFinal;
    ServiceListRecyclerAdapter adapter;
    ArrayList<ServicePriceTime> alServiceList = new ArrayList<>();
    RecyclerView rv_service_detail;
    private String From = "Service_List";


    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_service_list);

        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                String StrjoHair = bd.getString("joHair");
                if (StrjoHair != null) {
                    joHair = new JSONObject(StrjoHair);
                }
                String StrjoBeard = bd.getString("joBeard");
                if (StrjoBeard != null) {
                    joBeard = new JSONObject(StrjoBeard);
                }
                String StrjoPamper = bd.getString("joPamper");
                if (StrjoPamper != null) {
                    joPamper = new JSONObject(StrjoPamper);
                }
                String StrjoMiscellnous = bd.getString("joMiscellaneous");
                if (StrjoMiscellnous != null) {
                    joMiscellaneous = new JSONObject(StrjoMiscellnous);
                }
                String StrjoMobile = bd.getString("joMobile");
                if (StrjoMobile != null) {
                    joMobile = new JSONObject(StrjoMobile);
                }
                From = bd.getString("From");
            }
            joFinal = new JSONObject();
            JSONArray jaData = new JSONArray();
            jaData.put(joHair);
            jaData.put(joBeard);
            jaData.put(joPamper);
            jaData.put(joMiscellaneous);
            jaData.put(joMobile);
            joFinal.put("data", jaData);
            if (joHair.length() > 0) {
                for (int i = 0; i < joHair.getJSONArray("sub_services").length(); i++) {
                    JSONObject jo = joHair.getJSONArray("sub_services").getJSONObject(i);
                    alServiceList.add(new ServicePriceTime(joHair.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price").replace("£", ""), jo.getString("time"), jo.getString("desc"), "false"));
                }
            }
            if (joBeard.length() > 0) {
                for (int i = 0; i < joBeard.getJSONArray("sub_services").length(); i++) {
                    JSONObject jo = joBeard.getJSONArray("sub_services").getJSONObject(i);
                    alServiceList.add(new ServicePriceTime(joBeard.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price").replace("£", ""), jo.getString("time"), jo.getString("desc"), "false"));
                }
            }

            if (joPamper.length() > 0) {
                for (int i = 0; i < joPamper.getJSONArray("sub_services").length(); i++) {
                    JSONObject jo = joPamper.getJSONArray("sub_services").getJSONObject(i);
                    alServiceList.add(new ServicePriceTime(joPamper.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price").replace("£", ""), jo.getString("time"), jo.getString("desc"), "false"));
                }
            }
            if (joMiscellaneous.length() > 0) {
                for (int i = 0; i < joMiscellaneous.getJSONArray("sub_services").length(); i++) {
                    JSONObject jo = joMiscellaneous.getJSONArray("sub_services").getJSONObject(i);
                    alServiceList.add(new ServicePriceTime(joMiscellaneous.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price").replace("£", ""), jo.getString("time"), jo.getString("desc"), "false"));
                }
            }
            if (joMobile.length() > 0) {
                for (int i = 0; i < joMobile.getJSONArray("sub_services").length(); i++) {
                    JSONObject jo = joMobile.getJSONArray("sub_services").getJSONObject(i);
                    alServiceList.add(new ServicePriceTime(joMobile.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price").replace("£", ""), jo.getString("time"), jo.getString("desc"), "true"));
                }
            }


        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        rv_service_detail = findViewById(R.id.rv_service_deatail);
        ImageView back_icon = findViewById(R.id.back_icon);
        ImageView tv_done = findViewById(R.id.tv_done);
        ImageView iv_edit = findViewById(R.id.iv_edit);
        ImageView iv_add = findViewById(R.id.iv_add);
        adapter = new ServiceListRecyclerAdapter(alServiceList, ServiceListActivity.this, ServiceListActivity.this);
        rv_service_detail.setLayoutManager(new LinearLayoutManager(ServiceListActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_service_detail.setHasFixedSize(true);
        rv_service_detail.setAdapter(adapter);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (From != null && From.equalsIgnoreCase("EditProfile")) {
                    finish();
                } else {
                    Intent it = new Intent(ServiceListActivity.this, AddServicePriceTimeActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMiscellaneous", joMiscellaneous.toString());
                    it.putExtra("joMobile", joMobile.toString());

                    startActivity(it);
                    finish();
                }
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HitWithNEwValues();
                callAddBarberServices(joFinal);

            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (From.equalsIgnoreCase("Signup")) {
                    Intent it = new Intent(ServiceListActivity.this, AddServicePriceTimeActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMobile", joMobile.toString());
                    it.putExtra("joMiscellaneous", joMiscellaneous.toString());
                    it.putExtra("From", "Signup");
                    startActivity(it);
                    finish();
                } else {
                    Intent it = new Intent(ServiceListActivity.this, AddServicePriceTimeActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMobile", joMobile.toString());
                    it.putExtra("joMiscellaneous", joMiscellaneous.toString());
                    it.putExtra("From", "EditProfile");
                    startActivity(it);
                    finish();
                }


            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (From.equalsIgnoreCase("Signup")) {
                    Intent it = new Intent(ServiceListActivity.this, PickServiceActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMobile", joMobile.toString());
                    it.putExtra("joMiscellaneous", joMiscellaneous.toString());
                    it.putExtra("From", "Signup");
                    startActivity(it);
                    finish();
                } else {
                    Intent it = new Intent(ServiceListActivity.this, PickServiceActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMobile", joMobile.toString());
                    it.putExtra("joMiscellaneous", joMiscellaneous.toString());
                    it.putExtra("From", "EditProfile");
                    startActivity(it);
                    finish();
                }


            }
        });
    }


    private void callAddBarberServices(final JSONObject jaMain) {
        Log.e("jaMain", jaMain.toString());
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(ServiceListActivity.this);
            Log.d("api call post",Constants.AddBarberService);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.AddBarberService,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("** RESPONSE **", response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String response_values = jsonObj.getString("success");
                                String message = jsonObj.getString("message");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    prefs.edit().putString(Constants.KEY_SERVICES, jaMain.toString()).apply();
                                    if (From != null && From.equalsIgnoreCase("EditProfile")) {
                                        Intent it = new Intent();
                                        setResult(RESULT_OK, it);
                                        finish();
                                    } else {
                                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "service_list").apply();
                                        Intent it = new Intent(ServiceListActivity.this, AddOpenHoursActivity.class);
                                        startActivity(it);
                                    }
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
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("authenticate", prefs.getString("token_value", "35f5df37-d3c9-4ed9-ad77-68cc58db4088"));
                    headers.put("user_id", prefs.getString("user_id", "28"));
                    return headers;
                }


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    //String services = jaMain.toString().replace("sub_services", "subservices");
                    params.put("services", jaMain.toString().replace("sub_services", "subservices"));
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    @Override
    public void callClick(int pos) {

        Intent it = new Intent(ServiceListActivity.this, ServiceDetailActivity.class);
        it.putExtra("ServiceDetail", alServiceList.get(pos));
        it.putExtra("Position", pos);
        startActivityForResult(it, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {

                    String strnewJoObject = data.getStringExtra("newJoObject");
                    try {
                        JSONObject newJoObject = new JSONObject(strnewJoObject);
                        int Position = data.getIntExtra("Position", 0);
                        JSONArray ja = newJoObject.getJSONArray("sub_services");
                        String category_name = newJoObject.getString("category_name");
                        if (ja.length() > 0) {
                            String service_name = ja.getJSONObject(0).getString("service_name");
                            String price = ja.getJSONObject(0).getString("price");
                            String time = ja.getJSONObject(0).getString("time");
                            String desc = ja.getJSONObject(0).getString("desc");
                            String sub_category_id = ja.getJSONObject(0).getString("sub_category_id");
                            String mobile = ja.getJSONObject(0).getString("mobile");
                            alServiceList.set(Position, new ServicePriceTime(category_name, sub_category_id, service_name, price, time, desc, mobile));
                        }
                        adapter = new ServiceListRecyclerAdapter(alServiceList, ServiceListActivity.this, ServiceListActivity.this);
                        rv_service_detail.setLayoutManager(new LinearLayoutManager(ServiceListActivity.this, LinearLayoutManager.VERTICAL, false));
                        rv_service_detail.setHasFixedSize(true);
                        rv_service_detail.setAdapter(adapter);
                        setValues();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }


        }

    }


    private void HitWithNEwValues() {
        try {
            JSONObject joHair = new JSONObject();
            JSONObject joBeard = new JSONObject();
            JSONObject joPamper = new JSONObject();
            JSONObject joMiscellnous = new JSONObject();
            JSONObject joMobile = new JSONObject();
            JSONArray jaSubHair = new JSONArray();
            JSONArray jaSubBeard = new JSONArray();
            JSONArray jaSubPamper = new JSONArray();
            JSONArray jaSubMiscellnous = new JSONArray();
            JSONArray jaSubMobile = new JSONArray();
            for (int i = 0; i < alServiceList.size(); i++) {
                ServicePriceTime objServicePriceTime = alServiceList.get(i);

                if (objServicePriceTime.catName.equalsIgnoreCase("Hair Cut")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", "£" + objServicePriceTime.subPrice.replace("£", ""));
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubHair.put(joInner);
                    joHair.put("category_id", "1");
                    // joHair.put("category_name", objServicePriceTime.catName);
                    joHair.put("sub_services", jaSubHair);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Beard Services")) {

                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", "£" + objServicePriceTime.subPrice.replace("£", ""));
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);
                    jaSubBeard.put(joInner);
                    joBeard.put("category_id", "2");
                    //  joBeard.put("category_name", objServicePriceTime.catName);
                    joBeard.put("sub_services", jaSubBeard);

                } else if (objServicePriceTime.catName.equalsIgnoreCase("Pamper Treatment")) {

                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", "£" + objServicePriceTime.subPrice.replace("£", ""));
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubPamper.put(joInner);
                    joPamper.put("category_id", "3");
                    //  joPamper.put("category_name", objServicePriceTime.catName);
                    joPamper.put("sub_services", jaSubPamper);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Miscellaneous")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", "£" + objServicePriceTime.subPrice.replace("£", ""));
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubMiscellnous.put(joInner);
                    joMiscellnous.put("category_id", "4");
                    //   joMiscellaneous.put("category_name", objServicePriceTime.catName);
                    joMiscellnous.put("sub_services", jaSubMiscellnous);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Mobile services")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", "£" + objServicePriceTime.subPrice.replace("£", ""));
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubMobile.put(joInner);
                    joMobile.put("category_id", "5");
                    //   joMiscellaneous.put("category_name", objServicePriceTime.catName);
                    joMobile.put("sub_services", jaSubMobile);
                }
            }
            Log.e("joHair", joHair.toString());
            Log.e("joHair", joBeard.toString());
            Log.e("joHair", joPamper.toString());
            Log.e("joHair", joMiscellnous.toString());
            Log.e("joHair", joMobile.toString());

            joFinal = new JSONObject();
            JSONArray jaData = new JSONArray();
            jaData.put(joHair);
            jaData.put(joBeard);
            jaData.put(joPamper);
            jaData.put(joMiscellnous);
            jaData.put(joMobile);
            joFinal.put("data", jaData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setValues() {
        try {
            joHair = new JSONObject();
            joBeard = new JSONObject();
            joPamper = new JSONObject();
            joMiscellaneous = new JSONObject();
            joMobile = new JSONObject();
            JSONArray jaSubHair = new JSONArray();
            JSONArray jaSubBeard = new JSONArray();
            JSONArray jaSubPamper = new JSONArray();
            JSONArray jaSubMiscellnous = new JSONArray();
            JSONArray jaSubMobile = new JSONArray();
            for (int i = 0; i < alServiceList.size(); i++) {
                ServicePriceTime objServicePriceTime = alServiceList.get(i);


                if (objServicePriceTime.catName.equalsIgnoreCase("Hair Cut")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", objServicePriceTime.subPrice);
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubHair.put(joInner);
                    joHair.put("category_id", "1");
                    joHair.put("category_name", objServicePriceTime.catName);
                    joHair.put("sub_services", jaSubHair);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Beard Services")) {

                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", objServicePriceTime.subPrice);
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);
                    jaSubBeard.put(joInner);
                    joBeard.put("category_id", "2");
                    joBeard.put("category_name", objServicePriceTime.catName);
                    joBeard.put("sub_services", jaSubBeard);

                } else if (objServicePriceTime.catName.equalsIgnoreCase("Pamper Treatment")) {

                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", objServicePriceTime.subPrice);
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubPamper.put(joInner);
                    joPamper.put("category_id", "3");
                    joPamper.put("category_name", objServicePriceTime.catName);
                    joPamper.put("sub_services", jaSubPamper);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Miscellaneous")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", objServicePriceTime.subPrice);
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubMiscellnous.put(joInner);
                    joMiscellaneous.put("category_id", "4");
                    joMiscellaneous.put("category_name", objServicePriceTime.catName);
                    joMiscellaneous.put("sub_services", jaSubMiscellnous);
                } else if (objServicePriceTime.catName.equalsIgnoreCase("Mobile services")) {
                    JSONObject joInner = new JSONObject();
                    joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                    joInner.put("service_name", objServicePriceTime.subName);
                    joInner.put("price", objServicePriceTime.subPrice);
                    joInner.put("time", objServicePriceTime.subTime);
                    joInner.put("desc", objServicePriceTime.subDes);

                    jaSubMobile.put(joInner);
                    joMobile.put("category_id", "5");
                    joMobile.put("category_name", objServicePriceTime.catName);
                    joMobile.put("sub_services", jaSubMobile);
                }
            }

            Log.e("joHair", joHair.toString());
            Log.e("joHair", joBeard.toString());
            Log.e("joHair", joPamper.toString());
            Log.e("joHair", joMiscellaneous.toString());
            Log.e("joMobile", joMobile.toString());

            joFinal = new JSONObject();
            JSONArray jaData = new JSONArray();
            jaData.put(joHair);
            jaData.put(joBeard);
            jaData.put(joPamper);
            jaData.put(joMiscellaneous);
            jaData.put(joMobile);
            joFinal.put("data", jaData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
