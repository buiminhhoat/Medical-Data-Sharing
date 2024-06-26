package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.util.List;
import java.util.logging.Logger;

public class PrescriptionDetailsCRUD {
    private final static Logger LOG = Logger.getLogger(PrescriptionDetailsCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public PrescriptionDetailsCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public PrescriptionDetails addPrescriptionDetails(PrescriptionDetails prescriptionDetails) {
        String prescriptionDetailsId = prescriptionDetails.getPrescriptionDetailId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, prescriptionDetailsId);
        String dbKey = compositeKey.toString();

        String prescriptionDetailsStr = genson.serialize(prescriptionDetails);
        ctx.getStub().putStringState(dbKey, prescriptionDetailsStr);
        return prescriptionDetails;
    }
}
