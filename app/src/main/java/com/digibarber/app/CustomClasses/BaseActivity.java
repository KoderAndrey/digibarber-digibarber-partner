package com.digibarber.app.CustomClasses;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends FragmentActivity   {


    public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.hrupin.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
    private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();
    public static final IntentFilter INTENT_FILTER = createIntentFilter();
    public  SharedPreferences prefs;
    public Activity activity;
    private static final String BASE_ACTIVITY_TAG ="BASE_ACTIVITY_TAG";
    public static final String TESTING_TAG ="TESTING_TAG";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        Log.i(BASE_ACTIVITY_TAG, "class " + this.getClass().getName());
        activity = BaseActivity.this;
        prefs = getSharedPreferences(Constants.SHARED_PREFRENCE_DB_NAME, MODE_PRIVATE);
        registerBaseActivityReceiver();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .disableCustomViewInflation() // <----- this fix
                .build());
    }


    private static IntentFilter createIntentFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
        return filter;
    }

    protected void registerBaseActivityReceiver() {
        registerReceiver(baseActivityReceiver, INTENT_FILTER);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    protected void unRegisterBaseActivityReceiver() {
        unregisterReceiver(baseActivityReceiver);
    }

    public class BaseActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION)){
                finish();
            }
        }
    }

    protected HashMap<String, String> getApiHeaders(){
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authenticate", prefs.getString("token_value", ""));
        headers.put("user_id", prefs.getString("user_id", ""));
        Log.d("loginParams",headers.toString());
        return headers;
    }

    public  void closeAllActivities(){
        sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBaseActivityReceiver();
    }
}