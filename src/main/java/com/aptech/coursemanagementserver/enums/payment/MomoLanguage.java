package com.aptech.coursemanagementserver.enums.payment;

import com.google.gson.annotations.SerializedName;

public enum MomoLanguage {
    @SerializedName("vi")
    VI("vi"),

    @SerializedName("en")
    EN("en");

    private final String value;

    MomoLanguage(String value) {
        this.value = value;
    }

    public static MomoLanguage findByName(String name) {
        for (MomoLanguage type : values()) {
            if (type.getMomoLanguage().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getMomoLanguage() {
        return value;
    }
}
