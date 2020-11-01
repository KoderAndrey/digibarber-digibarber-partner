package com.digibarber.app.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

public class PrivacyPolicyActivity extends BaseActivity {
    WebView webView;
    private ImageView back_icon;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constants.PRIVACY_POLICY_LINK);
        webView.setHorizontalScrollBarEnabled(false);
        back_icon=(ImageView)findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
