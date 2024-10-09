package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.ResearchCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchCenterRepository extends JpaRepository<ResearchCenter, String> {
    List<ResearchCenter> findAllBy();

    ResearchCenter findResearchCenterById(String id);

    ResearchCenter findResearchCenterByEmail(String email);

    List<ResearchCenter> findAllById(String id);
}
