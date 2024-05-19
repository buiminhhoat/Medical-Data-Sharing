package healthInformationSharing.dao;

import healthInformationSharing.dto.MedicalRecordDto;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;

import java.util.ArrayList;

public class MedicalRecordQuery {
    private final static Logger LOG = Logger.getLogger(MedicalRecordQuery.class.getName());
    private Context ctx;
    private String entityName;

    public MedicalRecordQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }

    public List<MedicalRecordDto> getMedicalRecordsPreview(
            String doctorId,
            String medicalInstitutionId,
            String from,
            String until,
            String testName,
            String medicalRecordStatus,
            String sortingOrder) {
        List<MedicalRecordDto> medicalRecordDtoList = new ArrayList<>();
        JSONObject queryJsonObject = createQuerySelector(
                doctorId,
                medicalInstitutionId,
                from,
                until,
                testName,
                medicalRecordStatus,
                sortingOrder);

        LOG.info("query: " + queryJsonObject.toString());

        QueryResultsIterator<KeyValue> resultsIterator = this.ctx.getStub().getQueryResult(queryJsonObject.toString());
//        this.ctx.getStub().getQueryResultWithPagination("query", 4, "bookmark")
        for (KeyValue keyValue : resultsIterator) {
            String key = keyValue.getKey();
            String value = keyValue.getStringValue();
            JSONObject jsonObject = new JSONObject(value);
            byte[] bytes = keyValue.getValue();
            LOG.info("keyValue class: " + keyValue.getClass().toString() + ", type: " + keyValue.getClass().getTypeName());
            MedicalRecordDto clinicalTrialDto = MedicalRecordDto.parseMedicalRecordDto(jsonObject);

            medicalRecordDtoList.add(clinicalTrialDto);
        }
        return medicalRecordDtoList;
    }

    public JSONObject createQuerySelector(
            String doctorId,
            String medicalInstitutionId,
            String from,
            String until,
            String testName,
            String medicalRecordStatus,
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
        jsonObjectSortTimeAttr.putOnce("time", sortingOrder);
        jsonArraySortAttributes.put(jsonObjectSortTimeAttr);

        JSONObject jsonObjectSelector = new JSONObject();
        jsonObjectSelector.putOnce("time", jsonObjectTimeRange);

        if (!doctorId.isEmpty()) {
            jsonObjectSelector.putOnce("doctorId", doctorId);
        }

        if (!testName.isEmpty()) {
            jsonObjectSelector.putOnce("testName", testName);
        }

        if (!medicalInstitutionId.isEmpty()) {
            jsonObjectSelector.putOnce("medicalInstitutionId", medicalInstitutionId);
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
