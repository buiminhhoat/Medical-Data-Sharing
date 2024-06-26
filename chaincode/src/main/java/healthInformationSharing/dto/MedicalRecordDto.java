package healthInformationSharing.dto;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import healthInformationSharing.entity.MedicalRecord;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@DataType
public class MedicalRecordDto {
    @Property()
    private String medicalRecordId;

    @Property()
    private String patientId;

    @Property()
    private String doctorId;

    @Property()
    private String medicalInstitutionId;

    @Property()
    private String dateCreated;

    @Property()
    private String testName;

    @Property()
    private String details;

    @Property()
    private String prescriptionId;

    @Property()
    private String hashFile;

    @Property()
    private String medicalRecordStatus;

    @Property()
    private String entityName;

    public static MedicalRecordDto parseMedicalRecordDto(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String prescriptionId = jsonObject.getString("prescriptionId");
        String hashFile = jsonObject.getString("hashFile");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details,
                prescriptionId,
                hashFile,
                medicalRecordStatus
        );
    }

    public static MedicalRecordDto createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details,
            String prescriptionId,
            String hashFile,
            String medicalRecordStatus
    ) {
        MedicalRecordDto medicalRecord = new MedicalRecordDto();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        medicalRecord.setPrescriptionId(prescriptionId);
        medicalRecord.setHashFile(hashFile);
        medicalRecord.setEntityName(MedicalRecord.class.getSimpleName());
        return medicalRecord;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public MedicalRecordDto setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public MedicalRecordDto setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public MedicalRecordDto setDoctorId(String doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public MedicalRecordDto setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public MedicalRecordDto setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public MedicalRecordDto setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public MedicalRecordDto setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public MedicalRecordDto setMedicalRecordStatus(String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public MedicalRecordDto setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static MedicalRecordDto deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, MedicalRecordDto.class);
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public MedicalRecordDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public MedicalRecordDto setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }
}