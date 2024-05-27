package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ViewRequest;
import org.hyperledger.fabric.contract.Context;

public class ViewRequestDAO {
    private ViewRequestCRUD viewRequestCRUD;
    private ViewRequestQuery viewViewRequestQuery;

    public ViewRequestDAO(Context context) {
        this.viewRequestCRUD = new ViewRequestCRUD(context, ViewRequest.class.getSimpleName(), new Genson());
        this.viewViewRequestQuery = new ViewRequestQuery(context, ViewRequest.class.getSimpleName());
    }

    public ViewRequestCRUD getViewRequestCRUD() {
        return viewRequestCRUD;
    }

    public ViewRequestDAO setViewRequestCRUD(ViewRequestCRUD requestCRUD) {
        this.viewRequestCRUD = requestCRUD;
        return this;
    }

    public ViewRequestQuery getViewRequestQuery() {
        return viewViewRequestQuery;
    }

    public ViewRequestDAO setViewRequestQuery(ViewRequestQuery requestQuery) {
        this.viewViewRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return viewRequestCRUD.requestExist(requestId);
    }

    public ViewRequest getViewRequest(
            String requestId
    ) {
        return viewRequestCRUD.getViewRequest(requestId);
    }

    public ViewRequest defineViewRequest(
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        return viewRequestCRUD.defineViewRequest(
                requestId,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }

    public ViewRequest defineViewRequest(
            String requestId,
            String requestStatus
    ) {
        return viewRequestCRUD.defineViewRequest(requestId, requestStatus);
    }

    public ViewRequest sendViewRequest(String senderId,
                                   String recipientId,
                                   String dateCreated,
                                   String requestType) {
        return viewRequestCRUD.sendViewRequest(
                senderId,
                recipientId,
                dateCreated,
                requestType
        );
    }
}
