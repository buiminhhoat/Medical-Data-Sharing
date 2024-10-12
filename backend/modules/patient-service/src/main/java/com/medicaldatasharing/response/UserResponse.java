package com.medicaldatasharing.response;

import com.medicaldatasharing.model.User;
import com.owlike.genson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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

    @JsonProperty("enabled")
    protected String enabled;

    @JsonProperty("medicalInstitutionId")
    protected String medicalInstitutionId;

    @JsonProperty("department")
    protected String department;

    public UserResponse() {
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.address = user.getAddress();
        this.avatar = user.getAvatar();
        this.enabled = String.valueOf(user.isEnabled());
    }
}
