package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DefineViewPrescriptionRequestForm {
    @NotBlank
    private String requestId;
    @NotBlank
    private String requestStatus;

    public DefineViewPrescriptionRequestForm(DefineRequestForm defineRequestForm) {
        this.requestId = defineRequestForm.getRequestId();
        this.requestStatus = defineRequestForm.getRequestStatus();
    }
}
