package com.digibarber.app.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.R;

/**
 * Created by DIGIBARBER LTD on 25/9/17.
 */

public class AddServiceDescriptionActivity extends BaseActivity {

    ImageView tv_done;
    private ImageView back_icon;
    private String groupPos;
    private String childPos;
    TextView tv_max;
    EditText edit_story;
    public int totalLength = 140;
    private String des = "";
    Typeface typefaceAvenirLight;

    @Override
    public void onCreate(Bundle bd) {
        super.onCreate(bd);
        setContentView(R.layout.layout_add_service_description);
        edit_story = (EditText) findViewById(R.id.edit_story);
        typefaceAvenirLight = Typeface.createFromAsset(getAssets(), "fonts/avenir_font.ttf");
        edit_story.setTypeface(typefaceAvenirLight);
        tv_max = (TextView) findViewById(R.id.tv_max);
        bd = getIntent().getExtras();
        if (bd != null) {
            groupPos = bd.getString("groupPos");
            childPos = bd.getString("childPos");
            des = bd.getString("des");
        }
        edit_story.setText(des);
        int length = edit_story.getText().length();
        length = totalLength - length;
        tv_max.setText("Max " + length + " " + "characters");
        tv_done = (ImageView) findViewById(R.id.tv_done);
        back_icon = (ImageView) findViewById(R.id.back_icon);


        edit_story.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                edit_story.setCursorVisible(true);
                edit_story.setSelection(edit_story.getText().length());
                return false;
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent();
                it.putExtra("des", edit_story.getText().toString());
                it.putExtra("groupPos", "" + groupPos);
                it.putExtra("childPos", "" + childPos);
                setResult(RESULT_OK, it);
                finish();


            }
        });


        edit_story.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edit_story.getText().toString().length() > 0) {
                    edit_story.setAlpha(1);
                } else {
                    edit_story.setAlpha(0.5f);
                }
                int length = edit_story.getText().length();
                length = totalLength - length;
                tv_max.setText("Max " + length + " " + "characters");
            }
        });
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
