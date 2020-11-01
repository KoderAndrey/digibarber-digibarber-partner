package com.digibarber.app.CustomClasses;

import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DIGIBARBER LTD on 4/11/17.
 */

public class CustomBottomSheetBehaviorWallet<V extends View> extends BottomSheetBehavior<V> {

    private boolean allowDragging = true;

    public void setAllowDragging(boolean allowDragging) {
        this.allowDragging = allowDragging;
    }


    public CustomBottomSheetBehaviorWallet() {
        super();
    }

    public CustomBottomSheetBehaviorWallet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        return false;
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int nestedScrollAxes) {
        return false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed) {
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target) {
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY) {
        return false;
    }
}
