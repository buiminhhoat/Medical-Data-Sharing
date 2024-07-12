package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.SearchMedicationForm;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.HyperledgerService;
import com.medicaldatasharing.service.ManufacturerService;
import com.medicaldatasharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/manufacturer")
public class ManufacturerController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private HyperledgerService hyperledgerService;

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping("/get-list-medication-by-manufacturerId")
    public ResponseEntity<?> getListMedicationByManufacturerId(@Valid @ModelAttribute SearchMedicationForm searchMedicationForm) throws Exception {
        try {
            String getListMedication = manufacturerService.getListMedicationByManufacturerId(searchMedicationForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
