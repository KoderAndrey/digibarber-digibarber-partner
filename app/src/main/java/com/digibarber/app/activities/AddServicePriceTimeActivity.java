package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.CustomAdapters.AddServiceTimePriceExapandbleAdapter;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.Interfaces.SelectServiceExapndblePosition;
import com.digibarber.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddServicePriceTimeActivity extends BaseActivity implements SelectServiceExapndblePosition {
    ImageView tv_done;

    ExpandableListView expandableListView;
    AddServiceTimePriceExapandbleAdapter expandableListAdapter;
    private ImageView back_icon;
    private List<String> expandableListTitle = new ArrayList<>();
    JSONObject joHair = new JSONObject();
    JSONObject joBeard = new JSONObject();
    JSONObject joPamper = new JSONObject();
    JSONObject joMiscellnous = new JSONObject();
    JSONObject joMobile = new JSONObject();
    HashMap<String, List<ServicePriceTime>> expandableListDetail = new HashMap<String, List<ServicePriceTime>>();
    HashMap<String, List<Boolean>> expandableListDetailState = new HashMap<String, List<Boolean>>();
    List<ServicePriceTime> hairCut = new ArrayList<ServicePriceTime>();
    List<ServicePriceTime> beardServices = new ArrayList<ServicePriceTime>();
    List<ServicePriceTime> pamPer = new ArrayList<ServicePriceTime>();
    List<ServicePriceTime> miscellaneous = new ArrayList<ServicePriceTime>();
    List<ServicePriceTime> mobile = new ArrayList<ServicePriceTime>();
    List<Boolean> hairCutState = new ArrayList<Boolean>();
    List<Boolean> beardServicesState = new ArrayList<Boolean>();
    List<Boolean> pamPerState = new ArrayList<Boolean>();
    List<Boolean> miscellaneousState = new ArrayList<Boolean>();
    List<Boolean> mobileState = new ArrayList<Boolean>();
    private String From = "";
    TextView tv_skip;
//    private String Latitude;
//    private String Longitude;
//    private String address;
//    private String workplace;
//    private String postcode;
//    private String firstname;
//    private String lastName;
//    private String email;
//    private String password;
//    private String phone;


    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_add_service_price_time);

        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                joHair = new JSONObject(bd.getString("joHair"));
                joBeard = new JSONObject(bd.getString("joBeard"));
                joPamper = new JSONObject(bd.getString("joPamper"));
                joMiscellnous = new JSONObject(bd.getString("joMiscellaneous"));
                joMobile = new JSONObject(bd.getString("joMobile"));
                From = bd.getString("From");
