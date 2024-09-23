package medicaldatasharing.dao;

import com.owlike.genson.Genson;
import medicaldatasharing.entity.ViewRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class ViewRequestDAO {
    private ViewRequestCRUD viewRequestCRUD;
    private ViewRequestQuery viewRequestQuery;

    public ViewRequestDAO(Context context) {
        this.viewRequestCRUD = new ViewRequestCRUD(context, ViewRequest.class.getSimpleName(), new Genson());
        this.viewRequestQuery = new ViewRequestQuery(context, ViewRequest.class.getSimpleName());
    }

    public ViewRequestCRUD getViewRequestCRUD() {
        return viewRequestCRUD;
    }

    public ViewRequestDAO setViewRequestCRUD(ViewRequestCRUD requestCRUD) {
        this.viewRequestCRUD = requestCRUD;
        return this;
    }

    public ViewRequestQuery getViewRequestQuery() {
        return viewRequestQuery;
    }

    public ViewRequestDAO setViewRequestQuery(ViewRequestQuery requestQuery) {
        this.viewRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return viewRequestCRUD.requestExist(requestId);
    }

    public ViewRequest getViewRequest(
            String requestId
    ) {
        return viewRequestCRUD.getViewRequest(requestId);
    }

    public ViewRequest defineViewRequest(
            JSONObject jsonDto
    ) {
        return viewRequestCRUD.defineViewRequest(
                jsonDto
        );
    }

    public ViewRequest sendViewRequest(JSONObject jsonDto) {
        return viewRequestCRUD.sendViewRequest(jsonDto);
    }

    public List<ViewRequest> getListViewRequestQuery(JSONObject jsonDto) {
        return viewRequestQuery.getListViewRequestQuery(jsonDto);
    }

    public List<ViewRequest> getListViewRequestBySenderQuery(JSONObject jsonDto) {
        return viewRequestQuery.getListViewRequestQuery(jsonDto);
    }


    public List<ViewRequest> getListViewRequestByRecipientQuery(JSONObject jsonDto) {
        return viewRequestQuery.getListViewRequestQuery(jsonDto);
    }

    public List<String> getListAllAuthorizedPatientForDoctor(JSONObject jsonDto) {
        return viewRequestQuery.getListAllAuthorizedPatientForDoctor(jsonDto);
    }

    public List<String> getListAllAuthorizedPatientForScientist(JSONObject jsonDto) {
        return viewRequestQuery.getListAllAuthorizedPatientForScientist(jsonDto);
    }

    public List<String> getListAllAuthorizedPatientForManufacturer(JSONObject jsonDto) {
        return viewRequestQuery.getListAllAuthorizedPatientForManufacturer(jsonDto);
    }

    public ViewRequest sendViewRequestAccepted(JSONObject jsonDto) {
        return viewRequestCRUD.sendViewRequestAccepted(jsonDto);
    }
}
