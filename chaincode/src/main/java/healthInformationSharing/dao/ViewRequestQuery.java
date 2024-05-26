package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class ViewRequestQuery {
    private final static Logger LOG = Logger.getLogger(ViewRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public ViewRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
