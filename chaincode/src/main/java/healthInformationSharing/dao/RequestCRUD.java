package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Request;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import java.util.logging.Logger;

public class RequestCRUD {
    private final static Logger LOG = Logger.getLogger(RequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public RequestCRUD(Context ctx, String entityName, Genson genson) {
        this.ctx = ctx;
        this.entityName = entityName;
        this.genson = genson;
    }

//    public Request sendRequest(
//            String senderId,
//            String recipientId,
//            String medicalRecordId,
//            String testName,
//            String dateCreated,
//            String requestType) {
//        String requestId = ctx.getStub().getTxId();
//        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
//        String dbKey = compositeKey.toString();
//
//        Request request = Request.createInstance(
//                requestId,
//                senderId,
//                recipientId,
//                dateCreated,
//                requestType,
//                RequestStatus.PENDING,
//                "",
//                "",
//                medicalRecordId,
//                testName);
//
//        String requestStr = genson.serialize(request);
//        ctx.getStub().putStringState(dbKey, requestStr);
//        return request;
//    }

    public boolean requestExist(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        return result.length > 0;
    }

    public Request getRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);

        return Request.deserialize(result);
    }

    public Request defineRequest(
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil) {
        Request request = getRequest(requestId);
        request.setRequestStatus(requestStatus);
        request.setAccessAvailableFrom(accessAvailableFrom);
        request.setAccessAvailableUntil(accessAvailableUntil);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public Request defineRequest(String requestId, String requestStatus) {
        Request request = getRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public Request sendAppointmentRequest(String senderId, String recipientId, String dateCreated, String requestType) {
        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        Request request = Request.createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                RequestStatus.PENDING,
                "",
                "");

        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }
}
