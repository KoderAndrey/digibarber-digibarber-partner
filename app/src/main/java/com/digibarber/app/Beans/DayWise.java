package com.digibarber.app.Beans;

/**
 * Created by DIGIBARBER LTD on 10/10/17.
 */

public class DayWise {

    public String booking_id;
    public  String reschedule;
    public  String user_id;
    public  String user_name;
    public  String booking_time;
    public  String service_name;
    public  String rescdule_time;
    public String date;




    public DayWise(String booking_id, String reschedule, String user_id, String user_name, String booking_time, String service_name, String rescdule_time, String date) {

        this.booking_id = booking_id;
        this.reschedule = reschedule;
        this.user_id = user_id;
        this.user_name = user_name;
        this.booking_time = booking_time;
        this.service_name = service_name;
        this.rescdule_time = rescdule_time;

        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getReschedule() {
        return reschedule;
    }

    public void setReschedule(String reschedule) {
        this.reschedule = reschedule;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getServices() {
        return service_name;
    }

    public void setServices(String services) {
        this.service_name = services;
    }

    public String getRescdule_time() {
        return rescdule_time;
    }

    public void setRescdule_time(String rescdule_time) {
        this.rescdule_time = rescdule_time;
    }


}
