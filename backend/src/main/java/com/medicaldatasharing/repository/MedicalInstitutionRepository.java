package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.MedicalInstitution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalInstitutionRepository extends JpaRepository<MedicalInstitution, String> {
    List<MedicalInstitution> findAllBy();

    MedicalInstitution findMedicalInstitutionById(String id);

    MedicalInstitution findMedicalInstitutionByEmail(String email);
}
