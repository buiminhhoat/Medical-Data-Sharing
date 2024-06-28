package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Prescription;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

public class PrescriptionDAO {
    private PrescriptionCRUD prescriptionCRUD;
    private PrescriptionQuery prescriptionQuery;

    public PrescriptionDAO(Context context) {
        this.prescriptionCRUD = new PrescriptionCRUD(context, Prescription.class.getSimpleName(), new Genson());
        this.prescriptionQuery = new PrescriptionQuery(context, Prescription.class.getSimpleName());
    }

    public Prescription addPrescription(JSONObject jsonDto) {
        return prescriptionCRUD.addPrescription(jsonDto);
    }

    public boolean prescriptionExist(String prescriptionId) {
        return prescriptionCRUD.prescriptionExist(prescriptionId);
    }

    public Prescription getPrescription(String prescriptionId) {
        return prescriptionCRUD.getPrescription(prescriptionId);
    }
}
