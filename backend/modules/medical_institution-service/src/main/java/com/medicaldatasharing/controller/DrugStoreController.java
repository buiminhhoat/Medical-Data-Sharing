package com.medicaldatasharing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicaldatasharing.dto.MedicationPurchaseDto;
import com.medicaldatasharing.dto.PurchaseDto;
import com.medicaldatasharing.form.GetPrescriptionForm;
import com.medicaldatasharing.form.SearchDrugForm;
import com.medicaldatasharing.form.SearchPurchaseForm;
import com.medicaldatasharing.form.SendViewPrescriptionRequestForm;
import com.medicaldatasharing.service.DrugStoreService;
import com.medicaldatasharing.util.StringUtil;
import com.owlike.genson.Genson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/drugstore")
public class DrugStoreController {
    @Autowired
    private DrugStoreService drugStoreService;

    @PostMapping("/get-prescription-by-drugstore")
    public ResponseEntity<?> getPrescriptionByDrugStore(@Valid @ModelAttribute GetPrescriptionForm getPrescriptionForm,
                                                             BindingResult result) throws Exception {
        try {
            String getPrescriptionByPrescriptionId = drugStoreService.getPrescriptionByDrugStore(getPrescriptionForm);
            return ResponseEntity.status(HttpStatus.OK).body(getPrescriptionByPrescriptionId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/send-view-prescription-request")
    public ResponseEntity<?> sendViewPrescriptionRequest(@Valid @ModelAttribute SendViewPrescriptionRequestForm sendViewPrescriptionRequestForm,
                                                         BindingResult result) throws Exception {
        try {
            sendViewPrescriptionRequestForm.setDateCreated(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setDateModified(StringUtil.parseDate(new Date()));
            sendViewPrescriptionRequestForm.setSenderId(drugStoreService.getLoggedUser().getId());
            String viewPrescriptionRequestStr = drugStoreService.sendViewPrescriptionRequest(sendViewPrescriptionRequestForm);
            return ResponseEntity.status(HttpStatus.OK).body(viewPrescriptionRequestStr);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-drug-by-medicationId-and-ownerId")
    public ResponseEntity<?> getListDrugByMedicationIdAndOwnerId(@Valid @ModelAttribute SearchDrugForm searchDrugForm,
                                                       BindingResult result) throws Exception {
        try {
            String getListDrugByMedicationIdAndOwnerId = drugStoreService.getListDrugByMedicationIdAndOwnerId(searchDrugForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListDrugByMedicationIdAndOwnerId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/add-purchase")
    public ResponseEntity<?> addPurchase(@RequestParam("sellingPrescriptionDrug") String sellingDrugJson,
                                                     @RequestParam("prescriptionId") String prescriptionId,
                                                     @RequestParam("patientId") String patientId) throws Exception {
        try {
            List<MedicationPurchaseDto> medicationPurchaseDtoList = new ObjectMapper().readValue(sellingDrugJson,
                    new TypeReference<List<MedicationPurchaseDto>>() {});

            PurchaseDto purchaseDto = new PurchaseDto();
            purchaseDto.setPrescriptionId(prescriptionId);
            purchaseDto.setMedicationPurchaseList(new Genson().serialize(medicationPurchaseDtoList));
            purchaseDto.setPatientId(patientId);
            purchaseDto.setDrugStoreId(drugStoreService.getLoggedUser().getId());
            purchaseDto.setDateCreated(StringUtil.parseDate(new Date()));
            purchaseDto.setDateModified(StringUtil.parseDate(new Date()));

            String addPurchase = drugStoreService.addPurchase(purchaseDto);
            return ResponseEntity.status(HttpStatus.OK).body(addPurchase);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-list-purchase-by-drugStoreId")
    public ResponseEntity<?> getListPurchaseByDrugStoreId(@Valid @ModelAttribute SearchPurchaseForm searchPurchaseForm,
                                                        BindingResult result) {
        try {
            String getListPurchaseByDrugStoreId = drugStoreService.getListPurchaseByDrugStoreId(searchPurchaseForm);
            return ResponseEntity.status(HttpStatus.OK).body(getListPurchaseByDrugStoreId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/get-purchase-by-purchaseId")
    public ResponseEntity<?> getPurchaseByPurchaseId(HttpServletRequest httpServletRequest) {
        try {
            String purchaseId = httpServletRequest.getParameter("purchaseId");
            String getPurchaseByPurchaseId = drugStoreService.getPurchaseByPurchaseId(purchaseId);
            return ResponseEntity.status(HttpStatus.OK).body(getPurchaseByPurchaseId);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
