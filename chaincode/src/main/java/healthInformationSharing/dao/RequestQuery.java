package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Request;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

public class RequestQuery {
    private final static Logger LOG = Logger.getLogger(RequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public RequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<Request> getListRequest(JSONObject jsonDto) {
        String requestId = jsonDto.has("requestId") ? jsonDto.getString("requestId") : "";
        String senderId = jsonDto.has("senderId") ? jsonDto.getString("senderId") : "";
        String recipientId = jsonDto.has("recipientId") ? jsonDto.getString("recipientId") : "";
        String requestType = jsonDto.has("requestType") ? jsonDto.getString("requestType") : "";
        String requestStatus = jsonDto.has("requestStatus") ? jsonDto.getString("requestStatus") : "";
        String from = jsonDto.has("from") ? jsonDto.getString("from") : "";
        String until = jsonDto.has("until") ? jsonDto.getString("until") : "";
        String sortingOrder = jsonDto.has("sortingOrder") ? jsonDto.getString("sortingOrder") : "";
        List<Request> requestList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(
                requestId,
                senderId,
                recipientId,
                requestType,
                requestStatus,
                from,
                until,
                sortingOrder
        );

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            Request request = new Genson().deserialize(value, Request.class);
            requestList.add(request);
        }
        return requestList;
    }

    public JSONObject createQuerySelector(
            String requestId,
            String senderId,
            String recipientId,
            String requestType,
            String requestStatus,
            String from,
            String until,
            String sortingOrder
    ) {
        JSONObject jsonObjectTimeRange = new JSONObject();
        if (!Objects.equals(from, "")) {
            jsonObjectTimeRange.putOnce("$gt", from);
        }
        if (!Objects.equals(until, "")) {
            jsonObjectTimeRange.putOnce("$lt", until);
        }

        JSONArray jsonArraySortAttributes = new JSONArray();
        JSONObject jsonObjectSortTimeAttr = new JSONObject();
        if (!sortingOrder.isEmpty()) {
            jsonObjectSortTimeAttr.putOnce("dateModified", sortingOrder);
            jsonArraySortAttributes.put(jsonObjectSortTimeAttr);
        }
        JSONObject jsonObjectSelector = new JSONObject();
        if (!jsonObjectTimeRange.isEmpty()) {
            jsonObjectSelector.putOnce("dateModified", jsonObjectTimeRange);
        }

        if (!requestId.isEmpty()) {
            jsonObjectSelector.putOnce("requestId", requestId);
        }

        if (!senderId.isEmpty()) {
            jsonObjectSelector.putOnce("senderId", senderId);
        }

        if (!recipientId.isEmpty()) {
            jsonObjectSelector.putOnce("recipientId", recipientId);
        }

        if (!requestType.isEmpty()) {
            jsonObjectSelector.putOnce("requestType", requestType);
        }

        if (!requestStatus.isEmpty()) {
            jsonObjectSelector.putOnce("requestStatus", requestStatus);
        }

//        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);
        jsonObject.putOnce("sort", jsonArraySortAttributes);

        return jsonObject;
    }
}
