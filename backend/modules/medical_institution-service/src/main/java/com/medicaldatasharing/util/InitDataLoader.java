package com.medicaldatasharing.util;

import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.*;
import com.medicaldatasharing.dto.*;
import com.medicaldatasharing.enumeration.MedicalRecordStatus;
import com.medicaldatasharing.enumeration.RequestStatus;
import com.medicaldatasharing.form.*;
import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.service.HyperledgerService;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class InitDataLoader implements CommandLineRunner {

    private final static Logger LOG = Logger.getLogger(InitDataLoader.class.getName());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;
    @Override
    public void run(String... args) throws Exception {
        initMedicalInstitutions();
    }

    private void initMedicalInstitutions() {
        if (!medicalInstitutionRepository.findAll().isEmpty()) {
            return;
        }

        MedicalInstitution medicalInstitution1 = MedicalInstitution
                .builder()
                .fullName("Bệnh viện ĐHQGHN")
                .email("benhviendhqghn@gmail.com")
                .role(Constants.ROLE_MEDICAL_INSTITUTION)
                .username("benhviendhqghn@gmail.com")
                .password(passwordEncoder.encode("benhviendhqghn@gmail.com"))
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .enabled(true)
                .build();

        MedicalInstitution medicalInstitution2 = MedicalInstitution
                .builder()
                .fullName("Bệnh viện Việt Đức")
                .email("benhvienvietduc@gmail.com")
                .role(Constants.ROLE_MEDICAL_INSTITUTION)
                .username("benhvienvietduc@gmail.com")
                .password(passwordEncoder.encode("benhvienvietduc@gmail.com"))
                .address("40 P. Tràng Thi, Hàng Bông")
                .enabled(true)
                .build();

        medicalInstitutionRepository.save(medicalInstitution1);
        medicalInstitutionRepository.save(medicalInstitution2);
    }

    private void init() throws Exception {
        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

