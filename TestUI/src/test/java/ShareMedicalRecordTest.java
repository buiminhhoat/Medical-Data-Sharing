import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ShareMedicalRecordTest {

    private static final long SLEEP_TIME = 5000;
    private WebDriver driver;

    private WebDriverWait wait;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void checkBasicComponents() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();

        assertEquals(driver.findElement(By.cssSelector("div[id=':r2:']")).getText(), "Đăng nhập");
        assertEquals(driver.findElement(By.cssSelector("label[title='Email']")).getText(), "Email");
        assertEquals(driver.findElement(By.cssSelector("label[title='Mật khẩu']")).getText(), "Mật khẩu");
        assertEquals(driver.findElement(By.cssSelector("label[title='Tổ chức']")).getText(), "Tổ chức");
        assertEquals(driver.findElement(By.cssSelector("#basic_remember")).isEnabled(), true);
        assertEquals(driver.findElement(By.cssSelector("body > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1) > form:nth-child(1) > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > label:nth-child(1) > span:nth-child(2)")).getText(), "Ghi nhớ đăng nhập");


        assertEquals(driver.findElement(By.xpath("//div[@title='Bệnh nhân']//div[1]")).getText(), "Bệnh nhân");
        assertEquals(driver.findElement(By.xpath("//div[@title='Bác sĩ']//div[1]")).getText(), "Bác sĩ");
        assertEquals(driver.findElement(By.xpath("//div[@title='Cơ sở y tế']//div[1]")).getText(), "Cơ sở y tế");
        assertEquals(driver.findElement(By.xpath("//div[@title='Trung tâm nghiên cứu']//div[1]")).getText(), "Trung tâm nghiên cứu");
        assertEquals(driver.findElement(By.xpath("//div[@title='Nhà khoa học']//div[1]")).getText(), "Nhà khoa học");
        assertEquals(driver.findElement(By.xpath("//div[@title='Công ty sản xuất thuốc']//div[1]")).getText(), "Công ty sản xuất thuốc");
        assertEquals(driver.findElement(By.xpath("//div[@title='Nhà thuốc']//div[1]")).getText(), "Nhà thuốc");
        assertEquals(driver.findElement(By.xpath("//div[@title='Quản trị viên']//div[1]")).getText(), "Quản trị viên");

        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit'] span"));
        assertEquals(loginButton.isDisplayed() && loginButton.isEnabled(), true);
        assertEquals(loginButton.getText(), "Đăng nhập");

        WebElement registerButton = driver.findElement(By.cssSelector("button[class='ant-btn css-dev-only-do-not-override-uz5yzm ant-btn-default'] span"));
        assertEquals(registerButton.isDisplayed() && registerButton.isEnabled(), true);
        assertEquals(registerButton.getText(), "Đăng ký");

        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();
        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
    }

    @Test
    public void checkShareMedicalRecord_ACCEPTED_Success() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();

        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Xem hồ sơ y tế");
        driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).sendKeys("Chờ xử lý");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), "Nguyễn Thanh Hải");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(3)")).getText(), "Đào Quang Vinh");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), "CHỜ XỬ LÝ");

        driver.findElement(By.cssSelector(".ant-table-row:nth-child(1) span:nth-child(2)")).click();
        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID yêu cầu')]")).getText(), "ID yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người gửi')]")).getText(), "ID người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người gửi')]")).getText(), "Tên người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người nhận')]")).getText(), "ID người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người nhận')]")).getText(), "Tên người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày tạo')]")).getText(), "Ngày tạo");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày chỉnh sửa')]")).getText(), "Ngày chỉnh sửa");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Loại yêu cầu')]")).getText(), "Loại yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Trạng thái')]")).getText(), "Trạng thái");

        assertNotEquals(driver.findElement(By.cssSelector("body div div[class='sc-gEvDqW lbcRDp'] div div:nth-child(1) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(2) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(3) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(4) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(5) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(6) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(7) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "");

        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "Chờ xử lý");

        assertEquals(driver.findElement(By.cssSelector("body div button:nth-child(2)")).isEnabled(), true);
        driver.findElement(By.cssSelector("body div button:nth-child(2)")).click();

        driver.findElement(By.cssSelector("button[class='ant-btn css-dev-only-do-not-override-uz5yzm ant-btn-primary']")).click();

        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(),"Thành công");
    }

    @Test
    public void checkShareMedicalRecord_DECLINED_Success() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();

        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Xem hồ sơ y tế");
        driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).sendKeys("Chờ xử lý");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), "Nguyễn Thanh Hải");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(3)")).getText(), "Đào Quang Vinh");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), "CHỜ XỬ LÝ");

        driver.findElement(By.cssSelector(".ant-table-row:nth-child(1) span:nth-child(2)")).click();
        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID yêu cầu')]")).getText(), "ID yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người gửi')]")).getText(), "ID người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người gửi')]")).getText(), "Tên người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người nhận')]")).getText(), "ID người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người nhận')]")).getText(), "Tên người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày tạo')]")).getText(), "Ngày tạo");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày chỉnh sửa')]")).getText(), "Ngày chỉnh sửa");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Loại yêu cầu')]")).getText(), "Loại yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Trạng thái')]")).getText(), "Trạng thái");

        assertNotEquals(driver.findElement(By.cssSelector("body div div[class='sc-gEvDqW lbcRDp'] div div:nth-child(1) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(2) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(3) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(4) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(5) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(6) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(7) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "");

        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "Chờ xử lý");

        assertEquals(driver.findElement(By.cssSelector("body div button:nth-child(3)")).isEnabled(), true);
        driver.findElement(By.cssSelector("body div button:nth-child(3)")).click();

        driver.findElement(By.cssSelector("button[class='ant-btn css-dev-only-do-not-override-uz5yzm ant-btn-primary']")).click();

        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(),"Thành công");
    }

    @Test
    public void checkShareMedicalRecord_INVOKED_Success() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();

        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Xem hồ sơ y tế");
        driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).sendKeys("Đồng ý");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), "Nguyễn Thanh Hải");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(3)")).getText(), "Đào Quang Vinh");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), "ĐỒNG Ý");

        driver.findElement(By.cssSelector(".ant-table-row:nth-child(1) span:nth-child(2)")).click();
        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID yêu cầu')]")).getText(), "ID yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người gửi')]")).getText(), "ID người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người gửi')]")).getText(), "Tên người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người nhận')]")).getText(), "ID người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người nhận')]")).getText(), "Tên người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày tạo')]")).getText(), "Ngày tạo");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày chỉnh sửa')]")).getText(), "Ngày chỉnh sửa");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Loại yêu cầu')]")).getText(), "Loại yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Trạng thái')]")).getText(), "Trạng thái");

        assertNotEquals(driver.findElement(By.cssSelector("body div div[class='sc-gEvDqW lbcRDp'] div div:nth-child(1) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(2) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(3) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(4) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(5) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(6) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(7) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "");
        assertNotEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "");

        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(8) div:nth-child(2)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("div[class='ant-modal-body'] div:nth-child(9) div:nth-child(2)")).getText(), "Đồng ý");

        assertEquals(driver.findElement(By.cssSelector("body div button:nth-child(2)")).isEnabled(), true);
        driver.findElement(By.cssSelector("body div button:nth-child(2)")).click();

        driver.findElement(By.cssSelector("button[class='ant-btn css-dev-only-do-not-override-uz5yzm ant-btn-primary']")).click();

        sleep(SLEEP_TIME);

        assertEquals(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(),"Thành công");
    }

    @Test
    public void checkSearchButton() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();
        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Xem hồ sơ y tế");
        driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).sendKeys("Đồng ý");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), "Nguyễn Thanh Hải");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(3)")).getText(), "Đào Quang Vinh");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), "Xem hồ sơ y tế");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), "ĐỒNG Ý");
    }

    @Test
    public void checkClearButton() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("daoquangvinh@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();
        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Mã yêu cầu']")).sendKeys("0b19cafdd264fbb958dc04b884b97694efedbc90c62a22497bb54c9b2c3ca278");
        driver.findElement(By.cssSelector("input[placeholder='Mã người gửi']")).sendKeys("Doctor-ca20c5ef-56dc-4d48-8872-76429612b3a5");
        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Mã người nhận']")).sendKeys("Patient-097bc462-b4dd-42f7-a8a2-1256477a5963");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Xem hồ sơ y tế");
        driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).sendKeys("Đồng ý");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Mã yêu cầu']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Mã người gửi']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Mã người nhận']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).getText(), "");
        assertEquals(driver.findElement(By.cssSelector("input[placeholder='Trạng thái']")).getText(), "");
    }
}
