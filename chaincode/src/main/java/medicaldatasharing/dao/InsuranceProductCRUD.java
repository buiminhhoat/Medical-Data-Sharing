package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.InsuranceProduct;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class InsuranceProductCRUD {
    private final static Logger LOG = Logger.getLogger(InsuranceProductCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public InsuranceProductCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public InsuranceProduct addInsuranceProduct(JSONObject jsonDto) {
        String insuranceProductName = jsonDto.has("insuranceProductName") ? jsonDto.getString("insuranceProductName") : "";
        String insuranceCompanyId = jsonDto.has("insuranceCompanyId") ? jsonDto.getString("insuranceCompanyId") : "";
        String dateCreated = jsonDto.has("dateCreated") ? jsonDto.getString("dateCreated") : "";
        String dateModified = jsonDto.has("dateModified") ? jsonDto.getString("dateModified") : "";
        String description = jsonDto.has("description") ? jsonDto.getString("description") : "";
        String numberOfDaysInsured = jsonDto.has("numberOfDaysInsured") ? jsonDto.getString("numberOfDaysInsured") : "0";
        String price = jsonDto.has("price") ? jsonDto.getString("price") : "";
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";


        String insuranceProductId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, insuranceProductId);
        String dbKey = compositeKey.toString();

        InsuranceProduct insuranceProduct = InsuranceProduct.createInstance(
                insuranceProductId,
                insuranceProductName,
                insuranceCompanyId,
                dateCreated,
                dateModified,
                description,
                numberOfDaysInsured,
                price,
                hashFile
        );

        String insuranceProductStr = genson.serialize(insuranceProduct);
        ctx.getStub().putStringState(dbKey, insuranceProductStr);
        return insuranceProduct;
    }

    public InsuranceProduct getInsuranceProduct(String insuranceProductId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, insuranceProductId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return InsuranceProduct.deserialize(result);
    }

    public InsuranceProduct editInsuranceProduct(JSONObject jsonDto) {
        String insuranceProductId = jsonDto.has("insuranceProductId") ? jsonDto.getString("insuranceProductId") : "";
        String insuranceProductName = jsonDto.has("insuranceProductName") ? jsonDto.getString("insuranceProductName") : "";
        String insuranceCompanyId = jsonDto.has("insuranceCompanyId") ? jsonDto.getString("insuranceCompanyId") : "";
        String dateCreated = jsonDto.has("dateCreated") ? jsonDto.getString("dateCreated") : "";
        String dateModified = jsonDto.has("dateModified") ? jsonDto.getString("dateModified") : "";
        String description = jsonDto.has("description") ? jsonDto.getString("description") : "";
        String numberOfDaysInsured = jsonDto.has("numberOfDaysInsured") ? jsonDto.getString("numberOfDaysInsured") : "";
        String price = jsonDto.has("price") ? jsonDto.getString("price") : "";
        String hashFile = jsonDto.has("hashFile") ? jsonDto.getString("hashFile") : "";

        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, insuranceProductId);
        String dbKey = compositeKey.toString();

        InsuranceProduct insuranceProduct = InsuranceProduct.createInstance(
                insuranceProductId,
                insuranceProductName,
                insuranceCompanyId,
                dateCreated,
                dateModified,
                description,
                numberOfDaysInsured,
                price,
                hashFile
        );

        String insuranceProductStr = genson.serialize(insuranceProduct);
        ctx.getStub().putStringState(dbKey, insuranceProductStr);
        return insuranceProduct;
    }

    public boolean insuranceProductExist(String insuranceProductId) {
        String dbKey = ctx.getStub().createCompositeKey(entityName, insuranceProductId).toString();
        byte[] result = ctx.getStub().getState(dbKey);
        return result.length > 0;
    }
}
