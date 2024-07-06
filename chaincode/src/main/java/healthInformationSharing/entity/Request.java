package healthInformationSharing.entity;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class Request {
    @Property()
    @JsonProperty("requestId")
    protected String requestId;

    @Property()
    @JsonProperty("senderId")
    protected String senderId;

    @Property()
    @JsonProperty("recipientId")
    protected String recipientId;

    @Property()
    @JsonProperty("dateModified")
    protected String dateModified;

    @Property()
    @JsonProperty("requestType")
    protected String requestType;

    @Property()
    @JsonProperty("requestStatus")
    protected String requestStatus;

    @Property()
    @JsonProperty("entityName")
    protected String entityName;

    public Request() {
    }
}
