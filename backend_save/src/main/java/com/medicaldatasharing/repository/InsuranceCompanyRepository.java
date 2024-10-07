package com.medicaldatasharing.repository;

import com.medicaldatasharing.model.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, String> {
    InsuranceCompany findInsuranceCompanyById(String id);

    InsuranceCompany findInsuranceCompanyByEmail(String mail);

    List<InsuranceCompany> findAllById(String id);
}
