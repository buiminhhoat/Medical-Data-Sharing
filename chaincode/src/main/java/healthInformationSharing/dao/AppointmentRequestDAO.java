package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.AppointmentRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

public class AppointmentRequestDAO {
    private AppointmentRequestCRUD appointmentRequestCRUD;
    private AppointmentRequestQuery appointmentRequestQuery;

    public AppointmentRequestDAO(Context context) {
        this.appointmentRequestCRUD = new AppointmentRequestCRUD(context, AppointmentRequest.class.getSimpleName(), new Genson());
        this.appointmentRequestQuery = new AppointmentRequestQuery(context, AppointmentRequest.class.getSimpleName());
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

    public AppointmentRequest getAppointmentRequest(
            String requestId
    ) {
        return appointmentRequestCRUD.getAppointmentRequest(requestId);
    }

    public AppointmentRequest defineRequest(
            JSONObject jsonDto
    ) {
        return appointmentRequestCRUD.defineAppointmentRequest(
                jsonDto
        );
    }

    public AppointmentRequest defineRequest(
            String requestId,
            String requestStatus
    ) {
        return appointmentRequestCRUD.defineAppointmentRequest(requestId, requestStatus);
    }

    public AppointmentRequest sendAppointmentRequest(JSONObject jsonDto) {
        return appointmentRequestCRUD.sendAppointmentRequest(jsonDto);
    }
}
