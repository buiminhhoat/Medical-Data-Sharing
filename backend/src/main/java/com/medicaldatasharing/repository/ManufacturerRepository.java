package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {
    Manufacturer findManufacturerById(String id);

    Manufacturer findManufacturerByEmail(String mail);
}
