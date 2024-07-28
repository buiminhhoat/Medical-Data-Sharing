package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class PrescriptionDetailsDAO {
    private PrescriptionDetailsCRUD prescriptionDetailsCRUD;
    private PrescriptionDetailsQuery prescriptionDetailsQuery;

    public PrescriptionDetailsDAO(Context context) {
        this.prescriptionDetailsCRUD = new PrescriptionDetailsCRUD(context, PrescriptionDetails.class.getSimpleName(), new Genson());
        this.prescriptionDetailsQuery = new PrescriptionDetailsQuery(context, PrescriptionDetails.class.getSimpleName());
    }

    public PrescriptionDetailsCRUD getPrescriptionDetailsCRUD() {
        return prescriptionDetailsCRUD;
    }

    public PrescriptionDetailsDAO setPrescriptionDetailsCRUD(PrescriptionDetailsCRUD prescriptionDetailsCRUD) {
        this.prescriptionDetailsCRUD = prescriptionDetailsCRUD;
        return this;
    }

    public PrescriptionDetailsQuery getPrescriptionDetailsQuery() {
        return prescriptionDetailsQuery;
    }

    public PrescriptionDetailsDAO setPrescriptionDetailsQuery(PrescriptionDetailsQuery prescriptionDetailsQuery) {
        this.prescriptionDetailsQuery = prescriptionDetailsQuery;
        return this;
    }

    public PrescriptionDetails addPrescriptionDetails(PrescriptionDetails prescriptionDetails) {
        return prescriptionDetailsCRUD.addPrescriptionDetails(prescriptionDetails);
    }

    public List<PrescriptionDetails> getListPrescriptionDetails(String prescriptionId) {
        return prescriptionDetailsQuery.getListPrescriptionDetails(prescriptionId);
    }

    public List<PrescriptionDetails> getListPrescriptionDetails(JSONObject jsonObject) {
        return prescriptionDetailsQuery.getListPrescriptionDetails(jsonObject);
    }

    public PrescriptionDetails getPrescriptionDetails(String prescriptionDetailId) {
        return prescriptionDetailsCRUD.getPrescriptionDetails(prescriptionDetailId);
    }

    public PrescriptionDetails updatePrescriptionDetails(PrescriptionDetails prescriptionDetails) {
        return prescriptionDetailsCRUD.updatePrescriptionDetails(prescriptionDetails);
    }
}
