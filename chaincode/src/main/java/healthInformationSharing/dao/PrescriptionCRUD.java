package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Prescription;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PrescriptionCRUD {
    private final static Logger LOG = Logger.getLogger(PrescriptionCRUD.class.getName());
    private org.hyperledger.fabric.contract.Context context;
    private String entityName;
    private Genson genson;

    public PrescriptionCRUD(Context context, String entityName, Genson genson) {
        this.context = context;
        this.entityName = entityName;
        this.genson = genson;
    }

    public Prescription addPrescription(JSONObject jsonDto) {
        String drugReaction = jsonDto.getString("drugReaction");
        String prescriptionId = context.getStub().getTxId();
        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, prescriptionId);
        String dbKey = compositeKey.toString();

        Prescription prescription = Prescription.createInstance(
                prescriptionId,
                drugReaction
        );

        String prescriptionStr = genson.serialize(prescription);
        context.getStub().putStringState(dbKey, prescriptionStr);
        return prescription;
    }

    public boolean prescriptionExist(String prescriptionId) {
        ChaincodeStub stub = context.getStub();
        String dbKey = stub.createCompositeKey(entityName, prescriptionId).toString();
        byte[] value = stub.getState(dbKey);
        return value.length > 0;
    }

    public Prescription getPrescription(String prescriptionId) {
        String dbKey = context.getStub().createCompositeKey(entityName, prescriptionId).toString();
        byte[] result = context.getStub().getState(dbKey);
        return Prescription.deserialize(result);
    }
}
