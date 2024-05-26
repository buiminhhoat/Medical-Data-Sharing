package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class EditRequestQuery {
    private final static Logger LOG = Logger.getLogger(EditRequestQuery.class.getName());
    private Context ctx;
    private String entityName;

    public EditRequestQuery(Context ctx, String entityName) {
        this.ctx = ctx;
        this.entityName = entityName;
    }
}
