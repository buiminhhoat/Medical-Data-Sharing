package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class MedicationQuery {
    private final static Logger LOG = Logger.getLogger(MedicationQuery.class.getName());
    private Context ctx;
    private String entityName;

    public MedicationQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
