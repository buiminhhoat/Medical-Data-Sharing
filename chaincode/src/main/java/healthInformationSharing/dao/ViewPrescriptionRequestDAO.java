package healthInformationSharing.dao;

public class ViewPrescriptionRequestDAO {
    private ViewPrescriptionRequestCRUD viewPrescriptionRequestCRUD;
    private ViewPrescriptionRequestQuery viewPrescriptionRequestQuery;

    public ViewPrescriptionRequestDAO(ViewPrescriptionRequestCRUD viewPrescriptionRequestCRUD, ViewPrescriptionRequestQuery viewPrescriptionRequestQuery) {
        this.viewPrescriptionRequestCRUD = viewPrescriptionRequestCRUD;
        this.viewPrescriptionRequestQuery = viewPrescriptionRequestQuery;
    }

    public ViewPrescriptionRequestCRUD getViewPrescriptionRequestCRUD() {
        return viewPrescriptionRequestCRUD;
    }

    public ViewPrescriptionRequestDAO setViewPrescriptionRequestCRUD(ViewPrescriptionRequestCRUD viewPrescriptionRequestCRUD) {
        this.viewPrescriptionRequestCRUD = viewPrescriptionRequestCRUD;
        return this;
    }

    public ViewPrescriptionRequestQuery getViewPrescriptionRequestQuery() {
        return viewPrescriptionRequestQuery;
    }

    public ViewPrescriptionRequestDAO setViewPrescriptionRequestQuery(ViewPrescriptionRequestQuery viewPrescriptionRequestQuery) {
        this.viewPrescriptionRequestQuery = viewPrescriptionRequestQuery;
        return this;
    }
}
