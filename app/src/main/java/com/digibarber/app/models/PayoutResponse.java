package com.digibarber.app.models;

import java.io.IOException;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PayoutResponse {

    private PayoutData data;
    private boolean success;
    private String message;
    private String status;

    public PayoutData getData() {
        return data;
    }

    public void setData(PayoutData value) {
        this.data = value;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean value) {
        this.success = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }
}

