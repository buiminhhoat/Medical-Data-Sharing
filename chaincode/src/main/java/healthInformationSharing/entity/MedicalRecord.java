package healthInformationSharing.entity;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType
public final class MedicalRecord {

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

    @Property
    private String medicalRecordStatus;

    @Property
    private List <MedicalRecord> changeHistory;

    @Property()
    private String entityName;

    public MedicalRecord() {
        super();
    }

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static MedicalRecord deserialize(byte[] data) {
        JSONObject jsonObject = new JSONObject(new String(data, UTF_8));
        return parseMedicalRecord(jsonObject);
    }

    private static MedicalRecord parseMedicalRecord(JSONObject jsonObject) {
        String medicalRecordId = jsonObject.getString("medicalRecordId");
        String patientId = jsonObject.getString("patientId");
        String doctorId = jsonObject.getString("doctorId");
        String medicalInstitutionId = jsonObject.getString("medicalInstitutionId");
        String dateCreated = jsonObject.getString("dateCreated");
        String testName = jsonObject.getString("testName");
        String details = jsonObject.getString("details");
        String medicalRecordStatus = jsonObject.getString("medicalRecordStatus");
        List<MedicalRecord> changeHistory = parseChangeHistory(jsonObject.getJSONArray("changeHistory"));

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details,
                medicalRecordStatus,
                changeHistory
        );
    }

    private static List<MedicalRecord> parseChangeHistory(JSONArray changeHistoryJson) {
        List<MedicalRecord> changeHistory = new ArrayList<>();
        for (int i = 0; i < changeHistoryJson.length(); i++) {
            JSONObject medicalRecordJson = changeHistoryJson.getJSONObject(i);
            MedicalRecord medicalRecord = parseMedicalRecord(medicalRecordJson);
            changeHistory.add(medicalRecord);
        }
        return changeHistory;
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details,
            String medicalRecordStatus,
            List<MedicalRecord> changeHistory
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
        medicalRecord.setChangeHistory(changeHistory);
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

    public List<MedicalRecord> getChangeHistory() {
        return changeHistory;
    }

    public MedicalRecord setChangeHistory(List<MedicalRecord> changeHistory) {
        this.changeHistory = changeHistory;
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
        return Objects.equals(medicalRecordId, that.medicalRecordId) && Objects.equals(patientId, that.patientId) && Objects.equals(doctorId, that.doctorId) && Objects.equals(medicalInstitutionId, that.medicalInstitutionId) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(testName, that.testName) && Objects.equals(details, that.details) && Objects.equals(medicalRecordStatus, that.medicalRecordStatus) && Objects.equals(changeHistory, that.changeHistory) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalRecordId, patientId, doctorId, medicalInstitutionId, dateCreated, testName, details, medicalRecordStatus, changeHistory, entityName);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "changeHistory=" + changeHistory +
                ", medicalRecordId='" + medicalRecordId + '\'' +
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
}
