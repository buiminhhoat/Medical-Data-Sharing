package healthInformationSharing.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;


@DataType
public class PurchaseDto {
    @JsonProperty("purchaseId")
    private String purchaseId;

    @JsonProperty("prescriptionId")
    private String prescriptionId;


    @JsonProperty("patientId")
    private String patientId;

    @JsonProperty("drugStoreId")
    private String drugStoreId;

    @JsonProperty("dateModified")
    private String dateModified;

    @JsonProperty("medicationPurchaseList")
    private List<MedicationPurchaseDto> medicationPurchaseList;

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public PurchaseDto setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
        return this;
    }

    public List<MedicationPurchaseDto> getMedicationPurchaseList() {
        return medicationPurchaseList;
    }

    public PurchaseDto setMedicationPurchaseList(List<MedicationPurchaseDto> medicationPurchaseList) {
        this.medicationPurchaseList = medicationPurchaseList;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public PurchaseDto setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getDrugStoreId() {
        return drugStoreId;
    }

    public PurchaseDto setDrugStoreId(String drugStoreId) {
        this.drugStoreId = drugStoreId;
        return this;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public PurchaseDto setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public PurchaseDto setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObj = new JSONObject();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                jsonObj.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObj;
    }
}
