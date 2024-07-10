package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import org.hyperledger.fabric.contract.Context;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

public class MedicalRecordQuery {
    private final static Logger LOG = Logger.getLogger(MedicalRecordQuery.class.getName());
    private Context ctx;
    private String entityName;

    public MedicalRecordQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<MedicalRecord> getListMedicalRecordByQuery(JSONObject jsonDto) {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            MedicalRecord medicalRecord = new Genson().deserialize(jsonObject.toString(), MedicalRecord.class);

            medicalRecordList.add(medicalRecord);
        }
        return medicalRecordList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String medicalRecordId = jsonDto.has("medicalRecordId") ? jsonDto.getString("medicalRecordId") : "";
        String patientId = jsonDto.has("patientId") ? jsonDto.getString("patientId") : "";
        String doctorId = jsonDto.has("doctorId") ? jsonDto.getString("doctorId") : "";
        String medicalInstitutionId = jsonDto.has("medicalInstitutionId") ? jsonDto.getString("medicalInstitutionId") : "";
        String testName = jsonDto.has("testName") ? jsonDto.getString("testName") : "";
        String details = jsonDto.has("details") ? jsonDto.getString("details") : "";
        String medicalRecordStatus = jsonDto.has("medicalRecordStatus") ? jsonDto.getString("medicalRecordStatus") : "";
        String sortingOrder = jsonDto.has("sortingOrder") ? jsonDto.getString("sortingOrder") : "";
        String from = jsonDto.has("from") ? jsonDto.getString("from") : "";
        String until = jsonDto.has("until") ? jsonDto.getString("until") : "";
        String prescriptionId = jsonDto.has("prescriptionId") ? jsonDto.getString("prescriptionId") : "";
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";

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

        if (!medicalRecordId.isEmpty()) {
            jsonObjectSelector.putOnce("medicalRecordId", medicalRecordId);
        }

        if (!patientId.isEmpty()) {
            jsonObjectSelector.putOnce("patientId", patientId);
        }

        if (!doctorId.isEmpty()) {
            jsonObjectSelector.putOnce("doctorId", doctorId);
        }

        if (!testName.isEmpty()) {
            jsonObjectSelector.putOnce("testName", testName);
        }

        if (!medicalInstitutionId.isEmpty()) {
            jsonObjectSelector.putOnce("medicalInstitutionId", medicalInstitutionId);
        }

        if (!details.isEmpty()) {
            jsonObjectSelector.putOnce("details", details);
        }

        if (!prescriptionId.isEmpty()) {
            jsonObjectSelector.putOnce("prescriptionId", prescriptionId);
        }

        if (!hashFile.isEmpty()) {
            jsonObjectSelector.putOnce("hashFile", hashFile);
        }

        jsonObjectSelector.putOnce("entityName", entityName);

        if (!medicalRecordStatus.isEmpty()) {
            jsonObjectSelector.putOnce("medicalRecordStatus", medicalRecordStatus);
        }

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
