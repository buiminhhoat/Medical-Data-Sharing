package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchViewRequestForm {
    private String requestId;
    private String senderId;
    private String recipientId;
    private String requestType;
    private String requestStatus;
    private Date from;
    private Date until;
    @NotBlank
    private String sortingOrder;
    private String prescriptionId;
    private String hashFile;

    public SearchViewRequestForm() {
        requestId = "";
        senderId = "";
        recipientId = "";
        requestType = "";
        requestStatus = "";
        sortingOrder = "desc";
    }
}

