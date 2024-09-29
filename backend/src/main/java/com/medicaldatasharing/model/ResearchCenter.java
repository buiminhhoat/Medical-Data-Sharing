package com.medicaldatasharing.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.PrePersist;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResearchCenter extends User {
    @PrePersist
    private void addPrefixToId() {
        if (this.getId() == null || this.getId().isEmpty()) {
            this.setId("ResearchCenter-" + java.util.UUID.randomUUID().toString());
        }
    }
}
