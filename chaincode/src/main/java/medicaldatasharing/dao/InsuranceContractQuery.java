package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.InsuranceContract;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class InsuranceContractQuery {
    private final static Logger LOG = Logger.getLogger(InsuranceContractQuery.class.getName());
    private Context ctx;
    private String entityName;

    public InsuranceContractQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<InsuranceContract> getListInsuranceContractByQuery(JSONObject jsonDto) {
        List<InsuranceContract> insuranceContractList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            InsuranceContract insuranceContract = new Genson().deserialize(jsonObject.toString(), InsuranceContract.class);

            insuranceContractList.add(insuranceContract);
        }
        return insuranceContractList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String insuranceContractId = jsonDto.has("insuranceContractId") ? jsonDto.getString("insuranceContractId") : "";
        String insuranceProductId = jsonDto.has("insuranceProductId") ? jsonDto.getString("insuranceProductId") : "";
        String patientId = jsonDto.has("patientId") ? jsonDto.getString("patientId") : "";
        String insuranceCompanyId = jsonDto.has("insuranceCompanyId") ? jsonDto.getString("insuranceCompanyId") : "";
        String startDate = jsonDto.has("startDate") ? jsonDto.getString("startDate") : "";
        String endDate = jsonDto.has("endDate") ? jsonDto.getString("endDate") : "";
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";
        String sortingOrder = jsonDto.has("sortingOrder") ? jsonDto.getString("sortingOrder") : "";
        String from = jsonDto.has("from") ? jsonDto.getString("from") : "";
        String until = jsonDto.has("until") ? jsonDto.getString("until") : "";

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

        if (!insuranceContractId.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceContractId", insuranceContractId);
        }

        if (!insuranceProductId.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceProductId", insuranceProductId);
        }

        if (!patientId.isEmpty()) {
            jsonObjectSelector.putOnce("patientId", patientId);
        }

        if (!insuranceCompanyId.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceCompanyId", insuranceCompanyId);
        }

        if (!startDate.isEmpty()) {
            jsonObjectSelector.putOnce("startDate", startDate);
        }

        if (!endDate.isEmpty()) {
            jsonObjectSelector.putOnce("endDate", endDate);
        }

        if (!hashFile.isEmpty()) {
            jsonObjectSelector.putOnce("hashFile", hashFile);
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
