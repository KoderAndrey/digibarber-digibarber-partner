package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digibarber.app.R;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 4/10/17.
 */

public class LiveAvailavilityIndexRecyclerAdapter extends RecyclerView.Adapter<LiveAvailavilityIndexRecyclerAdapter.MyHolderV> {

    private Context context;
    private ArrayList<String> availbleTime;

    Typeface typefaceUITextBold;

    Typeface typefaceAvenirLight;

    public LiveAvailavilityIndexRecyclerAdapter(Context context, ArrayList<String> availbleTime) {
        this.context = context;
        this.availbleTime = availbleTime;
    }

    @Override
    public MyHolderV onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_availablity_index_item, parent, false);
        MyHolderV mhv = new MyHolderV(view);

        return mhv;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyHolderV holder, final int position) {

        holder.textView.setText(""+availbleTime.get(position));
        holder.textView.setBackgroundResource(0);
    }

    @Override
    public int getItemCount() {
        return availbleTime.size();
    }




    public class MyHolderV extends RecyclerView.ViewHolder {
        TextView textView;

        public MyHolderV(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.time_view_index);

        }
    }

}