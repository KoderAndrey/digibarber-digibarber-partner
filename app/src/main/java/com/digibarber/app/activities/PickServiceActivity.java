package com.digibarber.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.AsyncTask;
import com.android.volley.request.StringRequest;
import com.digibarber.app.Beans.ServiceList;
import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.CustomAdapters.PickBeardServiceRecyclerAdapter;
import com.digibarber.app.CustomAdapters.PickHairCutServiceRecyclerAdapter;
import com.digibarber.app.CustomAdapters.PickMiscellnousServiceRecyclerAdapter;
import com.digibarber.app.CustomAdapters.PickMobileServiceRecyclerAdapter;
import com.digibarber.app.CustomAdapters.PickPamertreatmentServiceRecyclerAdapter;
import com.digibarber.app.CustomAdapters.ServiceListRecyclerAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.RecyclerItemClickListener;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.digibarber.app.CustomClasses.Constants.getWidthRatio;

public class PickServiceActivity extends BaseActivity {


    ArrayList<ServiceList> alServiceHair = new ArrayList<>();
    ArrayList<ServiceList> alServiceBeard = new ArrayList<>();
    ArrayList<ServiceList> alServicePamper = new ArrayList<>();
    ArrayList<ServiceList> alServiceMecellnous = new ArrayList<>();
    ArrayList<ServiceList> alServiceMobile = new ArrayList<>();


    ArrayList<Boolean> alServiceHairTemp = new ArrayList<>();
    ArrayList<Boolean> alServiceBeardTemp = new ArrayList<>();
    ArrayList<Boolean> alServicePamperTemp = new ArrayList<>();
    ArrayList<Boolean> alServiceMecellnousTemp = new ArrayList<>();
    ArrayList<Boolean> alServiceMobileTemp = new ArrayList<>();

    RecyclerView rvHairService;
    RecyclerView rvBeardService;
    RecyclerView rvPamperService;
    RecyclerView rvMecellnous;
    RecyclerView rvMobile;

    JSONObject joHair = new JSONObject();
    JSONObject joBeard = new JSONObject();
    JSONObject joPamper = new JSONObject();
    JSONObject joMiscellnous = new JSONObject();
    JSONObject joMobile = new JSONObject();

    int cellWidth = 0;
    int cellHeight = 0;
    private ImageView tv_next;

    boolean isAnyServiceSelected = false;
    ImageView back_icon;
    private PickHairCutServiceRecyclerAdapter adapterHair;
    private PickBeardServiceRecyclerAdapter adapterBeard;
    private PickPamertreatmentServiceRecyclerAdapter adapterPamper;
    private PickMiscellnousServiceRecyclerAdapter adapterMiscellnous;
    private PickMobileServiceRecyclerAdapter adapterMobile;

