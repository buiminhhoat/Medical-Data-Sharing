import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.MedicalRecord;
import org.medicaldatasharing.dto.PrescriptionDetails;
import org.medicaldatasharing.dto.ViewRequest;
import org.medicaldatasharing.form.AddMedicalRecordForm;
import org.medicaldatasharing.form.AddPrescriptionForm;
import org.medicaldatasharing.form.SendViewRequestForm;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SendViewRequestTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ3V5ZW50aGFuaGhhaUBnbWFpbC5jb20iLCJpYXQiOjE3Mjg5MjMzMDAsImV4cCI6MTc4OTQwMzMwMH0.wyu1ftGWD0UHI8GFcYiU3rSeuVHPK_N4JTR0apOUvjiCE7d595K4Wa9M2Y1IqqM3VCVrgGqMRbN8ulYjDRbr9A";
    private static String ACCESS_TOKEN_NOT_SENDER_ID = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYW9xdWFuZ3ZpbmhAZ21haWwuY29tIiwiaWF0IjoxNzI4OTI0Njk3LCJleHAiOjE3ODk0MDQ2OTd9.YbqZMLF4C48EM4wdq_qbzCNTR2FBh6MXxaN49qIyJIzW0qrwsApRMFFE-UO1UkW2udvie7ZYfqIQHHY3WYLQHg";
    private static String RECIPIENT_ID = "Patient-305f5d47-b01b-4a3e-b2e5-ea36fa950ecf";
    private static String RECIPIENT_ID_NOT_PATIENT = "Doctor-305f5d47-b01b-4a3e-b2e5-ea36fa950ecf";
    private static String SENDER_ID = "Doctor-76a1c4e7-bc03-4d97-b183-750bf5603f77";
    private static String SENDER_ID_NOT_DOCTOR = "Scientist-ae411d24-df5c-4015-835a-d2a05dd141b9";
    private static String SENDER_ID_NOT_ACCESS_TOKEN = "Doctor-d63de258-8b68-482f-b8ef-9d68099d14d1";

    private static String API_URL = "http://localhost:8000/api/doctor/send-view-request";

    private ResponseEntity<?> sendViewRequest(String accessToken, SendViewRequestForm sendViewRequestForm) {
        RestTemplate restTemplate = new RestTemplate();

        JSONObject jsonObject = sendViewRequestForm.toJSONObject();
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
    public void testSendViewRequest_InvalidLogin() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        ResponseEntity<?> responseEntity = sendViewRequest(INVALID_ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra thông tin đăng nhập không phải là của người gửi yêu cầu (ID người gửi tồn tại nhưng không phải là access token)
    @Test
    @Order(2)
    public void testSendViewRequest_LoginNotMatchSenderId() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        sendViewRequestForm.setSenderId(SENDER_ID_NOT_ACCESS_TOKEN);
        sendViewRequestForm.setRecipientId(RECIPIENT_ID);
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID người gửi bị trống
    @Test
    @Order(3)
    public void testSendViewRequest_EmptySenderId() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        String senderID = "";
        sendViewRequestForm.setSenderId(senderID);
        sendViewRequestForm.setRecipientId(RECIPIENT_ID);
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID người gửi yêu cầu không phải là bác sĩ
    @Test
    @Order(4)
    public void testSendViewRequest_SenderNotDoctor() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        sendViewRequestForm.setSenderId(SENDER_ID_NOT_DOCTOR);
        sendViewRequestForm.setRecipientId(RECIPIENT_ID);
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID người nhận không phải là bệnh nhân
    @Test
    @Order(5)
    public void testSendViewRequest_ReceiverNotPatient() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        sendViewRequestForm.setSenderId(SENDER_ID);
        sendViewRequestForm.setRecipientId(RECIPIENT_ID_NOT_PATIENT);
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra ID người nhận bị trống
    @Test
    @Order(6)
    public void testSendViewRequest_EmptyRecipientId() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        sendViewRequestForm.setSenderId(SENDER_ID);
        sendViewRequestForm.setRecipientId("");
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // Kiểm tra gửi yêu cầu xem hồ sơ y tế thành công
    @Test
    @Order(7)
    public void testSendViewRequest_Success() {
        SendViewRequestForm sendViewRequestForm = new SendViewRequestForm();
        sendViewRequestForm.setSenderId(SENDER_ID);
        sendViewRequestForm.setRecipientId(RECIPIENT_ID);
        ResponseEntity<?> responseEntity = sendViewRequest(ACCESS_TOKEN, sendViewRequestForm);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String viewRequestStr = responseEntity.getBody().toString();

        System.out.println("viewRequest: " + viewRequestStr);
        ViewRequest viewRequest = new Genson().deserialize(viewRequestStr, ViewRequest.class);
        assertNotEquals("", viewRequest.getRequestId());
        assertEquals(SENDER_ID, viewRequest.getSenderId());
        assertEquals(RECIPIENT_ID, viewRequest.getRecipientId());
        assertEquals(StringUtil.parseDate(new Date()), viewRequest.getDateCreated());
        assertEquals(StringUtil.parseDate(new Date()), viewRequest.getDateModified());
        assertEquals("Xem hồ sơ y tế", viewRequest.getRequestType());
        assertEquals("Chờ xử lý", viewRequest.getRequestStatus());
    }
}
