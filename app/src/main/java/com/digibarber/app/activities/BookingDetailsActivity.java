package com.digibarber.app.activities;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.digibarber.app.Beans.UpcomingBookingNotification;
import com.digibarber.app.R;

import java.util.Locale;

public class BookingDetailsActivity extends AppCompatActivity {
    TextView tv_service_name;
    TextView tv_barber_name;
    TextView tv_date;
    TextView tv_time;
    TextView tv_cancel;
    TextView tv_location;
    ImageView barber_profile_image, back_icon;

    UpcomingBookingNotification upcomingBookingNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        tv_service_name = findViewById(R.id.tv_service_name);
        tv_barber_name = findViewById(R.id.tv_barber_name);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_location = findViewById(R.id.tv_location);
        back_icon = findViewById(R.id.back_icon);
        barber_profile_image = findViewById(R.id.barber_profile_image);
        upcomingBookingNotification = (UpcomingBookingNotification) getIntent().getSerializableExtra("bookingNotificationArrayList");
        tv_service_name.setText(upcomingBookingNotification.service_name);
        tv_barber_name.setText(upcomingBookingNotification.name);
        tv_date.setText(upcomingBookingNotification.booking_date);
        tv_time.setText(upcomingBookingNotification.booking_time);
        tv_location.setText(upcomingBookingNotification.address);

        if (upcomingBookingNotification.image != null && !upcomingBookingNotification.image.equalsIgnoreCase("")) {
            Picasso.get().load(upcomingBookingNotification.image).placeholder(R.color.colorGrey)
                    .fit().into(barber_profile_image);
        }

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(upcomingBookingNotification.lat), Double.parseDouble(upcomingBookingNotification.lon));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
