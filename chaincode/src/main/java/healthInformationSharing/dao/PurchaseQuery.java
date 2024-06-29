package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PurchaseQuery {
    private final static Logger LOG = Logger.getLogger(PurchaseQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PurchaseQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
