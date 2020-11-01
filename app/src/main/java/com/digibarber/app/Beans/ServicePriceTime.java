package com.digibarber.app.Beans;

import java.io.Serializable;

/**
 * Created by DIGIBARBER LTD on 25/9/17.
 */

public class ServicePriceTime implements Serializable{



    public String catName;
    public String mobile;
    public String sub_category_id;
    public String subName;
    public String subPrice;
    public String subTime;
    public String subDes;

    public ServicePriceTime( String catName, String sub_category_id,String subName, String subPrice, String subTime,String subDes,String mobile) {
        this.catName = catName;
        this.sub_category_id = sub_category_id;
        this.subName = subName;
        this.subPrice = subPrice;
        this.subTime = subTime;
        this.subDes = subDes;
        this.mobile = mobile;
    }
}
