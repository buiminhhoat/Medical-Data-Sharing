package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.ResearchInstitute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchInstituteRepository extends JpaRepository<ResearchInstitute, String> {
    List<ResearchInstitute> findAllBy();

    ResearchInstitute findResearchInstituteByResearchInstituteId(String researchInstituteId);
}
