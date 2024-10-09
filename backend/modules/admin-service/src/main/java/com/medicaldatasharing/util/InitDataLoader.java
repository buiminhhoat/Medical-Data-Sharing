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
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        init();
    }


    private void initUsers() throws Exception {
        if (!adminRepository.findAll().isEmpty()) {
            return;
        }

        Admin admin = Admin
                .builder()
                .fullName("Bùi Minh Hoạt")
                .username("official.buiminhhoat@gmail.com")
                .email("official.buiminhhoat@gmail.com")
                .password(passwordEncoder.encode("official.buiminhhoat@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_ADMIN)
                .build();


        try {
            adminRepository.save(admin);

        } catch (Exception e) {
            adminRepository.delete(admin);
            e.printStackTrace();
        }
    }


    private void init() throws Exception {
        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

