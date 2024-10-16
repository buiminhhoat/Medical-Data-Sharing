import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.Drug;
import org.medicaldatasharing.dto.MedicalRecord;
import org.medicaldatasharing.dto.PrescriptionDetails;
import org.medicaldatasharing.form.AddDrugForm;
import org.medicaldatasharing.form.AddMedicalRecordForm;
import org.medicaldatasharing.form.AddPrescriptionForm;
import org.medicaldatasharing.util.StringUtil;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddDrugTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb25ndHlkdW9jcGhhbWFAZ21haWwuY29tIiwiaWF0IjoxNzI5MDg5NDc1LCJleHAiOjE3ODk1Njk0NzV9.9wuHJjBaEemSJ2V-0ZiiL2L34yex92VhSCg9ce3haOFXeYeHnPv4VWbT2GCm2eKqD5ueyN0UmDmUB_3aklEGyQ";

    private static final String MANUFACTURER_ID = "Manufacturer-3fe465a1-2d7b-4360-8063-a752ab0d2330";
    private static final String MEDICATION_ID = "fc2987a8acfc1a141acbea8b4c17bdfd607801887aa7c6f6105c2bf59b311544";
    private static final String MANUFACTURE_DATE = "2024-10-16";
    private static final String EXPIRATION_DATE = "2028-10-16";
    private static final String QUANTITY = "10";
    private static final String UNIT = "Viên";

    private static final String ACCESS_TOKEN_NOT_MANUFACTURER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW9xdWFuZ3ZpbmhAZ21haWwuY29tIiwiaWF0IjoxNzI5MDA4Nzk0LCJleHAiOjE3ODk0ODg3OTR9.x7wFF87wHY-dW6Sdw_ZrBbEVlmuyMgXdIdGfB6R1nsRML8Qo_M-Dev4zS_ruCJ9sVxup1COv1yPJ1bmEyO2qCQ";
    private static final String INVALID_MEDICATION_ID = "7e0cbe42a223a48c29cebd974b9d29766436747df650c3ad476bd56f4b58fgk3";
    private static final String INVALID_EXPIRATION_DATE = "2020-10-16";
    private static final String QUANTITY_EQUAL_ZERO = "0";
    private static final String QUANTITY_LESS_THAN_ZERO = "-5";
    private static final String QUANTITY_NOT_INTERGER = "3.5";
    private static final String UNIT_TOO_LONG = "Đơn vị: Viên thuốc ()";

    private static String API_URL = "http://localhost:8000/api/manufacturer/add-drug";

    private ResponseEntity<?> addDrug(String accessToken, AddDrugForm addDrugForm) {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = addDrugForm.toJSONObject();
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
    public void testAddDrug_InvalidLogin() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MANUFACTURER_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(INVALID_ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra người tạo thuốc không phải là công ty sản xuất thuốc
    @Test
    @Order(2)
    public void testAddDrug_CreatorNotManufacturer() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MANUFACTURER_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN_NOT_MANUFACTURER, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID loại thuốc không hợp lệ
    @Test
    @Order(3)
    public void testAddDrug_InvalidMedicationId() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(INVALID_MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ngày sản xuất bị trống
    @Test
    @Order(4)
    public void testAddDrug_ManufactureDateEmpty() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate("");
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ngày hết hạn bị trống
    @Test
    @Order(5)
    public void testAddDrug_ExpirationDateEmpty() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate("");
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ngày hết hạn nhỏ hơn ngày sản xuất
    @Test
    @Order(6)
    public void testAddDrug_ExpirationDateBeforeManufactureDate() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(INVALID_EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng bị trống
    @Test
    @Order(7)
    public void testAddDrug_QuantityEmpty() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity("");
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng = 0
    @Test
    @Order(8)
    public void testAddDrug_QuantityEqualZero() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY_EQUAL_ZERO);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng < 0
    @Test
    @Order(8)
    public void testAddDrug_QuantityLessThanZero() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY_LESS_THAN_ZERO);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra số lượng không phải là số nguyên
    @Test
    @Order(9)
    public void testAddDrug_QuantityNotInteger() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY_NOT_INTERGER);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra đơn vị bị trống
    @Test
    @Order(10)
    public void testAddDrug_UnitEmpty() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit("");

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra đơn vị vượt quá 20 kí tự
    @Test
    @Order(11)
    public void testAddDrug_UnitTooLong() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT_TOO_LONG);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra tạo thuốc thành công
    @Test
    @Order(12)
    public void testAddDrug_Success() {
        AddDrugForm addDrugForm = new AddDrugForm();
        addDrugForm.setMedicationId(MEDICATION_ID);
        addDrugForm.setManufactureDate(MANUFACTURE_DATE);
        addDrugForm.setExpirationDate(EXPIRATION_DATE);
        addDrugForm.setQuantity(QUANTITY);
        addDrugForm.setUnit(UNIT);

        ResponseEntity<?> responseEntity = addDrug(ACCESS_TOKEN, addDrugForm);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String drugListStr = responseEntity.getBody().toString();

        List<Drug> drugList = new Genson().deserialize(drugListStr,
                new GenericType<List<Drug>>() {
        });

        assertEquals(drugList.size(), Long.parseLong(QUANTITY));

        for (Drug drug: drugList) {
            assertEquals(MEDICATION_ID, drug.getMedicationId());
            assertEquals(MANUFACTURE_DATE, drug.getManufactureDate());
            assertEquals(EXPIRATION_DATE, drug.getExpirationDate());
            assertEquals(UNIT, drug.getUnit());
            assertEquals(MANUFACTURER_ID, drug.getOwnerId());
        }
    }
}
