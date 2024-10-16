import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.dto.Drug;
import org.medicaldatasharing.form.AddDrugForm;
import org.medicaldatasharing.form.AddPurchaseForm;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddPurchaseTest {
    private static String INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN";
    private static String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuaGF0aHVvY2FAZ21haWwuY29tIiwiaWF0IjoxNzI5MDk3MTk0LCJleHAiOjE3ODk1NzcxOTR9.Lns_1LmizOSab3fEzvNR1NL_eLhNHhFkpPPQ5s_C8Bof2LOI2H1iU7YELpZYxlO5xi3-gEi3xAi2qy94UWDPlg";

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
}
