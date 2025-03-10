package medicaldatasharing.component;

import medicaldatasharing.dao.*;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class MedicalRecordContext extends Context {

    private MedicalRecordDAO medicalRecordDAO;
    private AppointmentRequestDAO appointmentRequestDAO;
    private ViewRequestDAO viewRequestDAO;
    private MedicationDAO medicationDAO;
    private DrugDAO drugDAO;
    private PrescriptionDAO prescriptionDAO;
    private PrescriptionDetailsDAO prescriptionDetailsDAO;
    private ViewPrescriptionRequestDAO viewPrescriptionRequestDAO;
    private PurchaseDAO purchaseDAO;
    private PurchaseDetailsDAO purchaseDetailsDAO;
    private PaymentRequestDAO paymentRequestDAO;
    private RequestDAO requestDAO;

    public MedicalRecordContext(ChaincodeStub stub) {
        super(stub);
        medicalRecordDAO = new MedicalRecordDAO(this);
        appointmentRequestDAO = new AppointmentRequestDAO(this);
        viewRequestDAO = new ViewRequestDAO(this);
        medicationDAO = new MedicationDAO(this);
        drugDAO = new DrugDAO(this);
        prescriptionDAO = new PrescriptionDAO(this);
        prescriptionDetailsDAO = new PrescriptionDetailsDAO(this);
        viewPrescriptionRequestDAO = new ViewPrescriptionRequestDAO(this);
        purchaseDAO = new PurchaseDAO(this);
        purchaseDetailsDAO = new PurchaseDetailsDAO(this);
        paymentRequestDAO = new PaymentRequestDAO(this);
        requestDAO = new RequestDAO(this);
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

    public ViewRequestDAO getViewRequestDAO() {
        return viewRequestDAO;
    }

    public MedicalRecordContext setViewRequestDAO(ViewRequestDAO viewRequestDAO) {
        this.viewRequestDAO = viewRequestDAO;
        return this;
    }

    public MedicationDAO getMedicationDAO() {
        return medicationDAO;
    }

    public MedicalRecordContext setMedicationDAO(MedicationDAO medicationDAO) {
        this.medicationDAO = medicationDAO;
        return this;
    }

    public DrugDAO getDrugDAO() {
        return drugDAO;
    }

    public MedicalRecordContext setDrugDAO(DrugDAO drugDAO) {
        this.drugDAO = drugDAO;
        return this;
    }

    public PrescriptionDAO getPrescriptionDAO() {
        return prescriptionDAO;
    }

    public MedicalRecordContext setPrescriptionDAO(PrescriptionDAO prescriptionDAO) {
        this.prescriptionDAO = prescriptionDAO;
        return this;
    }

    public PrescriptionDetailsDAO getPrescriptionDetailsDAO() {
        return prescriptionDetailsDAO;
    }

    public MedicalRecordContext setPrescriptionDetailsDAO(PrescriptionDetailsDAO prescriptionDetailsDAO) {
        this.prescriptionDetailsDAO = prescriptionDetailsDAO;
        return this;
    }

    public ViewPrescriptionRequestDAO getViewPrescriptionRequestDAO() {
        return viewPrescriptionRequestDAO;
    }

    public MedicalRecordContext setViewPrescriptionRequestDAO(ViewPrescriptionRequestDAO viewPrescriptionRequestDAO) {
        this.viewPrescriptionRequestDAO = viewPrescriptionRequestDAO;
        return this;
    }

    public PurchaseDAO getPurchaseDAO() {
        return purchaseDAO;
    }

    public MedicalRecordContext setPurchaseDAO(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
        return this;
    }

    public PurchaseDetailsDAO getPurchaseDetailsDAO() {
        return purchaseDetailsDAO;
    }

    public MedicalRecordContext setPurchaseDetailsDAO(PurchaseDetailsDAO purchaseDetailsDAO) {
        this.purchaseDetailsDAO = purchaseDetailsDAO;
        return this;
    }

    public PaymentRequestDAO getPaymentRequestDAO() {
        return paymentRequestDAO;
    }

    public MedicalRecordContext setPaymentRequestDAO(PaymentRequestDAO paymentRequestDAO) {
        this.paymentRequestDAO = paymentRequestDAO;
        return this;
    }

    public RequestDAO getRequestDAO() {
        return requestDAO;
    }

    public MedicalRecordContext setRequestDAO(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
        return this;
    }
}
