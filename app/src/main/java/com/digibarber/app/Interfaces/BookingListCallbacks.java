package com.digibarber.app.Interfaces;

/**
 * Created by DIGIBARBER LTD on 30/9/17.
 */

public interface BookingListCallbacks {


    void confirmBookingListener(int pos);

    void startCloseBookingListener(int pos);

    void rescheduleBookingListener(int pos);

    void cancelDeclineBookingListener(int pos ,String type);

    void onPostCodeClicked(int position);
}
