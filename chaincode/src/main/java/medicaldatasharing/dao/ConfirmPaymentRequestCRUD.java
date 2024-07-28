package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.ConfirmPaymentRequest;
import medicaldatasharing.enumeration.RequestStatus;
import medicaldatasharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class ConfirmPaymentRequestCRUD {
    private final static Logger LOG = Logger.getLogger(ConfirmPaymentRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public ConfirmPaymentRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public ConfirmPaymentRequest getConfirmPaymentRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);

        Genson genson = new Genson();
        return genson.deserialize(result, ConfirmPaymentRequest.class);
    }

    public ConfirmPaymentRequest defineConfirmPaymentRequest(
            JSONObject jsonDto
    ) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");

        ConfirmPaymentRequest request = getConfirmPaymentRequest(requestId);

        request.setRequestStatus(requestStatus);
        request.setDateCreated(dateCreated);
        request.setDateModified(dateModified);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public ConfirmPaymentRequest defineConfirmPaymentRequest(String requestId, String requestStatus) {
        ConfirmPaymentRequest request = getConfirmPaymentRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public ConfirmPaymentRequest sendConfirmPaymentRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String paymentRequestId = jsonDto.getString("paymentRequestId");
        String requestId = ctx.getStub().getTxId();
        String requestType = RequestType.CONFIRM_PAYMENT;
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        ConfirmPaymentRequest request = ConfirmPaymentRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                dateModified,
                requestType,
                RequestStatus.PENDING,
                paymentRequestId
        );

        System.out.println("sendConfirmPaymentRequest: " + request);
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);

        System.out.println("sendConfirmPaymentRequest: " + requestStr);
        return request;
    }
}
