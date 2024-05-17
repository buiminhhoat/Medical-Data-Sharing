package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Request;
import org.hyperledger.fabric.contract.Context;

public class RequestDAO {
    private RequestCRUD requestCRUD;
    private RequestQuery requestQuery;

    public RequestDAO(Context context) {
        this.requestCRUD = new RequestCRUD(context, Request.class.getSimpleName(), new Genson());
        this.requestQuery = new RequestQuery(context, Request.class.getSimpleName());
    }

    public RequestCRUD getRequestCRUD() {
        return requestCRUD;
    }

    public RequestDAO setRequestCRUD(RequestCRUD requestCRUD) {
        this.requestCRUD = requestCRUD;
        return this;
    }

    public RequestQuery getRequestQuery() {
        return requestQuery;
    }

    public RequestDAO setRequestQuery(RequestQuery requestQuery) {
        this.requestQuery = requestQuery;
        return this;
    }

    public Request sendRequest(
            String senderId,
            String recipientId,
            String medicalRecordId,
            String testName,
            String dateCreated,
            String requestType
    ) {
        return requestCRUD.sendRequest(
                senderId,
                recipientId,
                medicalRecordId,
                testName,
                dateCreated,
                requestType
        );
    }

    public boolean requestExist(String requestId) {
        return requestCRUD.requestExist(requestId);
    }

    public Request getRequest(
            String requestId
    ) {
        return requestCRUD.getRequest(requestId);
    }

    public Request defineRequest(
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        return requestCRUD.defineRequest(
                requestId,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }
}
