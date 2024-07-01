package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.PaymentRequest;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class PaymentRequestCRUD {
    private final static Logger LOG = Logger.getLogger(PaymentRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public PaymentRequestCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

    public boolean requestExist(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        return result.length > 0;
    }

    public PaymentRequest getPaymentRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);

        Genson genson = new Genson();
        return genson.deserialize(result, PaymentRequest.class);
    }

    public PaymentRequest definePaymentRequest(
            JSONObject jsonDto
    ) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        PaymentRequest request = getPaymentRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public PaymentRequest definePaymentRequest(String requestId, String requestStatus) {
        PaymentRequest request = getPaymentRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public PaymentRequest sendPaymentRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateModified = jsonDto.getString("dateModified");
        String requestType = jsonDto.getString("requestType");
        String insuranceContractId = jsonDto.getString("insuranceContractId");
        String medicalRecordId = jsonDto.getString("medicalRecordId");
        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        PaymentRequest request = PaymentRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateModified,
                requestType,
                RequestStatus.PENDING,
                insuranceContractId,
                medicalRecordId);

        System.out.println("sendPaymentRequest: " + request);
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);

        System.out.println("sendPaymentRequest: " + requestStr);
        return request;
    }
}
