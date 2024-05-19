package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Request;
import org.hyperledger.fabric.contract.Context;

public class AppointmentRequestDAO {
    private AppointmentRequestCRUD appointmentRequestCRUD;
    private AppointmentRequestQuery appointmentRequestQuery;

    public AppointmentRequestDAO(Context context) {
        this.appointmentRequestCRUD = new AppointmentRequestCRUD(context, Request.class.getSimpleName(), new Genson());
        this.appointmentRequestQuery = new AppointmentRequestQuery(context, Request.class.getSimpleName());
    }

    public AppointmentRequestCRUD getAppointmentRequestCRUD() {
        return appointmentRequestCRUD;
    }

    public AppointmentRequestDAO setAppointmentRequestCRUD(AppointmentRequestCRUD requestCRUD) {
        this.appointmentRequestCRUD = requestCRUD;
        return this;
    }

    public AppointmentRequestQuery getAppointmentRequestQuery() {
        return appointmentRequestQuery;
    }

    public AppointmentRequestDAO setAppointmentRequestQuery(AppointmentRequestQuery requestQuery) {
        this.appointmentRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return appointmentRequestCRUD.requestExist(requestId);
    }

    public Request getRequest(
            String requestId
    ) {
        return appointmentRequestCRUD.getAppointmentRequest(requestId);
    }

    public Request defineRequest(
            String requestId,
            String requestStatus,
            String accessAvailableFrom,
            String accessAvailableUntil
    ) {
        return appointmentRequestCRUD.defineAppointmentRequest(
                requestId,
                requestStatus,
                accessAvailableFrom,
                accessAvailableUntil
        );
    }

    public Request defineRequest(
            String requestId,
            String requestStatus
    ) {
        return appointmentRequestCRUD.defineAppointmentRequest(requestId, requestStatus);
    }

    public Request sendAppointmentRequest(String senderId,
                                          String recipientId,
                                          String dateCreated,
                                          String requestType) {
        return appointmentRequestCRUD.sendAppointmentRequest(
                senderId,
                recipientId,
                dateCreated,
                requestType
        );
    }
}
