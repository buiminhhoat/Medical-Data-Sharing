package healthInformationSharing.component;

import healthInformationSharing.dao.AppointmentRequestCRUD;
import healthInformationSharing.dao.AppointmentRequestDAO;
import healthInformationSharing.dao.RequestDAO;
import healthInformationSharing.dao.MedicalRecordDAO;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class MedicalRecordContext extends Context {

    private MedicalRecordDAO medicalRecordDAO;
    private RequestDAO requestDAO;
    private AppointmentRequestDAO appointmentRequestDAO;

    public MedicalRecordContext(ChaincodeStub stub) {
        super(stub);
        medicalRecordDAO = new MedicalRecordDAO(this);
        requestDAO = new RequestDAO(this);
        appointmentRequestDAO = new AppointmentRequestDAO(this);
    }

    public MedicalRecordDAO getMedicalRecordDAO() {
        return medicalRecordDAO;
    }

    public MedicalRecordContext setMedicalRecordDAO(MedicalRecordDAO medicalRecordDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
        return this;
    }

    public RequestDAO getRequestDAO() {
        return requestDAO;
    }

    public MedicalRecordContext setRequestDAO(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
        return this;
    }

    public AppointmentRequestDAO getAppointmentRequestDAO() {
        return appointmentRequestDAO;
    }

    public MedicalRecordContext setAppointmentRequestDAO(AppointmentRequestDAO appointmentRequestDAO) {
        this.appointmentRequestDAO = appointmentRequestDAO;
        return this;
    }
}
