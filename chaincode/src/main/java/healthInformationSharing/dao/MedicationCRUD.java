package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.MedicalRecord;
import healthInformationSharing.entity.Medication;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class MedicationCRUD {
    private final static Logger LOG = Logger.getLogger(MedicationCRUD.class.getName());
    private org.hyperledger.fabric.contract.Context ctx;
    private String entityName;
    private Genson genson;

    public MedicationCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public Medication addMedication(JSONObject jsonDto) {
        String manufacturerId = jsonDto.getString("manufacturerId");
        String medicationName = jsonDto.getString("medicationName");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String description = jsonDto.getString("description");
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";

        String medicationId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, medicationId);
        String dbKey = compositeKey.toString();

        Medication medication = Medication.createInstance(
                medicationId,
                manufacturerId,
                medicationName,
                dateCreated,
                dateModified,
                description,
                hashFile
        );

        String medicationStr = genson.serialize(medication);
        ctx.getStub().putStringState(dbKey, medicationStr);
        return medication;
    }

    public Medication getMedication(String medicationId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, medicationId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return Medication.deserialize(result);
    }

    public Medication editMedication(JSONObject jsonDto) {
        String medicationId = jsonDto.getString("medicationId");
        String manufacturerId = jsonDto.getString("manufacturerId");
        String medicationName = jsonDto.getString("medicationName");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String description = jsonDto.getString("description");
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";

        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, medicationId);
        String dbKey = compositeKey.toString();

        Medication medication = Medication.createInstance(
                medicationId,
                manufacturerId,
                medicationName,
                dateCreated,
                dateModified,
                description,
                hashFile
        );

        String medicationStr = genson.serialize(medication);
        ctx.getStub().putStringState(dbKey, medicationStr);
        return medication;
    }

    public boolean medicationExist(String medicationId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, medicationId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }
}
