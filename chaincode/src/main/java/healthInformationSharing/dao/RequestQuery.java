package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class RequestQuery {
    private final static Logger LOG = Logger.getLogger(RequestQuery.class.getName());
    private Context context;
    private String entityName;

    public RequestQuery(Context context, String entityName) {
        this.context = context;
        this.entityName = entityName;
    }
}
