package com.digibarber.app.Beans;

/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */

public class walletHistoryItems {
    public String booking_id = "";
    public String reason_code = "";
    public String deduct_amount = "";
    public String price = "";
    public  String confirmed = "";
    public  String booking_date = "";
    public  String services = "";


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getReason_code() {
        return reason_code;
    }

    public void setReason_code(String reason_code) {
        this.reason_code = reason_code;
    }

    public String getDeduct_amount() {
        return deduct_amount;
    }

    public void setDeduct_amount(String deduct_amount) {
        this.deduct_amount = deduct_amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
