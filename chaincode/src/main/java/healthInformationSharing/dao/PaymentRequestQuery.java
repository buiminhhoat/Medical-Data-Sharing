package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PaymentRequestQuery {
    private final static Logger LOG = Logger.getLogger(PaymentRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PaymentRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
