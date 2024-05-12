package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {

    Patient findByUsername(String username);
}

