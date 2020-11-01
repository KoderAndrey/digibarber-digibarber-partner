
package com.digibarber.app.activities;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.digibarber.app.apicalls.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.digibarber.app.CustomClasses.Constants.checkString;

public class ChangePasswordActivity extends BaseActivity {

    private EditText password;
    private EditText new_password;
    private EditText new_password_again;
    private ImageView back_icon;
    private ImageView iv_done;
    SharedPreferences prefs;

    ImageView iv_tick_success;
    TextView tv_your_pass_changed;
    TextView tv_your_pass_error;
    TextView tv_pass_new_error;
    TextView tv_password_not_macth;

    ColorStateList colorStateListRed;
    ColorStateList colorStateListGrey;
    ColorStateList colorStateListGreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        colorStateListRed = ColorStateList.valueOf(getResources().getColor(R.color.red_light));
        colorStateListGrey = ColorStateList.valueOf(getResources().getColor(R.color.colorGrey));
        colorStateListGreen = ColorStateList.valueOf(getResources().getColor(R.color.green_success));

        prefs = getSharedPreferences(Constants.SHARED_PREFRENCE_DB_NAME, MODE_PRIVATE);
        password = (EditText) findViewById(R.id.password);

        new_password = (EditText) findViewById(R.id.new_password);
        new_password_again = (EditText) findViewById(R.id.new_password_again);


        back_icon = (ImageView) findViewById(R.id.back_icon);
        iv_done = (ImageView) findViewById(R.id.iv_done);
        iv_tick_success = (ImageView) findViewById(R.id.iv_tick_success);

        password.setBackgroundTintList(colorStateListGrey);
        new_password.setBackgroundTintList(colorStateListGrey);
        new_password_again.setBackgroundTintList(colorStateListGrey);

        tv_your_pass_changed = (TextView) findViewById(R.id.tv_your_pass_changed);
        tv_your_pass_error = (TextView) findViewById(R.id.tv_your_pass_error);
        tv_pass_new_error = (TextView) findViewById(R.id.tv_pass_new_error);
        tv_password_not_macth = (TextView) findViewById(R.id.tv_password_not_macth);

        password.addTextChangedListener(new CustomTextWatcher(password, tv_your_pass_error));
        new_password.addTextChangedListener(new CustomTextWatcher(new_password, tv_pass_new_error));
        new_password_again.addTextChangedListener(new CustomTextWatcher(new_password_again, tv_password_not_macth));


        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_your_pass_changed.setVisibility(View.GONE);
                password.setBackgroundTintList(colorStateListGrey);

                iv_tick_success.setVisibility(View.GONE);
                if (password.getText().length() <= 0) {
                    password.setBackgroundTintList(colorStateListRed);
                    tv_your_pass_error.setVisibility(View.VISIBLE);
                    tv_your_pass_error.setText(getResources().getString(R.string.empty_your_password));
                } else if (new_password.getText().length() <= 0) {
                    new_password.setBackgroundTintList(colorStateListRed);
                    tv_pass_new_error.setVisibility(View.VISIBLE);
                    tv_pass_new_error.setText(getResources().getString(R.string.empty_new_password));
                } else if (new_password.getText().length() <= 5) {
                    new_password.setBackgroundTintList(colorStateListRed);
                    tv_pass_new_error.setVisibility(View.VISIBLE);
                    tv_pass_new_error.setText(getResources().getString(R.string.password_must_six));
                } else if (!checkString(new_password.getText().toString()))
                {
                    new_password.setBackgroundTintList(colorStateListRed);

                    tv_pass_new_error.setVisibility(View.VISIBLE);
                    tv_pass_new_error.setText(getResources().getString(R.string.password_must_six));
                } else if (!new_password.getText().toString().equalsIgnoreCase(new_password_again.getText().toString())) {
                    new_password_again.setBackgroundTintList(colorStateListRed);
                    new_password_again.setText("");
                    tv_password_not_macth.setVisibility(View.VISIBLE);
                    tv_password_not_macth.setText(getResources().getString(R.string.empty_new_not_match_password));
                } else {
                    callChangePassword(password.getText().toString(), new_password.getText().toString());
                }

            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class CustomTextWatcher implements TextWatcher {
        private EditText mEditText;
        private TextView tv_errror;

        public CustomTextWatcher(EditText etOpenClsoe, TextView tv_errror) {
            mEditText = etOpenClsoe;
            this.tv_errror = tv_errror;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mEditText.getText().toString().length() > 0 && tv_errror.getVisibility() == View.VISIBLE) {
                tv_errror.setVisibility(View.GONE);
                mEditText.setBackgroundTintList(colorStateListGrey);
            }
        }
    }

    private void callChangePassword(final String old_pass, final String new_password) {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(ChangePasswordActivity.this);
            Log.d("api call post",Constants.PasswordChange);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.PasswordChange,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {
                                JSONObject jo = new JSONObject(response);
                                String success = jo.getString("success");
                                String message = jo.getString("message");
                                if (success.equalsIgnoreCase("true")) {
                                    tv_your_pass_changed.setVisibility(View.VISIBLE);
                                    password.setBackgroundTintList(colorStateListGreen);
                                    iv_tick_success.setVisibility(View.VISIBLE);
                                    Constants.showToast(ChangePasswordActivity.this,message);
                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            finish();
                                        }
                                    }, 1000);
                                } else {
                                    password.setBackgroundTintList(colorStateListRed);
                                    tv_your_pass_error.setVisibility(View.VISIBLE);
                                    tv_your_pass_error.setText("Incorrect password - please try again!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Constants.dismissProgress();
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

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("old_pass", old_pass);
                    params.put("new_password", new_password);
                    return params;
                }
            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "Barber Reviews Listing");
        } else {
            Constants.showPopupInternet(activity);
        }
    }
}
