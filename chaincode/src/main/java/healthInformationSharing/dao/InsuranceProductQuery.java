package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.InsuranceProduct;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class InsuranceProductQuery {
    private final static Logger LOG = Logger.getLogger(InsuranceProductQuery.class.getName());
    private Context ctx;
    private String entityName;

    public InsuranceProductQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<InsuranceProduct> getListInsuranceProduct(JSONObject jsonDto) {
        List<InsuranceProduct> insuranceProductList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            InsuranceProduct insuranceProduct = new Genson().deserialize(jsonObject.toString(), InsuranceProduct.class);

            insuranceProductList.add(insuranceProduct);
        }
        return insuranceProductList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String insuranceProductId = jsonDto.has("insuranceProductId") ? jsonDto.getString("insuranceProductId") : "";
        String insuranceProductName = jsonDto.has("insuranceProductName") ? jsonDto.getString("insuranceProductName") : "";
        String insuranceCompanyId = jsonDto.has("insuranceCompanyId") ? jsonDto.getString("insuranceCompanyId") : "";
        String dateModified = jsonDto.has("dateModified") ? jsonDto.getString("dateModified") : "";
        String description = jsonDto.has("description") ? jsonDto.getString("description") : "";
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

        JSONArray jsonArraySortAttributes = new JSONArray();

        JSONObject jsonObjectSelector = new JSONObject();
        if (!jsonObjectTimeRange.isEmpty()) {
            jsonObjectSelector.putOnce("dateModified", jsonObjectTimeRange);
        }

        if (!insuranceProductId.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceProductId", insuranceProductId);
        }

        if (!insuranceProductName.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceProductName", insuranceProductName);
        }

        if (!insuranceCompanyId.isEmpty()) {
            jsonObjectSelector.putOnce("insuranceCompanyId", insuranceCompanyId);
        }

        if (!description.isEmpty()) {
            jsonObjectSelector.putOnce("description", description);
        }

        if (!hashFile.isEmpty()) {
            jsonObjectSelector.putOnce("hashFile", hashFile);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);

        if (!dateModified.isEmpty()) {
            JSONObject jsonObjectSortTimeAttr = new JSONObject();
            jsonObjectSortTimeAttr.putOnce("dateModified", sortingOrder);
            jsonArraySortAttributes.put(jsonObjectSortTimeAttr);
            jsonObject.putOnce("sort", jsonArraySortAttributes);
        }

        return jsonObject;
    }
}
