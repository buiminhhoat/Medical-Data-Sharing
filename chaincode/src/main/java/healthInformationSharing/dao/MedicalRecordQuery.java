package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class MedicalRecordQuery {
    private final static Logger LOG = Logger.getLogger(MedicalRecordQuery.class.getName());
    private Context context;
    private String entityName;

    public MedicalRecordQuery(Context context, String entityName) {
        this.context = context;
        this.entityName = entityName;
    }
}
