package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.contract.MedicalRecordContract;
import healthInformationSharing.entity.Drug;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

    public List<Drug> addDrug(JSONObject jsonDto) {
        String medicationId = jsonDto.getString("medicationId");
        String manufactureDate = jsonDto.getString("manufactureDate");
        String expirationDate = jsonDto.getString("expirationDate");
        String ownerId = jsonDto.getString("ownerId");
        Long quantity = Long.valueOf(jsonDto.getString("quantity"));

//        LOG.info("quantity: " + quantity);
        List<Drug> drugList = new ArrayList<>();
        String txId = ctx.getStub().getTxId();
        for (int i = 1; i <= quantity; ++i) {
            String drugId = txId + i;
//            LOG.info("i: " + i + ", drugId: " + drugId);
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
            drugList.add(drug);
        }
        LOG.info("drugList.size(): " + drugList.size());
        return drugList;
    }

    public boolean drugExist(String drugId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, drugId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }

    public Drug getDrug(String drugId) throws ChaincodeException {
        if (!drugExist(drugId)) {
            throw new ChaincodeException("Drug " + drugId + " does not exist",
                    MedicalRecordContract.ContractErrors.DRUG_NOT_FOUND.toString());
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
