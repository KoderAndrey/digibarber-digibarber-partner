package com.digibarber.app.CustomClasses;

/**
 * Created by DIGIBARBER LTD on 30/9/17.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.digibarber.app.R;
import com.xenione.libs.swipemaker.AbsCoordinatorLayout;
import com.xenione.libs.swipemaker.SwipeLayout;


/**
 * Created on 06/04/16.
 */
public class BothSideCoordinatorLayout extends AbsCoordinatorLayout {

    public View tv_accept;
    public View rl_right;
    public SwipeLayout mForegroundView;

    public BothSideCoordinatorLayout(Context context) {
        super(context);
    }

    public BothSideCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BothSideCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public BothSideCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void doInitialViewsLocation() {
        mForegroundView = (SwipeLayout) findViewById(R.id.foregroundView);
        tv_accept = findViewById(R.id.tv_accept);
        rl_right = findViewById(R.id.rl_right);
        mForegroundView.anchor(-rl_right.getWidth(), 0, tv_accept.getRight());
    }

    @Override
    public void onTranslateChange(float global, int index, float relative) {

    }
}