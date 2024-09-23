package medicaldatasharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PurchaseRequestQuery {
    private final static Logger LOG = Logger.getLogger(PurchaseRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PurchaseRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
