package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.dto.MedicalRecordsPreviewResponse;
import healthInformationSharing.entity.MedicalRecord;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class MedicalRecordDAO {
    private MedicalRecordCRUD medicalRecordCRUD;
    private MedicalRecordQuery medicalRecordQuery;

    public MedicalRecordDAO(Context context) {
        this.medicalRecordCRUD = new MedicalRecordCRUD(context, MedicalRecord.class.getSimpleName(), new Genson());
        this.medicalRecordQuery = new MedicalRecordQuery(context, MedicalRecord.class.getSimpleName());
    }

    public MedicalRecord addMedicalRecord(JSONObject jsonDto) {
        return medicalRecordCRUD.addMedicalRecord(jsonDto);
    }

    public boolean medicalRecordExist(String medicalRecordId) {
        return medicalRecordCRUD.medicalRecordExist(medicalRecordId);
    }

    public MedicalRecord getMedicalRecord(String medicalRecordId) {
        return medicalRecordCRUD.getMedicalRecord(medicalRecordId);
    }

    public MedicalRecord defineMedicalRecord(JSONObject jsonDto) {
        return medicalRecordCRUD.defineMedicalRecord(jsonDto);
    }

    public List<MedicalRecordDto> getListMedicalRecordByQuery(JSONObject jsonDto) {
        return medicalRecordQuery.getListMedicalRecordByQuery(
                jsonDto
        );
    }

    public MedicalRecord editMedicalRecord(JSONObject jsonDto) {
        return medicalRecordCRUD.editMedicalRecord(jsonDto);
    }
}
