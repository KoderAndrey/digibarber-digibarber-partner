package com.digibarber.app.Beans;

/**
 * Created by DIGIBARBER LTD on 3/10/17.
 */

public class BankDetailModel {
    private String accountHolderName;
    private String accoundNumber;
    private String sortCode;
    private String address;

    public BankDetailModel(String accountHolderName, String accoundNumber, String sortCode, String address) {
        this.accountHolderName = accountHolderName;
        this.accoundNumber = accoundNumber;
        this.sortCode = sortCode;
        this.address = address;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccoundNumber() {
        return accoundNumber;
    }

    public void setAccoundNumber(String accoundNumber) {
        this.accoundNumber = accoundNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
