package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class AppointmentRequestQuery {
    private final static Logger LOG = Logger.getLogger(AppointmentRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public AppointmentRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
