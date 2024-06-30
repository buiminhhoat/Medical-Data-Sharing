package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.contract.MedicalRecordContract;
import healthInformationSharing.entity.Purchase;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PurchaseCRUD {
    private final static Logger LOG = Logger.getLogger(PurchaseCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public PurchaseCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public Purchase addPurchase(JSONObject jsonDto) {
        String prescriptionId = jsonDto.getString("prescriptionId");
        String patientId = jsonDto.getString("patientId");
        String drugStoreId = jsonDto.getString("drugStoreId");
        String dateModified = jsonDto.getString("dateModified");

        String purchaseId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, purchaseId);
        String dbKey = compositeKey.toString();

        Purchase purchase = Purchase.createInstance(
                purchaseId,
                prescriptionId,
                patientId,
                drugStoreId,
                dateModified
        );

        String purchaseStr = genson.serialize(purchase);
        ctx.getStub().putStringState(dbKey, purchaseStr);
        return purchase;
    }

    public boolean purchaseExist(String purchaseId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, purchaseId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }

    public Purchase getPurchase(String purchaseId) throws ChaincodeException {
        if (!purchaseExist(purchaseId)) {
            throw new ChaincodeException("Purchase " + purchaseId + " does not exist",
                    MedicalRecordContract.ContractErrors.PURCHASE_NOT_FOUND.toString());
        }
        String dbKey = ctx.getStub().createCompositeKey(entityName, purchaseId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return Purchase.deserialize(result);
    }
}
