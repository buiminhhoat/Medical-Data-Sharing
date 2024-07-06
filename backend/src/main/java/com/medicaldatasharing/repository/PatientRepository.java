package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {

    Patient findByUsername(String username);
    Patient findPatientById(String id);
}

