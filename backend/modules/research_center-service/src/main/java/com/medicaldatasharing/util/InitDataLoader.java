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
    private ResearchCenterRepository researchCenterRepository;

    @Override
    public void run(String... args) throws Exception {
        initResearchCenter();
        init();
    }

    private void initResearchCenter() {
        if (!researchCenterRepository.findAll().isEmpty()) {
            return;
        }

        ResearchCenter researchCenter1 = ResearchCenter
                .builder()
                .fullName("Trung tâm nghiên cứu dược phẩm ABC")
                .email("trungtamnghiencuuabc@gmail.com")
                .username("trungtamnghiencuuabc@gmail.com")
                .password(passwordEncoder.encode("trungtamnghiencuuabc@gmail.com"))
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .role(Constants.ROLE_RESEARCH_CENTER)
                .enabled(true)
                .build();

        ResearchCenter researchCenter2 = ResearchCenter
                .builder()
                .fullName("Trung tâm nghiên cứu nghiên cứu thuốc XYZ")
                .email("trungtamnghiencuuthuocxyz@gmail.com")
                .username("trungtamnghiencuuthuocxyz@gmail.com")
                .password(passwordEncoder.encode("trungtamnghiencuuthuocxyz@gmail.com"))
                .address("40 P. Tràng Thi, Hàng Bông")
                .role(Constants.ROLE_RESEARCH_CENTER)
                .enabled(true)
                .build();
        researchCenterRepository.save(researchCenter1);
        researchCenterRepository.save(researchCenter2);
    }

    private void init() throws Exception {
        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

