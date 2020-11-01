package com.digibarber.app.loader;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.digibarber.app.R;


/**
 * Created by user on 11/16/2016.
 */

public class BarberLoader extends Dialog {


    AVLoadingIndicatorView avLoadingIndicatorView;


    public BarberLoader(Context ctx) {
        super(ctx);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.showprogressloader);
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loadingIndicator);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
        avLoadingIndicatorView.smoothToShow();


    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if (avLoadingIndicatorView != null)
            avLoadingIndicatorView.smoothToHide();
    }
}
