package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PrescriptionQuery {
    private final static Logger LOG = Logger.getLogger(PrescriptionQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PrescriptionQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
