import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.MedicalRecord;
import org.medicaldatasharing.dto.PrescriptionDetails;
import org.medicaldatasharing.form.AddMedicalRecordForm;
import org.medicaldatasharing.form.AddPrescriptionForm;
import org.medicaldatasharing.util.StringUtil;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddMedicalRecordTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW50aGFuaGhhaUBnbWFpbC5jb20iLCJpYXQiOjE3Mjg4MzkyNzksImV4cCI6MTc4OTMxOTI3OX0.XfCNwunqThSHv97dymFBerCpsuw316tqd0R_EnQDN78zGsmnLUq0Dn5lBLahEzjyYLy7nJcPG6tiIzbYnY7h5g";

    private static String REQUEST_ID = "f760c43d1514c8045989c41b15a5e39593992119a3da5f61a66c3c64d8049343";
    private static String PATIENT_ID = "Patient-305f5d47-b01b-4a3e-b2e5-ea36fa950ecf";
    private static String DOCTOR_ID = "Doctor-76a1c4e7-bc03-4d97-b183-750bf5603f77";
    private static String MEDICAL_INSTITUTION_ID = "MedicalInstitution-da886f1d-9185-4344-b3ae-70f4a2dfad49";
    private static String TEST_NAME = "Xét nghiệm máu";
    private static String MEDICAL_RECORD_DETAILS = "Tất cả các chỉ số bình thường";
    private static String HASH_FILE = "QmZBhK3zL59mrspzjyZaTkXokNp5DjZunrEdu7wcuZ5ctM";
    private static String MEDICATION_ID = "e493d0d38a97fea26d068fe059dcfdbd1a59ecb1f12cba78f25f3786f57606c7";
    private static String QUANTITY = "10";
    private static String DETAILS = "Uống 2 viên vào mỗi trưa và tối";

    private static String API_URL = "http://localhost:8000/api/doctor/add-medical-record";

    private ResponseEntity<?> addMedicalRecord(String accessToken, AddMedicalRecordForm addMedicalRecordForm) {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = addMedicalRecordForm.toJSONObject();
        Iterator<String> keys = jsonObject.keys();

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.get(key).toString();
            builder.queryParam(key, value);
        }

        // Tạo headers cho yêu cầu POST
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        String uriString = builder.build().encode().toUriString();
        String formUrlEncodedData = !uriString.isEmpty() ? uriString.substring(1) : ""; // Sửa tại đây
        HttpEntity<String> request = new HttpEntity<>(formUrlEncodedData, headers);

        try {
            // Gửi yêu cầu POST và nhận phản hồi
            ResponseEntity<?> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
            return responseEntity;
        }
        catch (HttpClientErrorException exception) {
            System.out.println(exception);
            return ResponseEntity
                    .status(exception.getStatusCode())
                    .body(exception.getMessage());
        }
    }

    // Kiểm tra người tạo hồ sơ y tế không hợp lệ
    @Test
    @Order(1)
    public void testAddMedicalRecord_InvalidCreator() {
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        ResponseEntity<?> responseEntity = addMedicalRecord(INVALID_ACCESS_TOKEN, addMedicalRecordForm);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    public void testAddMedicalRecord_InvalidRequestId() {
        String INVALID_REQUEST_ID = "1867c4371e5a85b087abc9be3b26f642ee6e0ed7b5a40a8da1773e4dbe1a3dc4";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(INVALID_REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID bệnh nhân không hợp lệ
    @Test
    @Order(3)
    public void testAddMedicalRecord_InvalidPatientId() {
        String INVALID_PATIENT_ID = "Patient-bd0b461e-3446-4f3e-8b74-5237b03a80a4";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(INVALID_PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID bác sĩ không hợp lệ
    @Test
    @Order(4)
    public void testAddMedicalRecord_InvalidDoctorId() {
        String INVALID_DOCTOR_ID = "Doctor-3306faeb-836e-43d2-a006-19f310af044g";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(INVALID_DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // todo
    // Kiểm tra ID cơ sở y tế không hợp lệ
    @Test
    @Order(5)
    public void testAddMedicalRecord_InvalidMedicalInstitutionId() {
        String INVALID_MEDICAL_INSTITUTION_ID = "MedicalInstitution-b7cb4980-8642-4e4b-b066-8e708b9f2082";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(INVALID_MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra tên xét nghiệm bị trống
    @Test
    @Order(6)
    public void testAddMedicalRecord_InvalidTestNameEmpty() {
        String INVALID_TEST_NAME = "";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(INVALID_TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra tên xét nghiệm dài quá 100 kí tự
    @Test
    @Order(7)
    public void testAddMedicalRecord_InvalidTestNameTooLong() {
        String INVALID_TEST_NAME = "Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu  ";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(INVALID_TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra chi tiết xét nghiệm bị trống
    @Test
    @Order(8)
    public void testAddMedicalRecord_InvalidMedicalRecordDetailsEmpty() {
        String INVALID_MEDICAL_RECORD_DETAILS = "";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(INVALID_MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra chi tiết xét nghiệm dài quá 100 kí tự
    @Test
    @Order(9)
    public void testAddMedicalRecord_InvalidMedicalRecordDetailsTooLong() {
        String INVALID_MEDICAL_RECORD_DETAILS = "Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu - Xét nghiệm máu  ";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(INVALID_MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID loại thuốc không tồn tại
    @Test
    @Order(10)
    public void testAddMedicalRecord_InvalidMedicationIdNotExist() {
        String INVALID_MEDICATION_ID = "1a19de12eaad0b497206abaca0d8fe37c4970ac6a21d4ee6804a94b97037985f";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                INVALID_MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng thuốc bị trống
    @Test
    @Order(11)
    public void testAddMedicalRecord_InvalidPrescriptionQuantityEmpty() {
        String INVALID_QUANTITY = "";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, INVALID_QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng thuốc không phải là số nguyên
    @Test
    @Order(12)
    public void testAddMedicalRecord_InvalidPrescriptionQuantityNotInteger() {
        String INVALID_QUANTITY = "2.5";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, INVALID_QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng thuốc <= 0
    @Test
    @Order(13)
    public void testAddMedicalRecord_InvalidPrescriptionQuantityLessThanOrEqualZero() {
        String INVALID_QUANTITY = "-105";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, INVALID_QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra cách dùng bị trống
    @Test
    @Order(14)
    public void testAddMedicalRecord_InvalidPrescriptionDetailsEmpty() {
        String INVALID_DETAILS = "";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, INVALID_DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra cách dùng dài quá 100 kí tự
    @Test
    @Order(15)
    public void testAddMedicalRecord_InvalidPrescriptionDetailsTooLong() {
        String INVALID_DETAILS = "Uống 2 viên                                                                                          ";
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, INVALID_DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @Order(16)
    public void testAddMedicalRecord_Valid() {
        AddMedicalRecordForm addMedicalRecordForm = new AddMedicalRecordForm();
        AddPrescriptionForm addPrescriptionForm = new AddPrescriptionForm();
        List<PrescriptionDetails> prescriptionDetailsList = new ArrayList<>();
        prescriptionDetailsList.add(new PrescriptionDetails("", "",
                MEDICATION_ID, QUANTITY, DETAILS));

        addMedicalRecordForm.setRequestId(REQUEST_ID);
        addMedicalRecordForm.setPatientId(PATIENT_ID);
        addMedicalRecordForm.setDoctorId(DOCTOR_ID);
        addMedicalRecordForm.setMedicalInstitutionId(MEDICAL_INSTITUTION_ID);
        addMedicalRecordForm.setDateCreated(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setDateModified(StringUtil.parseDate(new Date()));
        addMedicalRecordForm.setTestName(TEST_NAME);
        addMedicalRecordForm.setDetails(MEDICAL_RECORD_DETAILS);
        addMedicalRecordForm.setHashFile(HASH_FILE);
        addMedicalRecordForm.setAddPrescription(new Genson().serialize(prescriptionDetailsList));
        ResponseEntity<?> responseEntity = addMedicalRecord(ACCESS_TOKEN, addMedicalRecordForm);

        String medicalRecordStr = responseEntity.getBody().toString();

        MedicalRecord medicalRecord = new Genson().deserialize(medicalRecordStr, MedicalRecord.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(medicalRecord.getPatientId(), PATIENT_ID);
        assertEquals(medicalRecord.getDoctorId(), DOCTOR_ID);
        assertEquals(medicalRecord.getMedicalInstitutionId(), MEDICAL_INSTITUTION_ID);
        assertEquals(medicalRecord.getTestName(), TEST_NAME);
        assertEquals(medicalRecord.getDetails(), MEDICAL_RECORD_DETAILS);
        assertEquals(medicalRecord.getHashFile(), HASH_FILE);
    }
}
