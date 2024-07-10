package com.medicaldatasharing.response;

import com.owlike.genson.annotation.JsonProperty;

public class UserResponse {
    @JsonProperty("id")
    protected String id;

    @JsonProperty("email")
    protected String email;

    @JsonProperty("fullName")
    protected String fullName;

    @JsonProperty("role")
    protected String role;
}
