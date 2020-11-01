package com.digibarber.app.Beans;

import java.io.Serializable;

/**
 * Created by DIGIBARBER LTD on 18/11/17.
 */

public class NotificationServerSide implements Serializable {

    public String type;
    public  String message;
    public String barber_name;
    public  String user_name;
    public String booking_date;
    public  String booking_time;
    public String service_name;
    public String price;
    public String address;
    public  String workplace;
    public String postcode;
    public String notify_id;
    public String image;
    public String booking_id;

    public NotificationServerSide() {
    }
}
