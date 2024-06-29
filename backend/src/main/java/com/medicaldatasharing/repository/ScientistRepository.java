package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Scientist;
import com.medicaldatasharing.model.ResearchCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScientistRepository extends JpaRepository<Scientist, String> {

    Scientist findByUsername(String username);

    List<Scientist> findAllBy();

    List<Scientist> findAllByRole(String role);

    List<Scientist> findAllByResearchCenter(ResearchCenter researchCenter);

    List<Scientist> findAllByResearchCenterAndRole(ResearchCenter researchCenter, String role);
}
