package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.DrugStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrugStoreRepository extends JpaRepository<DrugStore, String> {
    DrugStore findDrugStoreById(String id);

    DrugStore findDrugStoreByEmail(String mail);

    List<DrugStore> findAllById(String id);
}
