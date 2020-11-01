package com.digibarber.app.CustomClasses;

/**
 * Created by DIGIBARBER LTD on 19/6/17.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Text view with a custom font.
 * <p/>
 * In the XML, use something like {@code customAttrs:customFont="roboto-thin"}. The list of fonts
 * that are currently supported are defined in the enum {@link CustomFontTextView}. Remember to also add
 * {@code xmlns:customAttrs="http://schemas.android.com/apk/res-auto"} in the header.
 */
public class CustomFontTextView extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomFontTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null)
        {
           /* TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_font);*/


            //Typeface custom_font1 = Typeface.createFromAsset(getAssets(),  "fonts/sf_ui_text_light.otf");
            String fontName = "sf_ui_display_light.otf";

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
