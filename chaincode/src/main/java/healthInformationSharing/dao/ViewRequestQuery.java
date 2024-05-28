package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.entity.ViewRequest;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class ViewRequestQuery {
    private final static Logger LOG = Logger.getLogger(ViewRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public ViewRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<ViewRequest> getListViewRequestBySenderQuery(JSONObject jsonDto) {
        String requestId = jsonDto.getString("requestId");
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String requestType = jsonDto.getString("requestType");
        String requestStatus = jsonDto.getString("requestStatus");
        String from = jsonDto.getString("from");
        String until = jsonDto.getString("until");
        String sortingOrder = jsonDto.getString("sortingOrder");
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
//        this.ctx.getStub().getQueryResultWithPagination("query", 4, "bookmark")
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
        jsonObjectSortTimeAttr.putOnce("dateCreated", sortingOrder);
        jsonArraySortAttributes.put(jsonObjectSortTimeAttr);

        JSONObject jsonObjectSelector = new JSONObject();
        jsonObjectSelector.putOnce("dateCreated", jsonObjectTimeRange);

        if (!requestId.isEmpty()) {
            jsonObjectSelector.putOnce("requestId", requestId);
        }

        if (!senderId.isEmpty()) {
            jsonObjectSelector.putOnce("senderId", senderId);
        }

        if (!recipientId.isEmpty()) {
            jsonObjectSelector.putOnce("testName", recipientId);
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
        jsonObject.putOnce("sort", jsonArraySortAttributes);

        return jsonObject;
    }
}
