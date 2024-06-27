package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.EditRequest;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class EditRequestCRUD {
    private final static Logger LOG = Logger.getLogger(EditRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public EditRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public EditRequest getEditRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);

        Genson genson = new Genson();
        return genson.deserialize(result, EditRequest.class);
    }

    public EditRequest defineEditRequest(
            JSONObject jsonDto
    ) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        EditRequest request = getEditRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public EditRequest defineEditRequest(String requestId, String requestStatus) {
        EditRequest request = getEditRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public EditRequest sendEditRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateModified = jsonDto.getString("dateModified");
        String requestType = jsonDto.getString("requestType");
        String medicalRecordJson = jsonDto.getString("medicalRecordJson");
        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        EditRequest request = EditRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateModified,
                requestType,
                RequestStatus.PENDING,
                medicalRecordJson);

        System.out.println("sendEditRequest: " + request);
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);

        System.out.println("sendEditRequest: " + requestStr);
        return request;
    }
}
