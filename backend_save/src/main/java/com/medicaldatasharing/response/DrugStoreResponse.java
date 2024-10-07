package com.medicaldatasharing.response;

import com.medicaldatasharing.model.DrugStore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrugStoreResponse extends UserResponse {
    private String drugStoreId;
    private String businessLicenseNumber;

    public DrugStoreResponse(DrugStore drugStore) {
        this.id = drugStore.getId();
        this.drugStoreId = drugStore.getId();
        this.email = drugStore.getEmail();
        this.fullName = drugStore.getFullName();
        this.avatar = drugStore.getAvatar();
        this.role = drugStore.getRole();
        this.address = drugStore.getAddress();
        this.enabled = String.valueOf(drugStore.isEnabled());
        this.businessLicenseNumber = drugStore.getBusinessLicenseNumber();
    }
}
