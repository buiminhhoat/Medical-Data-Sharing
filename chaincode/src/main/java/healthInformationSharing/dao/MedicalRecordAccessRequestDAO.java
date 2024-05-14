package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.MedicalRecordAccessRequest;
import org.hyperledger.fabric.contract.Context;

public class MedicalRecordAccessRequestDAO {
    private MedicalRecordAccessRequestCRUD medicalRecordAccessRequestCRUD;
    private MedicalRecordAccessRequestQuery medicalRecordAccessRequestQuery;

    public MedicalRecordAccessRequestDAO(Context context) {
        this.medicalRecordAccessRequestCRUD = new MedicalRecordAccessRequestCRUD(context, MedicalRecordAccessRequest.class.getSimpleName(), new Genson());
        this.medicalRecordAccessRequestQuery = new MedicalRecordAccessRequestQuery(context, MedicalRecordAccessRequest.class.getSimpleName());
    }

    public MedicalRecordAccessRequestCRUD getMedicalRecordAccessRequestCRUD() {
        return medicalRecordAccessRequestCRUD;
    }

    public MedicalRecordAccessRequestDAO setMedicalRecordAccessRequestCRUD(MedicalRecordAccessRequestCRUD medicalRecordAccessRequestCRUD) {
        this.medicalRecordAccessRequestCRUD = medicalRecordAccessRequestCRUD;
        return this;
    }

    public MedicalRecordAccessRequestQuery getMedicalRecordAccessRequestQuery() {
        return medicalRecordAccessRequestQuery;
    }

    public MedicalRecordAccessRequestDAO setMedicalRecordAccessRequestQuery(MedicalRecordAccessRequestQuery medicalRecordAccessRequestQuery) {
        this.medicalRecordAccessRequestQuery = medicalRecordAccessRequestQuery;
        return this;
    }

    public MedicalRecordAccessRequest addMedicalRecordAccessRequest(
            String patientId,
            String requesterId,
            String medicalRecordId,
            String testName,
            String dateCreated
    ) {
        return medicalRecordAccessRequestCRUD.addMedicalRecordAccessRequest(
                patientId,
                requesterId,
                medicalRecordId,
                testName,
                dateCreated
        );
    }
}
