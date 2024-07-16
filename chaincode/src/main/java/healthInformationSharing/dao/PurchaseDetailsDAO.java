package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Purchase;
import healthInformationSharing.entity.PurchaseDetails;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class PurchaseDetailsDAO {
    private PurchaseDetailsCRUD purchaseDetailsCRUD;
    private PurchaseDetailsQuery purchaseDetailsQuery;

    public PurchaseDetailsDAO(Context context) {
        this.purchaseDetailsCRUD = new PurchaseDetailsCRUD(context, PurchaseDetails.class.getSimpleName(), new Genson());
        this.purchaseDetailsQuery = new PurchaseDetailsQuery(context, PurchaseDetails.class.getSimpleName());
    }

    public PurchaseDetailsCRUD getPurchaseDetailsCRUD() {
        return purchaseDetailsCRUD;
    }

    public PurchaseDetailsDAO setPurchaseDetailsCRUD(PurchaseDetailsCRUD purchaseDetailsCRUD) {
        this.purchaseDetailsCRUD = purchaseDetailsCRUD;
        return this;
    }

    public PurchaseDetailsQuery getPurchaseDetailsQuery() {
        return purchaseDetailsQuery;
    }

    public PurchaseDetailsDAO setPurchaseDetailsQuery(PurchaseDetailsQuery purchaseDetailsQuery) {
        this.purchaseDetailsQuery = purchaseDetailsQuery;
        return this;
    }

    public PurchaseDetails addPurchaseDetails(JSONObject jsonDto) {
        return purchaseDetailsCRUD.addPurchaseDetails(jsonDto);
    }

    public PurchaseDetails getPurchaseDetails(String purchaseId) {
        return purchaseDetailsCRUD.getPurchaseDetails(purchaseId);
    }

    public List<PurchaseDetails> getListPurchaseDetailsQuery(JSONObject jsonDto) {
        return purchaseDetailsQuery.getListPurchaseDetailsQuery(jsonDto);
    }
}
