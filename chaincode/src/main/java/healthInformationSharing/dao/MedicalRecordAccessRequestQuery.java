package healthInformationSharing.dao;

import org.hyperledger.fabric.contract.Context;

import java.util.logging.Logger;

public class MedicalRecordAccessRequestQuery {
    private final static Logger LOG = Logger.getLogger(MedicalRecordAccessRequestQuery.class.getName());
    private Context context;
    private String entityName;

    public MedicalRecordAccessRequestQuery(Context context, String entityName) {
        this.context = context;
        this.entityName = entityName;
    }
}
