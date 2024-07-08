package healthInformationSharing.entity;

import com.owlike.genson.Genson;
import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType()
public class InsuranceProduct {
    @Property
    @JsonProperty("insuranceProductId")
    private String insuranceProductId;

    @Property
    @JsonProperty("insuranceProductName")
    private String insuranceProductName;

    @Property
    @JsonProperty("insuranceCompanyId")
    private String insuranceCompanyId;

    @Property
    @JsonProperty("dateCreated")
    private String dateCreated;

    @Property
    @JsonProperty("dateModified")
    private String dateModified;

    @Property
    @JsonProperty("description")
    private String description;

    @Property
    @JsonProperty("numberOfDaysInsured")
    private String numberOfDaysInsured;

    @Property
    @JsonProperty("price")
    private String price;

    @Property
    @JsonProperty("hashFile")
    private String hashFile;

    @Property()
    @JsonProperty("entityName")
    private String entityName;

    public InsuranceProduct() {
        this.entityName = InsuranceProduct.class.getSimpleName();
    }

    public InsuranceProduct(String insuranceProductId,
                            String insuranceProductName,
                            String insuranceCompanyId,
                            String dateCreated,
                            String dateModified,
                            String description,
                            String numberOfDaysInsured,
                            String price,
                            String hashFile) {
        super();
        this.insuranceProductId = insuranceProductId;
        this.insuranceProductName = insuranceProductName;
        this.insuranceCompanyId = insuranceCompanyId;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.description = description;
        this.numberOfDaysInsured = numberOfDaysInsured;
        this.price = price;
        this.hashFile = hashFile;
    }

    public static InsuranceProduct createInstance(String insuranceProductId,
                                                  String insuranceProductName,
                                                  String insuranceCompanyId,
                                                  String dateCreated,
                                                  String dateModified,
                                                  String description,
                                                  String numberOfDaysInsured,
                                                  String price,
                                                  String hashFile) {
        InsuranceProduct insuranceProduct = new InsuranceProduct();
        insuranceProduct.setInsuranceProductId(insuranceProductId);
        insuranceProduct.setInsuranceProductName(insuranceProductName);
        insuranceProduct.setInsuranceCompanyId(insuranceCompanyId);
        insuranceProduct.setDateCreated(dateCreated);
        insuranceProduct.setDateModified(dateModified);
        insuranceProduct.setDescription(description);
        insuranceProduct.setNumberOfDaysInsured(numberOfDaysInsured);
        insuranceProduct.setPrice(price);
        insuranceProduct.setHashFile(hashFile);
        return insuranceProduct;
    }

    public String getInsuranceProductId() {
        return insuranceProductId;
    }

    public InsuranceProduct setInsuranceProductId(String insuranceProductId) {
        this.insuranceProductId = insuranceProductId;
        return this;
    }

    public String getInsuranceProductName() {
        return insuranceProductName;
    }

    public InsuranceProduct setInsuranceProductName(String insuranceProductName) {
        this.insuranceProductName = insuranceProductName;
        return this;
    }

    public String getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public InsuranceProduct setInsuranceCompanyId(String insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InsuranceProduct setDescription(String description) {
        this.description = description;
        return this;
    }

    public static byte[] serialize(Object object) {
        Genson genson = new Genson();
        return genson.serializeBytes(object);
    }

    public static InsuranceProduct deserialize(byte[] data) {
        Genson genson = new Genson();
        return genson.deserialize(data, InsuranceProduct.class);
    }

    public String getEntityName() {
        return entityName;
    }

    public InsuranceProduct setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public InsuranceProduct setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public InsuranceProduct setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getHashFile() {
        return hashFile;
    }

    public InsuranceProduct setHashFile(String hashFile) {
        this.hashFile = hashFile;
        return this;
    }

    public String getNumberOfDaysInsured() {
        return numberOfDaysInsured;
    }

    public InsuranceProduct setNumberOfDaysInsured(String numberOfDaysInsured) {
        this.numberOfDaysInsured = numberOfDaysInsured;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public InsuranceProduct setPrice(String price) {
        this.price = price;
        return this;
    }
}
