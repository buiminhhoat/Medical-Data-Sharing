package com.medicaldatasharing.form;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchConfirmPaymentRequestForm {
    private String requestId;
    private String paymentRequestId;
    private String senderId;
    private String recipientId;
    private String dateModified;
    private String requestType;
    private String requestStatus;
    private Date from;
    private Date until;
    private String sortingOrder;
}

