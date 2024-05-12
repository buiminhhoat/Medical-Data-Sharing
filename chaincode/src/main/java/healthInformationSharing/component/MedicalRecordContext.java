package healthInformationSharing.component;

import healthInformationSharing.dao.MedicalRecordDAO;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class MedicalRecordContext extends Context {

    private healthInformationSharing.dao.MedicalRecordDAO MedicalRecordDAO;

    public MedicalRecordContext(ChaincodeStub stub) {
        super(stub);
        MedicalRecordDAO = new MedicalRecordDAO(this);
    }

    public MedicalRecordDAO getMedicalRecordDAO() {
        return MedicalRecordDAO;
    }
}
