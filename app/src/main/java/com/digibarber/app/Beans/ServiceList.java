package com.digibarber.app.Beans;

import java.io.Serializable;

/**
 * Created by DIGIBARBER LTD on 21/9/17.
 */

public class ServiceList implements Serializable {

    public String category_name;
    public String sub_category_id;
    public String sub_category_name;
    public String catPrice;
    public String catTime;
    public String catDes;
    public ServiceList(String category_name, String sub_category_id, String sub_category_name,String catPrice, String catTime,String catDes) {
        this.category_name = category_name;
        this.sub_category_id = sub_category_id;
        this.sub_category_name = sub_category_name;
        this.catPrice = catPrice;
        this.catTime = catTime;
        this.catDes = catDes;
    }


}
