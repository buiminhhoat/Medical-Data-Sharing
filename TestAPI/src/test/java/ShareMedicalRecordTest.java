import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.form.DefineRequestForm;
import org.medicaldatasharing.form.SendViewRequestForm;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShareMedicalRecordTest {

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

    }

    // Kiểm tra người đồng ý chia sẻ hồ sơ y tế không có thẩm quyền
    @Test
    @Order(2)
    public void testShareMedicalRecord_UnauthorizedUser() {

    }

    // Kiểm tra ID yêu cầu không hợp lệ
    @Test
    @Order(3)
    public void testShareMedicalRecord_InvalidRequestId() {

    }

    // Kiểm tra trạng thái cập nhật không hợp lệ
    @Test
    @Order(4)
    public void testShareMedicalRecord_InvalidRequestStatus() {

    }

    // Kiểm tra trạng thái cập nhật bị trống
    @Test
    @Order(5)
    public void testShareMedicalRecord_EmptyRequestId() {

    }

    // Kiểm tra loại yêu cầu không hợp lệ
    @Test
    @Order(6)
    public void testShareMedicalRecord_InvalidRequestType() {
    }

    // Kiểm tra loại yêu cầu bị trống
    @Test
    @Order(7)
    public void testShareMedicalRecord_EmptyRequestType() {

    }

    // Kiểm tra trường hợp chia sẻ hồ sơ y tế thành công - Đồng ý chia sẻ
    @Test
    @Order(8)
    public void testShareMedicalRecord_Success_ACCEPTED() {

    }

    // Kiểm tra trường hợp chia sẻ hồ sơ y tế thành công - Từ chối chia sẻ
    @Test
    @Order(9)
    public void testShareMedicalRecord_Success_DECLINED() {

    }
}
