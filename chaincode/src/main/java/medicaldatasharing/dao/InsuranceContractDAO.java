package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.InsuranceContract;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class InsuranceContractDAO {
    private InsuranceContractCRUD insuranceContractCRUD;
    private InsuranceContractQuery insuranceContractQuery;

    public InsuranceContractDAO(Context context) {
        this.insuranceContractCRUD = new InsuranceContractCRUD(context, InsuranceContract.class.getSimpleName(), new Genson());
        this.insuranceContractQuery = new InsuranceContractQuery(context, InsuranceContract.class.getSimpleName());
    }

    public InsuranceContract addInsuranceContract(JSONObject jsonDto) {
        return insuranceContractCRUD.addInsuranceContract(jsonDto);
    }

    public boolean insuranceContractExist(String insuranceContractId) {
        return insuranceContractCRUD.insuranceContractExist(insuranceContractId);
    }

    public InsuranceContract getInsuranceContract(String insuranceContractId) {
        return insuranceContractCRUD.getInsuranceContract(insuranceContractId);
    }

    public InsuranceContract defineInsuranceContract(JSONObject jsonDto) {
        return insuranceContractCRUD.defineInsuranceContract(jsonDto);
    }

    public List<InsuranceContract> getListInsuranceContractByQuery(JSONObject jsonDto) {
        return insuranceContractQuery.getListInsuranceContractByQuery(
                jsonDto
        );
    }

    public InsuranceContract editInsuranceContract(JSONObject jsonDto) {
        return insuranceContractCRUD.editInsuranceContract(jsonDto);
    }
}
