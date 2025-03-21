package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.ViewRequest;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.logging.Logger;

public class ViewRequestQuery {
    private final static Logger LOG = Logger.getLogger(ViewRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public ViewRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<ViewRequest> getListViewRequestQuery(JSONObject jsonDto) {
        String requestId = jsonDto.has("requestId") ? jsonDto.getString("requestId") : "";
        String senderId = jsonDto.has("senderId") ? jsonDto.getString("senderId") : "";
        String recipientId = jsonDto.has("recipientId") ? jsonDto.getString("recipientId") : "";
        String requestType = jsonDto.has("requestType") ? jsonDto.getString("requestType") : "";
        String requestStatus = jsonDto.has("requestStatus") ? jsonDto.getString("requestStatus") : "";
        String from = jsonDto.has("from") ? jsonDto.getString("from") : "";
        String until = jsonDto.has("until") ? jsonDto.getString("until") : "";
        String sortingOrder = jsonDto.has("sortingOrder") ? jsonDto.getString("sortingOrder") : "";
        List<ViewRequest> viewRequestList = new ArrayList<>();
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
            ViewRequest viewRequest = new Genson().deserialize(value, ViewRequest.class);
            viewRequestList.add(viewRequest);
        }
        return viewRequestList;
    }

    public List<String> getListAllAuthorizedPatientForDoctor(JSONObject jsonDto) {
        List<ViewRequest> viewRequestList = getListViewRequestQuery(jsonDto);
        Set<String> stringSet = new HashSet<>();
        for (ViewRequest viewRequest: viewRequestList) {
            stringSet.add(viewRequest.getRecipientId());
        }
        List<String> stringList = new ArrayList<>();
        for (String s: stringSet) {
            stringList.add(s);
        }
        return stringList;
    }

    public List<String> getListAllAuthorizedPatientForScientist(JSONObject jsonDto) {
        List<ViewRequest> viewRequestList = getListViewRequestQuery(jsonDto);
        Set<String> stringSet = new HashSet<>();
        for (ViewRequest viewRequest: viewRequestList) {
            stringSet.add(viewRequest.getRecipientId());
        }
        List<String> stringList = new ArrayList<>();
        for (String s: stringSet) {
            stringList.add(s);
        }
        return stringList;
    }

    public List<String> getListAllAuthorizedPatientForManufacturer(JSONObject jsonDto) {
        List<ViewRequest> viewRequestList = getListViewRequestQuery(jsonDto);
        Set<String> stringSet = new HashSet<>();
        for (ViewRequest viewRequest: viewRequestList) {
            stringSet.add(viewRequest.getRecipientId());
        }
        List<String> stringList = new ArrayList<>();
        for (String s: stringSet) {
            stringList.add(s);
        }
        return stringList;
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

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);
        if (!jsonArraySortAttributes.isEmpty()) {
            jsonObject.putOnce("sort", jsonArraySortAttributes);
        }

        return jsonObject;
    }
}
