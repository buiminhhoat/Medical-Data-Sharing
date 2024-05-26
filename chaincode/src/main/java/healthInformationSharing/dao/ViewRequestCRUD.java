package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ViewRequest;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.util.logging.Logger;

public class ViewRequestCRUD {
    private final static Logger LOG = Logger.getLogger(ViewRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public ViewRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public ViewRequest getViewRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        Genson genson = new Genson();
        return genson.deserialize(result, ViewRequest.class);
    }

    public ViewRequest defineViewRequest(
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil) {
        ViewRequest request = getViewRequest(requestId);
        request.setRequestStatus(requestStatus);
        request.setAccessAvailableFrom(accessAvailableFrom);
        request.setAccessAvailableUntil(accessAvailableUntil);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public ViewRequest defineViewRequest(String requestId, String requestStatus) {
        ViewRequest request = getViewRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public ViewRequest sendViewRequest(String senderId,
                                       String recipientId,
                                       String dateCreated,
                                       String requestType,
                                       String medicalRecordId) {
        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        ViewRequest request = ViewRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                RequestStatus.PENDING,
                "",
                "",
                medicalRecordId);

        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }
}
