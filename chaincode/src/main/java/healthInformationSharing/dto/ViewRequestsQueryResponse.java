package healthInformationSharing.dto;

import healthInformationSharing.entity.ViewRequest;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

@DataType()
public class ViewRequestsQueryResponse {
    @Property
    private int total;

    @Property
    private List<ViewRequest> viewRequestList;

    public ViewRequestsQueryResponse(int total, List<ViewRequest> viewRequestList) {
        this.total = total;
        this.viewRequestList = viewRequestList;
    }

    public int getTotal() {
        return total;
    }

    public ViewRequestsQueryResponse setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<ViewRequest> getViewRequestList() {
        return viewRequestList;
    }

    public ViewRequestsQueryResponse setViewRequestList(List<ViewRequest> viewRequestList) {
        this.viewRequestList = viewRequestList;
        return this;
    }
}
