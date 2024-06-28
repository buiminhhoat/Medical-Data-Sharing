package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.PrescriptionDetails;
import org.hyperledger.fabric.contract.Context;

import java.util.List;

public class PrescriptionDetailsDAO {
    private PrescriptionDetailsCRUD prescriptionDetailsCRUD;
    private PrescriptionDetailsQuery prescriptionDetailsQuery;

    public PrescriptionDetailsDAO(Context context) {
        this.prescriptionDetailsCRUD = new PrescriptionDetailsCRUD(context, PrescriptionDetails.class.getSimpleName(), new Genson());
        this.prescriptionDetailsQuery = new PrescriptionDetailsQuery(context, PrescriptionDetails.class.getSimpleName());
    }

    public PrescriptionDetails addPrescriptionDetails(PrescriptionDetails prescriptionDetails) {
        return prescriptionDetailsCRUD.addPrescriptionDetails(prescriptionDetails);
    }

    public List<PrescriptionDetails> getListPrescriptionDetails(String prescriptionId) {
        return prescriptionDetailsQuery.getListPrescriptionDetails(prescriptionId);
    }
}
