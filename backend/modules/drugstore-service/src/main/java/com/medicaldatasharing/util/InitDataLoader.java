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
    private DrugStoreRepository drugStoreRepository;
    @Autowired
    private HyperledgerService hyperledgerService;

    @Override
    public void run(String... args) throws Exception {
        initDrugStore();
        init();
    }

    private void initDrugStore() {
        if (!drugStoreRepository.findAll().isEmpty()) {
            return;
        }
        DrugStore drugStoreA = DrugStore
                .builder()
                .fullName("Nhà thuốc A")
                .email("nhathuoca@gmail.com")
                .businessLicenseNumber("82933928848")
                .role(Constants.ROLE_DRUG_STORE)
                .username("nhathuoca@gmail.com")
                .password(passwordEncoder.encode("nhathuoca@gmail.com"))
                .enabled(true)
                .build();
        drugStoreRepository.save(drugStoreA);

        DrugStore drugStoreB = DrugStore
                .builder()
                .fullName("Nhà thuốc B")
                .email("nhathuocb@gmail.com")
                .businessLicenseNumber("00033928848")
                .role(Constants.ROLE_DRUG_STORE)
                .username("nhathuocb@gmail.com")
                .password(passwordEncoder.encode("nhathuocb@gmail.com"))
                .enabled(true)
                .build();
        drugStoreRepository.save(drugStoreB);

        try {
            RegisterUserHyperledger.enrollOrgAppUsers(drugStoreA.getEmail(), Config.DRUG_STORE_ORG, drugStoreA.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(drugStoreB.getEmail(), Config.DRUG_STORE_ORG, drugStoreB.getId());
        } catch (Exception e) {
            drugStoreRepository.delete(drugStoreA);
            drugStoreRepository.delete(drugStoreB);
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

