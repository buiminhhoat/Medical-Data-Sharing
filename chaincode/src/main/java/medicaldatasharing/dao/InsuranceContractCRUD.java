package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.InsuranceContract;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class InsuranceContractCRUD {
    private final static Logger LOG = Logger.getLogger(InsuranceContractCRUD.class.getName());
    private Context context;
    private String entityName;
    private Genson genson;

    public InsuranceContractCRUD(Context context, String entityName, Genson genson) {
        this.context = context;
        this.entityName = entityName;
        this.genson = genson;
    }

    public InsuranceContract addInsuranceContract(JSONObject jsonDto) {
        String insuranceContractId = jsonDto.getString("insuranceContractId");
        String insuranceProductId = jsonDto.getString("insuranceProductId");
        String patientId = jsonDto.getString("patientId");
        String insuranceCompanyId = jsonDto.getString("insuranceCompanyId");
        String startDate = jsonDto.getString("startDate");
        String endDate = jsonDto.getString("endDate");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String hashFile = jsonDto.getString("hashFile");

        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, insuranceContractId);
        String dbKey = compositeKey.toString();

        InsuranceContract insuranceContract = InsuranceContract.createInstance(
                insuranceContractId,
                insuranceProductId,
                patientId,
                insuranceCompanyId,
                startDate,
                endDate,
                dateCreated,
                dateModified,
                hashFile
        );

        String entityJsonString = genson.serialize(insuranceContract);
        context.getStub().putStringState(dbKey, entityJsonString);

        return insuranceContract;
    }

    public boolean insuranceContractExist(String insuranceContractId) {
        ChaincodeStub stub = context.getStub();
        String dbKey = stub.createCompositeKey(entityName, insuranceContractId).toString();
        byte[] value = stub.getState(dbKey);
        return value.length > 0;
    }

    public InsuranceContract getInsuranceContract(String insuranceContractId) {
        String dbKey = context.getStub().createCompositeKey(entityName, insuranceContractId).toString();
        byte[] result = context.getStub().getState(dbKey);
        return InsuranceContract.deserialize(result);
    }

    public InsuranceContract defineInsuranceContract(JSONObject jsonDto) {
        String insuranceContractId = jsonDto.getString("insuranceContractId");
        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, insuranceContractId);
        String dbKey = compositeKey.toString();

        InsuranceContract insuranceContract = getInsuranceContract(insuranceContractId);

        String entityJsonString = genson.serialize(insuranceContract);
        context.getStub().putStringState(dbKey, entityJsonString);

        return insuranceContract;
    }

    public InsuranceContract editInsuranceContract(JSONObject jsonDto) {
        String insuranceContractJson = jsonDto.getString("insuranceContractJson");

        Genson genson = new Genson();
        InsuranceContract insuranceContract = genson.deserialize(insuranceContractJson, InsuranceContract.class);
        String insuranceContractId = insuranceContract.getInsuranceContractId();

        System.out.println("editInsuranceContract: insuranceContractId: " + insuranceContractId);

        CompositeKey compositeKey = context.getStub().createCompositeKey(entityName, insuranceContractId);
        String dbKey = compositeKey.toString();

        System.out.println("dbKey: " + dbKey);

        String entityJsonString = genson.serialize(insuranceContract);
        context.getStub().putStringState(dbKey, entityJsonString);

        System.out.println("editInsuranceContract: " + insuranceContract.toString());
        System.out.println("entityJsonString: " + entityJsonString);
        return insuranceContract;
    }
}
