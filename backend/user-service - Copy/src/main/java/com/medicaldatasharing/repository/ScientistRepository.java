package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Scientist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScientistRepository extends JpaRepository<Scientist, String> {

    Scientist findByUsername(String username);
    Scientist findScientistById(String id);

    List<Scientist> findAllBy();

    List<Scientist> findAllByRole(String role);

    Scientist findScientistByEmail(String email);

    List<Scientist> findAllById(String id);

    List<Scientist> findScientistByIdAndResearchCenterId(String id, String researchCenterId);

    List<Scientist> findAllByResearchCenterId(String researchCenterId);
}
