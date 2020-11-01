package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.Beans.ServicePriceTime;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceDetailActivity extends BaseActivity {

    private TextView tv_cat_name;
    private TextView tv_service_name;
    private TextView tv_price;
    private TextView tv_desc;
    private TextView tv_time;
    private ImageView iv_edit_price;
    private ImageView iv_des_price;
    private ImageView iv_srivce_time;
    private ImageView back_icon;
    private ImageView tv_done;

    private ServicePriceTime objServicePriceTime;

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
    private int Position;
    private JSONObject newJoObject = new JSONObject();


    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_service_detail);


        bd = getIntent().getExtras();
        if (bd != null) {
            objServicePriceTime = (ServicePriceTime) bd.getSerializable("ServiceDetail");
            Position = bd.getInt("Position");

        }
        back_icon = (ImageView) findViewById(R.id.back_icon);
        tv_cat_name = (TextView) findViewById(R.id.tv_cat_name);
        tv_service_name = (TextView) findViewById(R.id.tv_service_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_edit_price = (ImageView) findViewById(R.id.iv_edit_price);
        iv_des_price = (ImageView) findViewById(R.id.iv_des_price);
        tv_done = (ImageView) findViewById(R.id.tv_done);
        iv_srivce_time = (ImageView) findViewById(R.id.iv_srivce_time);
        iv_edit_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ServiceDetailActivity.this, AddServicePriceTimeActivity.class);
                it.putExtra("joHair", joHair.toString());
                it.putExtra("joBeard", joBeard.toString());
                it.putExtra("joPamper", joPamper.toString());
                it.putExtra("mobile", joMobile.toString());
                it.putExtra("joMiscellaneous", joMiscellnous.toString());
                it.putExtra("From", "ServiceDetail");

                startActivityForResult(it, 2);
            }
        });
        iv_des_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (false) {


                    showNoDescription();

                } else {
                    Intent it = new Intent(ServiceDetailActivity.this, AddServiceDescriptionActivity.class);
                    it.putExtra("des", objServicePriceTime.subDes);
                    startActivityForResult(it, 1);
                }
            }
        });
        iv_srivce_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ServiceDetailActivity.this, AddServicePriceTimeActivity.class);
                it.putExtra("joHair", joHair.toString());
                it.putExtra("joBeard", joBeard.toString());
                it.putExtra("joPamper", joPamper.toString());
                it.putExtra("joMiscellaneous", joMiscellnous.toString());
                it.putExtra("mobile", joMobile.toString());
                it.putExtra("From", "ServiceDetail");
                startActivityForResult(it, 2);
            }
        });
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.putExtra("newJoObject", newJoObject.toString());
                it.putExtra("Position", Position);
                setResult(RESULT_OK, it);
                finish();

            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setValues();

    }

    private void showNoDescription() {
        final Dialog dialog_first = new Dialog(ServiceDetailActivity.this);
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

    private void setValues() {
        tv_cat_name.setText(objServicePriceTime.catName);
        tv_service_name.setText(objServicePriceTime.subName);
        tv_price.setText("£" + objServicePriceTime.subPrice);
        tv_desc.setText(objServicePriceTime.subDes);
        tv_time.setText(objServicePriceTime.subTime + " min");
        try {
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
                joPamper.put("category_id", "1");
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
                joMiscellnous.put("category_id", "1");
                joMiscellnous.put("category_name", objServicePriceTime.catName);
                joMiscellnous.put("sub_services", jaSubMiscellnous);
            } else if (objServicePriceTime.catName.equalsIgnoreCase("Mobile services")) {
                JSONObject joInner = new JSONObject();
                joInner.put("sub_category_id", "" + objServicePriceTime.sub_category_id);
                joInner.put("service_name", objServicePriceTime.subName);
                joInner.put("price", objServicePriceTime.subPrice);
                joInner.put("time", objServicePriceTime.subTime);
                joInner.put("desc", objServicePriceTime.subDes);
                jaSubMobile.put(joInner);
                joMobile.put("category_id", "1");
                joMobile.put("category_name", objServicePriceTime.catName);
                joMobile.put("sub_services", jaSubMiscellnous);
            } else {

            }
        } catch (JSONException e) {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == 2) {

                    if (data != null) {
                        String strjoHair = data.getStringExtra("joHair");
                        String strjoBeard = data.getStringExtra("joBeard");
                        String strjoPamper = data.getStringExtra("joPamper");
                        String strjoMiscellnous = data.getStringExtra("joMiscellaneous");

                        joHair = new JSONObject(strjoHair);
                        joBeard = new JSONObject(strjoBeard);
                        joPamper = new JSONObject(strjoPamper);
                        joMiscellnous = new JSONObject(strjoMiscellnous);
                        if (joHair != null && joHair.length() > 0) {
                            SetNewValues(joHair);
                        } else if (joBeard != null && joBeard.length() > 0) {
                            SetNewValues(joBeard);
                        } else if (joPamper != null && joPamper.length() > 0) {
                            SetNewValues(joPamper);
                        } else if (joMiscellnous != null && joMiscellnous.length() > 0) {
                            SetNewValues(joMiscellnous);
                        }

                    }
                } else if (requestCode == 1) {
                    String des = data.getStringExtra("des");
                    if (joHair != null && joHair.length() > 0) {
                        joHair.getJSONArray("sub_services").getJSONObject(0).put("desc", des);
                        SetNewValues(joHair);
                    } else if (joBeard != null && joBeard.length() > 0) {
                        joBeard.getJSONArray("sub_services").getJSONObject(0).put("desc", des);
                        SetNewValues(joBeard);
                    } else if (joPamper != null && joPamper.length() > 0) {
                        joPamper.getJSONArray("sub_services").getJSONObject(0).put("desc", des);
                        SetNewValues(joPamper);
                    } else if (joMiscellnous != null && joMiscellnous.length() > 0) {
                        joMiscellnous.getJSONArray("sub_services").getJSONObject(0).put("desc", des);
                        SetNewValues(joMiscellnous);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }


    private void SetNewValues(JSONObject joHair) {
        try {
            newJoObject = joHair;
            JSONArray ja = joHair.getJSONArray("sub_services");
            String category_name = joHair.getString("category_name");
            if (ja.length() > 0) {
                String service_name = ja.getJSONObject(0).getString("service_name");
                String price = ja.getJSONObject(0).getString("price");
                String time = ja.getJSONObject(0).getString("time");
                String desc = ja.getJSONObject(0).getString("desc");
                tv_cat_name.setText(category_name);
                tv_service_name.setText(service_name);
                tv_price.setText("£" + price);
                tv_desc.setText(desc);
                tv_time.setText(time + " min");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
