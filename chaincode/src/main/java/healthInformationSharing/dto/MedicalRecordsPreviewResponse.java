package healthInformationSharing.dto;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

@DataType()
public class MedicalRecordsPreviewResponse {
    @Property
    private int total;

    @Property
    private List<MedicalRecordDto> medicalRecordDtoList;

    public MedicalRecordsPreviewResponse(int total, List<MedicalRecordDto> medicalRecordDtoList) {
        this.total = total;
        this.medicalRecordDtoList = medicalRecordDtoList;
    }

    public int getTotal() {
        return total;
    }

    public MedicalRecordsPreviewResponse setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<MedicalRecordDto> getMedicalRecordDtoList() {
        return medicalRecordDtoList;
    }

    public MedicalRecordsPreviewResponse setMedicalRecordDtoList(List<MedicalRecordDto> medicalRecordDtoList) {
        this.medicalRecordDtoList = medicalRecordDtoList;
        return this;
    }
}
