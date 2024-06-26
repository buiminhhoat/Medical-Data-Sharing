package healthInformationSharing.dao;

import com.owlike.genson.Genson;

import com.owlike.genson.Genson;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.dto.MedicalRecordsPreviewResponse;
import healthInformationSharing.entity.Drug;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Medication;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class DrugDAO {
    private DrugCRUD drugCRUD;
    private DrugQuery drugQuery;

    public DrugDAO(Context context) {
        this.drugCRUD = new DrugCRUD(context, Drug.class.getSimpleName(), new Genson());
        this.drugQuery = new DrugQuery(context, Drug.class.getSimpleName());
    }

    public DrugCRUD getDrugCRUD() {
        return drugCRUD;
    }

    public DrugDAO setDrugCRUD(DrugCRUD drugCRUD) {
        this.drugCRUD = drugCRUD;
        return this;
    }

    public DrugQuery getDrugQuery() {
        return drugQuery;
    }

    public DrugDAO setDrugQuery(DrugQuery drugQuery) {
        this.drugQuery = drugQuery;
        return this;
    }

    public Drug addDrug(JSONObject jsonDto) {
        return drugCRUD.addDrug(jsonDto);
    }
}
