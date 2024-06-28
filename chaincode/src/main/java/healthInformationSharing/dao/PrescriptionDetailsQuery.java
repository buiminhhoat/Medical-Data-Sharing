package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class PrescriptionDetailsQuery {
    private final static Logger LOG = Logger.getLogger(PrescriptionDetailsQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PrescriptionDetailsQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<PrescriptionDetails> getListPrescriptionDetails(JSONObject jsonDto) {
        String prescriptionDetailId = jsonDto.has("prescriptionDetailId") ? jsonDto.getString("prescriptionDetailId") : "";
        String prescriptionId = jsonDto.has("prescriptionId") ? jsonDto.getString("prescriptionId") : "";
        String medicationId = jsonDto.has("medicationId") ? jsonDto.getString("medicationId") : "";
        String quantity = jsonDto.has("quantity") ? jsonDto.getString("quantity") : "";
        String requestStatus = jsonDto.has("requestStatus") ? jsonDto.getString("requestStatus") : "";
        String details = jsonDto.has("details") ? jsonDto.getString("details") : "";

        List<PrescriptionDetails> PrescriptionDetailsList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(
                prescriptionDetailId,
                prescriptionId,
                medicationId,
                quantity,
                requestStatus,
                details
        );

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            PrescriptionDetails PrescriptionDetails = new Genson().deserialize(value, PrescriptionDetails.class);
            PrescriptionDetailsList.add(PrescriptionDetails);
        }
        return PrescriptionDetailsList;
    }

    public JSONObject createQuerySelector(
            String prescriptionDetailId,
            String prescriptionId,
            String medicationId,
            String quantity,
            String requestStatus,
            String details
    ) {
        JSONArray jsonArraySortAttributes = new JSONArray();
        JSONObject jsonObjectSortTimeAttr = new JSONObject();
        jsonArraySortAttributes.put(jsonObjectSortTimeAttr);

        JSONObject jsonObjectSelector = new JSONObject();

        if (!prescriptionDetailId.isEmpty()) {
            jsonObjectSelector.putOnce("prescriptionDetailId", prescriptionDetailId);
        }

        if (!prescriptionId.isEmpty()) {
            jsonObjectSelector.putOnce("prescriptionId", prescriptionId);
        }

        if (!medicationId.isEmpty()) {
            jsonObjectSelector.putOnce("medicationId", medicationId);
        }

        if (!quantity.isEmpty()) {
            jsonObjectSelector.putOnce("quantity", quantity);
        }

        if (!requestStatus.isEmpty()) {
            jsonObjectSelector.putOnce("requestStatus", requestStatus);
        }

        if (!details.isEmpty()) {
            jsonObjectSelector.putOnce("details", details);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);

        return jsonObject;
    }

    public List<PrescriptionDetails> getListPrescriptionDetails(String prescriptionId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prescriptionId", prescriptionId);
        return getListPrescriptionDetails(jsonObject);
    }
}
