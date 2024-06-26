package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class PrescriptionDetailsQuery {
    private final static Logger LOG = Logger.getLogger(PrescriptionDetailsQuery.class.getName());
    private Context ctx;
    private String entityName;

    public PrescriptionDetailsQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
