package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.dto.MedicalRecordDto;
import healthInformationSharing.dto.MedicalRecordsPreviewResponse;
import healthInformationSharing.entity.MedicalRecord;
import org.hyperledger.fabric.contract.Context;

import java.util.List;

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
            String dateCreated,
            String testName,
            String details
    ) {
        return medicalRecordCRUD.addMedicalRecord(
                medicalRecordId,
                patientId,
                doctorId,
                medicalInstitutionId,
                dateCreated,
                testName,
                details
        );
    }

    public boolean medicalRecordExist(String medicalRecordId) {
        return medicalRecordCRUD.medicalRecordExist(medicalRecordId);
    }

    public MedicalRecord getMedicalRecord(String medicalRecordId) {
        return medicalRecordCRUD.getMedicalRecord(medicalRecordId);
    }

    public MedicalRecord defineMedicalRecord(String medicalRecordId, String medicalRecordStatus) {
        return medicalRecordCRUD.defineMedicalRecord(medicalRecordId, medicalRecordStatus);
    }

    public List<MedicalRecordDto> getMedicalRecordsPreview(String patientId,
                                                           String doctorId,
                                                           String medicalInstitutionId,
                                                           String from,
                                                           String until,
                                                           String testName,
                                                           String medicalRecordStatus,
                                                           String details,
                                                           String sortingOrder) {
        return medicalRecordQuery.getMedicalRecordsPreview(
                patientId,
                doctorId,
                medicalInstitutionId,
                from,
                until,
                testName,
                medicalRecordStatus,
                details,
                sortingOrder
        );
    }
}
