package com.digibarber.app.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class StripeDashboardActivity extends BaseActivity {
    WebView webView;
    private ImageView back_icon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_dashboard);
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Constants.showPorgess(StripeDashboardActivity.this);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Constants.dismissProgress();
            }
        });

        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setPadding(0, 0, 0, 0);
        webView.setHorizontalScrollBarEnabled(false);
        back_icon = findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
        callStripeConnect();
    }

    public void callStripeConnect() {
        Constants.showPorgess(StripeDashboardActivity.this);

        String url = ApiUrls.Stripe_Dashboard;
        //String url = Constants.Stripe_Dashboard;
        Log.d("api call post",url);
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Constants.dismissProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("true") && jsonObject.optString("status").equals("200")) {
                                webView.loadUrl(URLDecoder.decode(jsonObject.optString("url"), "UTF-8"));
                            }

                        } catch (Exception e) {
                            Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //webView.loadUrl(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                Constants.dismissProgress();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authenticate", prefs.getString("token_value", ""));
                headers.put("user_id", prefs.getString("user_id", ""));
                return headers;
            }
        };
        req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
        AppController.getInstance().addToRequestQueue(req, "LOGIN");
    }
}