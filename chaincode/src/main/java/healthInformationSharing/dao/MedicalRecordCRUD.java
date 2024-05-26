package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.enumeration.MedicalRecordStatus;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.util.ArrayList;
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

    public MedicalRecord addMedicalRecord(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String dateCreated,
            String testName,
            String details) {
        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, medicalRecordId);
        String dbKey = compositeKey.toString();

        MedicalRecord medicalRecord = MedicalRecord.createInstance(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details,
                MedicalRecordStatus.PENDING,
                new ArrayList<MedicalRecord>()
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
        Genson genson = new Genson();
        return genson.deserialize(result, MedicalRecord.class);
    }

    public MedicalRecord defineMedicalRecord(String medicalRecordId, String medicalRecordStatus) {
        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, medicalRecordId);
        String dbKey = compositeKey.toString();

        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        medicalRecord.setMedicalRecordStatus(medicalRecordStatus);

        String entityJsonString = genson.serialize(medicalRecord);
        context.getStub().putStringState(dbKey, entityJsonString);

        return medicalRecord;
    }
}
