package com.medicaldatasharing.response;

import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
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

    @JsonProperty("address")
    protected String address;

    @JsonProperty("avatar")
    protected String avatar;

    public UserResponse() {
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.address = user.getAddress();
        this.avatar = user.getAvatar();
    }
}
