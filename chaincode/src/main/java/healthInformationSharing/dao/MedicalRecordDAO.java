package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import org.hyperledger.fabric.contract.Context;

public class MedicalRecordDAO {
    private MedicalRecordCRUD medicalRecordCRUD;
    private MedicalRecordQuery medicalRecordQuery;

    public MedicalRecordDAO(Context context) {
        this.medicalRecordCRUD = new MedicalRecordCRUD(context, MedicalRecord.class.getSimpleName(), new Genson());
        this.medicalRecordQuery = new MedicalRecordQuery(context, MedicalRecord.class.getSimpleName());
    }

    public MedicalRecord addMedicalRecord(
            String medicalRecordId,
            String patientId,
            String doctorId,
            String medicalInstitutionId,
            String time,
            String testName,
            String relevantParameters
    ) {
        return medicalRecordCRUD.addMedicalRecord(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                time,
                testName,
                relevantParameters
        );
    }

    public boolean MedicalRecordExist(String medicalRecordId) {
        return medicalRecordCRUD.medicalRecordExist(medicalRecordId);
    }

    public MedicalRecord getMedicalRecord(String medicalRecordId) {
        return medicalRecordCRUD.getMedicalRecord(medicalRecordId);
    }
}
