package com.medicaldatasharing.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import java.util.Date;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Scientist extends User {

    private String researchCenterId;
    private String gender;
    private Date dateBirthday;

    @PrePersist
    private void addPrefixToId() {
        if (this.getId() == null || this.getId().isEmpty()) {
            this.setId("Scientist-" + java.util.UUID.randomUUID().toString());
        }
    }
}
