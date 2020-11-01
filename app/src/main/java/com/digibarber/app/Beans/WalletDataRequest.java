package com.digibarber.app.Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 11/10/17.
 */

public class WalletDataRequest implements Serializable {


    public String total_amount = "";
    public String payout_date = "";
    public String payout_id = "";
    public ArrayList<WalletRequestListResponse> request;
    public String payout_time = "";
    public String status = "";


}
