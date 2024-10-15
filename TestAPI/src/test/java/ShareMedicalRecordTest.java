import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.RequestResponse;
import org.medicaldatasharing.dto.ViewRequest;
import org.medicaldatasharing.form.DefineRequestForm;
import org.medicaldatasharing.form.SendViewRequestForm;
import org.medicaldatasharing.util.StringUtil;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShareMedicalRecordTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String UNAUTHORIZED_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWh1eTVjMjAwM0BnbWFpbC5jb20iLCJpYXQiOjE3MjkwMDgzNzgsImV4cCI6MTc4OTQ4ODM3OH0.FPFtyLBIj_sHnX_nlkGFqvUSqDZaaDp_zmsnGGv-inNGFW4H8oSCkFD3AK6rR45_C14MPQ58r11VO_pk1W-biA";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW9xdWFuZ3ZpbmhAZ21haWwuY29tIiwiaWF0IjoxNzI5MDA4Nzk0LCJleHAiOjE3ODk0ODg3OTR9.x7wFF87wHY-dW6Sdw_ZrBbEVlmuyMgXdIdGfB6R1nsRML8Qo_M-Dev4zS_ruCJ9sVxup1COv1yPJ1bmEyO2qCQ";

    private static String REQUEST_ID = "8e16c2d5445dd1e291f24de4978eada44e7a0ff1fdc0d912be5205ee41aa0544";
    private static String INVALID_REQUEST_ID = "44f7a95c4201ac4a8de3bc8822af431877aa3ae0556d1649d4c0dea7aedf373c";

    private static String REQUEST_STATUS_ACCEPTED = "Đồng ý";
    private static String REQUEST_STATUS_DECLINED = "Từ chối";
    private static String INVALID_REQUEST_STATUS = "Invalid";

    private static String REQUEST_TYPE = "Xem hồ sơ y tế";
    private static String INVALID_REQUEST_TYPE = "Đặt lịch khám";
    private static String REQUEST_TYPE_NOT_EXIST = "Xem hồ sơ y tế :)";


    private static String API_URL = "http://localhost:8000/api/patient/define-request";
    private ResponseEntity<?> shareMedicalRecord(String accessToken, DefineRequestForm defineRequestForm) {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = defineRequestForm.toJSONObject();
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
        String formUrlEncodedData = !uriString.isEmpty() ? uriString.substring(1) : "";
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
    public void testShareMedicalRecord_InvalidLogin() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(INVALID_ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra người đồng ý chia sẻ hồ sơ y tế không có thẩm quyền
    @Test
    @Order(2)
    public void testShareMedicalRecord_UnauthorizedUser() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(UNAUTHORIZED_ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID yêu cầu không hợp lệ
    @Test
    @Order(3)
    public void testShareMedicalRecord_InvalidRequestId() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(INVALID_REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID yêu cầu bị trống
    @Test
    @Order(3)
    public void testShareMedicalRecord_EmptyRequestId() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId("");
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra trạng thái cập nhật không hợp lệ
    @Test
    @Order(4)
    public void testShareMedicalRecord_InvalidRequestStatus() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(INVALID_REQUEST_STATUS);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra trạng thái cập nhật bị trống
    @Test
    @Order(5)
    public void testShareMedicalRecord_EmptyRequestStatus() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus("");
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra loại yêu cầu không hợp lệ (sai loại yêu cầu nhưng loại yêu cầu đó vẫn tồn tại)
    @Test
    @Order(6)
    public void testShareMedicalRecord_InvalidRequestType() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(INVALID_REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra loại yêu cầu không tồn tại
    @Test
    @Order(7)
    public void testShareMedicalRecord_RequestTypeNotExist() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE_NOT_EXIST);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra loại yêu cầu bị trống
    @Test
    @Order(8)
    public void testShareMedicalRecord_EmptyRequestType() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType("");
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra trường hợp chia sẻ hồ sơ y tế thành công - Đồng ý chia sẻ
    @Test
    @Order(9)
    public void testShareMedicalRecord_Success_ACCEPTED() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_ACCEPTED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String defineRequestStr = responseEntity.getBody().toString();

        System.out.println("defineRequestStr: " + defineRequestStr);
        RequestResponse requestResponse = new Genson().deserialize(defineRequestStr, RequestResponse.class);
        assertEquals(REQUEST_ID, requestResponse.getRequestId());
        assertEquals(REQUEST_TYPE, requestResponse.getRequestType());
        assertEquals(REQUEST_STATUS_ACCEPTED, requestResponse.getRequestStatus());
    }

    // Kiểm tra trường hợp chia sẻ hồ sơ y tế thành công - Từ chối chia sẻ
    @Test
    @Order(10)
    public void testShareMedicalRecord_Success_DECLINED() {
        DefineRequestForm defineRequestForm = new DefineRequestForm();
        defineRequestForm.setRequestId(REQUEST_ID);
        defineRequestForm.setRequestType(REQUEST_TYPE);
        defineRequestForm.setRequestStatus(REQUEST_STATUS_DECLINED);
        ResponseEntity<?> responseEntity = shareMedicalRecord(ACCESS_TOKEN, defineRequestForm);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String defineRequestStr = responseEntity.getBody().toString();

        System.out.println("defineRequestStr: " + defineRequestStr);
        RequestResponse requestResponse = new Genson().deserialize(defineRequestStr, RequestResponse.class);
        assertEquals(REQUEST_ID, requestResponse.getRequestId());
        assertEquals(REQUEST_TYPE, requestResponse.getRequestType());
        assertEquals(REQUEST_STATUS_DECLINED, requestResponse.getRequestStatus());
    }
}
