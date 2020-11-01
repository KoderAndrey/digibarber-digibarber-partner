package com.digibarber.app.CustomClasses;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DIGIBARBER LTD on 19/6/17.
 */
public class CustomAvenirTextView extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    public CustomAvenirTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public CustomAvenirTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomAvenirTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomAvenirTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null)
        {
           /* TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_font);*/


            //Typeface custom_font1 = Typeface.createFromAsset(getAssets(),  "fonts/sf_ui_text_light.otf");
            String fontName = "avenir_font.ttf";

            try
            {
                if (fontName != null)
                {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  a.recycle();
        }
    }
}
