package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.Drug;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DrugQuery {
    private final static Logger LOG = Logger.getLogger(DrugQuery.class.getName());
    private Context ctx;
    private String entityName;

    public DrugQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<Drug> getListDrugByQuery(JSONObject jsonDto) {
        List<Drug> drugList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            Drug drug = new Genson().deserialize(jsonObject.toString(), Drug.class);

            drugList.add(drug);
        }
        return drugList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String drugId = jsonDto.has("drugId") ? jsonDto.getString("drugId") : "";
        String medicationId = jsonDto.has("medicationId") ? jsonDto.getString("medicationId") : "";
        String unit = jsonDto.has("unit") ? jsonDto.getString("unit") : "";
        String manufactureDate = jsonDto.has("manufactureDate") ? jsonDto.getString("manufactureDate") : "";
        String expirationDate = jsonDto.has("expirationDate") ? jsonDto.getString("expirationDate") : "";
        String ownerId = jsonDto.has("ownerId") ? jsonDto.getString("ownerId") : "";


        JSONObject jsonObjectSelector = new JSONObject();

        if (!drugId.isEmpty()) {
            jsonObjectSelector.putOnce("drugId", drugId);
        }

        if (!medicationId.isEmpty()) {
            jsonObjectSelector.putOnce("medicationId", medicationId);
        }

        if (!unit.isEmpty()) {
            jsonObjectSelector.putOnce("unit", unit);
        }

        if (!manufactureDate.isEmpty()) {
            jsonObjectSelector.putOnce("manufactureDate", manufactureDate);
        }

        if (!expirationDate.isEmpty()) {
            jsonObjectSelector.putOnce("expirationDate", expirationDate);
        }

        if (!ownerId.isEmpty()) {
            jsonObjectSelector.putOnce("ownerId", ownerId);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);
        return jsonObject;
    }
}
