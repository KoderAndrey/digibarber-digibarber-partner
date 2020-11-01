package com.digibarber.app.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Payout {
    private double price;
    private long created;
    private String id;
    private String status;
    private String arrivalDate;
    private ArrayList<transactionDetails> transactionDetails;

    public Payout()
    {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payout(JSONObject object)
    {
        /*{"id":"po_1H4cwnF2I6yZOPcLuYZLrgYE",
        "amount":12594,"created":1594690120,
        "status":"paid","transactionDetails":[]}*/
        try {
            if(object.has(id))
            {
                this.id = object.getString("id");
            }
            if(object.has("amount"))
            {
                this.price = object.getLong("amount");
            }
            if(object.has("created"))
            {
                this.created = object.getLong("created");
            }
            if(object.has("status"))
            {
                this.status = object.getString("status");
            }
        }
        catch (JSONException ex)
        {}
    }
    public void InitializeHistory(JSONObject object)
    {
        Log.d("loginParams", object.toString());
        /*[{"services":"[{\"category_id\":\"1\",\"sub_services\":
        [{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]",
        "bookingDate":"Friday, July 10, 2020","price":"30.0","created":"2020-07-10 17:13:32"},
        {"services":"[{\"category_id\":\"1\",\"sub_services\":
        [{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Saturday, July 11, 2020","price":"38.03",
        "created":"2020-07-11 11:32:57"},{"services":"[{\"category_id\":\"1\",\"sub_services\":
        [{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Saturday, July 11, 2020","price":"38.03",
        "created":"2020-07-11 12:34:23"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Wednesday, July 15, 2020","price":"38.03","created":"2020-07-12 23:39:31"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Thursday, July 09, 2020","price":"30.0","created":"2020-07-09 00:14:21"},{"services":"[{\"category_id\":\"2\",\"sub_services\":[{\"sub_category_id\":\"22\",\"service_name\":\"Beard Shape Up\"}]}]","bookingDate":"Thursday, July 09, 2020","price":"30.0","created":"2020-07-09 13:45:44"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"38","created":"2020-07-12 17:02:43"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"34.68","created":"2020-07-12 22:39:13"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Friday, July 10, 2020","price":"30.0","created":"2020-07-10 19:02:09"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"38","created":"2020-07-12 15:34:07"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"34.68","created":"2020-07-12 21:23:04"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"34.68","created":"2020-07-12 22:11:38"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Monday, July 13, 2020","price":"38.03","created":"2020-07-12 22:38:15"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"38","created":"2020-07-12 14:54:32"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Sunday, July 12, 2020","price":"38","created":"2020-07-12 15:42:00"},{"services":"[{\"category_id\":\"1\",\"sub_services\":[{\"sub_category_id\":\"2\",\"service_name\":\"Scissors Cut\"}]}]","bookingDate":"Monday, July 13, 2020","price":"34.68","created":"2020-07-13 02:10:27"}]
*/
        try {
            if(object.has(id))
            {
                this.id = object.getString("id");
            }
            if(object.has("amount"))
            {
                this.price = object.getLong("amount");
            }
            if(object.has("created"))
            {
                this.created = object.getLong("created");
            }
            if(object.has("status"))
            {
                this.status = object.getString("status");
            }
        }
        catch (JSONException ex)
        {}
    }


    public double getPrice() { return price; }
    public void setPrice(long value) { this.price = value; }

    public long getCreated() { return created; }
    public void setCreated(long value) { this.created = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public ArrayList<com.digibarber.app.models.transactionDetails> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(ArrayList<com.digibarber.app.models.transactionDetails> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}