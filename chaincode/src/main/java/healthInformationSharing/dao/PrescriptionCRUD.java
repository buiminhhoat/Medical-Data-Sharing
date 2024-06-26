package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Prescription;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PrescriptionCRUD {
    private final static Logger LOG = Logger.getLogger(PrescriptionCRUD.class.getName());
    private org.hyperledger.fabric.contract.Context ctx;
    private String entityName;
    private Genson genson;

    public PrescriptionCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public Prescription addPrescription(JSONObject jsonDto) {
        String usageCount = jsonDto.getString("usageCount");
        String drugReaction = jsonDto.getString("drugReaction");
        String prescriptionId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, prescriptionId);
        String dbKey = compositeKey.toString();

        Prescription prescription = Prescription.createInstance(
                prescriptionId,
                usageCount,
                drugReaction
        );

        String prescriptionStr = genson.serialize(prescription);
        ctx.getStub().putStringState(dbKey, prescriptionStr);
        return prescription;
    }
}
