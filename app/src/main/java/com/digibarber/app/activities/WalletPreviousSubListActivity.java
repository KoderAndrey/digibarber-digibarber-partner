package com.digibarber.app.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.digibarber.app.Beans.WalletDataRequest;
import com.digibarber.app.Beans.WalletRequestListResponse;
import com.digibarber.app.CustomAdapters.WalletRecyclerAdapter;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.R;

import java.util.ArrayList;

public class WalletPreviousSubListActivity extends BaseActivity {

    WalletDataRequest objWalletDataRequest;

    ArrayList<WalletRequestListResponse> objWalletRequestListResponses=new ArrayList<>();

    RelativeLayout rl_sub_click;
    RecyclerView rv_sub_list;
    Animation animMove;
    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.activity_wallet_previous_sub_list);
        bd = getIntent().getExtras();
        if (bd != null) { objWalletDataRequest = (WalletDataRequest) bd.getSerializable("data");
        }

        animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_to_right);animMove.start();
        animMove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


      //  rl_sub_click=(RelativeLayout)findViewById(R.id.rl_sub_click);


      /*  rl_sub_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        rv_sub_list=(RecyclerView)findViewById(R.id.rv_sub_list);
        objWalletRequestListResponses = objWalletDataRequest.request;

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(WalletPreviousSubListActivity.this, LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);

      /*  WalletRecyclerAdapter wallet_fragment_recycler_adapter = new WalletRecyclerAdapter(objWalletRequestListResponses, WalletPreviousSubListActivity.this, "Sublist");
        rv_sub_list.setLayoutManager(mLayoutManager);
        rv_sub_list.setAdapter(wallet_fragment_recycler_adapter);
        wallet_fragment_recycler_adapter.notifyDataSetChanged();*/


    }
}
