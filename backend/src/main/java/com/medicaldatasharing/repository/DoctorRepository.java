package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.MedicalInstitution;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.Doc;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    Doctor findByUsername(String username);
    Doctor findDoctorById(String id);

    List<Doctor> findAllBy();

    List<Doctor> findAllByRole(String role);

    List<Doctor> findAllByMedicalInstitutionId(String medicalInstitutionId);

    List<Doctor> findAllById(String id);

    List<Doctor> findDoctorByIdAndMedicalInstitutionId(String id, String medicalInstitutionId);
}
