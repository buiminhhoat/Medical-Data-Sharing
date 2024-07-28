package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.ConfirmPaymentRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class ConfirmPaymentRequestDAO {
    private ConfirmPaymentRequestCRUD confirmPaymentRequestCRUD;
    private ConfirmPaymentRequestQuery confirmPaymentRequestQuery;

    public ConfirmPaymentRequestDAO(Context context) {
        this.confirmPaymentRequestCRUD = new ConfirmPaymentRequestCRUD(context, ConfirmPaymentRequest.class.getSimpleName(), new Genson());
        this.confirmPaymentRequestQuery = new ConfirmPaymentRequestQuery(context, ConfirmPaymentRequest.class.getSimpleName());
    }

    public ConfirmPaymentRequestCRUD getConfirmPaymentRequestCRUD() {
        return confirmPaymentRequestCRUD;
    }

    public ConfirmPaymentRequestDAO setConfirmPaymentRequestCRUD(ConfirmPaymentRequestCRUD requestCRUD) {
        this.confirmPaymentRequestCRUD = requestCRUD;
        return this;
    }

    public ConfirmPaymentRequestQuery getConfirmPaymentRequestQuery() {
        return confirmPaymentRequestQuery;
    }

    public ConfirmPaymentRequestDAO setConfirmPaymentRequestQuery(ConfirmPaymentRequestQuery requestQuery) {
        this.confirmPaymentRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return confirmPaymentRequestCRUD.requestExist(requestId);
    }

    public ConfirmPaymentRequest getConfirmPaymentRequest(
            String requestId
    ) {
        return confirmPaymentRequestCRUD.getConfirmPaymentRequest(requestId);
    }

    public ConfirmPaymentRequest defineConfirmPaymentRequest(
            JSONObject jsonDto
    ) {
        return confirmPaymentRequestCRUD.defineConfirmPaymentRequest(
                jsonDto
        );
    }

    public ConfirmPaymentRequest defineConfirmPaymentRequest(
            String requestId,
            String requestStatus
    ) {
        return confirmPaymentRequestCRUD.defineConfirmPaymentRequest(requestId, requestStatus);
    }

    public ConfirmPaymentRequest sendConfirmPaymentRequest(JSONObject jsonDto) {
        return confirmPaymentRequestCRUD.sendConfirmPaymentRequest(jsonDto);
    }

    public List<ConfirmPaymentRequest> getListConfirmPaymentRequestByQuery(JSONObject jsonDto) {
        return confirmPaymentRequestQuery.getListConfirmPaymentRequestByQuery(
                jsonDto
        );
    }
}
