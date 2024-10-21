import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AddMedicalRecordTest {
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
        driver.findElement(By.id("basic_email")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("nguyenthanhhai@gmail.com");
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

        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();
        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
    }

    @Test
    public void checkAddMedicalRecordSuccess() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();

        sleep(SLEEP_TIME);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")));

        driver.findElement(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(2) > span:nth-child(1) > span:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".ant-btn-primary > span")).click();
        driver.findElement(By.cssSelector(".ant-table-cell:nth-child(7) .ant-dropdown-trigger")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1) > span:nth-child(1) > span:nth-child(2)")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector("div[class='ant-dropdown css-dev-only-do-not-override-uz5yzm ant-dropdown-placement-bottomRight'] button:nth-child(2) span:nth-child(1)")).click();
        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), is("Đặt lịch khám"));
        assertThat(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), is("CHỜ XỬ LÝ"));
        driver.findElement(By.cssSelector(".ant-table-row:nth-child(1) span:nth-child(2)")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-btn:nth-child(3) > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.id("basic_testName")).click();
        driver.findElement(By.id("basic_testName")).sendKeys("Xét nghiệm máu");
        driver.findElement(By.id("basic_details")).click();
        driver.findElement(By.id("basic_details")).sendKeys("Tất cả các chỉ số bình thường");
        driver.findElement(By.cssSelector(".ant-btn-dashed")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_medicationId")).click();
        driver.findElement(By.cssSelector(".ant-select-tree-treenode:nth-child(2) .ant-select-tree-title")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_quantity")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_quantity")).sendKeys("10");
        driver.findElement(By.id("basic_prescriptionDetailsList_0_details")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_details")).sendKeys("Uống 2 viên mỗi ngày");
        driver.findElement(By.cssSelector("#basic > div > .ant-btn > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-modal-footer > .ant-btn-primary > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-with-icon")).click();
        assertThat(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(), is("Thành công"));
    }

    @Test
    public void checkAfterAddMedicalRecordSuccess() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();

        sleep(SLEEP_TIME);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")));

        driver.findElement(By.cssSelector(".ant-table-cell:nth-child(6) .ant-dropdown-trigger")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(2) > span:nth-child(1) > span:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".ant-btn-primary > span")).click();
        driver.findElement(By.cssSelector(".ant-table-cell:nth-child(7) .ant-dropdown-trigger")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1) > span:nth-child(1) > span:nth-child(2)")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector("div[class='ant-dropdown css-dev-only-do-not-override-uz5yzm ant-dropdown-placement-bottomRight'] button:nth-child(2) span:nth-child(1)")).click();
        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), is("Đặt lịch khám"));
        assertThat(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(7)")).getText(), is("CHỜ XỬ LÝ"));
        driver.findElement(By.cssSelector(".ant-table-row:nth-child(1) span:nth-child(2)")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-btn:nth-child(3) > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.id("basic_testName")).click();
        driver.findElement(By.id("basic_testName")).sendKeys("Xét nghiệm máu");
        driver.findElement(By.id("basic_details")).click();
        driver.findElement(By.id("basic_details")).sendKeys("Tất cả các chỉ số bình thường");
        driver.findElement(By.cssSelector(".ant-btn-dashed")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_medicationId")).click();
        driver.findElement(By.cssSelector(".ant-select-tree-treenode:nth-child(2) .ant-select-tree-title")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_quantity")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_quantity")).sendKeys("10");
        driver.findElement(By.id("basic_prescriptionDetailsList_0_details")).click();
        driver.findElement(By.id("basic_prescriptionDetailsList_0_details")).sendKeys("Uống 2 viên mỗi ngày");
        driver.findElement(By.cssSelector("#basic > div > .ant-btn > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-modal-footer > .ant-btn-primary > span")).click();
        sleep(SLEEP_TIME);
        driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-with-icon")).click();
        assertThat(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(), is("Thành công"));

        driver.findElement(By.cssSelector("div[class='ant-notification-notice ant-notification-notice-success ant-notification-notice-closable'] a[aria-label='Close']")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID yêu cầu')]")).getText(), "ID yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người gửi')]")).getText(), "ID người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người gửi')]")).getText(), "Tên người gửi");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID người nhận')]")).getText(), "ID người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên người nhận')]")).getText(), "Tên người nhận");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'ID cơ sở y tế')]")).getText(), "ID cơ sở y tế");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Tên cơ sở y tế')]")).getText(), "Tên cơ sở y tế");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày tạo')]")).getText(), "Ngày tạo");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Ngày chỉnh sửa')]")).getText(), "Ngày chỉnh sửa");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Loại yêu cầu')]")).getText(), "Loại yêu cầu");
        assertEquals(driver.findElement(By.xpath("//div[contains(text(),'Trạng thái')]")).getText(), "Trạng thái");
        assertEquals(driver.findElement(By.cssSelector("div:nth-child(10) div:nth-child(2)")).getText(), "Đặt lịch khám");
        assertEquals(driver.findElement(By.cssSelector("div:nth-child(11) div:nth-child(2)")).getText(), "Đồng ý");
    }

    @Test
    public void checkSearchButton() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).getText(), is("Quản lý yêu cầu"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý yêu cầu')]")).click();
        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='Tên người gửi']")).sendKeys("Đào Quang Vinh");
        driver.findElement(By.cssSelector("input[placeholder='Tên người nhận']")).sendKeys("Nguyễn Thanh Hải");
        driver.findElement(By.cssSelector("input[placeholder='Loại yêu cầu']")).sendKeys("Đặt lịch khám");
        driver.findElement(By.cssSelector("body > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > button:nth-child(1)")).click();

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), "Đào Quang Vinh");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(3)")).getText(), "Nguyễn Thanh Hải");
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(6)")).getText(), "Đặt lịch khám");
    }

    @Test
    public void checkClearButton() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("nguyenthanhhai@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1)")).click();
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
