package com.digibarber.app.CustomAdapters;

import android.app.Activity;

import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.digibarber.app.R;

/**
 * Created by DIGIBARBER LTD on 6/6/17.
 */
public class SplashViewPagerAdapter extends PagerAdapter {

    private Activity mContext;
    private String[] mText;


    public SplashViewPagerAdapter(Activity mContext, String[] mText) {
        this.mContext = mContext;
        this.mText = mText;
    }

    @Override
    public int getCount() {
        return mText.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        FrameLayout fl_visibility_main = (FrameLayout) itemView.findViewById(R.id.fl_visibility_main);
        FrameLayout fl_work_group_5 = (FrameLayout) itemView.findViewById(R.id.fl_work_group_5);
        FrameLayout fl_work_group_4 = (FrameLayout) itemView.findViewById(R.id.fl_work_group_4);
        FrameLayout fl_work_group_3 = (FrameLayout) itemView.findViewById(R.id.fl_work_group_3);
        FrameLayout fl_work_group_2 = (FrameLayout) itemView.findViewById(R.id.fl_work_group_2);
        FrameLayout fl_work_group_1 = (FrameLayout) itemView.findViewById(R.id.fl_work_group_1);
        if (position == 0) {
            fl_visibility_main.setVisibility(View.GONE);
            fl_work_group_1.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            fl_work_group_1.setVisibility(View.GONE);
            fl_visibility_main.setVisibility(View.VISIBLE);
            fl_work_group_2.setVisibility(View.VISIBLE);
            fl_work_group_3.setVisibility(View.GONE);
            fl_work_group_4.setVisibility(View.GONE);
            fl_work_group_5.setVisibility(View.GONE);
        } else if (position == 2) {
            fl_work_group_1.setVisibility(View.GONE);
            fl_visibility_main.setVisibility(View.VISIBLE);
            fl_work_group_2.setVisibility(View.GONE);
            fl_work_group_3.setVisibility(View.VISIBLE);
            fl_work_group_4.setVisibility(View.GONE);
            fl_work_group_5.setVisibility(View.GONE);
        } else if (position == 3) {
            fl_work_group_1.setVisibility(View.GONE);
            fl_work_group_2.setVisibility(View.GONE);
            fl_visibility_main.setVisibility(View.VISIBLE);
            fl_work_group_3.setVisibility(View.GONE);
            fl_work_group_5.setVisibility(View.GONE);
            fl_work_group_4.setVisibility(View.VISIBLE);
        } else if (position == 4)  {
            fl_work_group_1.setVisibility(View.GONE);
            fl_work_group_2.setVisibility(View.GONE);
            fl_visibility_main.setVisibility(View.VISIBLE);
            fl_work_group_3.setVisibility(View.GONE);
            fl_work_group_4.setVisibility(View.GONE);
            fl_work_group_5.setVisibility(View.VISIBLE);
        }


        // itemView.setVerticalScrollbarPosition(position);

        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
