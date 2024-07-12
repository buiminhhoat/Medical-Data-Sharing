package com.medicaldatasharing.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResearchCenter extends User {

}
