package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.Request;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.logging.Logger;

public class RequestCRUD {
    private final static Logger LOG = Logger.getLogger(RequestCRUD.class.getName());
    private Context ctx;
    private String entityName;
    private Genson genson;

    public RequestCRUD(Context ctx, String entityName, Genson genson) {
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

    public Request getRequest(String requestId) {
        ChaincodeStub chaincodeStub = ctx.getStub();
        String dbKey = chaincodeStub.createCompositeKey(entityName, requestId).toString();

        byte [] result = chaincodeStub.getState(dbKey);
        Genson genson = new Genson();
        return genson.deserialize(result, Request.class);
    }
}
