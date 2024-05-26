package com.medicaldatasharing.util;

import com.google.gson.Gson;
import com.medicaldatasharing.chaincode.Config;
import com.medicaldatasharing.chaincode.client.RegisterUserHyperledger;
import com.medicaldatasharing.chaincode.dto.AppointmentRequest;
import com.medicaldatasharing.chaincode.dto.EditRequest;
import com.medicaldatasharing.chaincode.dto.MedicalRecord;
import com.medicaldatasharing.chaincode.dto.MedicalRecordPreviewResponse;
import com.medicaldatasharing.dto.MedicalRecordDto;
import com.medicaldatasharing.dto.MedicalRecordPreviewDto;
import com.medicaldatasharing.enumeration.MedicalRecordStatus;
import com.medicaldatasharing.form.DefineMedicalRecordForm;
import com.medicaldatasharing.form.SearchMedicalRecordForm;
import com.medicaldatasharing.form.SendAppointmentRequestForm;
import com.medicaldatasharing.form.SendEditRequestForm;
import com.medicaldatasharing.model.Admin;
import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.MedicalInstitution;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.repository.AdminRepository;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.MedicalInstitutionRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.service.HyperledgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    private HyperledgerService hyperledgerService;

    @Override
    public void run(String... args) throws Exception {
        initMedicalInstitutions();
        initUsers();
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

    private void init() throws Exception {
        Patient patient = patientRepository.findByUsername("lehuy5c2003@gmail.com");
        String patientId = patient.getId();
        Doctor doctor = doctorRepository.findByUsername("nguyenthanhhai@gmail.com");
        String doctorId = doctor.getId();
        MedicalInstitution medicalInstitution = doctor.getMedicalInstitution();
        String medicalInstitutionId = medicalInstitution.getMedicalInstitutionId();

        try {
            SendAppointmentRequestForm sendAppointmentRequestForm = new SendAppointmentRequestForm();
            sendAppointmentRequestForm.setSenderId(patientId);
            sendAppointmentRequestForm.setRecipientId(doctorId);
            sendAppointmentRequestForm.setDateCreated(new Date().toString());
            AppointmentRequest appointmentRequest = hyperledgerService.sendAppointmentRequest(
                    patient,
                    sendAppointmentRequestForm);
            System.out.println("appointmentRequest: " + appointmentRequest);

            Date dateCreated = new Date();

            String testName = "Cardiovascular Test";

            String details = "relevant Parameters";

            MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
            medicalRecordDto.setRequestId(appointmentRequest.getRequestId());
            medicalRecordDto.setPatientId(patientId);
            medicalRecordDto.setDoctorId(doctorId);
            medicalRecordDto.setMedicalInstitutionId(medicalInstitutionId);
            medicalRecordDto.setDateCreated(StringUtil.parseDate(dateCreated));
            medicalRecordDto.setTestName(testName);
            medicalRecordDto.setDetails(details);
            MedicalRecord medicalRecord = hyperledgerService.addMedicalRecord(doctor, medicalRecordDto);
            System.out.println("chaincodeMedicalRecord: " + medicalRecord);

            MedicalRecord getMedicalRecord = hyperledgerService.getMedicalRecord(patient, medicalRecord.getMedicalRecordId());
            System.out.println("getChaincodeMedicalRecord: " + getMedicalRecord);


            DefineMedicalRecordForm defineMedicalRecordForm = new DefineMedicalRecordForm();
            defineMedicalRecordForm.setMedicalRecordId(medicalRecord.getMedicalRecordId());
            defineMedicalRecordForm.setMedicalRecordStatus(MedicalRecordStatus.ACCEPTED.toString());

            medicalRecord = hyperledgerService.defineMedicalRecord(patient, defineMedicalRecordForm);
            System.out.println(medicalRecord);

            SearchMedicalRecordForm searchMedicalRecordForm = new SearchMedicalRecordForm();
            searchMedicalRecordForm.setPatientId(patientId);
            searchMedicalRecordForm.setFrom(StringUtil.createDate("2024-01-01"));
            searchMedicalRecordForm.setUntil(StringUtil.createDate("2024-12-31"));
            List<MedicalRecordPreviewDto> medicalRecordPreviewDtoList
                    = hyperledgerService.getListMedicalRecordByPatientQuery(patient, searchMedicalRecordForm);

            System.out.println("medicalRecordPreviewDtoList: " + medicalRecordPreviewDtoList);

            SendEditRequestForm sendEditRequestForm = new SendEditRequestForm();
            sendEditRequestForm.setSenderId(doctorId);
            sendEditRequestForm.setRecipientId(patientId);
            sendEditRequestForm.setDateCreated(StringUtil.parseDate(dateCreated));
            medicalRecord.setDetails("details :)");
            sendEditRequestForm.setMedicalRecordJson(JsonConverter.objectToJson(medicalRecord).toString());

            EditRequest editRequest = hyperledgerService.sendEditRequest(doctor, sendEditRequestForm);
            System.out.println("editRequest: " + editRequest);

            EditRequest getEditRequest = hyperledgerService.getEditRequest(doctor, editRequest.getRequestId());
            System.out.println("getEditRequest: " + getEditRequest);

        } catch (Exception exception) {
            System.out.println(exception);
        }

        LOG.info("INIT DATA LOADER IS FINISHED");
    }
}

