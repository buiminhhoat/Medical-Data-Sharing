package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.Purchase;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PurchaseQuery {
    private final static Logger LOG = Logger.getLogger(PurchaseQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PurchaseQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public Context getCtx() {
        return ctx;
    }

    public PurchaseQuery setCtx(Context ctx) {
        this.ctx = ctx;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public PurchaseQuery setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<Purchase> getListPurchaseByQuery(JSONObject jsonDto) {
        List<Purchase> purchaseList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(
                jsonDto
        );

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            Purchase purchase = new Genson().deserialize(value, Purchase.class);
            purchaseList.add(purchase);
        }
        return purchaseList;
    }

    public JSONObject createQuerySelector(
            JSONObject jsonDto
    ) {
        String purchaseId = jsonDto.has("purchaseId") ? jsonDto.getString("purchaseId") : "";
        String prescriptionId = jsonDto.has("prescriptionId") ? jsonDto.getString("prescriptionId") : "";
        String patientId = jsonDto.has("patientId") ? jsonDto.getString("patientId") : "";
        String drugStoreId = jsonDto.has("drugStoreId") ? jsonDto.getString("drugStoreId") : "";

        JSONObject jsonObjectSelector = new JSONObject();

        if (!purchaseId.isEmpty()) {
            jsonObjectSelector.putOnce("purchaseId", purchaseId);
        }

        if (!prescriptionId.isEmpty()) {
            jsonObjectSelector.putOnce("prescriptionId", prescriptionId);
        }

        if (!patientId.isEmpty()) {
            jsonObjectSelector.putOnce("patientId", patientId);
        }

        if (!drugStoreId.isEmpty()) {
            jsonObjectSelector.putOnce("drugStoreId", drugStoreId);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);

        return jsonObject;
    }
}
