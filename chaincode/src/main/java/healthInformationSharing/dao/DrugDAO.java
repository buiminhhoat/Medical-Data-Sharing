package healthInformationSharing.dao;

import com.owlike.genson.Genson;

import healthInformationSharing.entity.Drug;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

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

    public Drug getDrug(String drugId) {
        return drugCRUD.getDrug(drugId);
    }

    public Drug transferDrug(JSONObject jsonDto) {
        return drugCRUD.transferDrug(jsonDto);
    }
}
