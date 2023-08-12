package com.aptech.coursemanagementserver.dtos.payment;

import com.aptech.coursemanagementserver.enums.payment.MomoLanguage;
import com.aptech.coursemanagementserver.enums.payment.MomoRequestType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MomoRequestDto {
    private String partnerCode = "MOMOLRJZ20181206";
    private String requestId = String.valueOf(System.currentTimeMillis());
    private String orderId = String.valueOf(System.currentTimeMillis());
    private String lang = MomoLanguage.EN.getMomoLanguage();
    private String orderInfo = "Thanh toán qua MoMo";
    private long startTime = System.currentTimeMillis();

    private long amount = 10000;
    private String partnerName;
    private String subPartnerCode;
    private String requestType = MomoRequestType.PAY_WITH_ATM.getMomoRequestType();
    private String redirectUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
    private String ipnUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
    private String storeId;
    private String extraData;
    private String partnerClientId;
    private Boolean autoCapture = true;
    private Long orderGroupId;
    private String signature;
    private long userId;
    private long courseId;
    private String userDescription;

}