//                Latitude = bd.getString("Latitude");
//                Longitude = bd.getString("Longitude");
//                workplace = bd.getString("workplace");
//                address = bd.getString("address");
//                postcode = bd.getString("postcode");
//                firstname = bd.getString("first_name");
//                lastName = bd.getString("last_name");
//                email = bd.getString("email");
//                password = bd.getString("password");
//                phone = bd.getString("phone");

            }
            for (int i = 0; i < joHair.getJSONArray("sub_services").length(); i++) {
                JSONObject jo = joHair.getJSONArray("sub_services").getJSONObject(i);
                hairCut.add(new ServicePriceTime(joHair.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                hairCutState.add(false);
            }
            if (hairCut.size() > 0) {
                expandableListTitle.add("Hair Cut");
                expandableListDetail.put("Hair Cut", hairCut);
                expandableListDetailState.put("Hair Cut", hairCutState);
            }

            for (int i = 0; i < joBeard.getJSONArray("sub_services").length(); i++) {
                JSONObject jo = joBeard.getJSONArray("sub_services").getJSONObject(i);
                beardServices.add(new ServicePriceTime(joBeard.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                beardServicesState.add(false);
            }
            if (beardServices.size() > 0) {
                expandableListTitle.add("Beard Services");
                expandableListDetail.put("Beard Services", beardServices);
                expandableListDetailState.put("Beard Services", beardServicesState);
            }

            for (int i = 0; i < joPamper.getJSONArray("sub_services").length(); i++) {
                JSONObject jo = joPamper.getJSONArray("sub_services").getJSONObject(i);
                pamPer.add(new ServicePriceTime(joPamper.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                pamPerState.add(false);
            }
            if (pamPer.size() > 0) {
                expandableListTitle.add("Pamper Treatment");
                expandableListDetail.put("Pamper Treatment", pamPer);
                expandableListDetailState.put("Pamper Treatment", pamPerState);
            }

            for (int i = 0; i < joMiscellnous.getJSONArray("sub_services").length(); i++) {
                JSONObject jo = joMiscellnous.getJSONArray("sub_services").getJSONObject(i);
                miscellaneous.add(new ServicePriceTime(joMiscellnous.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "false"));
                miscellaneousState.add(false);
            }
            if (miscellaneous.size() > 0) {
                expandableListTitle.add("Miscellaneous");
                expandableListDetail.put("Miscellaneous", miscellaneous);
                expandableListDetailState.put("Miscellaneous", miscellaneousState);
            }

            for (int i = 0; i < joMobile.getJSONArray("sub_services").length(); i++) {
                JSONObject jo = joMobile.getJSONArray("sub_services").getJSONObject(i);
                mobile.add(new ServicePriceTime(joMobile.getString("category_name"), jo.getString("sub_category_id"), jo.getString("service_name"), jo.getString("price"), jo.getString("time"), jo.getString("desc"), "true"));
                mobileState.add(false);
            }
            if (mobile.size() > 0) {
                expandableListTitle.add("Mobile services");
                expandableListDetail.put("Mobile services", mobile);
                expandableListDetailState.put("Mobile services", mobileState);
            }


        } catch (JSONException | NullPointerException ignored) {
        }


        tv_done = findViewById(R.id.tv_done);
        tv_skip = findViewById(R.id.tv_skip);
        back_icon = findViewById(R.id.back_icon);
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        expandableListAdapter = new AddServiceTimePriceExapandbleAdapter(this, expandableListTitle, expandableListDetail, this, expandableListDetailState, From, "false");
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long l) {

                ImageView grupIndictr = view.findViewById(R.id.iv_expand);

                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    grupIndictr.setImageResource(R.drawable.group_plus);
                } else {
                    parent.expandGroup(groupPosition);
                    grupIndictr.setImageResource(R.drawable.group_minus);
                }
                return true;
            }
        });


        if (From != null && From.equalsIgnoreCase("Signup")) {
            tv_skip.setVisibility(View.VISIBLE);
            back_icon.setVisibility(View.GONE);
        } else {
            tv_skip.setVisibility(View.GONE);
            back_icon.setVisibility(View.VISIBLE);
        }

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(AddServicePriceTimeActivity.this, AddOpenHoursActivity.class);
                startActivity(it);
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    expandableListAdapter.notfiyAdapter();
                    expandableListAdapter.notifyDataSetChanged();

                    if (!expandableListAdapter.isEmpty()) {
                        Log.e("PrintAble", "false");
                        expandableListAdapter.clikedNext();
                    } else {

                        for (int i = 0; i < hairCut.size(); i++) {
                            joHair.getJSONArray("sub_services").getJSONObject(i).put("price", hairCut.get(i).subPrice);
                            joHair.getJSONArray("sub_services").getJSONObject(i).put("time", hairCut.get(i).subTime);
                            joHair.getJSONArray("sub_services").getJSONObject(i).put("desc", hairCut.get(i).subDes);
                        }
                        for (int i = 0; i < beardServices.size(); i++) {
                            joBeard.getJSONArray("sub_services").getJSONObject(i).put("price", beardServices.get(i).subPrice);
                            joBeard.getJSONArray("sub_services").getJSONObject(i).put("time", beardServices.get(i).subTime);
                            joBeard.getJSONArray("sub_services").getJSONObject(i).put("desc", beardServices.get(i).subDes);
                        }
                        for (int i = 0; i < pamPer.size(); i++) {
                            joPamper.getJSONArray("sub_services").getJSONObject(i).put("price", pamPer.get(i).subPrice);
                            joPamper.getJSONArray("sub_services").getJSONObject(i).put("time", pamPer.get(i).subTime);
                            joPamper.getJSONArray("sub_services").getJSONObject(i).put("desc", pamPer.get(i).subDes);
                        }
                        for (int i = 0; i < miscellaneous.size(); i++) {
                            joMiscellnous.getJSONArray("sub_services").getJSONObject(i).put("price", miscellaneous.get(i).subPrice);
                            joMiscellnous.getJSONArray("sub_services").getJSONObject(i).put("time", miscellaneous.get(i).subTime);
                            joMiscellnous.getJSONArray("sub_services").getJSONObject(i).put("desc", miscellaneous.get(i).subDes);
                        }
                        for (int i = 0; i < mobile.size(); i++) {
                            joMobile.getJSONArray("sub_services").getJSONObject(i).put("price", mobile.get(i).subPrice);
                            joMobile.getJSONArray("sub_services").getJSONObject(i).put("time", mobile.get(i).subTime);
                            joMobile.getJSONArray("sub_services").getJSONObject(i).put("desc", mobile.get(i).subDes);
                        }
                        if (From != null && From.equalsIgnoreCase("ServiceDetail")) {
                            Intent it = new Intent();
                            it.putExtra("joHair", joHair.toString());
                            it.putExtra("joBeard", joBeard.toString());
                            it.putExtra("joPamper", joPamper.toString());
                            it.putExtra("joMiscellaneous", joMiscellnous.toString());
                            it.putExtra("joMobile", joMobile.toString());
                            setResult(RESULT_OK, it);
                            finish();
                        } else if (From != null && From.equalsIgnoreCase("EditProfile")) {
                            Intent it = new Intent(AddServicePriceTimeActivity.this, ServiceListActivity.class);
                            it.putExtra("joHair", joHair.toString());
                            it.putExtra("joBeard", joBeard.toString());
                            it.putExtra("joPamper", joPamper.toString());
                            it.putExtra("joMiscellaneous", joMiscellnous.toString());
                            it.putExtra("joMobile", joMobile.toString());
                            it.putExtra("From", "EditProfile");
                            startActivity(it);
                            finish();
                        } else {
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "add_service_price_time").apply();
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_HAIR, joHair.toString()).apply();
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_BEARD, joBeard.toString()).apply();
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_PAMPER, joPamper.toString()).apply();
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_MISCELL, joMiscellnous.toString()).apply();
                            prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_S_MOBILE, joMobile.toString()).apply();
                            Intent it = new Intent(AddServicePriceTimeActivity.this, ServiceListActivity.class);
                            it.putExtra("joHair", joHair.toString());
                            it.putExtra("joBeard", joBeard.toString());
                            it.putExtra("joPamper", joPamper.toString());
                            it.putExtra("joMiscellaneous", joMiscellnous.toString());
                            it.putExtra("joMobile", joMobile.toString());
                            it.putExtra("From", "Signup");
                            startActivity(it);
                            finish();
                        }
                        Log.e("Print joHair", "" + joHair);
                        Log.e("Print joBeard", "" + joBeard);
                        Log.e("Print joPamper", "" + joPamper);
                        Log.e("Print joMiscellaneous", "" + joMiscellnous);
                        Log.e("Print joMiscellaneous", "" + joMiscellnous);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (From != null && From.equalsIgnoreCase("EditProfile")) {
                    finish();
                } else if (From != null && From.equalsIgnoreCase("ServiceDetail")) {
                    finish();
                } else {
                    Intent it = new Intent(AddServicePriceTimeActivity.this, PickServiceActivity.class);
                    it.putExtra("joHair", joHair.toString());
                    it.putExtra("joBeard", joBeard.toString());
                    it.putExtra("joPamper", joPamper.toString());
                    it.putExtra("joMiscellaneous", joMiscellnous.toString());
                    it.putExtra("joMobile", joMobile.toString());
                    startActivity(it);
                    finish();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    String des = data.getStringExtra("des");
                    String strGroupPos = data.getStringExtra("groupPos");
                    String strChildPos = data.getStringExtra("childPos");

                    int groupPos = Integer.parseInt(strGroupPos);
                    int chilPos = Integer.parseInt(strChildPos);

                    String catName = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).catName;
                    String subName = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subName;
                    String sub_category_id = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).sub_category_id;
                    String catPrice = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subPrice;
                    String catTime = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).subTime;
                    String mobile = expandableListDetail.get(expandableListTitle.get(groupPos)).get(chilPos).mobile;
                    expandableListDetail.get(expandableListTitle.get(groupPos)).set(chilPos, new ServicePriceTime(catName, sub_category_id, subName, catPrice, catTime, des, mobile));
                    expandableListAdapter.notifyDataSetChanged();
                }
            }

        }

    }

    private void showNoDescription() {
        final Dialog dialog_first = new Dialog(AddServicePriceTimeActivity.this);
        // setting custom layout to dialog
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_no_description);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView tv_cancel = (TextView) dialog_first.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_first.dismiss();

            }
        });
        dialog_first.show();

    }


    @Override
    public void onDesSelcted(int groupPos, int childPos) {

        if (false) {
            showNoDescription();


        } else {
            Intent it = new Intent(AddServicePriceTimeActivity.this, AddServiceDescriptionActivity.class);
            it.putExtra("groupPos", "" + groupPos);
            it.putExtra("childPos", "" + childPos);
            String subDes = "";
            if (groupPos == 0) {

                if (expandableListDetail.get("Hair Cut") != null && expandableListDetail.get("Hair Cut").size() > childPos)
                    subDes = expandableListDetail.get("Hair Cut").get(childPos).subDes;
            } else if (groupPos == 1) {
                if (expandableListDetail.get("Beard Services") != null && expandableListDetail.get("Beard Services").size() > childPos)

                    subDes = expandableListDetail.get("Beard Services").get(childPos).subDes;
            } else if (groupPos == 2) {
                if (expandableListDetail.get("Pamper Treatment") != null && expandableListDetail.get("Pamper Treatment").size() > childPos)

                    subDes = expandableListDetail.get("Pamper Treatment").get(childPos).subDes;
            } else if (groupPos == 3) {
                if (expandableListDetail.get("Miscellaneous") != null && expandableListDetail.get("Miscellaneous").size() > childPos)

                    subDes = expandableListDetail.get("Miscellaneous").get(childPos).subDes;
            } else if (groupPos == 4) {
                if (expandableListDetail.get("Mobile services") != null && expandableListDetail.get("Mobile services").size() > childPos)

                    subDes = expandableListDetail.get("Mobile services").get(childPos).subDes;
            }
            it.putExtra("des", subDes);
            startActivityForResult(it, 1);
        }
    }
}
