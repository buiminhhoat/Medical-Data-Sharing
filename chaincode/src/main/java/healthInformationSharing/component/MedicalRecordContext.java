package healthInformationSharing.component;

import healthInformationSharing.dao.*;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class MedicalRecordContext extends Context {

    private MedicalRecordDAO medicalRecordDAO;
    private AppointmentRequestDAO appointmentRequestDAO;
    private EditRequestDAO editRequestDAO;
    private ViewRequestDAO viewRequestDAO;

    public MedicalRecordContext(ChaincodeStub stub) {
        super(stub);
        medicalRecordDAO = new MedicalRecordDAO(this);
        appointmentRequestDAO = new AppointmentRequestDAO(this);
        editRequestDAO = new EditRequestDAO(this);
        viewRequestDAO = new ViewRequestDAO(this);
    }

    public MedicalRecordDAO getMedicalRecordDAO() {
        return medicalRecordDAO;
    }

    public MedicalRecordContext setMedicalRecordDAO(MedicalRecordDAO medicalRecordDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
        return this;
    }

    public AppointmentRequestDAO getAppointmentRequestDAO() {
        return appointmentRequestDAO;
    }

    public MedicalRecordContext setAppointmentRequestDAO(AppointmentRequestDAO appointmentRequestDAO) {
        this.appointmentRequestDAO = appointmentRequestDAO;
        return this;
    }

    public EditRequestDAO getEditRequestDAO() {
        return editRequestDAO;
    }

    public MedicalRecordContext setEditRequestDAO(EditRequestDAO editRequestDAO) {
        this.editRequestDAO = editRequestDAO;
        return this;
    }

    public ViewRequestDAO getViewRequestDAO() {
        return viewRequestDAO;
    }

    public MedicalRecordContext setViewRequestDAO(ViewRequestDAO viewRequestDAO) {
        this.viewRequestDAO = viewRequestDAO;
        return this;
    }
}
