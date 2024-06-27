package healthInformationSharing.dao;

import com.owlike.genson.Genson;

import healthInformationSharing.entity.Medication;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class MedicationDAO {
    private MedicationCRUD medicationCRUD;
    private MedicationQuery medicationQuery;

    public MedicationDAO(Context context) {
        this.medicationCRUD = new MedicationCRUD(context, Medication.class.getSimpleName(), new Genson());
        this.medicationQuery = new MedicationQuery(context, Medication.class.getSimpleName());
    }

    public MedicationCRUD getMedicationCRUD() {
        return medicationCRUD;
    }

    public MedicationDAO setMedicationCRUD(MedicationCRUD medicationCRUD) {
        this.medicationCRUD = medicationCRUD;
        return this;
    }

    public MedicationQuery getMedicationQuery() {
        return medicationQuery;
    }

    public MedicationDAO setMedicationQuery(MedicationQuery medicationQuery) {
        this.medicationQuery = medicationQuery;
        return this;
    }

    public Medication addMedication(JSONObject jsonDto) {
        return medicationCRUD.addMedication(jsonDto);
    }

    public Medication editMedication(JSONObject jsonDto) {
        return medicationCRUD.editMedication(jsonDto);
    }

    public String getManufacturerId(String medicationId) {
        Medication medication = medicationCRUD.getMedication(medicationId);
        return medication.getManufacturerId();
    }

    public boolean medicationExist(String medicationId) {
        return medicationCRUD.medicationExist(medicationId);
    }

    public List<Medication> getListMedication(JSONObject jsonDto) {
        return medicationQuery.getListMedication(jsonDto);
    }
}
