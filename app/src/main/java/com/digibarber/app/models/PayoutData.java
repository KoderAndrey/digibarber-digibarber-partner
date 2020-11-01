package com.digibarber.app.models;


import java.util.ArrayList;

public class PayoutData {
    private ArrayList<Payout> payouts;
    private long total_amount;

    public ArrayList<Payout> getPayouts() { return payouts; }
    public void setPayouts(ArrayList<Payout> value) { this.payouts = value; }

    public long getTotalAmount() { return total_amount; }
    public void setTotalAmount(long value) { this.total_amount = value; }
}