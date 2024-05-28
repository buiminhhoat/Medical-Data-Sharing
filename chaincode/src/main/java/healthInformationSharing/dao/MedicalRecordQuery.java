package healthInformationSharing.dao;

import healthInformationSharing.dto.MedicalRecordDto;
import org.hyperledger.fabric.contract.Context;
import com.owlike.genson.annotation.JsonProperty;

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

    public List<MedicalRecordDto> getListMedicalRecordByQuery(JSONObject jsonDto) {
        List<MedicalRecordDto> medicalRecordDtoList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(jsonDto);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            MedicalRecordDto medicalRecordDto = MedicalRecordDto.parseMedicalRecordDto(jsonObject);

            medicalRecordDtoList.add(medicalRecordDto);
        }
        return medicalRecordDtoList;
    }

    public JSONObject createQuerySelector(JSONObject jsonDto) {
        String medicalRecordId = jsonDto.getString("medicalRecordId");
        String patientId = jsonDto.getString("patientId");
        String doctorId = jsonDto.getString("doctorId");
        String medicalInstitutionId = jsonDto.getString("medicalInstitutionId");
        String testName = jsonDto.getString("testName");
        String details = jsonDto.getString("details");
        String medicalRecordStatus = jsonDto.getString("medicalRecordStatus");
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
        jsonObjectSortTimeAttr.putOnce("dateCreated", sortingOrder);
        jsonArraySortAttributes.put(jsonObjectSortTimeAttr);

        JSONObject jsonObjectSelector = new JSONObject();
        jsonObjectSelector.putOnce("dateCreated", jsonObjectTimeRange);

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

        jsonObjectSelector.putOnce("entityName", entityName);

        if (!medicalRecordStatus.isEmpty()) {
            jsonObjectSelector.putOnce("medicalRecordStatus", medicalRecordStatus);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("selector", jsonObjectSelector);
        jsonObject.putOnce("sort", jsonArraySortAttributes);

        return jsonObject;
    }
}
