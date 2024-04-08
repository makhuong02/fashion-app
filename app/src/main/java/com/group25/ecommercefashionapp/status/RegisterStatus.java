package com.group25.ecommercefashionapp.status;

import com.google.gson.annotations.SerializedName;

public class RegisterStatus {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
