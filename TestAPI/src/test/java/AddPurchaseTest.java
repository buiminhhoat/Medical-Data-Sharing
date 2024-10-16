import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.Drug;
import org.medicaldatasharing.dto.MedicationPurchaseDto;
import org.medicaldatasharing.dto.Purchase;
import org.medicaldatasharing.form.AddDrugForm;
import org.medicaldatasharing.form.AddPurchaseForm;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddPurchaseTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaGF0aHVvY2FAZ21haWwuY29tIiwiaWF0IjoxNzI5MDk3MTk0LCJleHAiOjE3ODk1NzcxOTR9.Lns_1LmizOSab3fEzvNR1NL_eLhNHhFkpPPQ5s_C8Bof2LOI2H1iU7YELpZYxlO5xi3-gEi3xAi2qy94UWDPlg";

    private static final String PATIENT_ID = "Patient-2e910383-ac0e-47ab-bf93-fc45ade8381a";
    private static final String DRUGSTORE_ID = "DrugStore-e9fa961d-22ee-4259-a67a-d7de2eb2c40d";
    private static final String MEDICATION_ID = "fc2987a8acfc1a141acbea8b4c17bdfd607801887aa7c6f6105c2bf59b311544";
    private static final String PRESCRIPTION_ID = "29a0a1de3175d909de6f0173fe9ddddfed350855dc8ecf9c66935a63b8967940";
    private static final String PRESCRIPTION_DETAIL_ID = "29a0a1de3175d909de6f0173fe9ddddfed350855dc8ecf9c66935a63b8967940-0";
    private static final String DRUG_ID_1 = "9a3d6911a56dae6c622ea45fabe7938f1e71bc075fcf6fdb987aced1f2040ba54";
    private static final String DRUG_ID_2 = "9a3d6911a56dae6c622ea45fabe7938f1e71bc075fcf6fdb987aced1f2040ba55";

    private static String API_URL = "http://localhost:8000/api/drugstore/add-purchase";

    private ResponseEntity<?> addPurchase(String accessToken, AddPurchaseForm addPurchaseForm) {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = addPurchaseForm.toJSONObject();
        Iterator<String> keys = jsonObject.keys();

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = jsonObject.get(key).toString();
            builder.queryParam(key, value);
        }

        // Tạo headers cho yêu cầu POST
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        String uriString = builder.build().encode().toUriString();
        String formUrlEncodedData = !uriString.isEmpty() ? uriString.substring(1) : ""; // Sửa tại đây
        HttpEntity<String> request = new HttpEntity<>(formUrlEncodedData, headers);

        try {
            // Gửi yêu cầu POST và nhận phản hồi
            ResponseEntity<?> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);
            return responseEntity;
        }
        catch (HttpClientErrorException exception) {
            System.out.println(exception);
            return ResponseEntity
                    .status(exception.getStatusCode())
                    .body(exception.getMessage());
        }
    }

    // Kiểm tra sử dụng thông tin đăng nhập không hợp lệ
    @Test
    @Order(1)
    public void testAddPurchase_InvalidLogin() {
    }

    // Kiểm tra người đang thực hiện bán thuốc không phải là nhà thuốc
    @Test
    @Order(2)
    public void testAddPurchase_SellerNotDrugStore() {
    }

    // Kiểm tra ID loại thuốc không hợp lệ
    @Test
    @Order(3)
    public void testAddPurchase_InvalidMedicationId() {
    }

    // Kiểm tra ID chi tiết đơn thuốc không hợp lệ
    @Test
    @Order(4)
    public void testAddPurchase_InvalidPrescriptionDetailId() {
    }

    // Kiểm tra ID thuốc được bán không hợp lệ
    @Test
    @Order(5)
    public void testAddPurchase_InvalidSoldDrugId() {
    }

    // Kiểm tra ID thuốc được bán không thuộc quyền sở hữu của nhà thuốc
    @Test
    @Order(6)
    public void testAddPurchase_SoldMedicineNotOwnedByDrugStore() {
    }

    // Kiểm tra bán thuốc thành công
    @Test
    @Order(7)
    public void testAddPurchase_Success() {
        MedicationPurchaseDto medicationPurchaseDto = new MedicationPurchaseDto();
        medicationPurchaseDto.setMedicationId(MEDICATION_ID);
        medicationPurchaseDto.setPrescriptionDetailId(PRESCRIPTION_DETAIL_ID);
        medicationPurchaseDto.setDrugIdList(List.of(DRUG_ID_1, DRUG_ID_2));

        List<MedicationPurchaseDto> medicationPurchaseDtoList = new ArrayList<>();
        medicationPurchaseDtoList.add(medicationPurchaseDto);

        AddPurchaseForm addPurchaseForm = new AddPurchaseForm();
        addPurchaseForm.setPatientId(PATIENT_ID);
        addPurchaseForm.setPrescriptionId(PRESCRIPTION_ID);
        addPurchaseForm.setSellingPrescriptionDrug(new Genson().serialize(medicationPurchaseDtoList));

        ResponseEntity<?> responseEntity = addPurchase(ACCESS_TOKEN, addPurchaseForm);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String purchaseStr = responseEntity.getBody().toString();

        System.out.println("purchase: " + purchaseStr);

        Purchase purchase = new Genson().deserialize(purchaseStr, Purchase.class);

        assertNotEquals("", purchase.getPurchaseId());
        assertEquals(PRESCRIPTION_ID, purchase.getPrescriptionId());
        assertEquals(PATIENT_ID, purchase.getPatientId());
        assertEquals(DRUGSTORE_ID, purchase.getDrugStoreId());
    }
}
