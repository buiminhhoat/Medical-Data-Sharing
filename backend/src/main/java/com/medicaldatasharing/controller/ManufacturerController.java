package com.medicaldatasharing.controller;

import com.medicaldatasharing.form.AddDrugForm;
import com.medicaldatasharing.form.AddMedicationForm;
import com.medicaldatasharing.form.GetDrugReactionForm;
import com.medicaldatasharing.form.SearchMedicationForm;
import com.medicaldatasharing.service.DoctorService;
import com.medicaldatasharing.service.HyperledgerService;
import com.medicaldatasharing.service.ManufacturerService;
import com.medicaldatasharing.service.UserService;
import com.medicaldatasharing.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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

    @PostMapping("/add-medication")
    public ResponseEntity<?> addMedication(@Valid @ModelAttribute AddMedicationForm addMedicationForm, BindingResult result) throws Exception {
        try {
            addMedicationForm.setDateCreated(StringUtil.parseDate(new Date()));
            addMedicationForm.setDateModified(StringUtil.parseDate(new Date()));
            String addMedication = manufacturerService.addMedication(addMedicationForm);
            return ResponseEntity.status(HttpStatus.OK).body(addMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-drug")
    public ResponseEntity<?> addDrug(@Valid @ModelAttribute AddDrugForm addDrugForm, BindingResult result) throws Exception {
        try {
            String addDrug = manufacturerService.addDrug(addDrugForm);
            return ResponseEntity.status(HttpStatus.OK).body(addDrug);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-drug-reaction-by-manufacturer")
    public ResponseEntity<?> getListDrugReactionByManufacturer() throws Exception {
        try {
            String getListMedication = manufacturerService.getListDrugReactionByManufacturer();
            return ResponseEntity.status(HttpStatus.OK).body(getListMedication);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