    RecyclerView recycle_service_liist;
    ArrayList<ServicePriceTime> alServiceList = new ArrayList<>();
    ServiceListRecyclerAdapter adapter;
    private String From;


    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_pick_service);


        try {
            bd = getIntent().getExtras();
            if (bd != null) {

                From = bd.getString("From");

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
                    joMiscellnous = new JSONObject(StrjoMiscellnous);
                }
                String StrjoMobile = bd.getString("joMobile");
                if (StrjoMobile != null) {
                    joMobile = new JSONObject(StrjoMobile);
                }


                if (joHair.length() > 0) {
                    for (int i = 0; i < joHair.getJSONArray("sub_services").length(); i++) {
                        JSONObject jo = joHair.getJSONArray("sub_services").getJSONObject(i);
                        alServiceList.add(new ServicePriceTime(joHair.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                    }
                }
                if (joBeard.length() > 0) {

                    for (int i = 0; i < joBeard.getJSONArray("sub_services").length(); i++) {
                        JSONObject jo = joBeard.getJSONArray("sub_services").getJSONObject(i);
                        alServiceList.add(new ServicePriceTime(joBeard.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                    }
                }

                if (joPamper.length() > 0) {
                    for (int i = 0; i < joPamper.getJSONArray("sub_services").length(); i++) {
                        JSONObject jo = joPamper.getJSONArray("sub_services").getJSONObject(i);
                        alServiceList.add(new ServicePriceTime(joPamper.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                    }
                }
                if (joMiscellnous.length() > 0) {
                    for (int i = 0; i < joMiscellnous.getJSONArray("sub_services").length(); i++) {
                        JSONObject jo = joMiscellnous.getJSONArray("sub_services").getJSONObject(i);
                        alServiceList.add(new ServicePriceTime(joMiscellnous.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                    }
                }
                if (joMobile.length() > 0) {
                    for (int i = 0; i < joMobile.getJSONArray("sub_services").length(); i++) {
                        JSONObject jo = joMobile.getJSONArray("sub_services").getJSONObject(i);
                        alServiceList.add(new ServicePriceTime(joMobile.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "true"));
                    }
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        recycle_service_liist = (RecyclerView) findViewById(R.id.recycle_service_liist);
        if (alServiceList.size() > 0) {
            adapter = new ServiceListRecyclerAdapter(alServiceList, PickServiceActivity.this, "NotEmpty");
            recycle_service_liist.setLayoutManager(new LinearLayoutManager(PickServiceActivity.this, LinearLayoutManager.VERTICAL, false));
            recycle_service_liist.setHasFixedSize(true);
            recycle_service_liist.setAdapter(adapter);
        }
        rvHairService = (RecyclerView) findViewById(R.id.recyclerView);
        rvBeardService = (RecyclerView) findViewById(R.id.recyclerView2);
        rvPamperService = (RecyclerView) findViewById(R.id.recyclerView3);
        rvMecellnous = (RecyclerView) findViewById(R.id.recyclerView4);
        rvMobile = (RecyclerView) findViewById(R.id.recyclerView5);


        tv_next = (ImageView) findViewById(R.id.tv_next);

        back_icon = (ImageView) findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMethodPickService();
            }
        });

        callgetAllServiceList();

    }

    private void callMethodPickService() {


        AsyncTask<Void, Void, Void> asyc = new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {

                try {

                    joHair = new JSONObject();
                    joBeard = new JSONObject();
                    joPamper = new JSONObject();
                    joMiscellnous = new JSONObject();
                    joMobile = new JSONObject();

                    JSONArray jaSubHair = new JSONArray();
                    JSONArray jaSubBeard = new JSONArray();
                    JSONArray jaSubPamper = new JSONArray();
                    JSONArray jaSubMiscellnous = new JSONArray();
                    JSONArray jaSubMobile = new JSONArray();

                    for (int i = 0; i < alServiceHairTemp.size(); i++) {

                        if (alServiceHairTemp.get(i)) {
                            isAnyServiceSelected = true;
                            JSONObject joInner = new JSONObject();
                            joInner.put("sub_category_id", alServiceHair.get(i).sub_category_id.toString());
                            joInner.put("service_name", alServiceHair.get(i).sub_category_name.toString());
                            joInner.put("price", alServiceHair.get(i).catPrice.toString());
                            joInner.put("time", alServiceHair.get(i).catTime.toString());
                            joInner.put("desc", alServiceHair.get(i).catDes.toString());
                            jaSubHair.put(joInner);
                        }
                    }

                    joHair.put("category_id", "1");
                    joHair.put("category_name", alServiceHair.get(0).category_name.toString());
                    joHair.put("sub_services", jaSubHair);
                    for (int i = 0; i < alServiceBeardTemp.size(); i++) {

                        if (alServiceBeardTemp.get(i)) {
                            isAnyServiceSelected = true;
                            JSONObject joInner = new JSONObject();
                            joInner.put("sub_category_id", alServiceBeard.get(i).sub_category_id.toString());
                            joInner.put("service_name", alServiceBeard.get(i).sub_category_name.toString());
                            joInner.put("price", alServiceBeard.get(i).catPrice.toString());
                            joInner.put("time", alServiceBeard.get(i).catTime.toString());
                            joInner.put("desc", alServiceBeard.get(i).catDes.toString());
                            jaSubBeard.put(joInner);
                        }
                    }
                    joBeard.put("category_id", "2");
                    joBeard.put("category_name", alServiceBeard.get(0).category_name.toString());
                    joBeard.put("sub_services", jaSubBeard);

                    for (int i = 0; i < alServicePamperTemp.size(); i++) {
                        if (alServicePamperTemp.get(i)) {
                            isAnyServiceSelected = true;
                            JSONObject joInner = new JSONObject();
                            joInner.put("sub_category_id", alServicePamper.get(i).sub_category_id.toString());
                            joInner.put("service_name", alServicePamper.get(i).sub_category_name.toString());
                            joInner.put("price", alServicePamper.get(i).catPrice.toString());
                            joInner.put("time", alServicePamper.get(i).catTime.toString());
                            joInner.put("desc", alServicePamper.get(i).catDes.toString());
                            jaSubPamper.put(joInner);
                        }
                    }
                    joPamper.put("category_id", "3");
                    joPamper.put("category_name", alServicePamper.get(0).category_name.toString());
                    joPamper.put("sub_services", jaSubPamper);

                    for (int i = 0; i < alServiceMecellnousTemp.size(); i++) {

                        if (alServiceMecellnousTemp.get(i)) {
                            isAnyServiceSelected = true;
                            JSONObject joInner = new JSONObject();
                            joInner.put("sub_category_id", alServiceMecellnous.get(i).sub_category_id.toString());
                            joInner.put("service_name", alServiceMecellnous.get(i).sub_category_name.toString());
                            joInner.put("price", alServiceMecellnous.get(i).catPrice.toString());
                            joInner.put("time", alServiceMecellnous.get(i).catTime.toString());
                            joInner.put("desc", alServiceMecellnous.get(i).catDes.toString());
                            jaSubMiscellnous.put(joInner);
                        }
                    }
                    joMiscellnous.put("category_id", "4");
                    joMiscellnous.put("category_name", alServiceMecellnous.get(0).category_name.toString());
                    joMiscellnous.put("sub_services", jaSubMiscellnous);


                    for (int i = 0; i < alServiceMobileTemp.size(); i++) {

                        if (alServiceMobileTemp.get(i)) {
                            isAnyServiceSelected = true;
                            JSONObject joInner = new JSONObject();
                            joInner.put("sub_category_id", alServiceMobile.get(i).sub_category_id.toString());
                            joInner.put("service_name", alServiceMobile.get(i).sub_category_name.toString());
                            joInner.put("price", alServiceMobile.get(i).catPrice.toString());
                            joInner.put("time", alServiceMobile.get(i).catTime.toString());
                            joInner.put("desc", alServiceMobile.get(i).catDes.toString());
                            jaSubMobile.put(joInner);
                        }
                    }
                    joMobile.put("category_id", "5");
                    if (alServiceMobile.size() > 0) {
                        joMobile.put("category_name", alServiceMobile.get(0).category_name.toString());
                    }
                    joMobile.put("sub_services", jaSubMobile);

                } catch (JSONException e) {

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (isAnyServiceSelected) {
                    if (From.equalsIgnoreCase("Signup")) {

                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "select_services").apply();
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_HAIR, joHair.toString()).apply();
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_BEARD, joBeard.toString()).apply();
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_PAMPER, joPamper.toString()).apply();
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_MISCELL, joMiscellnous.toString()).apply();
                        prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_MOBILE, joMobile.toString()).apply();
                        Intent it = new Intent(PickServiceActivity.this, AddServicePriceTimeActivity.class);
                        it.putExtra("joHair", joHair.toString());
                        it.putExtra("joBeard", joBeard.toString());
                        it.putExtra("joPamper", joPamper.toString());
                        it.putExtra("joMobile", joMiscellnous.toString());
                        it.putExtra("joMiscellaneous", joMobile.toString());
                        it.putExtra("From", "Signup");
                        startActivity(it);
                        finish();
                    } else {
                        Intent it = new Intent(PickServiceActivity.this, AddServicePriceTimeActivity.class);
                        it.putExtra("joHair", joHair.toString());
                        it.putExtra("joBeard", joBeard.toString());
                        it.putExtra("joPamper", joPamper.toString());
                        it.putExtra("joMobile", joMobile.toString());
                        it.putExtra("joMiscellaneous", joMiscellnous.toString());
                        it.putExtra("From", "EditProfile");
                        startActivity(it);
                        finish();
                    }


                } else {

                }


            }
        };
        asyc.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void callgetAllServiceList() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(PickServiceActivity.this);


            Log.d("api call post",Constants.FiltersListing);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.FiltersListing,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Deep", "callgetAllServiceList M response:" + response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                String message = jsonobj.getString("message");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    JSONArray jaData = jsonobj.getJSONArray("data");
                                    for (int i = 0; i < jaData.length(); i++) {
                                        JSONObject jo0 = jaData.getJSONObject(i);
                                        String category_name = jo0.getString("category_name");
                                        JSONArray jaSub = jo0.getJSONArray("Subcategories");
                                        for (int j = 0; j < jaSub.length(); j++) {
                                            JSONObject joInner = jaSub.getJSONObject(j);
                                            String sub_category_id = joInner.getString("sub_category_id");
                                            String sub_category_name = joInner.getString("sub_category_name");
                                            if (i == 0) {
                                                int isAdded = 0;
                                                if (joHair.has("category_id")) {
                                                    for (int k = 0; k < joHair.getJSONArray("sub_services").length(); k++) {
                                                        JSONObject jo = joHair.getJSONArray("sub_services").getJSONObject(k);
                                                        if (jo.getString("service_name").equalsIgnoreCase(sub_category_name)) {
                                                            isAdded = 1;
                                                            alServiceHairTemp.add(true);
                                                            alServiceHair.add(new ServiceList(category_name, sub_category_id, sub_category_name, jo.getString("price"), jo.getString("time"), jo.getString("desc")));
                                                            break;
                                                        }
                                                    }

                                                    if (isAdded == 0) {
                                                        alServiceHairTemp.add(false);
                                                        alServiceHair.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                    } else {
                                                        isAdded = 0;
                                                    }

                                                } else {
                                                    alServiceHairTemp.add(false);
                                                    alServiceHair.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                }
                                            } else if (i == 1) {
                                                int isAdded = 0;
                                                if (joBeard.has("category_id")) {
                                                    for (int k = 0; k < joBeard.getJSONArray("sub_services").length(); k++) {
                                                        JSONObject jo = joBeard.getJSONArray("sub_services").getJSONObject(k);
                                                        if (jo.getString("service_name").equalsIgnoreCase(sub_category_name)) {
                                                            isAdded = 1;
                                                            alServiceBeardTemp.add(true);
                                                            alServiceBeard.add(new ServiceList(category_name, sub_category_id, sub_category_name, jo.getString("price"), jo.getString("time"), jo.getString("desc")));
                                                            break;
                                                        }
                                                    }
                                                    if (isAdded == 0) {
                                                        alServiceBeardTemp.add(false);
                                                        alServiceBeard.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                    } else {
                                                        isAdded = 0;
                                                    }
                                                } else {
                                                    alServiceBeardTemp.add(false);
                                                    alServiceBeard.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                }
                                            } else if (i == 2) {
                                                int isAdded = 0;
                                                if (joPamper.has("category_id")) {
                                                    for (int k = 0; k < joPamper.getJSONArray("sub_services").length(); k++) {
                                                        JSONObject jo = joPamper.getJSONArray("sub_services").getJSONObject(k);
                                                        if (jo.getString("service_name").equalsIgnoreCase(sub_category_name)) {
                                                            isAdded = 1;
                                                            alServicePamperTemp.add(true);
                                                            alServicePamper.add(new ServiceList(category_name, sub_category_id, sub_category_name, jo.getString("price"), jo.getString("time"), jo.getString("desc")));
                                                            break;
                                                        }
                                                    }
                                                    if (isAdded == 0) {
                                                        alServicePamperTemp.add(false);
                                                        alServicePamper.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                    } else {
                                                        isAdded = 0;
                                                    }

                                                } else {
                                                    alServicePamperTemp.add(false);
                                                    alServicePamper.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                }
                                            } else if (i == 3) {
                                                if (joMiscellnous.has("category_id")) {
                                                    int isAdded = 0;

                                                    for (int k = 0; k < joMiscellnous.getJSONArray("sub_services").length(); k++) {
                                                        JSONObject jo = joMiscellnous.getJSONArray("sub_services").getJSONObject(k);
                                                        if (jo.getString("service_name").equalsIgnoreCase(sub_category_name)) {
                                                            isAdded = 1;
                                                            alServiceMecellnousTemp.add(true);
                                                            alServiceMecellnous.add(new ServiceList(category_name, sub_category_id, sub_category_name, jo.getString("price"), jo.getString("time"), jo.getString("desc")));
                                                            break;
                                                        }
                                                    }

                                                    if (isAdded == 0) {
                                                        alServiceMecellnousTemp.add(false);
                                                        alServiceMecellnous.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                    } else {
                                                        isAdded = 0;
                                                    }
                                                } else {
                                                    alServiceMecellnousTemp.add(false);
                                                    alServiceMecellnous.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                }
                                            } else if (i == 4) {
                                                if (joMobile.has("category_id")) {
                                                    int isAdded = 0;

                                                    for (int k = 0; k < joMobile.getJSONArray("sub_services").length(); k++) {
                                                        JSONObject jo = joMobile.getJSONArray("sub_services").getJSONObject(k);
                                                        if (jo.getString("service_name").equalsIgnoreCase(sub_category_name)) {
                                                            isAdded = 1;
                                                            alServiceMobileTemp.add(true);
                                                            alServiceMobile.add(new ServiceList(category_name, sub_category_id, sub_category_name, jo.getString("price"), jo.getString("time"), jo.getString("desc")));
                                                            break;
                                                        }
                                                    }

                                                    if (isAdded == 0) {
                                                        alServiceMobileTemp.add(false);
                                                        alServiceMobile.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                    } else {
                                                        isAdded = 0;
                                                    }
                                                } else {
                                                    alServiceMobileTemp.add(false);
                                                    alServiceMobile.add(new ServiceList(category_name, sub_category_id, sub_category_name, "", "", ""));
                                                }

                                            }
                                        }
                                    }



                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.HORIZONTAL, false);
                                    rvHairService.setLayoutManager(gridLayoutManager);
                                    adapterHair = new PickHairCutServiceRecyclerAdapter(PickServiceActivity.this, alServiceHair, alServiceHairTemp, new PickHairCutServiceRecyclerAdapter.HairServiceListner() {
                                        @Override
                                        public void onClick(int position) {
                                            if (alServiceHairTemp.get(position)) {
                                                alServiceHairTemp.set(position, false);
                                                adapterHair.notifyDataSetChanged();
                                            } else {
                                                alServiceHairTemp.set(position, true);
                                                adapterHair.notifyDataSetChanged();
                                            }
                                        }

                                });
                                rvHairService.setAdapter(adapterHair);
                                //    setWidthOfRecylerviewToShowAll(rvHairService,alServiceHair);
                                //............................Second RecyclerView

                                GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.HORIZONTAL, false);
                                rvBeardService.setLayoutManager(gridLayoutManager1);
                                adapterBeard = new PickBeardServiceRecyclerAdapter(PickServiceActivity.this, alServiceBeard, alServiceBeardTemp, new PickBeardServiceRecyclerAdapter.PickBreadServiceListner() {
                                    @Override
                                    public void onClick(int position) {
                                        if (alServiceBeardTemp.get(position)) {
                                            alServiceBeardTemp.set(position, false);
                                            adapterBeard.notifyDataSetChanged();
                                        } else {
                                            alServiceBeardTemp.set(position, true);
                                            adapterBeard.notifyDataSetChanged();
                                        }
                                    }
                                });
                                rvBeardService.setAdapter(adapterBeard);



                                //............................Third RecyclerView

                                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.HORIZONTAL, false);
                                rvPamperService.setLayoutManager(gridLayoutManager2);
                                adapterPamper = new PickPamertreatmentServiceRecyclerAdapter(PickServiceActivity.this, alServicePamper, alServicePamperTemp, new PickPamertreatmentServiceRecyclerAdapter.PickPamertreatmentServiceListener() {
                                    @Override
                                    public void onClick(int position) {
                                        if (alServicePamperTemp.get(position)) {
                                            alServicePamperTemp.set(position, false);
                                            adapterPamper.notifyDataSetChanged();
                                        } else {
                                            alServicePamperTemp.set(position, true);
                                            adapterPamper.notifyDataSetChanged();
                                        }

                                    }
                                });
                                rvPamperService.setAdapter(adapterPamper);
                                //      setWidthOfRecylerviewToShowAll(rvPamperService,alServicePamper);

                                //............................Fourth RecyclerView

                                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.HORIZONTAL, false);
                                rvMecellnous.setLayoutManager(gridLayoutManager3);
                                adapterMiscellnous = new PickMiscellnousServiceRecyclerAdapter(PickServiceActivity.this, alServiceMecellnous, alServiceMecellnousTemp, new PickMiscellnousServiceRecyclerAdapter.PickMiscellnousServiceListner() {
                                    @Override
                                    public void onClick(int position) {
                                        if (alServiceMecellnousTemp.get(position)) {
                                            alServiceMecellnousTemp.set(position, false);
                                            adapterMiscellnous.notifyDataSetChanged();
                                        } else {
                                            alServiceMecellnousTemp.set(position, true);
                                            adapterMiscellnous.notifyDataSetChanged();
                                        }
                                    }
                                });
                                rvMecellnous.setAdapter(adapterMiscellnous);
                                //   setWidthOfRecylerviewToShowAll(rvMecellnous,alServiceMecellnous);


                                GridLayoutManager gridLayoutManager4 = new GridLayoutManager(getApplicationContext(), 4, LinearLayoutManager.HORIZONTAL, false);
                                rvMobile.setLayoutManager(gridLayoutManager4);
                                adapterMobile = new PickMobileServiceRecyclerAdapter(PickServiceActivity.this, alServiceMobile, alServiceMobileTemp, new PickMobileServiceRecyclerAdapter.PickMobileServiceListner() {
                                    @Override
                                    public void onClick(int position) {
                                        if (alServiceMobileTemp.get(position)) {
                                            alServiceMobileTemp.set(position, false);
                                            adapterMobile.notifyDataSetChanged();
                                        } else {
                                            alServiceMobileTemp.set(position, true);
                                            adapterMobile.notifyDataSetChanged();
                                        }
                                    }
                                });
                                rvMobile.setAdapter(adapterMobile);


                            } else{

                            }
                        } catch(
                        JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                Constants.dismissProgress();
                Constants.showPopupServer(activity);
            }
        }){
            @Override
            public Map<String, String> getHeaders () throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //panesar@gmail.com
                //123456
                headers.put("authenticate", prefs.getString("token_value", "35f5df37-d3c9-4ed9-ad77-68cc58db4088"));
                headers.put("user_id", prefs.getString("user_id", "28"));



                return headers;
            }

        } ;
        req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
        AppController.getInstance().addToRequestQueue(req, "LOGIN");
    } else

    {
        Constants.showPopupInternet(activity);
    }

}

    private void setWidthOfRecylerviewToShowAll(final RecyclerView recylerview, ArrayList list) {
        {


            final int items = list.size();


            cellWidth = getWidthRatio(485, PickServiceActivity.this); // this will give you cell width dynamically
            int dividend = items / 4;
            dividend++;

            int totalHeight = cellWidth * dividend;


            ViewGroup.LayoutParams params = recylerview.getLayoutParams();
            params.width = totalHeight;
            recylerview.setLayoutParams(params);


        }
    }


    public void WidthOfAdpterItem(int cellWidthi) {
        final int items = alServiceHair.size();


        cellWidth = cellWidthi;// this will give you cell width dynamically
        int dividend = items / 4;
        dividend++;

        int totalHeight = cellWidth * dividend;


        ViewGroup.LayoutParams params = rvHairService.getLayoutParams();
        params.width = totalHeight;
        rvHairService.setLayoutParams(params);
    }

    public void WidthOfAdpterItemBeaRD(int cellWidthi) {
        final int items = alServiceBeard.size();


        cellWidth = cellWidthi;// this will give you cell width dynamically
        int dividend = items / 4;
        dividend++;

        int totalHeight = cellWidth * dividend;


        ViewGroup.LayoutParams params = rvBeardService.getLayoutParams();
        params.width = totalHeight;
        rvBeardService.setLayoutParams(params);
    }

    public void WidthOfAdpterItemParmertment(int cellWidthi) {
        final int items = alServicePamper.size();


        cellWidth = cellWidthi;// this will give you cell width dynamically
        int dividend = items / 4;
        dividend++;

        int totalHeight = cellWidth * dividend;


        ViewGroup.LayoutParams params = rvPamperService.getLayoutParams();
        params.width = totalHeight;
        rvPamperService.setLayoutParams(params);
    }

    public void WidthOfAdpterItemMiscellnous(int cellWidthi) {
        final int items = alServiceMecellnous.size();


        cellWidth = cellWidthi;// this will give you cell width dynamically
        int dividend = items / 4;
        dividend++;

        int totalHeight = cellWidth * dividend;


        ViewGroup.LayoutParams params = rvMecellnous.getLayoutParams();
        params.width = totalHeight;
        rvMecellnous.setLayoutParams(params);
    }
}
