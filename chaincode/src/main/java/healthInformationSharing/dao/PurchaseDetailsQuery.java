package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Purchase;
import healthInformationSharing.entity.PurchaseDetails;
import healthInformationSharing.entity.ViewPrescriptionRequest;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class PurchaseDetailsQuery {
    private final static Logger LOG = Logger.getLogger(PurchaseDetailsQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PurchaseDetailsQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<PurchaseDetails> getListPurchaseDetailsQuery(JSONObject jsonDto) {
        String purchaseDetailId = jsonDto.has("purchaseDetailId") ? jsonDto.getString("purchaseDetailId") : "";
        String prescriptionDetailId = jsonDto.has("prescriptionDetailId") ? jsonDto.getString("prescriptionDetailId") : "";
        String medicationId = jsonDto.has("medicationId") ? jsonDto.getString("medicationId") : "";
        String drugId = jsonDto.has("drugId") ? jsonDto.getString("drugId") : "";

        List<PurchaseDetails> purchaseDetailsList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(
                purchaseDetailId,
                prescriptionDetailId,
                medicationId,
                drugId
        );

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            PurchaseDetails purchaseDetails = new Genson().deserialize(value, PurchaseDetails.class);
            purchaseDetailsList.add(purchaseDetails);
        }
        return purchaseDetailsList;
    }

    public JSONObject createQuerySelector(
            String purchaseDetailId,
            String prescriptionDetailId,
            String medicationId,
            String drugId
    ) {
        JSONObject jsonObjectSelector = new JSONObject();

        if (!purchaseDetailId.isEmpty()) {
            jsonObjectSelector.putOnce("purchaseDetailId", purchaseDetailId);
        }

        if (!prescriptionDetailId.isEmpty()) {
            jsonObjectSelector.putOnce("prescriptionDetailId", prescriptionDetailId);
        }

        if (!medicationId.isEmpty()) {
            jsonObjectSelector.putOnce("medicationId", medicationId);
        }

        if (!drugId.isEmpty()) {
            jsonObjectSelector.putOnce("drugId", drugId);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);

        return jsonObject;
    }
}
