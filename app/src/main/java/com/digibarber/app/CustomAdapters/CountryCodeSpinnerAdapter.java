package com.digibarber.app.CustomAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digibarber.app.FireBase.CountryData;

public class CountryCodeSpinnerAdapter extends ArrayAdapter<String> {

    private String mCustomText = "";

    public CountryCodeSpinnerAdapter(Context context){
        super(context, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView tv = (TextView)view.findViewById(android.R.id.text1);
        tv.setText(mCustomText);

        return view;
    }

    public void setCustomText(String customText) {
        // Call to set the text that must be shown in the spinner for the custom option.
        mCustomText = customText;
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // No need for this override, actually. It's just to clarify the difference.
        return super.getDropDownView(position, convertView, parent);
    }
}