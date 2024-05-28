package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.owlike.genson.annotation.JsonProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType()
public final class MedicalRecord {

    @Property()
    @JsonProperty("medicalRecordId")
    private String medicalRecordId;

    @Property()
    @JsonProperty("patientId")
    private String patientId;

    @Property()
    @JsonProperty("doctorId")
    private String doctorId;

    @Property()
    @JsonProperty("medicalInstitutionId")
    private String medicalInstitutionId;

    @Property()
    @JsonProperty("dateCreated")
    private String dateCreated;

    @Property()
    @JsonProperty("testName")
    private String testName;

    @Property()
    @JsonProperty("details")
    private String details;

    @Property()
    @JsonProperty("medicalRecordStatus")
    private String medicalRecordStatus;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public MedicalRecord() {
        super();
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details,
            String medicalRecordStatus
    ) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setDateCreated(dateCreated);
        medicalRecord.setTestName(testName);
        medicalRecord.setDetails(details);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);
        medicalRecord.setEntityName(MedicalRecord.class.getSimpleName());
        return medicalRecord;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedicalInstitutionId() {
        return medicalInstitutionId;
    }

    public void setMedicalInstitutionId(String medicalInstitutionId) {
        this.medicalInstitutionId = medicalInstitutionId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMedicalRecordStatus() {
        return medicalRecordStatus;
    }

    public MedicalRecord setMedicalRecordStatus(String medicalRecordStatus) {
        this.medicalRecordStatus = medicalRecordStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return Objects.equals(medicalRecordId, that.medicalRecordId) && Objects.equals(patientId, that.patientId) && Objects.equals(doctorId, that.doctorId) && Objects.equals(medicalInstitutionId, that.medicalInstitutionId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(testName, that.testName) && Objects.equals(details, that.details) && Objects.equals(medicalRecordStatus, that.medicalRecordStatus) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalRecordId, patientId, doctorId, medicalInstitutionId, dateCreated, testName, details, medicalRecordStatus, entityName);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "medicalRecordId='" + medicalRecordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", medicalInstitutionId='" + medicalInstitutionId + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", testName='" + testName + '\'' +
                ", details='" + details + '\'' +
                ", medicalRecordStatus='" + medicalRecordStatus + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static MedicalRecord deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, MedicalRecord.class);
    }
}
