package com.digibarber.app.Beans;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */

public class WalletHistory {

    public  String week;
    public  String month;
    public  ArrayList<walletHistoryItems> item_values;

    public WalletHistory()
    {

    }
    public WalletHistory(String week,String month, ArrayList<walletHistoryItems> item_values)
    {
        this.week=week;
        this.month=month;
        this.item_values=item_values;
    }


}
