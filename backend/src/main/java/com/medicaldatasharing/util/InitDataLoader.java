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
import org.bouncycastle.cert.ocsp.Req;
import org.json.JSONObject;
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
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private DrugStoreRepository drugStoreRepository;
    @Autowired
    private ScientistRepository scientistRepository;
    @Autowired
    private ResearchCenterRepository researchCenterRepository;
    @Autowired
    private HyperledgerService hyperledgerService;

    @Override
    public void run(String... args) throws Exception {
        initMedicalInstitutions();
        initManufacturer();
        initDrugStore();
        initResearchCenter();
        initUsers();
        initScientist();
        init();
    }

    private void initMedicalInstitutions() {
        if (!medicalInstitutionRepository.findAll().isEmpty()) {
            return;
        }

        MedicalInstitution medicalInstitution1 = MedicalInstitution
                .builder()
                .name("Bệnh viện ĐHQGHN")
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .membershipOrganizationId(Config.ORG2)
                .build();

        MedicalInstitution medicalInstitution2 = MedicalInstitution
                .builder()
                .name("Bệnh viện Việt Đức")
                .address("40 P. Tràng Thi, Hàng Bông")
                .membershipOrganizationId(Config.ORG2)
                .build();

        medicalInstitutionRepository.save(medicalInstitution1);
        medicalInstitutionRepository.save(medicalInstitution2);
    }

    private void initManufacturer() {
        if (!manufacturerRepository.findAll().isEmpty()) {
            return;
        }
        Manufacturer manufacturer1 = Manufacturer
                .builder()
                .firstName("ABC")
                .lastName("Công ty dược phẩm")
                .email("congtyduocphama@gmail.com")
                .businessLicenseNumber("01993884423")
                .role(Constants.ROLE_MANUFACTURER)
                .username("congtyduocphama")
                .password("congtyduocphama")
                .build();
        manufacturerRepository.save(manufacturer1);

        try {
            RegisterUserHyperledger.enrollOrgAppUsers(manufacturer1.getEmail(), Config.MANUFACTURER_ORG, manufacturer1.getId());
        } catch (Exception e) {
            manufacturerRepository.delete(manufacturer1);
            e.printStackTrace();
        }
    }

    private void initDrugStore() {
        if (!drugStoreRepository.findAll().isEmpty()) {
            return;
        }
        DrugStore drugStoreA = DrugStore
                .builder()
                .firstName("A")
                .lastName("Nhà thuốc")
                .email("nhathuoca@gmail.com")
                .businessLicenseNumber("82933928848")
                .role(Constants.ROLE_DRUG_STORE)
                .username("nhathuoca")
                .password("nhathuoca")
                .build();
        drugStoreRepository.save(drugStoreA);

        DrugStore drugStoreB = DrugStore
                .builder()
                .firstName("B")
                .lastName("Nhà thuốc")
                .email("nhathuocb@gmail.com")
                .businessLicenseNumber("00033928848")
                .role(Constants.ROLE_DRUG_STORE)
                .username("nhathuocb")
                .password("nhathuocb")
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

    private void initResearchCenter() {
        if (!researchCenterRepository.findAll().isEmpty()) {
            return;
        }

        ResearchCenter researchCenter1 = ResearchCenter
                .builder()
                .name("Viện nghiên cứu dược phẩm ABC")
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .membershipOrganizationId(Config.ORG5)
                .build();

        ResearchCenter researchCenter2 = ResearchCenter
                .builder()
                .name("Viện nghiên cứu thuốc XYZ")
                .address("40 P. Tràng Thi, Hàng Bông")
                .membershipOrganizationId(Config.ORG5)
                .build();
        researchCenterRepository.save(researchCenter1);
        researchCenterRepository.save(researchCenter2);
    }

    private void initUsers() throws Exception {
        if (!patientRepository.findAll().isEmpty()) {
            return;
        }
        Patient patient1 = Patient
                .builder()
                .firstName("Vinh")
                .lastName("Đào Quang")
                .address("144 Xuân Thủy, Cầu Giấy, Hà Nội")
                .gender("Male")
                .birthday(new Date(1047722400000l))
                .username("daoquangvinh@gmail.com")
                .email("daoquangvinh@gmail.com")
                .password(passwordEncoder.encode("daoquangvinh@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_PATIENT)
                .build();
        patientRepository.save(patient1);

        Patient patient2 = Patient
                .builder()
                .firstName("Huy")
                .lastName("Phạm Lê")
                .address("")
                .gender("Male")
                .birthday(new Date(1062928800000l)) //29.05.1996 10h
                .username("lehuy5c2003@gmail.com")
                .email("lehuy5c2003@gmail.com")
                .password(passwordEncoder.encode("lehuy5c2003@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_PATIENT)
                .build();
        patientRepository.save(patient2);

        List<MedicalInstitution> medicalInstitutions = medicalInstitutionRepository.findAll();
        MedicalInstitution medicalInstitution1 = medicalInstitutions.get(0);
        MedicalInstitution medicalInstitution2 = medicalInstitutions.get(1);

        Doctor doctor1 = Doctor
                .builder()
                .firstName("Tâm")
                .lastName("Trần Thanh")
                .username("tranthanhtam@gmail.com")
                .email("tranthanhtam@gmail.com")
                .password(passwordEncoder.encode("tranthanhtam"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .occupation("oncologist")
                .medicalInstitution(medicalInstitution1)
                .build();
        LOG.info(doctor1.toString());

        Doctor doctor2 = Doctor
                .builder()
                .firstName("Hải")
                .lastName("Nguyễn Thanh")
                .username("nguyenthanhhai@gmail.com")
                .email("nguyenthanhhai@gmail.com")
                .password(passwordEncoder.encode("nguyenthanhhai"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .occupation("cardiologist")
                .medicalInstitution(medicalInstitution2)
                .build();
        LOG.info(doctor2.toString());

        Doctor doctorAdmin = Doctor
                .builder()
                .firstName("Dũng")
                .lastName("Nguyễn Tiến")
                .username("nguyentiendung@gmail.com")
                .email("nguyentiendung@gmail.com")
                .password(passwordEncoder.encode("nguyentiendung"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR_ADMIN)
                .medicalInstitution(medicalInstitution1)
                .build();

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
        doctorRepository.save(doctorAdmin);

        Admin admin = Admin
                .builder()
                .firstName("Hoạt")
                .lastName("Bùi Minh")
                .username("official.buiminhhoat@gmail.com")
                .email("official.buiminhhoat@gmail.com")
                .password(passwordEncoder.encode("official.buiminhhoat@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_SUPER_ADMIN)
                .build();

        adminRepository.save(admin);

        try {
            RegisterUserHyperledger.enrollOrgAppUsers(doctor1.getEmail(), Config.DOCTOR_ORG, doctor1.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(doctor2.getEmail(), Config.DOCTOR_ORG, doctor2.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(patient1.getEmail(), Config.PATIENT_ORG, patient1.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(patient2.getEmail(), Config.PATIENT_ORG, patient2.getId());
        } catch (Exception e) {
            patientRepository.delete(patient1);
            patientRepository.delete(patient2);
            doctorRepository.delete(doctor1);
            doctorRepository.delete(doctor2);
            adminRepository.delete(admin);
            e.printStackTrace();
        }
    }

    private void initScientist() {
        if (!scientistRepository.findAll().isEmpty()) {
            return;
        }
        List<ResearchCenter> researchCenterList = researchCenterRepository.findAll();
        ResearchCenter researchCenter1 = researchCenterList.get(0);
        ResearchCenter researchCenter2 = researchCenterList.get(1);
        Scientist scientist1 = Scientist
                .builder()
                .firstName("Hoạt")
                .lastName("Bùi Minh Hoạt")
                .username("scientist1@gmail.com")
                .email("scientist1@gmail.com")
                .password(passwordEncoder.encode("scientist1@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_SCIENTIST)
                .researchCenter(researchCenter1)
                .build();

        scientistRepository.save(scientist1);
        LOG.info(scientist1.toString());
        try {
            RegisterUserHyperledger.enrollOrgAppUsers(scientist1.getEmail(), Config.SCIENTIST_ORG, scientist1.getId());
        } catch (Exception e) {
            scientistRepository.delete(scientist1);
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        Patient patient = patientRepository.findByUsername("lehuy5c2003@gmail.com");
        String patientId = patient.getId();

        Doctor doctor1 = doctorRepository.findByUsername("nguyenthanhhai@gmail.com");
        String doctor1Id = doctor1.getId();

        Doctor doctor2 = doctorRepository.findByUsername("tranthanhtam@gmail.com");
        String doctor2Id = doctor2.getId();

        Scientist scientist1 = scientistRepository.findByUsername("scientist1@gmail.com");
        String scientist1Id = scientist1.getId();

        try {
            Date dateModified = new Date();

            SendAppointmentRequestForm sendAppointmentRequestForm = new SendAppointmentRequestForm();
            sendAppointmentRequestForm.setSenderId(patientId);
            sendAppointmentRequestForm.setRecipientId(doctor1Id);
            sendAppointmentRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            AppointmentRequest appointmentRequest = hyperledgerService.sendAppointmentRequest(
                    patient,
                    sendAppointmentRequestForm);
            System.out.println("appointmentRequest: " + appointmentRequest);

            Manufacturer manufacturer = manufacturerRepository.findManufacturerByEmail("congtyduocphama@gmail.com");

            AddMedicationForm addMedicationForm = new AddMedicationForm();
            addMedicationForm.setManufacturerId(manufacturer.getId());
            addMedicationForm.setMedicationName("Paracetamol");
            addMedicationForm.setDescription("Điều trị đau đầu");
            addMedicationForm.setDateModified(StringUtil.parseDate(dateModified));
            Medication medication = hyperledgerService.addMedication(manufacturer, addMedicationForm);

            AddDrugForm addDrugForm = new AddDrugForm();
            addDrugForm.setMedicationId(medication.getMedicationId());
            addDrugForm.setManufactureDate(StringUtil.parseDate(StringUtil.createDate("2024-01-01")));
            addDrugForm.setExpirationDate(StringUtil.parseDate(StringUtil.createDate("2024-12-31")));

            Drug drug = hyperledgerService.addDrug(manufacturer, addDrugForm);
            System.out.println(drug);
            AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
            addPrescriptionForm.setDrugReaction("");
            List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
            prescriptionDetailsList.add(new PrescriptionDetails("", "",
                    medication.getMedicationId(), "10", "Uống 2 viên vào mỗi trưa và tối"));
            prescriptionDetailsList.add(new PrescriptionDetails("", "",
                    medication.getMedicationId(), "20", "Uống 4 viên vào mỗi trưa và tối"));
            addPrescriptionForm.setPrescriptionDetailsList(new Genson().serialize(prescriptionDetailsList));

            String testName = "Cardiovascular Test";

            String details = "relevant Parameters";

            MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
            medicalRecordDto.setRequestId(appointmentRequest.getRequestId());
            medicalRecordDto.setPatientId(patientId);
            medicalRecordDto.setDoctorId(doctor1Id);
            medicalRecordDto.setMedicalInstitutionId(doctor1.getMedicalInstitution().getMedicalInstitutionId());
            medicalRecordDto.setDateModified(StringUtil.parseDate(dateModified));
            medicalRecordDto.setTestName(testName);
            medicalRecordDto.setDetails(details);
            medicalRecordDto.setHashFile("");
            medicalRecordDto.setAddPrescription(addPrescriptionForm.toJSONObject().toString());

            MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(doctor1, medicalRecordDto);
            System.out.println("chaincodeMedicalRecord: " + medicalRecord);

            MedicalRecord getMedicalRecordByPatient = hyperledgerService.getMedicalRecordByPatient(
                    patient,
                    medicalRecord.getMedicalRecordId()
            );

            System.out.println("getMedicalRecordByPatient: " + getMedicalRecordByPatient);


            DefineMedicalRecordForm defineMedicalRecordForm = new DefineMedicalRecordForm();
            defineMedicalRecordForm.setMedicalRecordId(medicalRecord.getMedicalRecordId());
            defineMedicalRecordForm.setMedicalRecordStatus(MedicalRecordStatus.ACCEPTED.toString());

            medicalRecord = hyperledgerService.defineMedicalRecord(patient, defineMedicalRecordForm);
            System.out.println(medicalRecord);

            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setPatientId(patientId);
            searchMedicalRecordForm.setFrom(StringUtil.createDate("2024-01-01"));
            searchMedicalRecordForm.setUntil(StringUtil.createDate("2024-12-31"));
            searchMedicalRecordForm.setPrescriptionId(medicalRecord.getPrescriptionId());
            searchMedicalRecordForm.setHashFile("");
            List<MedicalRecordPreviewDto> medicalRecordPreviewDtoList
                    = hyperledgerService.getListMedicalRecordByPatientQuery(patient, searchMedicalRecordForm);

            System.out.println("medicalRecordPreviewDtoList: " + medicalRecordPreviewDtoList);

            SendEditRequestForm sendEditRequestForm = new SendEditRequestForm();
            sendEditRequestForm.setSenderId(doctor1Id);
            sendEditRequestForm.setRecipientId(patientId);
            sendEditRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            medicalRecord.setDetails("details -_-");
            sendEditRequestForm.setMedicalRecordJson(JsonConverter.objectToJson(medicalRecord).toString());

            EditRequest editRequest = hyperledgerService.sendEditRequest(doctor1, sendEditRequestForm);
            System.out.println("editRequest: " + editRequest);

            EditRequest getEditRequest = hyperledgerService.getEditRequest(doctor1, editRequest.getRequestId());
            System.out.println("getEditRequest: " + getEditRequest);

            DefineRequestForm defineRequestForm = new DefineRequestForm();
            defineRequestForm.setRequestId(editRequest.getRequestId());
            defineRequestForm.setRequestStatus(RequestStatus.ACCEPTED.toString());
            MedicalRecord defineEditRequest = hyperledgerService.defineEditRequest(
                    patient,
                    defineRequestForm
            );

            System.out.println(defineEditRequest.toString());

            SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
            sendViewRequestForm.setSenderId(doctor2Id);
            sendViewRequestForm.setRecipientId(patientId);
            sendViewRequestForm.setDateModified(StringUtil.parseDate(dateModified));

            ViewRequest viewRequest = hyperledgerService.sendViewRequest(doctor2, sendViewRequestForm);
            System.out.println(viewRequest);

            SearchViewRequestForm searchViewRequestForm = new SearchViewRequestForm();
            searchViewRequestForm.setRequestId(viewRequest.getRequestId());
            searchViewRequestForm.setSenderId(doctor2Id);
            searchViewRequestForm.setFrom(StringUtil.createDate("2024-01-01"));
            searchViewRequestForm.setUntil(StringUtil.createDate("2024-12-31"));
            searchViewRequestForm.setPrescriptionId(medicalRecord.getPrescriptionId());
            searchViewRequestForm.setHashFile("");
            List<ViewRequest> viewRequestList
                    = hyperledgerService.getListViewRequestBySenderQuery(doctor2, searchViewRequestForm);

            System.out.println(viewRequestList);

            List<MedicalRecord> changeHistory = hyperledgerService.getMedicalRecordChangeHistory(patient, medicalRecord.getMedicalRecordId());

            SearchMedicationForm searchMedicationForm = new SearchMedicationForm();
            searchMedicationForm.setMedicationId(medication.getMedicationId());
            searchMedicationForm.setFrom(StringUtil.createDate("2024-01-01"));
            searchMedicationForm.setUntil(StringUtil.createDate("2024-12-31"));
            List<MedicationPreviewDto> medicationPreviewDtoList = hyperledgerService.getListMedication(doctor1, searchMedicationForm);
            System.out.println(medicationPreviewDtoList);


            DrugStore drugStore = drugStoreRepository.findDrugStoreByEmail("nhathuoca@gmail.com");

            SendViewPrescriptionRequestDto sendViewPrescriptionRequestDto = new SendViewPrescriptionRequestDto();
            sendViewPrescriptionRequestDto.setSenderId(drugStore.getId());
            sendViewPrescriptionRequestDto.setRecipientId(patientId);
            sendViewPrescriptionRequestDto.setPrescriptionId(medicalRecord.getPrescriptionId());
            sendViewPrescriptionRequestDto.setDateModified(StringUtil.parseDate(dateModified));

            ViewPrescriptionRequest sendViewPrescriptionRequest = hyperledgerService.sendViewPrescriptionRequest(
                    drugStore,
                    sendViewPrescriptionRequestDto
            );

            System.out.println(sendViewPrescriptionRequest);

            DefineViewPrescriptionRequestDto defineViewPrescriptionRequestDto = new DefineViewPrescriptionRequestDto();
            defineViewPrescriptionRequestDto.setRequestId(sendViewPrescriptionRequest.getRequestId());
            defineViewPrescriptionRequestDto.setRequestStatus(RequestStatus.ACCEPTED.toString());
            ViewPrescriptionRequest defineViewPrescriptionRequest = hyperledgerService.defineViewPrescriptionRequest(
                    patient,
                    defineViewPrescriptionRequestDto
            );
            System.out.println(defineViewPrescriptionRequest);

            GetPrescriptionForm getPrescriptionForm = new GetPrescriptionForm();
            getPrescriptionForm.setPrescriptionId(medicalRecord.getPrescriptionId());
            getPrescriptionForm.setDrugStoreId(drugStore.getId());
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescription(
                    drugStore,
                    getPrescriptionForm
            );
            System.out.println(prescriptionDto);

            TransferDrugDto transferDrugDto = new TransferDrugDto();
            transferDrugDto.setDrugId(drug.getDrugId());
            transferDrugDto.setNewOwnerId(drugStore.getId());
            drug = hyperledgerService.transferDrug(
                    manufacturer,
                    transferDrugDto
            );

            System.out.println(drug);
            
            MedicationPurchaseDto medicationPurchaseDto = new MedicationPurchaseDto();
            medicationPurchaseDto.setMedicationId(medication.getMedicationId());
            List<String> drugIdList = new ArrayList<>();
            drugIdList.add(drug.getDrugId());
            medicationPurchaseDto.setDrugIdList(drugIdList);
            medicationPurchaseDto.setPrescriptionDetailId(
                    prescriptionDto.getPrescriptionDetailsList().get(0).getPrescriptionDetailId()
            );

            PurchaseDto purchaseDto = new PurchaseDto();
            purchaseDto.setPrescriptionId(medicalRecord.getPrescriptionId());

            List<MedicationPurchaseDto> medicationPurchaseList = new ArrayList<>();
            medicationPurchaseList.add(medicationPurchaseDto);
            purchaseDto.setMedicationPurchaseList(new Genson().serialize(medicationPurchaseList));
            purchaseDto.setPatientId(patientId);
            purchaseDto.setDrugStoreId(drugStore.getId());
            purchaseDto.setDateModified(StringUtil.parseDate(dateModified));

            Purchase purchase = hyperledgerService.addPurchase(
                    drugStore,
                    purchaseDto
            );
            System.out.println(purchase);

            SendViewRequestForm sendViewRequestFormByScientist = new SendViewRequestForm();
            sendViewRequestFormByScientist.setSenderId(scientist1Id);
            sendViewRequestFormByScientist.setRecipientId(patientId);
            sendViewRequestFormByScientist.setDateModified(StringUtil.parseDate(dateModified));

            ViewRequest viewRequestByScientist = hyperledgerService.sendViewRequest(
                    scientist1,
                    sendViewRequestFormByScientist);
            System.out.println(viewRequestByScientist);


            DefineViewRequestDto defineViewRequestDto = new DefineViewRequestDto();
            defineViewRequestDto.setRequestId(viewRequestByScientist.getRequestId());
            defineViewRequestDto.setRequestStatus(RequestStatus.ACCEPTED.toString());

            ViewRequest defineViewRequestByScientist = hyperledgerService.defineViewRequest(
                    patient,
                    defineViewRequestDto
            );
            System.out.println(defineViewRequestByScientist);

            GetListAllAuthorizedPatientForScientistDto getListAllAuthorizedPatientForScientistDto = new GetListAllAuthorizedPatientForScientistDto();
            getListAllAuthorizedPatientForScientistDto.setScientistId(scientist1Id);

            List<String> getListAllAuthorizedPatientForScientistList = hyperledgerService.getListAllAuthorizedPatientForScientist(
                    scientist1,
                    getListAllAuthorizedPatientForScientistDto
            );

            System.out.println(getListAllAuthorizedPatientForScientistList);

            GetListAuthorizedMedicalRecordByScientistQueryDto getListAuthorizedMedicalRecordByScientistQueryDto = new GetListAuthorizedMedicalRecordByScientistQueryDto();
            getListAuthorizedMedicalRecordByScientistQueryDto.setScientistId(scientist1Id);
            getListAuthorizedMedicalRecordByScientistQueryDto.setPatientId(patientId);

            List<MedicalRecord> getListAuthorizedMedicalRecordByScientistQuery
                    = hyperledgerService.getListAuthorizedMedicalRecordByScientistQuery(
                    scientist1,
                    getListAuthorizedMedicalRecordByScientistQueryDto
            );

            System.out.println(getListAuthorizedMedicalRecordByScientistQuery);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

