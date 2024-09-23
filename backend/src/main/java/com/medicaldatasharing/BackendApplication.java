package com.medicaldatasharing;

import com.medicaldatasharing.util.AESVaultUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BackendApplication.class, args);
    }

}
