package com.medicaldatasharing.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String messageStatus;
    private HttpStatus status;
}
