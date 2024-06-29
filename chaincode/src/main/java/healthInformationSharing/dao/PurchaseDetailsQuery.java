package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PurchaseDetailsQuery {
    private final static Logger LOG = Logger.getLogger(PurchaseDetailsQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PurchaseDetailsQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
