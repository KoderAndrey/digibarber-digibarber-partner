package com.digibarber.app.Beans;

import java.io.Serializable;

/**
 * Created by DIGIBARBER LTD on 28/8/17.
 */
public class BookingList implements Serializable {

    public String booking_id = "";
    public String reschedule = "";
    public String user_id = "";
    public String is_confirmed = "";
    public String user_name = "";
    public String booking_time = "";
    public String services = "";
    public String rescdule_time = "";
    public String date;
    public String isfirstConfBooking="";
    public String customerPostCode="";
    public BookingList() {

    }


}
