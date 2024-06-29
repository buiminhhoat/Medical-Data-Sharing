package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.contract.MedicalRecordContract;
import healthInformationSharing.entity.Drug;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class DrugCRUD {
    private final static Logger LOG = Logger.getLogger(DrugCRUD.class.getName());
    private org.hyperledger.fabric.contract.Context ctx;
    private String entityName;
    private Genson genson;

    public DrugCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public Drug addDrug(JSONObject jsonDto) {
        String medicationId = jsonDto.getString("medicationId");
        String manufactureDate = jsonDto.getString("manufactureDate");
        String expirationDate = jsonDto.getString("expirationDate");
        String ownerId = jsonDto.getString("ownerId");

        String drugId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, drugId);
        String dbKey = compositeKey.toString();

        Drug drug = Drug.createInstance(
                drugId,
                medicationId,
                manufactureDate,
                expirationDate,
                ownerId
        );

        String drugStr = genson.serialize(drug);
        ctx.getStub().putStringState(dbKey, drugStr);
        return drug;
    }

    public boolean drugExist(String drugId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, drugId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }

    public Drug getDrug(String drugId) throws ChaincodeException {
        if (!drugExist(drugId)) {
            throw new ChaincodeException("Drug " + drugId + " does not exist",
                    MedicalRecordContract.MedicalRecordContractErrors.DRUG_NOT_FOUND.toString());
        }
        String dbKey = ctx.getStub().createCompositeKey(entityName, drugId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return Drug.deserialize(result);
    }

    public Drug transferDrug(JSONObject jsonDto) {
        String drugId = jsonDto.getString("drugId");
        String newOwnerId = jsonDto.getString("newOwnerId");

        Drug drug = getDrug(drugId);
        drug.setOwnerId(newOwnerId);

        String drugStr = genson.serialize(drug);

        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, drugId);
        String dbKey = compositeKey.toString();
        ctx.getStub().putStringState(dbKey, drugStr);
        return drug;
    }
}
