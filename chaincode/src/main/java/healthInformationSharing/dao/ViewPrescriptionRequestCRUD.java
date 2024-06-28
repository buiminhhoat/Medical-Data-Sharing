package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ViewPrescriptionRequest;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;

public class ViewPrescriptionRequestCRUD {
    private final static Logger LOG = Logger.getLogger(ViewPrescriptionRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public ViewPrescriptionRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public ViewPrescriptionRequest getViewPrescriptionRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        Genson genson = new Genson();
        return genson.deserialize(result, ViewPrescriptionRequest.class);
    }

    public ViewPrescriptionRequest defineViewPrescriptionRequest(JSONObject jsonDto) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        ViewPrescriptionRequest request = getViewPrescriptionRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public ViewPrescriptionRequest sendViewPrescriptionRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateModified = jsonDto.getString("dateModified");
        String requestType = jsonDto.getString("requestType");

        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        ViewPrescriptionRequest request = ViewPrescriptionRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateModified,
                requestType,
                RequestStatus.PENDING
        );

        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }
}
