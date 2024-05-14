package healthInformationSharing.entity;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

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
    private String time;

    @Property()
    private String testName;

    @Property()
    private String relevantParameters;

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
        String time = jsonObject.getString("time");
        String testName = jsonObject.getString("testName");
        String relevantParameters = jsonObject.getString("relevantParameters");

        return createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                time,
                testName,
                relevantParameters
        );
    }

    public static MedicalRecord createInstance(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String time,
            String testName,
            String relevantParameters
    ) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setMedicalRecordId(medicalRecordId);
        medicalRecord.setPatientId(patientId);
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setMedicalInstitutionId(medicalInstitutionId);
        medicalRecord.setTime(time);
        medicalRecord.setTestName(testName);
        medicalRecord.setRelevantParameters(relevantParameters);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getRelevantParameters() {
        return relevantParameters;
    }

    public void setRelevantParameters(String relevantParameters) {
        this.relevantParameters = relevantParameters;
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
        return Objects.equals(medicalRecordId, that.medicalRecordId) && Objects.equals(patientId, that.patientId) && Objects.equals(doctorId, that.doctorId) && Objects.equals(medicalInstitutionId, that.medicalInstitutionId) && Objects.equals(time, that.time) && Objects.equals(testName, that.testName) && Objects.equals(relevantParameters, that.relevantParameters) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalRecordId, patientId, doctorId, medicalInstitutionId, time, testName, relevantParameters, entityName);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "medicalRecordId='" + medicalRecordId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", medicalInstitutionId='" + medicalInstitutionId + '\'' +
                ", time='" + time + '\'' +
                ", testName='" + testName + '\'' +
                ", relevantParameters='" + relevantParameters + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}
