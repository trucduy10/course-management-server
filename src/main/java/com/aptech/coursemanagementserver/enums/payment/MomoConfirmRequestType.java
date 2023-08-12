package com.aptech.coursemanagementserver.enums.payment;

import com.google.gson.annotations.SerializedName;

public enum MomoConfirmRequestType {

    /**
     * The capture momo wallet.
     */
    @SerializedName("capture")
    CAPTURE("capture"),

    /**
     * The pay with atm
     */
    @SerializedName("cancel")
    CANCEL("cancel");

    private final String value;

    MomoConfirmRequestType(String value) {
        this.value = value;
    }

    public static MomoConfirmRequestType findByName(String name) {
        for (MomoConfirmRequestType type : values()) {
            if (type.getMomoConfirmRequestType().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getMomoConfirmRequestType() {
        return value;
    }
}
