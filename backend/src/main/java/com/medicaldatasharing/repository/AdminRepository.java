package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Admin findByUsername(String username);
}
