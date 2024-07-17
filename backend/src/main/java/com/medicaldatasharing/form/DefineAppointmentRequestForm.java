package com.medicaldatasharing.form;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefineAppointmentRequestForm {
    @NotBlank
    private String requestId;
    @NotBlank
    private String requestStatus;

    public DefineAppointmentRequestForm(DefineRequestForm defineRequestForm) {
        this.requestId = defineRequestForm.getRequestId();
        this.requestStatus = defineRequestForm.getRequestStatus();
    }
}

