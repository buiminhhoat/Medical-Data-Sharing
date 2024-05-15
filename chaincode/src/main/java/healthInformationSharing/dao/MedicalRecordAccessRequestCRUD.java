package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecordAccessRequest;
import healthInformationSharing.enumeration.AccessType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.util.logging.Logger;

public class MedicalRecordAccessRequestCRUD {
    private final static Logger LOG = Logger.getLogger(MedicalRecordAccessRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public MedicalRecordAccessRequestCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public MedicalRecordAccessRequest addMedicalRecordAccessRequest(
            String patientId,
            String requesterId,
            String medicalRecordId,
            String testName,
            String dateCreated) {
        String medicalRecordAccessRequestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, medicalRecordAccessRequestId);
        String dbKey = compositeKey.toString();

        MedicalRecordAccessRequest medicalRecordAccessRequest = MedicalRecordAccessRequest.createInstance(
                medicalRecordAccessRequestId,
                patientId,
                requesterId,
                dateCreated,
                AccessType.IDLE,
                "",
                "",
                medicalRecordId,
                testName
        );

        String medicalRecordAccessRequestStr = genson.serialize(medicalRecordAccessRequest);
        ctx.getStub().putStringState(dbKey, medicalRecordAccessRequestStr);
        return medicalRecordAccessRequest;
    }

    public boolean medicalRecordAccessRequestExist(String medicalRecordAccessRequestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, medicalRecordAccessRequestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        return result.length > 0;
    }

    public MedicalRecordAccessRequest getMedicalRecordAccessRequest(String medicalRecordAccessRequestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, medicalRecordAccessRequestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);

        return MedicalRecordAccessRequest.deserialize(result);
    }

    public MedicalRecordAccessRequest defineMedicalRecordAccessRequest(
            String medicalRecordAccessRequestId,
            String decision,
            String accessAvailableFrom,
            String accessAvailableUntil) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        MedicalRecordAccessRequest medicalRecordAccessRequest = getMedicalRecordAccessRequest(medicalRecordAccessRequestId);
        medicalRecordAccessRequest.setDecision(decision);
        medicalRecordAccessRequest.setAccessAvailableFrom(accessAvailableFrom);
        medicalRecordAccessRequest.setAccessAvailableUntil(accessAvailableUntil);

        String dbKey = ctx.getStub().createCompositeKey(entityName, medicalRecordAccessRequestId).toString();
        String medicalRecordAccessRequestStr = genson.serialize(medicalRecordAccessRequest);
        ctx.getStub().putStringState(dbKey, medicalRecordAccessRequestStr);
        return medicalRecordAccessRequest;
    }
}
