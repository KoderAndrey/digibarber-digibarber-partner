package com.digibarber.app.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

public class TermConditionActiivty extends BaseActivity {
    private   WebView webView;
    private   TextView header_text;
    private ImageView back_icon;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition_actiivty);
        header_text = findViewById(R.id.header_text);

        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            url = savedInstanceState.getString("booking_id");
            header_text.setVisibility(View.GONE);
            if (url != null) {

                String headerstr = "";
                headerstr = savedInstanceState.getString("headername");

                if (headerstr != null && !headerstr.equalsIgnoreCase("")) {
                    header_text.setVisibility(View.VISIBLE);
                    header_text.setText(headerstr);
                }

            }
            else {
                header_text.setVisibility(View.VISIBLE);

                url = Constants.TERM_CONDITION_LINK;
            }
        }else {
            header_text.setVisibility(View.VISIBLE);

            url = Constants.TERM_CONDITION_LINK;
        }
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setPadding(0, 0, 0, 0);
        webView.setHorizontalScrollBarEnabled(false);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



   /* private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(PIC_WIDTH);
        val = val * 100d;
        return val.intValue();
    }*/
}
