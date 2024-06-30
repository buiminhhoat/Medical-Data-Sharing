package healthInformationSharing.dao;

import com.owlike.genson.Genson;
import healthInformationSharing.entity.Purchase;
import org.hyperledger.fabric.contract.Context;
import org.json.JSONObject;

import java.util.List;

public class PurchaseDAO {
    private PurchaseCRUD purchaseCRUD;
    private PurchaseQuery purchaseQuery;

    public PurchaseDAO(Context context) {
        this.purchaseCRUD = new PurchaseCRUD(context, Purchase.class.getSimpleName(), new Genson());
        this.purchaseQuery = new PurchaseQuery(context, Purchase.class.getSimpleName());
    }

    public PurchaseCRUD getPurchaseCRUD() {
        return purchaseCRUD;
    }

    public PurchaseDAO setPurchaseCRUD(PurchaseCRUD purchaseCRUD) {
        this.purchaseCRUD = purchaseCRUD;
        return this;
    }

    public PurchaseQuery getPurchaseQuery() {
        return purchaseQuery;
    }

    public PurchaseDAO setPurchaseQuery(PurchaseQuery purchaseQuery) {
        this.purchaseQuery = purchaseQuery;
        return this;
    }

    public Purchase addPurchase(JSONObject jsonDto) {
        return purchaseCRUD.addPurchase(jsonDto);
    }

    public Purchase getPurchase(String purchaseId) {
        return purchaseCRUD.getPurchase(purchaseId);
    }

    public List<Purchase> getListPurchaseByQuery(JSONObject jsonDto) {
        return purchaseQuery.getListPurchaseByQuery(jsonDto);
    }
}
