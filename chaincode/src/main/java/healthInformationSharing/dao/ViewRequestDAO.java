package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.ViewRequest;
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

    public List<ViewRequest> getListViewRequestBySenderQuery(JSONObject jsonDto) {
        return viewRequestQuery.getListViewRequestBySenderQuery(jsonDto);
    }
}
