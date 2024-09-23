package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.PurchaseRequest;
import medicaldatasharing.enumeration.RequestStatus;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PurchaseRequestCRUD {
    private final static Logger LOG = Logger.getLogger(PurchaseRequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public PurchaseRequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public PurchaseRequest getPurchaseRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        System.out.println("dbKey: " + dbKey);
        byte [] result = chaincodeStub.getState(dbKey);

        System.out.println(new String(result, UTF_8));

        Genson genson = new Genson();
        return genson.deserialize(result, PurchaseRequest.class);
    }

    public PurchaseRequest definePurchaseRequest(JSONObject jsonDto) {
        String requestId = jsonDto.getString("requestId");
        String requestStatus = jsonDto.getString("requestStatus");
        String hashFile = jsonDto.getString("hashFile");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        PurchaseRequest request = getPurchaseRequest(requestId);
        request.setRequestStatus(requestStatus);
        request.setHashFile(hashFile);
        request.setDateCreated(dateCreated);
        request.setDateModified(dateModified);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public PurchaseRequest definePurchaseRequest(String requestId, String requestStatus) {
        PurchaseRequest request = getPurchaseRequest(requestId);
        request.setRequestStatus(requestStatus);

        String dbKey = ctx.getStub().createCompositeKey(entityName, requestId).toString();
        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }

    public PurchaseRequest sendPurchaseRequest(JSONObject jsonDto) {
        String senderId = jsonDto.getString("senderId");
        String recipientId = jsonDto.getString("recipientId");
        String dateCreated = jsonDto.getString("dateCreated");
        String dateModified = jsonDto.getString("dateModified");
        String requestType = jsonDto.getString("requestType");
        String insuranceProductId = jsonDto.getString("insuranceProductId");
        String startDate = jsonDto.getString("startDate");
        Long numberOfDaysInsured = Long.parseLong(jsonDto.getString("numberOfDaysInsured"));
        LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = date.plusDays(numberOfDaysInsured).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String hashFile = "";


        String requestId = ctx.getStub().getTxId();
        CompositeKey compositeKey = ctx.getStub().createCompositeKey(entityName, requestId);
        String dbKey = compositeKey.toString();

        PurchaseRequest request = PurchaseRequest.createInstance(
                requestId,
                senderId,
                recipientId,
                dateCreated,
                dateModified,
                requestType,
                RequestStatus.PENDING,
                insuranceProductId,
                startDate,
                endDate,
                hashFile
        );

        String requestStr = genson.serialize(request);
        ctx.getStub().putStringState(dbKey, requestStr);
        return request;
    }
}
