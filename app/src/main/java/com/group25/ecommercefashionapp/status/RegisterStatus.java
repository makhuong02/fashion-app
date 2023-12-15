package com.group25.ecommercefashionapp.status;

import com.google.gson.annotations.SerializedName;

public class RegisterStatus {
    @SerializedName("data")
    public boolean status = false;
    private String log;

    public String getLog() {
        return log;
    }
}
