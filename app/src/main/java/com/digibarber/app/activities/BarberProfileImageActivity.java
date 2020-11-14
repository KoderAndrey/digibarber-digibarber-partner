package com.digibarber.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.R;

public class BarberProfileImageActivity extends BaseActivity {
    ImageView iv_images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        setContentView(R.layout.activity_barber_profile_image);
        iv_images = (ImageView) findViewById(R.id.iv_images);

        String imagePath = prefs.getString(Constants.KEY_PROFILE_IMAGE, "");
        if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
            Picasso.get().load(imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_images);
        }
        iv_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }
}
