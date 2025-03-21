package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.MedicalRecord;
import medicaldatasharing.enumeration.MedicalRecordStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class MedicalRecordCRUD {
    private final static Logger LOG = Logger.getLogger(MedicalRecordCRUD.class.getName());
    private Context context;
    private String entityName;
    private Genson genson;

    public MedicalRecordCRUD(Context context, String entityName, Genson genson) {
        this.context = context;
        this.entityName = entityName;
        this.genson = genson;
    }

    public MedicalRecord addMedicalRecord(JSONObject jsonDto) {
        String medicalRecordId = jsonDto.getString("medicalRecordId");
        String patientId = jsonDto.getString("patientId");
        String doctorId = jsonDto.getString("doctorId");
        String medicalInstitutionId = jsonDto.getString("medicalInstitutionId");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String testName = jsonDto.getString("testName");
        String details = jsonDto.getString("details");
        String prescriptionId = jsonDto.getString("prescriptionId");
        String hashFile = jsonDto.getString("hashFile");

        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, medicalRecordId);
        String dbKey = compositeKey.toString();

        MedicalRecord medicalRecord = MedicalRecord.createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                dateModified,
                testName,
                details,
                prescriptionId,
                hashFile,
                MedicalRecordStatus.PENDING
        );

        String entityJsonString = genson.serialize(medicalRecord);
        context.getStub().putStringState(dbKey, entityJsonString);

        return medicalRecord;
    }

    public boolean medicalRecordExist(String medicalRecordId) {
        ChaincodeStub stub = context.getStub();
        String dbKey = stub.createCompositeKey(entityName, medicalRecordId).toString();
        byte[] value = stub.getState(dbKey);
        return value.length > 0;
    }

    public MedicalRecord getMedicalRecord(String medicalRecordId) {
        String dbKey = context.getStub().createCompositeKey(entityName, medicalRecordId).toString();
        byte[] result = context.getStub().getState(dbKey);
        return MedicalRecord.deserialize(result);
    }

    public MedicalRecord defineMedicalRecord(JSONObject jsonDto) {
        String medicalRecordId = jsonDto.getString("medicalRecordId");
        String medicalRecordStatus = jsonDto.getString("medicalRecordStatus");
        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, medicalRecordId);
        String dbKey = compositeKey.toString();

        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);

        String entityJsonString = genson.serialize(medicalRecord);
        context.getStub().putStringState(dbKey, entityJsonString);

        return medicalRecord;
    }
}
