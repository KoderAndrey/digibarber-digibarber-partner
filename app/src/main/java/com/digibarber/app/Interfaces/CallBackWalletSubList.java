package com.digibarber.app.Interfaces;

import com.digibarber.app.models.transactionDetails;

import java.util.ArrayList;

/**
 * Created by DIGIBARBER LTD on 11/10/17.
 */

public interface CallBackWalletSubList {

    void sonItemClick(int pos, ArrayList<transactionDetails> transactionDetails);

}
