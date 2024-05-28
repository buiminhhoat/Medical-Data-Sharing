package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.EditRequest;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

public class EditRequestDAO {
    private EditRequestCRUD editRequestCRUD;
    private EditRequestQuery editRequestQuery;

    public EditRequestDAO(Context context) {
        this.editRequestCRUD = new EditRequestCRUD(context, EditRequest.class.getSimpleName(), new Genson());
        this.editRequestQuery = new EditRequestQuery(context, EditRequest.class.getSimpleName());
    }

    public EditRequestCRUD getEditRequestCRUD() {
        return editRequestCRUD;
    }

    public EditRequestDAO setEditRequestCRUD(EditRequestCRUD requestCRUD) {
        this.editRequestCRUD = requestCRUD;
        return this;
    }

    public EditRequestQuery getEditRequestQuery() {
        return editRequestQuery;
    }

    public EditRequestDAO setEditRequestQuery(EditRequestQuery requestQuery) {
        this.editRequestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return editRequestCRUD.requestExist(requestId);
    }

    public EditRequest getEditRequest(
            String requestId
    ) {
        return editRequestCRUD.getEditRequest(requestId);
    }

    public EditRequest defineEditRequest(
            JSONObject jsonDto
    ) {
        return editRequestCRUD.defineEditRequest(
                jsonDto
        );
    }

    public EditRequest defineEditRequest(
            String requestId,
            String requestStatus
    ) {
        return editRequestCRUD.defineEditRequest(requestId, requestStatus);
    }

    public EditRequest sendEditRequest(JSONObject jsonDto) {
        return editRequestCRUD.sendEditRequest(jsonDto);
    }
}
