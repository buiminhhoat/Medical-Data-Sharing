package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import healthInformationSharing.enumeration.RequestType;
import org.hyperledger.fabric.contract.annotation.DataType;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.Property;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType()
public class ViewRequest extends Request {
    public ViewRequest() {
        this.requestType = RequestType.VIEW_RECORD;
    }

    public String getRequestId() {
        return requestId;
    }

    public ViewRequest setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getSenderId() {
        return senderId;
    }

    public ViewRequest setSenderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public ViewRequest setRecipientId(String recipientId) {
        this.recipientId = recipientId;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public ViewRequest setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public ViewRequest setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public ViewRequest setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ViewRequest setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public static ViewRequest createInstance(
            String requestId,
            String senderId,
            String recipientId,
            String dateCreated,
            String requestType,
            String requestStatus
    ) {
        ViewRequest request = new ViewRequest();
        request.setRequestId(requestId);
        request.setSenderId(senderId);
        request.setRecipientId(recipientId);
        request.setDateCreated(dateCreated);
        request.setRequestType(requestType);
        request.setRequestStatus(requestStatus);
        request.setEntityName(ViewRequest.class.getSimpleName());
        return request;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static ViewRequest deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, ViewRequest.class);
    }
}
