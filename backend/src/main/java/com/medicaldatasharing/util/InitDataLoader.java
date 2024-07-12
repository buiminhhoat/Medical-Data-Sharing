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
    private InsuranceCompanyRepository insuranceCompanyRepository;
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
        initInsuranceCompany();
        init();
    }

    private void initMedicalInstitutions() {
        if (!medicalInstitutionRepository.findAll().isEmpty()) {
            return;
        }

        MedicalInstitution medicalInstitution1 = MedicalInstitution
                .builder()
                .fullName("Bệnh viện ĐHQGHN")
                .email("benhviendhqghn@gmail.com")
                .role(Constants.ROLE_HOSPITAL)
                .username("benhviendhqghn@gmail.com")
                .password(passwordEncoder.encode("benhviendhqghn@gmail.com"))
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .build();

        MedicalInstitution medicalInstitution2 = MedicalInstitution
                .builder()
                .fullName("Bệnh viện Việt Đức")
                .email("benhvienvietduc@gmail.com")
                .role(Constants.ROLE_HOSPITAL)
                .username("benhvienvietduc@gmail.com")
                .password(passwordEncoder.encode("benhvienvietduc@gmail.com"))
                .address("40 P. Tràng Thi, Hàng Bông")
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
                .fullName("Công ty dược phẩm A")
                .email("congtyduocphama@gmail.com")
                .businessLicenseNumber("01993884423")
                .role(Constants.ROLE_MANUFACTURER)
                .username("congtyduocphama@gmail.com")
                .password(passwordEncoder.encode("congtyduocphama@gmail.com"))
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
                .fullName("Nhà thuốc A")
                .email("nhathuoca@gmail.com")
                .businessLicenseNumber("82933928848")
                .role(Constants.ROLE_DRUG_STORE)
                .username("nhathuoca@gmail.com")
                .password(passwordEncoder.encode("nhathuoca@gmail.com"))
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

    private void initInsuranceCompany() {
        if (!insuranceCompanyRepository.findAll().isEmpty()) {
            return;
        }
        InsuranceCompany insuranceCompany1 = InsuranceCompany
                .builder()
                .fullName("Công ty bảo hiểm C")
                .email("congtybaohiemc@gmail.com")
                .businessLicenseNumber("596475233654")
                .role(Constants.ROLE_DRUG_STORE)
                .username("congtybaohiemc@gmail.com")
                .password(passwordEncoder.encode("congtybaohiemc@gmail.com"))
                .build();
        insuranceCompanyRepository.save(insuranceCompany1);

        try {
            RegisterUserHyperledger.enrollOrgAppUsers(insuranceCompany1.getEmail(), Config.INSURANCE_COMPANY_ORG, insuranceCompany1.getId());
        } catch (Exception e) {
            insuranceCompanyRepository.delete(insuranceCompany1);
            e.printStackTrace();
        }
    }

    private void initResearchCenter() {
        if (!researchCenterRepository.findAll().isEmpty()) {
            return;
        }

        ResearchCenter researchCenter1 = ResearchCenter
                .builder()
                .fullName("Viện nghiên cứu dược phẩm ABC")
                .email("viennghiencuuduocphamabc@gmail.com")
                .username("viennghiencuuduocphamabc@gmail.com")
                .password(passwordEncoder.encode("viennghiencuuduocphamabc@gmail.com"))
                .address("182 Lương Thế Vinh, Thanh Xuân Bắc, Thanh Xuân, Hà Nội")
                .build();

        ResearchCenter researchCenter2 = ResearchCenter
                .builder()
                .fullName("Viện nghiên cứu thuốc XYZ")
                .email("viennghiencuuthuocxyz@gmail.com")
                .username("viennghiencuuthuocxyz@gmail.com")
                .password(passwordEncoder.encode("viennghiencuuthuocxyz@gmail.com"))
                .address("40 P. Tràng Thi, Hàng Bông")
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
                .fullName("Đào Quang Vinh")
                .address("144 Xuân Thủy, Cầu Giấy, Hà Nội")
                .gender("Nam")
                .dateBirthday(new Date(1047722400000l))
                .username("daoquangvinh@gmail.com")
                .email("daoquangvinh@gmail.com")
                .password(passwordEncoder.encode("daoquangvinh@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_PATIENT)
                .build();
        patientRepository.save(patient1);

        Patient patient2 = Patient
                .builder()
                .fullName("Phạm Lê Huy")
                .address("")
                .gender("Nam")
                .dateBirthday(new Date(1062928800000l)) //29.05.1996 10h
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
                .fullName("Trần Thanh Tâm")
                .username("tranthanhtam@gmail.com")
                .email("tranthanhtam@gmail.com")
                .password(passwordEncoder.encode("tranthanhtam@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .department("Chuyên khoa ung thư")
                .medicalInstitutionId(medicalInstitution1.getId())
                .avatar("https://cdn-icons-png.flaticon.com/512/6998/6998099.png")
                .build();
        LOG.info(doctor1.toString());

        Doctor doctor2 = Doctor
                .builder()
                .fullName("Nguyễn Thanh Hải")
                .username("nguyenthanhhai@gmail.com")
                .email("nguyenthanhhai@gmail.com")
                .password(passwordEncoder.encode("nguyenthanhhai@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .department("Chuyên khoa tim mạch")
                .medicalInstitutionId(medicalInstitution2.getId())
                .avatar("https://cdn-icons-png.flaticon.com/512/6998/6998099.png")
                .build();
        LOG.info(doctor2.toString());

        Doctor doctor3 = Doctor
                .builder()
                .fullName("Nguyễn Tiến Dũng")
                .username("nguyentiendung@gmail.com")
                .email("nguyentiendung@gmail.com")
                .password(passwordEncoder.encode("nguyentiendung@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .department("Chuyên khoa ung thư")
                .medicalInstitutionId(medicalInstitution1.getId())
                .avatar("https://cdn-icons-png.flaticon.com/512/6998/6998099.png")
                .build();

        Doctor doctor4 = Doctor
                .builder()
                .fullName("Nguyễn Tiến Cừu")
                .username("nguyentiencuu@gmail.com")
                .email("nguyentiencuu@gmail.com")
                .password(passwordEncoder.encode("nguyentiencuu@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_DOCTOR)
                .department("Chuyên khoa ung thư")
                .medicalInstitutionId(medicalInstitution1.getId())
                .avatar("https://cdn-icons-png.flaticon.com/512/6998/6998099.png")
                .build();

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);
        doctorRepository.save(doctor3);
        doctorRepository.save(doctor4);

        Admin admin = Admin
                .builder()
                .fullName("Bùi Minh Hoạt")
                .username("official.buiminhhoat@gmail.com")
                .email("official.buiminhhoat@gmail.com")
                .password(passwordEncoder.encode("official.buiminhhoat@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_ADMIN)
                .build();

        adminRepository.save(admin);

        try {
            RegisterUserHyperledger.enrollOrgAppUsers(doctor1.getEmail(), Config.DOCTOR_ORG, doctor1.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(doctor2.getEmail(), Config.DOCTOR_ORG, doctor2.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(doctor3.getEmail(), Config.DOCTOR_ORG, doctor3.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(doctor4.getEmail(), Config.DOCTOR_ORG, doctor4.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(patient1.getEmail(), Config.PATIENT_ORG, patient1.getId());
            RegisterUserHyperledger.enrollOrgAppUsers(patient2.getEmail(), Config.PATIENT_ORG, patient2.getId());
        } catch (Exception e) {
            patientRepository.delete(patient1);
            patientRepository.delete(patient2);
            doctorRepository.delete(doctor1);
            doctorRepository.delete(doctor2);
            doctorRepository.delete(doctor3);
            doctorRepository.delete(doctor4);
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
                .fullName("Nhà khoa học 1")
                .username("scientist1@gmail.com")
                .email("scientist1@gmail.com")
                .password(passwordEncoder.encode("scientist1@gmail.com"))
                .enabled(true)
                .role(Constants.ROLE_SCIENTIST)
                .researchCenterId(researchCenter1.getId())
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
        Patient patient = patientRepository.findByUsername("daoquangvinh@gmail.com");
        String patientId = patient.getId();

        Doctor doctor1 = doctorRepository.findByUsername("nguyenthanhhai@gmail.com");
        String doctor1Id = doctor1.getId();

        Doctor doctor2 = doctorRepository.findByUsername("tranthanhtam@gmail.com");
        String doctor2Id = doctor2.getId();

        Scientist scientist1 = scientistRepository.findByUsername("scientist1@gmail.com");
        String scientist1Id = scientist1.getId();

        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findInsuranceCompanyByEmail("congtybaohiemC@gmail.com");
        String insuranceCompanyId = insuranceCompany.getId();

        try {
            Date dateCreated = new Date();
            Date dateModified = new Date();

            SendAppointmentRequestForm sendAppointmentRequestForm = new SendAppointmentRequestForm();
            sendAppointmentRequestForm.setSenderId(patientId);
            sendAppointmentRequestForm.setRecipientId(doctor1Id);
            sendAppointmentRequestForm.setMedicalInstitutionId(doctor1.getMedicalInstitutionId());
            sendAppointmentRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            sendAppointmentRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            AppointmentRequest appointmentRequest = hyperledgerService.sendAppointmentRequest(
                    patient,
                    sendAppointmentRequestForm);
            System.out.println("appointmentRequest: " + appointmentRequest);

            appointmentRequest = hyperledgerService.sendAppointmentRequest(
                    patient,
                    sendAppointmentRequestForm);
            System.out.println("appointmentRequest2: " + appointmentRequest);

            Manufacturer manufacturer = manufacturerRepository.findManufacturerByEmail("congtyduocphama@gmail.com");

            AddMedicationForm addMedicationForm = new AddMedicationForm();
            addMedicationForm.setManufacturerId(manufacturer.getId());
            addMedicationForm.setMedicationName("Paracetamol");
            addMedicationForm.setDescription("Điều trị đau đầu");
            addMedicationForm.setDateCreated(StringUtil.parseDate(dateCreated));
            addMedicationForm.setDateModified(StringUtil.parseDate(dateModified));

            Medication medication = hyperledgerService.addMedication(manufacturer, addMedicationForm);

            EditMedicationForm editMedicationForm = new EditMedicationForm();
            editMedicationForm.setMedicationId(medication.getMedicationId());
            editMedicationForm.setManufacturerId(manufacturer.getId());
            editMedicationForm.setMedicationName("Paracetamol");
            editMedicationForm.setDescription("Điều trị đau đầu");
            editMedicationForm.setDateCreated(StringUtil.parseDate(dateCreated));
            editMedicationForm.setDateModified(StringUtil.parseDate(dateModified));

            medication = hyperledgerService.editMedication(manufacturer, editMedicationForm);

            AddDrugForm addDrugForm = new AddDrugForm();
            addDrugForm.setMedicationId(medication.getMedicationId());
            addDrugForm.setManufactureDate(StringUtil.parseDate(StringUtil.createDate("2024-01-01")));
            addDrugForm.setExpirationDate(StringUtil.parseDate(StringUtil.createDate("2024-12-31")));

            Drug drug = hyperledgerService.addDrug(manufacturer, addDrugForm);
            System.out.println(drug);
            AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
            List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
            prescriptionDetailsList.add(new PrescriptionDetails("", "",
                    medication.getMedicationId(), "10", "Uống 2 viên vào mỗi trưa và tối"));

            addPrescriptionForm.setPrescriptionDetailsList(new Genson().serialize(prescriptionDetailsList));

            String testName = "Cardiovascular Test";

            String details = "relevant Parameters";

            AddMedicalRecordForm medicalRecordDto = new AddMedicalRecordForm();
            medicalRecordDto.setRequestId(appointmentRequest.getRequestId());
            medicalRecordDto.setPatientId(patientId);
            medicalRecordDto.setDoctorId(doctor1Id);
            medicalRecordDto.setMedicalInstitutionId(doctor1.getMedicalInstitutionId());
            medicalRecordDto.setDateCreated(StringUtil.parseDate(dateCreated));
            medicalRecordDto.setDateModified(StringUtil.parseDate(dateModified));
            medicalRecordDto.setTestName(testName);
            medicalRecordDto.setDetails(details);
            medicalRecordDto.setHashFile("");
            medicalRecordDto.setAddPrescription(addPrescriptionForm.toJSONObject().toString());

            MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(doctor1, medicalRecordDto.toJSONObject());
            System.out.println("chaincodeMedicalRecord: " + medicalRecord);

            MedicalRecord getMedicalRecordByPatient = hyperledgerService.getMedicalRecordByPatient(
                    patient,
                    medicalRecord.getMedicalRecordId()
            );

            System.out.println("getMedicalRecordByPatient: " + getMedicalRecordByPatient);


            MedicalRecord getMedicalRecordByDoctor = hyperledgerService.getMedicalRecordByDoctor(
                    doctor1,
                    medicalRecord.getMedicalRecordId()
            );

            System.out.println("getMedicalRecordByDoctor: " + getMedicalRecordByDoctor);

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
            List<MedicalRecord> medicalRecordList
                    = hyperledgerService.getListMedicalRecordByPatientQuery(patient, searchMedicalRecordForm);

            System.out.println("medicalRecordList: " + medicalRecordList);

            SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
            sendViewRequestForm.setSenderId(doctor2Id);
            sendViewRequestForm.setRecipientId(patientId);
            sendViewRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
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
            List<Medication> medicationList = hyperledgerService.getListMedication(doctor1, searchMedicationForm);
            System.out.println(medicationList);


            DrugStore drugStore = drugStoreRepository.findDrugStoreByEmail("nhathuoca@gmail.com");

            SendViewPrescriptionRequestDto sendViewPrescriptionRequestDto = new SendViewPrescriptionRequestDto();
            sendViewPrescriptionRequestDto.setSenderId(drugStore.getId());
            sendViewPrescriptionRequestDto.setRecipientId(patientId);
            sendViewPrescriptionRequestDto.setPrescriptionId(medicalRecord.getPrescriptionId());
            sendViewPrescriptionRequestDto.setDateCreated(StringUtil.parseDate(dateCreated));
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
            PrescriptionDto prescriptionDto = hyperledgerService.getPrescriptionByDrugStore(
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
                    prescriptionDto.getPrescriptionDetailsListDto().get(0).getPrescriptionDetailId()
            );

            PurchaseDto purchaseDto = new PurchaseDto();
            purchaseDto.setPrescriptionId(medicalRecord.getPrescriptionId());

            List<MedicationPurchaseDto> medicationPurchaseList = new ArrayList<>();
            medicationPurchaseList.add(medicationPurchaseDto);
            purchaseDto.setMedicationPurchaseList(new Genson().serialize(medicationPurchaseList));
            purchaseDto.setPatientId(patientId);
            purchaseDto.setDrugStoreId(drugStore.getId());
            purchaseDto.setDateCreated(StringUtil.parseDate(dateCreated));
            purchaseDto.setDateModified(StringUtil.parseDate(dateModified));

            Purchase purchase = hyperledgerService.addPurchase(
                    drugStore,
                    purchaseDto
            );
            System.out.println(purchase);

            Prescription prescription = hyperledgerService.updateDrugReactionFromPatient(
                    patient,
                    new JSONObject().put("medicalRecordId", medicalRecord.getMedicalRecordId())
                            .put("prescriptionId", medicalRecord.getPrescriptionId())
                            .put("drugReaction", "Cảm thấy buồn ngủ sau khi uống")
            );

            SendViewRequestForm sendViewRequestFormByScientist = new SendViewRequestForm();
            sendViewRequestFormByScientist.setSenderId(scientist1Id);
            sendViewRequestFormByScientist.setRecipientId(patientId);
            sendViewRequestFormByScientist.setDateCreated(StringUtil.parseDate(dateCreated));
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

            AddInsuranceProductForm addInsuranceProductForm = new AddInsuranceProductForm();
            addInsuranceProductForm.setInsuranceProductName("Bảo hiểm ung thư");
            addInsuranceProductForm.setInsuranceCompanyId(insuranceCompanyId);
            addInsuranceProductForm.setDateCreated(StringUtil.parseDate(dateCreated));
            addInsuranceProductForm.setDateModified(StringUtil.parseDate(dateModified));
            addInsuranceProductForm.setDescription("Bảo hiểm ung thư");
            addInsuranceProductForm.setNumberOfDaysInsured("90");
            addInsuranceProductForm.setPrice("500000");
            addInsuranceProductForm.setHashFile("hashFile");

            InsuranceProduct insuranceProduct = hyperledgerService.addInsuranceProduct(
                    insuranceCompany,
                    addInsuranceProductForm
            );
            System.out.println(insuranceProduct);

            EditInsuranceProductForm editInsuranceProductForm = new EditInsuranceProductForm();
            editInsuranceProductForm.setInsuranceProductId(insuranceProduct.getInsuranceProductId());
            editInsuranceProductForm.setInsuranceProductName("Bảo hiểm ung thư");
            editInsuranceProductForm.setInsuranceCompanyId(insuranceCompanyId);
            editInsuranceProductForm.setDateCreated(StringUtil.parseDate(dateCreated));
            editInsuranceProductForm.setDateModified(StringUtil.parseDate(dateModified));
            editInsuranceProductForm.setDescription("Bảo hiểm ung thư");
            editInsuranceProductForm.setNumberOfDaysInsured("90");
            editInsuranceProductForm.setPrice("1000000");
            editInsuranceProductForm.setHashFile("hashFile");

            InsuranceProduct editInsuranceProduct = hyperledgerService.editInsuranceProduct(
                    insuranceCompany,
                    editInsuranceProductForm
            );

            System.out.println(editInsuranceProduct);

            SearchInsuranceProductForm searchInsuranceProductForm = new SearchInsuranceProductForm();
            searchInsuranceProductForm.setInsuranceCompanyId(insuranceCompany.getId());
            List<InsuranceProduct> insuranceProductList = hyperledgerService.getListInsuranceProduct(
                    insuranceCompany,
                    searchInsuranceProductForm
            );
            System.out.println(insuranceProductList);

            SendPurchaseRequestForm sendPurchaseRequestForm = new SendPurchaseRequestForm();
            sendPurchaseRequestForm.setSenderId(patientId);
            sendPurchaseRequestForm.setRecipientId(insuranceCompanyId);
            sendPurchaseRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            sendPurchaseRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            sendPurchaseRequestForm.setInsuranceProductId(insuranceProduct.getInsuranceProductId());
            sendPurchaseRequestForm.setStartDate(StringUtil.parseDate(StringUtil.createDate("2024-10-10")));
            PurchaseRequest purchaseRequest = hyperledgerService.sendPurchaseRequest(
                    patient,
                    sendPurchaseRequestForm);
            System.out.println("purchaseRequest: " + purchaseRequest);

            DefinePurchaseRequestForm definePurchaseRequestForm = new DefinePurchaseRequestForm();
            definePurchaseRequestForm.setRequestId(purchaseRequest.getRequestId());
            definePurchaseRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            definePurchaseRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            definePurchaseRequestForm.setRequestStatus(RequestStatus.APPROVED.toString());
            definePurchaseRequestForm.setHashFile("hashFile");

            PurchaseRequest definePurchaseRequest = hyperledgerService.definePurchaseRequest(
                    insuranceCompany,
                    definePurchaseRequestForm
            );
            System.out.println(definePurchaseRequest);

            definePurchaseRequestForm.setRequestId(purchaseRequest.getRequestId());
            definePurchaseRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            definePurchaseRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            definePurchaseRequestForm.setRequestStatus(RequestStatus.ACCEPTED.toString());
            definePurchaseRequestForm.setHashFile("hashFile");

            definePurchaseRequest = hyperledgerService.definePurchaseRequest(
                    insuranceCompany,
                    definePurchaseRequestForm
            );
            System.out.println(definePurchaseRequest);

            SearchInsuranceContractForm searchInsuranceContractForm = new SearchInsuranceContractForm();
            searchInsuranceContractForm.setPatientId(patientId);

            List<InsuranceContract> insuranceContractList
                    = hyperledgerService.getListInsuranceContractByPatientQuery(patient, searchInsuranceContractForm);

            SendPaymentRequestForm sendPaymentRequestForm = new SendPaymentRequestForm();
            sendPaymentRequestForm.setSenderId(patientId);
            sendPaymentRequestForm.setRecipientId(insuranceCompanyId);
            sendPaymentRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            sendPaymentRequestForm.setDateModified(StringUtil.parseDate(dateModified));
            sendPaymentRequestForm.setMedicalRecordId(medicalRecord.getMedicalRecordId());
            sendPaymentRequestForm.setInsuranceContractId(insuranceContractList.get(0).getInsuranceContractId());

            PaymentRequest paymentRequest = hyperledgerService.sendPaymentRequest(
                    patient,
                    sendPaymentRequestForm
            );

            System.out.println(paymentRequest);

            DefinePaymentRequestForm definePaymentRequestForm = new DefinePaymentRequestForm();
            definePaymentRequestForm.setRequestId(paymentRequest.getRequestId());
            definePaymentRequestForm.setRequestStatus(RequestStatus.ACCEPTED.toString());
            definePaymentRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            definePaymentRequestForm.setDateModified(StringUtil.parseDate(dateModified));

            paymentRequest = hyperledgerService.definePaymentRequest(
                    insuranceCompany,
                    definePaymentRequestForm
            );
            System.out.println(paymentRequest);

            SearchConfirmPaymentRequestForm searchConfirmPaymentRequestForm = new SearchConfirmPaymentRequestForm();
            searchConfirmPaymentRequestForm.setPaymentRequestId(paymentRequest.getRequestId());
            searchConfirmPaymentRequestForm.setRecipientId(patientId);

            List<ConfirmPaymentRequest> confirmPaymentRequestList = hyperledgerService.getListConfirmPaymentRequestByRecipientQuery(
                    patient,
                    searchConfirmPaymentRequestForm
            );

            System.out.println(confirmPaymentRequestList.size());

            DefineConfirmPaymentRequestForm defineConfirmPaymentRequestForm = new DefineConfirmPaymentRequestForm();
            defineConfirmPaymentRequestForm.setRequestId(confirmPaymentRequestList.get(0).getRequestId());
            defineConfirmPaymentRequestForm.setRequestStatus(RequestStatus.ACCEPTED.toString());
            defineConfirmPaymentRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            defineConfirmPaymentRequestForm.setDateModified(StringUtil.parseDate(dateModified));

            ConfirmPaymentRequest defineConfirmPaymentRequest = hyperledgerService.defineConfirmPaymentRequest(
                    patient,
                    defineConfirmPaymentRequestForm
            );

            System.out.println(defineConfirmPaymentRequest);

            List<Request> requestList = hyperledgerService.getAllRequest(
                    patient,
                    patientId
            );

            System.out.println(requestList);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

