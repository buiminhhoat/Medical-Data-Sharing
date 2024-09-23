package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.PaymentRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

public class PaymentRequestDAO {
    private PaymentRequestCRUD paymentRequestCRUD;
    private PaymentRequestQuery paymentRequestQuery;

    public PaymentRequestDAO(Context context) {
        this.paymentRequestCRUD = new PaymentRequestCRUD(context, PaymentRequest.class.getSimpleName(), new Genson());
        this.paymentRequestQuery = new PaymentRequestQuery(context, PaymentRequest.class.getSimpleName());
    }

    public PaymentRequestCRUD getPaymentRequestCRUD() {
        return paymentRequestCRUD;
    }

    public PaymentRequestDAO setPaymentRequestCRUD(PaymentRequestCRUD requestCRUD) {
        this.paymentRequestCRUD = requestCRUD;
        return this;
    }

    public PaymentRequestQuery getPaymentRequestQuery() {
        return paymentRequestQuery;
    }

    public PaymentRequestDAO setPaymentRequestQuery(PaymentRequestQuery requestQuery) {
        this.paymentRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return paymentRequestCRUD.requestExist(requestId);
    }

    public PaymentRequest getPaymentRequest(
            String requestId
    ) {
        return paymentRequestCRUD.getPaymentRequest(requestId);
    }

    public PaymentRequest definePaymentRequest(
            JSONObject jsonDto
    ) {
        return paymentRequestCRUD.definePaymentRequest(
                jsonDto
        );
    }

    public PaymentRequest definePaymentRequest(
            String requestId,
            String requestStatus
    ) {
        return paymentRequestCRUD.definePaymentRequest(requestId, requestStatus);
    }

    public PaymentRequest sendPaymentRequest(JSONObject jsonDto) {
        return paymentRequestCRUD.sendPaymentRequest(jsonDto);
    }
}
