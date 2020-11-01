package com.digibarber.app.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.digibarber.app.Beans.NotificationServerSide;
import com.digibarber.app.CustomClasses.Foreground;
import com.digibarber.app.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * Created by DIGIBARBER LTD on 5/27/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> jo = remoteMessage.getData();
        Log.i("notification_check", "" + jo);
        // {address=address, type=today_booking, barber_name=Dann, price=£ 10.0, user_name=Android Testdr1, service_name=Hair Cut, postcode=postcode, message=Psst! Your booking is today!, workplace=workplace, booking_date=Saturday, November 18, 2017, booking_time=18:30 - 19:15, notify_id=727}
        try {
            NotificationServerSide objNotificationServerSide = new NotificationServerSide();
            objNotificationServerSide.type = jo.get("type");
            objNotificationServerSide.message = jo.get("message");
            objNotificationServerSide.barber_name = jo.get("barber_name");
            objNotificationServerSide.user_name = jo.get("user_name");
            objNotificationServerSide.booking_date = jo.get("booking_date");
            objNotificationServerSide.booking_time = jo.get("booking_time");
            objNotificationServerSide.service_name = jo.get("service_name");
            objNotificationServerSide.price = jo.get("price");
            objNotificationServerSide.address = jo.get("address");
            objNotificationServerSide.workplace = jo.get("workplace");
            objNotificationServerSide.postcode = jo.get("postcode");
            objNotificationServerSide.notify_id = jo.get("notify_id");
            objNotificationServerSide.image = jo.get("barber_image");
            //  boolean isAppForground = ZoomConst.isAppInForground(getApplicationContext(), "com.wa.digibarberapp");
            boolean isAppForground = Foreground.get().isForeground();
            if (isAppForground) {
                Log.e("isAppForground is true", "==Type" + objNotificationServerSide.type + " == booking_date" + objNotificationServerSide.booking_date);
                EventBus.getDefault().post(objNotificationServerSide);
            } else {
                Log.e("isAppForground is false", "==Type" + objNotificationServerSide.type + " == booking_date" + objNotificationServerSide.booking_date);

                sendNotification(objNotificationServerSide);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void sendNotification(NotificationServerSide objNotificationServerSide) {
        try {
            int id = 0;
            if (objNotificationServerSide.notify_id != null && !objNotificationServerSide.notify_id.equalsIgnoreCase("")) {
                id = Integer.parseInt(objNotificationServerSide.notify_id.trim());
            }
            Intent notificationIntent = null;
            String type = objNotificationServerSide.type;
            if (type.equalsIgnoreCase("reschedule_confirm") || type.equalsIgnoreCase("reschedule_cancel") || type.equalsIgnoreCase("auto_cancel") || type.equalsIgnoreCase("cancel_by_user") || type.equalsIgnoreCase("activate") || type.equalsIgnoreCase("book_barber")) {
                notificationIntent = new Intent(this, HomeActivity.class);
                notificationIntent.putExtra("objNotificationServerSide", objNotificationServerSide);
            } else if (type.equalsIgnoreCase("first_review_barber") || type.equalsIgnoreCase("customer_review")) {
                notificationIntent = new Intent(this, ReviewActivity.class);
            } else {
                notificationIntent = new Intent(this, HomeActivity.class);
            }
            PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Resources res = this.getResources();
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.app_launcher))
                    .setTicker("DigiBarber")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("DigiBarber")
                    .setContentText(objNotificationServerSide.message).setStyle(new Notification.BigTextStyle()
                    .bigText(objNotificationServerSide.message));
            Notification n = builder.build();
            n.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
            n.flags |= Notification.FLAG_AUTO_CANCEL;

            final String NOTIFICATION_CHANNEL_ID = "10001";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                assert nm != null;
                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                nm.createNotificationChannel(notificationChannel);
            }


            nm.notify(id, n);
        } catch (NumberFormatException e) {

        }


    }


}
