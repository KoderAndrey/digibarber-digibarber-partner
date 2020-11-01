package com.digibarber.app.Beans;

/**
 * Created by DIGIBARBER LTD on 21/7/17.
 */
public class BookTimeChart {

    public String time;
    public String formatedTime;
    public String LunchTime;
    public String ConfirmationTime;

    public BookTimeChart(String time, String formatedTime, String LunchTime, String ConfirmationTime) {
        this.time = time;
        this.formatedTime = formatedTime;
        this.LunchTime = LunchTime;
        this.ConfirmationTime = ConfirmationTime;
    }
    public BookTimeChart() {

    }

}
