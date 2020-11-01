package com.digibarber.app.Beans;

/**
 * Created by DIGIBARBER LTD on 20/7/17.
 */
public class WeekCalender {

    public String dayName;
    public String date;
    public String formatedDate;
    public String formatedDateUse;
    public int daySelected;
    public String selectedDate;

    public WeekCalender(String dayName, String date, String formatedDate,String formatedDateUse, int daySelected,String selectedDate) {
        this.dayName = dayName;
        this.date = date;
        this.formatedDate = formatedDate;
        this.formatedDateUse=formatedDateUse;
        this.daySelected=daySelected;
        this.selectedDate=selectedDate;
    }

    public WeekCalender() {

    }
}
