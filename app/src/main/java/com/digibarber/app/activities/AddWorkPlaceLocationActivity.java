package com.digibarber.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.view.View.VISIBLE;

public class AddWorkPlaceLocationActivity extends BaseActivity implements WheelPicker.OnItemSelectedListener {
    private EditText et_post_code;
    private EditText et_place_work;
    private ImageView tv_done;
    private EditText et_text_selected;
    private ImageView back_icon;
    // WheelPicker wv_picker;
    ArrayList<String> address = new ArrayList<>();
    private String Latitude = null;
    private String Longitude = null;
    public String workplace = null;
    private String addres = null;
    private String From = "addWorkPlaceAddress";
    private TextView tv_please_enter_shop_name;
    private TextView tv_please_enter_post_code;
    private LinearLayout ll_postcode_back;
    private TextView tv_please_enter_address;
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_add_work_place_location);

        try {
            bd = getIntent().getExtras();
            if (bd != null) {
                firstname = bd.getString("first_name");
                lastName = bd.getString("last_name");
                email = bd.getString("email");
                password = bd.getString("password");
                phone = bd.getString("phone");
                From = bd.getString("From");
                workplace = bd.getString("workplace");
                addres = bd.getString("address");


                if (workplace != null) {
                    et_place_work.setText(workplace);
                }

                if (addres != null) {
                    et_text_selected.setText(addres);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ll_postcode_back = (LinearLayout) findViewById(R.id.ll_postcode_back);
        et_post_code = (EditText) findViewById(R.id.et_post_code);
        tv_done = (ImageView) findViewById(R.id.tv_done);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        et_place_work = (EditText) findViewById(R.id.et_place_work);
        et_text_selected = (EditText) findViewById(R.id.et_text_selected);
        tv_please_enter_shop_name = (TextView) findViewById(R.id.tv_please_enter_shop_name);
        tv_please_enter_post_code = (TextView) findViewById(R.id.tv_please_enter_post_code);
        tv_please_enter_address = (TextView) findViewById(R.id.tv_please_enter_address);
        et_place_work.addTextChangedListener(new CustomTextWatcher(et_place_work, tv_please_enter_shop_name, ll_postcode_back));
        et_post_code.addTextChangedListener(new CustomTextWatcher(et_post_code, tv_please_enter_post_code, ll_postcode_back));
        et_text_selected.addTextChangedListener(new CustomTextWatcher(et_text_selected, tv_please_enter_address, ll_postcode_back));


        if (From != null && From.equals("EditPrfoile")) {

            tv_done.setImageResource(R.mipmap.next);

        } else {
            tv_done.setImageResource(R.mipmap.next);
        }

        et_place_work.setImeActionLabel("Done", EditorInfo.IME_ACTION_GO);


        et_place_work.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                et_place_work.setCursorVisible(true);
                Constants.showKeyboard(AddWorkPlaceLocationActivity.this, et_place_work);
                return false;
            }
        });

        et_post_code.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                et_post_code.setCursorVisible(true);
                Constants.showKeyboard(AddWorkPlaceLocationActivity.this, et_post_code);
                return false;
            }
        });

        et_place_work.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == 66) {
                    String etworkplace = et_place_work.getText().toString();
                    if (etworkplace.isEmpty()) {
                        et_place_work.setBackgroundResource(R.mipmap.location_error);
                        tv_please_enter_shop_name.setVisibility(VISIBLE);
                    } else {
                        et_place_work.setBackgroundResource(R.mipmap.icon_address);
                        tv_please_enter_shop_name.setVisibility(View.GONE);
                        //    et_shop_no.requestFocus();
                    }

                }
                return false;
            }
        });
        et_post_code.setImeActionLabel("Done", EditorInfo.IME_ACTION_GO);
        et_post_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == keyEvent.ACTION_DOWN) {
                    // et_shop_no.setText("");
                    //et_text_selected.setText("");
                }
                return false;
            }
        });

        et_post_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                String placeWork = et_place_work.getText().toString();
                String postCode = et_post_code.getText().toString();
                String Postcode = postCode;
                Postcode = Postcode.replace(" ", "");
                //    String shop_no = et_shop_no.getText().toString();
                if (Postcode.length() > 0) {
                    ll_postcode_back.setBackgroundResource(R.mipmap.icon_address);
                    tv_please_enter_post_code.setVisibility(View.GONE);
                    getAddressList(Postcode);
                } else if (Postcode.length() > 0) {
                    ll_postcode_back.setBackgroundResource(R.mipmap.icon_address);
                    tv_please_enter_post_code.setVisibility(View.GONE);
                    getAddressList(Postcode);
                } else if (Postcode.length() <= 0) {
                    ll_postcode_back.setBackgroundResource(R.mipmap.location_error);
                    tv_please_enter_post_code.setVisibility(VISIBLE);
                    // ZoomConst.showToast(AddWorkPlaceLocationActivity.this, "Please enter Post code.");
                } else if (placeWork.length() <= 0) {
                    et_place_work.setBackgroundResource(R.mipmap.location_error);
                    tv_please_enter_shop_name.setVisibility(VISIBLE);
                }
                Constants.hideSoftKeyboard(AddWorkPlaceLocationActivity.this);
                return true;
            }
        });
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (From.equalsIgnoreCase("EditPrfoile")) {
                    Intent it = new Intent(AddWorkPlaceLocationActivity.this, ChangePrivateInformationActivity.class);
                    startActivity(it);
                } else {
                    Intent it = new Intent(AddWorkPlaceLocationActivity.this, CallSignupActivity.class);
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
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String placeWork = et_place_work.getText().toString();
                String Postcode = et_post_code.getText().toString();
                //  String shop_no = et_shop_no.getText().toString();
                if (placeWork.length() <= 0) {
                    et_place_work.setBackgroundResource(R.mipmap.location_error);
                    tv_please_enter_shop_name.setVisibility(VISIBLE);
                } else if (Postcode.length() <= 0) {
                    ll_postcode_back.setBackgroundResource(R.mipmap.location_error);
                    tv_please_enter_post_code.setVisibility(VISIBLE);
                } else if (et_text_selected.getText().toString().trim().length() <= 0) {
                    et_text_selected.setBackgroundResource(R.mipmap.location_error);
                    tv_please_enter_address.setVisibility(VISIBLE);
                } else {
                    ll_postcode_back.setBackgroundResource(R.mipmap.icon_address);
                    tv_please_enter_post_code.setVisibility(View.GONE);
                    String selectedAddress = et_text_selected.getText().toString();
                    selectedAddress = selectedAddress.trim();

                    if (selectedAddress.endsWith(",")) {
                        selectedAddress = selectedAddress.substring(0, selectedAddress.length() - 1);
                    }

                    callCheckAddress(Latitude, Longitude, selectedAddress, et_post_code.getText().toString().replace(" ", "%20"), et_place_work.getText().toString());
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (From.equalsIgnoreCase("EditPrfoile")) {
            Intent it = new Intent(AddWorkPlaceLocationActivity.this, ChangePrivateInformationActivity.class);
            startActivity(it);
        } else {
            Intent it = new Intent(AddWorkPlaceLocationActivity.this, CallSignupActivity.class);
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

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;
        LinearLayout ll_postcode_back;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror, LinearLayout ll_postcode_back) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
            this.ll_postcode_back = ll_postcode_back;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == VISIBLE) {
                if (mEditText.getId() == R.id.et_text_selected) {
                    mEditText.setBackgroundResource(R.mipmap.location_text);
                    tv_errror.setVisibility(View.INVISIBLE);
                } else if (mEditText.getId() == R.id.et_post_code) {
                    tv_errror.setVisibility(View.INVISIBLE);
                    ll_postcode_back.setBackgroundResource(R.mipmap.icon_address);
                    mEditText.setTextColor(Color.parseColor("#31363B"));

                } else {
                    tv_errror.setVisibility(View.INVISIBLE);
                    mEditText.setBackgroundResource(R.mipmap.icon_address);
                }
            } else {
                if (mEditText.getId() == R.id.et_post_code) {
                    if (mEditText.getText().toString().length() <= 0) {
                        //       et_shop_no.setText("");
                    }
                }
            }
        }
    }


    protected void getAddressList(final String codeNumber) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Constants.showPorgess(AddWorkPlaceLocationActivity.this);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String temp = "Empty";
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response;
                    try {
                        String url = "https://api.getaddress.io/v2/uk/" + codeNumber;
                        HttpGet get = new HttpGet(url + "?format=true&api-key=IcAa0iyXk0OH02pmuN4WTQ8738");
                        //get.setHeader("Authorization", ZoomConst.HeaderMangoPay);
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
                    Constants.dismissProgress();
                    address = new ArrayList<>();
                    try {
                        JSONObject jo = new JSONObject(Resposne);
                        Latitude = jo.getString("Latitude");
                        Longitude = jo.getString("Longitude");
                        JSONArray jaAddresses = jo.getJSONArray("Addresses");
                        if (jaAddresses.length() > 0) {
                            for (int i = 0; i < jaAddresses.length(); i++) {
                                StringBuilder buider = new StringBuilder();
                                JSONArray jaInner = jaAddresses.getJSONArray(i);
                                for (int j = 0; j < jaInner.length(); j++) {
                                    String data = jaInner.getString(j);
                                    if (jaInner.length() - 1 == j) {
                                        buider.append(data.replace(",", ""));
                                    } else {
                                        if (!data.equalsIgnoreCase("")) {
                                            buider.append(data.replace(",", "")).append(", ");
                                        }
                                    }
                                }
                                address.add(buider.toString());
                            }
                            //  wv_picker.setVisibility(View.VISIBLE);
                            showListAddress(address);
                            //wv_picker.setData(address);
                        } else {
                            tv_please_enter_post_code.setVisibility(VISIBLE);
                            tv_please_enter_post_code.setText("That postcode is not recognised!");
                            et_post_code.setTextColor(Color.parseColor("#F94444"));
                        }
                    } catch (JSONException e) {

                        tv_please_enter_post_code.setVisibility(VISIBLE);
                        tv_please_enter_post_code.setText("That postcode is not recognised!");
                        et_post_code.setTextColor(Color.parseColor("#F94444"));
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        tv_please_enter_post_code.setVisibility(VISIBLE);
                        tv_please_enter_post_code.setText("That postcode is not recognised!");
                        et_post_code.setTextColor(Color.parseColor("#F94444"));
                    }

                    Log.e("getDefaultCardDetail", Resposne);

                }
            };
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Constants.showPopupInternet(activity);
        }
    }


    public void showAddressDilaoug(final String lat, final String lon, final String address, final String postcode,
                                   final String workplace, final String strShopName) {
        final Dialog dialog_first = new Dialog(AddWorkPlaceLocationActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.poup_check_address);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tv_addrress = (TextView) dialog_first.findViewById(R.id.tv_addrress);
        tv_addrress.setText("to " + strShopName + " is this correct?");
        ImageView iv_add_address = (ImageView) dialog_first.findViewById(R.id.iv_add_address);
        ImageView iv_dont_add = (ImageView) dialog_first.findViewById(R.id.iv_dont_add);
        iv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "add_workplace").apply();
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_LAT, lat).apply();
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_LOG, lon).apply();
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_WORK_PLACE, strShopName).apply();
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_ADDRESS, address).apply();
                prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_POSTCODE, postcode).apply();
                Intent it = new Intent(AddWorkPlaceLocationActivity.this, LocationOnMapActivity.class);
                it.putExtra("Latitude", lat);
                it.putExtra("Longitude", lon);
                it.putExtra("workplace", strShopName);
                it.putExtra("address", address);
                it.putExtra("postcode", postcode);
                it.putExtra("From", "" + From);
                it.putExtra("first_name", firstname);
                it.putExtra("last_name", lastName);
                it.putExtra("email", email);
                it.putExtra("password", password);
                it.putExtra("phone", phone);
                startActivity(it);
                if (From.equalsIgnoreCase("EditPrfoile")) {
                    finish();
                } else {
                    finish();
                }
                dialog_first.dismiss();
            }
        });

        iv_dont_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_first.dismiss();
            }
        });
        dialog_first.show();
    }


    public void showListAddress(ArrayList<String> Address) {
        final Dialog dialog_first = new Dialog(AddWorkPlaceLocationActivity.this);
        dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_first.setContentView(R.layout.popup_address_list);
        dialog_first.setCancelable(false);
        dialog_first.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ListView lv_address = (ListView) dialog_first.findViewById(R.id.lv_address);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Address);
        lv_address.setAdapter(itemsAdapter);

        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialog_first.dismiss();
                try {
                    String strAddress = address.get(position);
                    String arAddress[] = strAddress.split(",");
                    //    et_shop_no.setText(arAddress[0]);
                    et_text_selected.setText(strAddress);

                } catch (NullPointerException e) {

                } catch (ArrayIndexOutOfBoundsException e) {

                }
            }
        });

        dialog_first.show();
    }


    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {

        try {
            String strAddress = address.get(position);
            String arAddress[] = strAddress.split(",");
            // et_shop_no.setText(arAddress[0]);
            et_text_selected.setText(strAddress);
        } catch (NullPointerException e) {

        } catch (ArrayIndexOutOfBoundsException e) {

        }


    }

    private void callCheckAddress(final String lat, final String lon, final String address, final String postcode, final String workplace) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(AddWorkPlaceLocationActivity.this);

            String url = Constants.CheckShopAddress + "address=" + address.replace(" ", "%20");

            Log.e("Deep", "lat lon :" + lat + " " + lon);
            Log.e("Deep", "CheckShopAddress Url:" + url);
            Log.d("api call post", url);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Deep", "CheckShopAddress reponse:" + response);
                            Constants.dismissProgress();
                            try {
                                JSONObject jsonobj = new JSONObject(response);
                                String response_values = jsonobj.getString("success");
                                if (!response_values.equalsIgnoreCase("false")) {
                                    showAddressDilaoug(lat, lon, address, postcode, workplace, jsonobj.getString("shop_name"));
                                } else {
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_MISSED, "add_workplace").apply();
                                    Intent it = new Intent(AddWorkPlaceLocationActivity.this, LocationOnMapActivity.class);
                                    it.putExtra("Latitude", lat);
                                    it.putExtra("Longitude", lon);
                                    it.putExtra("workplace", workplace);
                                    it.putExtra("address", address);
                                    it.putExtra("postcode", postcode);
                                    it.putExtra("From", "" + From);
                                    it.putExtra("first_name", firstname);
                                    it.putExtra("last_name", lastName);
                                    it.putExtra("email", email);
                                    it.putExtra("password", password);
                                    it.putExtra("phone", phone);
                                    it.putExtra("From", From);
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_LAT, lat).apply();
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_LOG, lon).apply();
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_WORK_PLACE, workplace).apply();
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_ADDRESS, address).apply();
                                    prefs.edit().putString(Constants.KEY_IS_SIGNUP_ON_POSTCODE, postcode).apply();
                                    startActivity(it);
                                    if (From.equalsIgnoreCase("EditPrfoile")) {
                                        finish();
                                    } else {
                                        finish();
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
                    //    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }


            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(req, "LOGIN");
        } else {
            Constants.showPopupInternet(activity);
        }
    }


}
