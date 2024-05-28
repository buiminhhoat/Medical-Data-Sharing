package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.AppointmentRequest;
import healthInformationSharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.util.logging.Logger;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AppointmentRequestCRUD {
    private final static Logger LOG = Logger.getLogger(AppointmentRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public AppointmentRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public AppointmentRequest getAppointmentRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        System.out.println("dbKey: " + dbKey);
        byte [] result = chaincodeStub.getState(dbKey);

        System.out.println(new String(result, UTF_8));

        Genson genson = new Genson();
        return genson.deserialize(result, AppointmentRequest.class);
    }

    public AppointmentRequest defineAppointmentRequest(JSONObject jsonDto) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        AppointmentRequest request = getAppointmentRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public AppointmentRequest defineAppointmentRequest(String requestId, String requestStatus) {
        AppointmentRequest request = getAppointmentRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public AppointmentRequest sendAppointmentRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateCreated = jsonDto.getString("dateCreated");
        String requestType = jsonDto.getString("requestType");
        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        AppointmentRequest request = AppointmentRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                requestType,
                RequestStatus.PENDING
        );

        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }
}
