package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ViewPrescriptionRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class ViewPrescriptionRequestDAO {
    private ViewPrescriptionRequestCRUD viewPrescriptionRequestCRUD;
    private ViewPrescriptionRequestQuery viewPrescriptionRequestQuery;

    public ViewPrescriptionRequestDAO(Context context) {
        this.viewPrescriptionRequestCRUD = new ViewPrescriptionRequestCRUD(context, ViewPrescriptionRequest.class.getSimpleName(), new Genson());
        this.viewPrescriptionRequestQuery = new ViewPrescriptionRequestQuery(context, ViewPrescriptionRequest.class.getSimpleName());
    }

    public ViewPrescriptionRequestCRUD getViewPrescriptionRequestCRUD() {
        return viewPrescriptionRequestCRUD;
    }

    public ViewPrescriptionRequestDAO setViewPrescriptionRequestCRUD(ViewPrescriptionRequestCRUD requestCRUD) {
        this.viewPrescriptionRequestCRUD = requestCRUD;
        return this;
    }

    public ViewPrescriptionRequestQuery getViewPrescriptionRequestQuery() {
        return viewPrescriptionRequestQuery;
    }

    public ViewPrescriptionRequestDAO setViewPrescriptionRequestQuery(ViewPrescriptionRequestQuery requestQuery) {
        this.viewPrescriptionRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return viewPrescriptionRequestCRUD.requestExist(requestId);
    }

    public ViewPrescriptionRequest getViewPrescriptionRequest(
            String requestId
    ) {
        return viewPrescriptionRequestCRUD.getViewPrescriptionRequest(requestId);
    }

    public ViewPrescriptionRequest defineViewPrescriptionRequest(
            JSONObject jsonDto
    ) {
        return viewPrescriptionRequestCRUD.defineViewPrescriptionRequest(
                jsonDto
        );
    }

    public ViewPrescriptionRequest sendViewPrescriptionRequest(JSONObject jsonDto) {
        return viewPrescriptionRequestCRUD.sendViewPrescriptionRequest(jsonDto);
    }

    public List<ViewPrescriptionRequest> getListViewPrescriptionRequestBySenderQuery(JSONObject jsonDto) {
        return viewPrescriptionRequestQuery.getListViewPrescriptionRequestBySenderQuery(jsonDto);
    }
}
