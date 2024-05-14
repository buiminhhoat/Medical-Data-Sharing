package healthInformationSharing.entity;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType
public class MedicalRecordAccessRequest {
    @Property()
    private String medicalRecordAccessRequestId;

    @Property
    private String patientId;

    @Property
    private String requesterId;

    @Property
    private String dateCreated;

    @Property
    private String decision;

    @Property
    private String accessAvailableFrom;

    @Property
    private String accessAvailableUntil;

    @Property
    private String medicalRecord;

    @Property
    private String testName;

    @Property
    private String entityName;

    public MedicalRecordAccessRequest() {
    }

    public static byte[] serialize(Object object) {
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(UTF_8);
    }

    public static MedicalRecordAccessRequest deserialize(byte[] data) {
        JSONObject jsonObject = new JSONObject(new String(data, UTF_8));
        return parseMedicalRecordAccessRequest(jsonObject);
    }

    public static MedicalRecordAccessRequest parseMedicalRecordAccessRequest(JSONObject jsonObject) {
        String medicalRecordAccessRequestId = jsonObject.getString("medicalRecordAccessRequestId");
        String patientId = jsonObject.getString("patientId");
        String requesterId = jsonObject.getString("requesterId");
        String dateCreated = jsonObject.getString("dateCreated");
        String decision = jsonObject.getString("decision");
        String accessAvailableFrom = jsonObject.getString("accessAvailableFrom");
        String accessAvailableUntil = jsonObject.getString("accessAvailableUntil");
        String medicalRecordJsonObject = jsonObject.getString("medicalRecord");
        String testName = jsonObject.getString("testName");

        return createInstance(
                medicalRecordAccessRequestId,
                patientId,
                requesterId,
                dateCreated,
                decision,
                accessAvailableFrom,
                accessAvailableUntil,
                medicalRecordJsonObject,
                testName
        );
    }

    public static MedicalRecordAccessRequest createInstance(
            String medicalRecordAccessRequestId,
            String patientId,
            String requesterId,
            String dateCreated,
            String decision,
            String accessAvailableFrom,
            String accessAvailableUntil,
            String medicalRecordJsonObject,
            String testName
    ) {
        MedicalRecordAccessRequest medicalRecordAccessRequest = new MedicalRecordAccessRequest();
        medicalRecordAccessRequest.setMedicalRecordAccessRequestId(medicalRecordAccessRequestId);
        medicalRecordAccessRequest.setPatientId(patientId);
        medicalRecordAccessRequest.setRequesterId(requesterId);
        medicalRecordAccessRequest.setDateCreated(dateCreated);
        medicalRecordAccessRequest.setDecision(decision);
        medicalRecordAccessRequest.setAccessAvailableFrom(accessAvailableFrom);
        medicalRecordAccessRequest.setAccessAvailableUntil(accessAvailableUntil);
        medicalRecordAccessRequest.setMedicalRecord(medicalRecordJsonObject);
        medicalRecordAccessRequest.setTestName(testName);
        return medicalRecordAccessRequest;
    }

    public String getMedicalRecordAccessRequestId() {
        return medicalRecordAccessRequestId;
    }

    public MedicalRecordAccessRequest setMedicalRecordAccessRequestId(String medicalRecordAccessRequestId) {
        this.medicalRecordAccessRequestId = medicalRecordAccessRequestId;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public MedicalRecordAccessRequest setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public MedicalRecordAccessRequest setRequesterId(String requesterId) {
        this.requesterId = requesterId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public MedicalRecordAccessRequest setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDecision() {
        return decision;
    }

    public MedicalRecordAccessRequest setDecision(String decision) {
        this.decision = decision;
        return this;
    }

    public String getAccessAvailableFrom() {
        return accessAvailableFrom;
    }

    public MedicalRecordAccessRequest setAccessAvailableFrom(String accessAvailableFrom) {
        this.accessAvailableFrom = accessAvailableFrom;
        return this;
    }

    public String getAccessAvailableUntil() {
        return accessAvailableUntil;
    }

    public MedicalRecordAccessRequest setAccessAvailableUntil(String accessAvailableUntil) {
        this.accessAvailableUntil = accessAvailableUntil;
        return this;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public MedicalRecordAccessRequest setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public String getTestName() {
        return testName;
    }

    public MedicalRecordAccessRequest setTestName(String testName) {
        this.testName = testName;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public MedicalRecordAccessRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}
