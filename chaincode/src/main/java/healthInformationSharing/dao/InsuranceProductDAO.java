package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.InsuranceProduct;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class InsuranceProductDAO {
    private InsuranceProductCRUD insuranceProductCRUD;
    private InsuranceProductQuery insuranceProductQuery;

    public InsuranceProductDAO(Context context) {
        this.insuranceProductCRUD = new InsuranceProductCRUD(context, InsuranceProduct.class.getSimpleName(), new Genson());
        this.insuranceProductQuery = new InsuranceProductQuery(context, InsuranceProduct.class.getSimpleName());
    }

    public InsuranceProductCRUD getInsuranceProductCRUD() {
        return insuranceProductCRUD;
    }

    public InsuranceProductDAO setInsuranceProductCRUD(InsuranceProductCRUD insuranceProductCRUD) {
        this.insuranceProductCRUD = insuranceProductCRUD;
        return this;
    }

    public InsuranceProductQuery getInsuranceProductQuery() {
        return insuranceProductQuery;
    }

    public InsuranceProductDAO setInsuranceProductQuery(InsuranceProductQuery insuranceProductQuery) {
        this.insuranceProductQuery = insuranceProductQuery;
        return this;
    }

    public InsuranceProduct addInsuranceProduct(JSONObject jsonDto) {
        return insuranceProductCRUD.addInsuranceProduct(jsonDto);
    }

    public InsuranceProduct editInsuranceProduct(JSONObject jsonDto) {
        return insuranceProductCRUD.editInsuranceProduct(jsonDto);
    }

    public String getInsuranceProductId(String insuranceProductId) {
        InsuranceProduct insuranceProduct = insuranceProductCRUD.getInsuranceProduct(insuranceProductId);
        return insuranceProduct.getInsuranceProductId();
    }

    public boolean insuranceProductExist(String insuranceProductId) {
        return insuranceProductCRUD.insuranceProductExist(insuranceProductId);
    }

    public List<InsuranceProduct> getListInsuranceProduct(JSONObject jsonDto) {
        return insuranceProductQuery.getListInsuranceProduct(jsonDto);
    }
}
