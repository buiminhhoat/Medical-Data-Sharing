import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.medicaldatasharing.form.SendViewRequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShareMedicalRecordTest {

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
