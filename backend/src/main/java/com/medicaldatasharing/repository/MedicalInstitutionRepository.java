package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.MedicalInstitution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalInstitutionRepository extends JpaRepository<MedicalInstitution, String> {

}
