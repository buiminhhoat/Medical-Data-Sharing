package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ConfirmPaymentRequest;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ConfirmPaymentRequestQuery {
    private final static Logger LOG = Logger.getLogger(ConfirmPaymentRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public ConfirmPaymentRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public Context getCtx() {
        return ctx;
    }

    public ConfirmPaymentRequestQuery setCtx(Context ctx) {
        this.ctx = ctx;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ConfirmPaymentRequestQuery setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public List<ConfirmPaymentRequest> getListConfirmPaymentRequestByQuery(JSONObject jsonDto) {
        List<ConfirmPaymentRequest> confirmPaymentRequestList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            ConfirmPaymentRequest confirmPaymentRequest = new Genson().deserialize(jsonObject.toString(), ConfirmPaymentRequest.class);

            confirmPaymentRequestList.add(confirmPaymentRequest);
        }
        return confirmPaymentRequestList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String requestId = jsonDto.has("requestId") ? jsonDto.getString("requestId") : "";
        String senderId = jsonDto.has("senderId") ? jsonDto.getString("senderId") : "";
        String recipientId = jsonDto.has("recipientId") ? jsonDto.getString("recipientId") : "";
        String dateCreated = jsonDto.has("dateCreated") ? jsonDto.getString("dateCreated") : "";
        String dateModified = jsonDto.has("dateModified") ? jsonDto.getString("dateModified") : "";
        String requestType = jsonDto.has("requestType") ? jsonDto.getString("requestType") : "";
        String requestStatus = jsonDto.has("requestStatus") ? jsonDto.getString("requestStatus") : "";
        String paymentRequestId = jsonDto.has("paymentRequestId") ? jsonDto.getString("paymentRequestId") : "";
        String from = jsonDto.has("from") ? jsonDto.getString("from") : "";
        String until = jsonDto.has("until") ? jsonDto.getString("until") : "";
        String sortingOrder = jsonDto.has("sortingOrder") ? jsonDto.getString("sortingOrder") : "";

        JSONObject jsonObjectTimeRange = new JSONObject();

        if (!Objects.equals(from, "")) {
            jsonObjectTimeRange.putOnce("$gt", from);
        }
        if (!Objects.equals(until, "")) {
            jsonObjectTimeRange.putOnce("$lt", until);
        }

        JSONObject jsonObjectSelector = new JSONObject();

        if (!jsonObjectTimeRange.isEmpty()) {
            jsonObjectSelector.putOnce("dateModified", jsonObjectTimeRange);
        }

        if (!requestId.isEmpty()) {
            jsonObjectSelector.putOnce("requestId", requestId);
        }

        if (!paymentRequestId.isEmpty()) {
            jsonObjectSelector.putOnce("paymentRequestId", paymentRequestId);
        }

        if (!senderId.isEmpty()) {
            jsonObjectSelector.putOnce("senderId", senderId);
        }

        if (!recipientId.isEmpty()) {
            jsonObjectSelector.putOnce("recipientId", recipientId);
        }

        if (!dateCreated.isEmpty()) {
            jsonObjectSelector.putOnce("dateCreated", dateCreated);
        }

        if (!dateModified.isEmpty()) {
            jsonObjectSelector.putOnce("dateModified", dateModified);
        }

        if (!requestType.isEmpty()) {
            jsonObjectSelector.putOnce("requestType", requestType);
        }

        if (!requestStatus.isEmpty()) {
            jsonObjectSelector.putOnce("requestStatus", requestStatus);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);

        if (!sortingOrder.isEmpty()) {
            JSONArray jsonArraySortAttributes = new JSONArray();
            JSONObject jsonObjectSortTimeAttr = new JSONObject();
            jsonObjectSortTimeAttr.putOnce("dateModified", sortingOrder);
            jsonArraySortAttributes.put(jsonObjectSortTimeAttr);
            jsonObject.putOnce("sort", jsonArraySortAttributes);
        }

        return jsonObject;
    }
}
