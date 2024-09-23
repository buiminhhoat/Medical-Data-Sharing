package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.contract.MedicalRecordContract;
import medicaldatasharing.entity.PurchaseDetails;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PurchaseDetailsCRUD {
    private final static Logger LOG = Logger.getLogger(PurchaseDetailsCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public PurchaseDetailsCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public PurchaseDetails addPurchaseDetails(JSONObject jsonDto) {
        String purchaseDetailId = jsonDto.getString("purchaseDetailId");
        String purchaseId = jsonDto.getString("purchaseId");
        String prescriptionDetailId = jsonDto.getString("prescriptionDetailId");
        String medicationId = jsonDto.getString("medicationId");
        String drugId = jsonDto.getString("drugId");

        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, purchaseDetailId);
        String dbKey = compositeKey.toString();

        PurchaseDetails purchaseDetails = PurchaseDetails.createInstance(
                purchaseDetailId,
                purchaseId,
                prescriptionDetailId,
                medicationId,
                drugId
        );

        String purchaseDetailsStr = genson.serialize(purchaseDetails);
        ctx.getStub().putStringState(dbKey, purchaseDetailsStr);
        return purchaseDetails;
    }

    public boolean purchaseDetailsExist(String purchaseDetailsId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, purchaseDetailsId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }

    public PurchaseDetails getPurchaseDetails(String purchaseDetailsId) throws ChaincodeException {
        if (!purchaseDetailsExist(purchaseDetailsId)) {
            throw new ChaincodeException("PurchaseDetails " + purchaseDetailsId + " does not exist",
                    MedicalRecordContract.ContractErrors.PURCHASE_NOT_FOUND.toString());
        }
        String dbKey = ctx.getStub().createCompositeKey(entityName, purchaseDetailsId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return PurchaseDetails.deserialize(result);
    }
}
