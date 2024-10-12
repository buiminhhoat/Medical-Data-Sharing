package com.medicaldatasharing.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDataResponse {
    private String fullName;
    private String role;

    public GetUserDataResponse(String fullName, String role) {
        this.fullName = fullName;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public GetUserDataResponse setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getRole() {
        return role;
    }

    public GetUserDataResponse setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "GetUserDataResponse{" +
                "fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
