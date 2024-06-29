package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.contract.MedicalRecordContract;
import healthInformationSharing.entity.PrescriptionDetails;
import healthInformationSharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
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

    public boolean drugExist(String drugId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, drugId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }

    public PrescriptionDetails getPrescriptionDetails(String drugId) throws ChaincodeException {
        if (!drugExist(drugId)) {
            throw new ChaincodeException("PrescriptionDetails " + drugId + " does not exist",
                    MedicalRecordContract.MedicalRecordContractErrors.PRESCRIPTION_DETAIL_NOT_FOUND.toString());
        }
        String dbKey = ctx.getStub().createCompositeKey(entityName, drugId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return PrescriptionDetails.deserialize(result);
    }

    public PrescriptionDetails updatePrescriptionDetails(PrescriptionDetails prescriptionDetails) {
        String prescriptionDetailsId = prescriptionDetails.getPrescriptionDetailId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, prescriptionDetailsId);
        String dbKey = compositeKey.toString();

        String prescriptionDetailsStr = genson.serialize(prescriptionDetails);
        ctx.getStub().putStringState(dbKey, prescriptionDetailsStr);
        return prescriptionDetails;
    }
}
