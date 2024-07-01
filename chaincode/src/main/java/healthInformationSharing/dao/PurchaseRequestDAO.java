package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.PurchaseRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

public class PurchaseRequestDAO {
    private PurchaseRequestCRUD purchaseRequestCRUD;
    private PurchaseRequestQuery purchaseRequestQuery;

    public PurchaseRequestDAO(Context context) {
        this.purchaseRequestCRUD = new PurchaseRequestCRUD(context, PurchaseRequest.class.getSimpleName(), new Genson());
        this.purchaseRequestQuery = new PurchaseRequestQuery(context, PurchaseRequest.class.getSimpleName());
    }

    public PurchaseRequestCRUD getPurchaseRequestCRUD() {
        return purchaseRequestCRUD;
    }

    public PurchaseRequestDAO setPurchaseRequestCRUD(PurchaseRequestCRUD requestCRUD) {
        this.purchaseRequestCRUD = requestCRUD;
        return this;
    }

    public PurchaseRequestQuery getPurchaseRequestQuery() {
        return purchaseRequestQuery;
    }

    public PurchaseRequestDAO setPurchaseRequestQuery(PurchaseRequestQuery requestQuery) {
        this.purchaseRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return purchaseRequestCRUD.requestExist(requestId);
    }

    public PurchaseRequest getPurchaseRequest(
            String requestId
    ) {
        return purchaseRequestCRUD.getPurchaseRequest(requestId);
    }

    public PurchaseRequest defineRequest(
            JSONObject jsonDto
    ) {
        return purchaseRequestCRUD.definePurchaseRequest(
                jsonDto
        );
    }

    public PurchaseRequest defineRequest(
            String requestId,
            String requestStatus
    ) {
        return purchaseRequestCRUD.definePurchaseRequest(requestId, requestStatus);
    }

    public PurchaseRequest sendPurchaseRequest(JSONObject jsonDto) {
        return purchaseRequestCRUD.sendPurchaseRequest(jsonDto);
    }
}
