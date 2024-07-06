package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Medication;
import healthInformationSharing.entity.Request;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class RequestDAO {
    private RequestCRUD requestCRUD;
    private RequestQuery requestQuery;

    public RequestDAO(Context context) {
        this.requestCRUD = new RequestCRUD(context, Request.class.getSimpleName(), new Genson());
        this.requestQuery = new RequestQuery(context, Request.class.getSimpleName());
    }

    public RequestCRUD getRequestCRUD() {
        return requestCRUD;
    }

    public RequestDAO setRequestCRUD(RequestCRUD requestCRUD) {
        this.requestCRUD = requestCRUD;
        return this;
    }

    public RequestQuery getRequestQuery() {
        return requestQuery;
    }

    public RequestDAO setRequestQuery(RequestQuery requestQuery) {
        this.requestQuery = requestQuery;
        return this;
    }

    public boolean requestExist(String requestId) {
        return requestCRUD.requestExist(requestId);
    }

    public Request getRequest(
            String requestId
    ) {
        return requestCRUD.getRequest(requestId);
    }

    public List<Request> getListRequest(JSONObject jsonDto) {
        return requestQuery.getListRequest(jsonDto);
    }
}
