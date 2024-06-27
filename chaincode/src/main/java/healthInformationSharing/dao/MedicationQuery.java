package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Medication;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class MedicationQuery {
    private final static Logger LOG = Logger.getLogger(MedicationQuery.class.getName());
    private Context ctx;
    private String entityName;

    public MedicationQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<Medication> getListMedication(JSONObject jsonDto) {
        List<Medication> medicationList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            Medication medication = new Genson().deserialize(jsonObject.toString(), Medication.class);

            medicationList.add(medication);
        }
        return medicationList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String medicationId = jsonDto.getString("medicationId");
        String manufacturerId = jsonDto.getString("manufacturerId");
        String medicationName = jsonDto.getString("medicationName");
        String description = jsonDto.getString("description");

        String sortingOrder = jsonDto.getString("sortingOrder");
        String from = jsonDto.getString("from");
        String until = jsonDto.getString("until");


        JSONObject jsonObjectTimeRange = new JSONObject();

        if (!Objects.equals(from, "")) {
            jsonObjectTimeRange.putOnce("$gt", from);
        }
        if (!Objects.equals(until, "")) {
            jsonObjectTimeRange.putOnce("$lt", until);
        }

        JSONArray jsonArraySortAttributes = new JSONArray();
        JSONObject jsonObjectSortTimeAttr = new JSONObject();
        jsonObjectSortTimeAttr.putOnce("dateModified", sortingOrder);
        jsonArraySortAttributes.put(jsonObjectSortTimeAttr);

        JSONObject jsonObjectSelector = new JSONObject();
        jsonObjectSelector.putOnce("dateModified", jsonObjectTimeRange);

        if (!medicationId.isEmpty()) {
            jsonObjectSelector.putOnce("medicationId", medicationId);
        }

        if (!manufacturerId.isEmpty()) {
            jsonObjectSelector.putOnce("manufacturerId", manufacturerId);
        }

        if (!medicationName.isEmpty()) {
            jsonObjectSelector.putOnce("medicationName", medicationName);
        }

        if (!description.isEmpty()) {
            jsonObjectSelector.putOnce("description", description);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);
        jsonObject.putOnce("sort", jsonArraySortAttributes);

        return jsonObject;
    }
}
