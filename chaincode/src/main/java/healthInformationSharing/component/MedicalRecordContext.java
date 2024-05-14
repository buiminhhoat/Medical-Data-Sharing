package healthInformationSharing.component;

import healthInformationSharing.dao.MedicalRecordAccessRequestDAO;
import healthInformationSharing.dao.MedicalRecordDAO;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class MedicalRecordContext extends Context {

    private MedicalRecordDAO medicalRecordDAO;
    private MedicalRecordAccessRequestDAO medicalRecordAccessRequestDAO;

    public MedicalRecordContext(ChaincodeStub stub) {
        super(stub);
        medicalRecordDAO = new MedicalRecordDAO(this);
        medicalRecordAccessRequestDAO = new MedicalRecordAccessRequestDAO(this);
    }

    public MedicalRecordDAO getMedicalRecordDAO() {
        return medicalRecordDAO;
    }

    public MedicalRecordContext setMedicalRecordDAO(MedicalRecordDAO medicalRecordDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
        return this;
    }

    public MedicalRecordAccessRequestDAO getMedicalRecordAccessRequestDAO() {
        return medicalRecordAccessRequestDAO;
    }

    public MedicalRecordContext setMedicalRecordAccessRequestDAO(MedicalRecordAccessRequestDAO medicalRecordAccessRequestDAO) {
        this.medicalRecordAccessRequestDAO = medicalRecordAccessRequestDAO;
        return this;
    }
}
