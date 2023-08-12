package com.aptech.coursemanagementserver.enums.payment;

import com.google.gson.annotations.SerializedName;

public enum MomoRequestType {

    /**
     * The capture momo wallet.
     */
    @SerializedName("captureWallet")
    CAPTURE_WALLET("captureWallet"),

    /**
     * The pay with atm
     */
    @SerializedName("payWithATM")
    PAY_WITH_ATM("payWithATM"),

    /**
     * The pay with method
     */
    @SerializedName("payWithMethod")
    PAY_WITH_METHOD("payWithMethod"),

    /**
     * The pay with credit Card
     */
    @SerializedName("payWithCC")
    PAY_WITH_CREDIT("payWithCC"),

    /**
     * The link momo wallet and pay momo wallet if amount > 0.
     */
    @SerializedName("linkWallet")
    LINK_WALLET("linkWallet");

    private final String value;

    MomoRequestType(String value) {
        this.value = value;
    }

    public static MomoRequestType findByName(String name) {
        for (MomoRequestType type : values()) {
            if (type.getMomoRequestType().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public String getMomoRequestType() {
        return value;
    }
}
