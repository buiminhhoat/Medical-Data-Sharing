import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
}
