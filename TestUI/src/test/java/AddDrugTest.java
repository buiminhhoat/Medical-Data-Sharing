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

public class AddDrugTest {
    private static final long SLEEP_TIME = 5000;

    private static final String MEDICATION_ID = "f40fa7b3df1703448a87c44012c0157c7aee7792d4628293a2dc94d19317e4cb";
    private static final String QUANTITY = "5";
    private static final String MANUFACTURE_DATE = "2024-10-23";
    private static final String EXPIRATION_DATE = "2025-10-23";

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
        driver.manage().window().maximize();
//        driver.manage().window().setSize(new Dimension(1552, 832));
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("congtyduocphama@gmail.com");
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

        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();
        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).getText(), is("Quản lý thuốc"));
    }

    @Test
    public void checkAddDrugSuccess() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);

        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).getText(), is("Quản lý thuốc"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).click();

        driver.findElement(By.cssSelector("div:nth-child(2) > div > div > .ant-btn > span")).click();

        assertEquals(driver.findElement(By.cssSelector("label[title='ID loại thuốc']")).getText(), "ID loại thuốc");
        assertEquals(driver.findElement(By.cssSelector("label[title='Đơn vị']")).getText(), "Đơn vị");
        assertEquals(driver.findElement(By.cssSelector("label[title='Số lượng']")).getText(), "Số lượng");
        assertEquals(driver.findElement(By.cssSelector("label[title='Ngày sản xuất']")).getText(), "Ngày sản xuất");
        assertEquals(driver.findElement(By.cssSelector("label[title='Ngày hết hạn']")).getText(), "Ngày hết hạn");

        driver.findElement(By.cssSelector("#addDrugForm_medicationId")).sendKeys(MEDICATION_ID);
        driver.findElement(By.id("addDrugForm_unit")).click();
        driver.findElement(By.cssSelector(".ant-select-item-option-active > .ant-select-item-option-content")).click();
        driver.findElement(By.id("addDrugForm_quantity")).click();
        driver.findElement(By.id("addDrugForm_quantity")).sendKeys(QUANTITY);
        driver.findElement(By.id("addDrugForm_manufactureDate")).click();
        driver.findElement(By.id("addDrugForm_manufactureDate")).sendKeys(MANUFACTURE_DATE);
        driver.findElement(By.id("addDrugForm_expirationDate")).click();
        driver.findElement(By.id("addDrugForm_expirationDate")).sendKeys(EXPIRATION_DATE);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-with-icon")).click();
        assertThat(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(), is("Thành công"));
    }

    @Test
    public void checkAddDrugAfterSuccess() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);

        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).getText(), is("Quản lý thuốc"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).click();

        driver.findElement(By.cssSelector("div:nth-child(2) > div > div > .ant-btn > span")).click();

        assertEquals(driver.findElement(By.cssSelector("label[title='ID loại thuốc']")).getText(), "ID loại thuốc");
        assertEquals(driver.findElement(By.cssSelector("label[title='Đơn vị']")).getText(), "Đơn vị");
        assertEquals(driver.findElement(By.cssSelector("label[title='Số lượng']")).getText(), "Số lượng");
        assertEquals(driver.findElement(By.cssSelector("label[title='Ngày sản xuất']")).getText(), "Ngày sản xuất");
        assertEquals(driver.findElement(By.cssSelector("label[title='Ngày hết hạn']")).getText(), "Ngày hết hạn");

        driver.findElement(By.cssSelector("#addDrugForm_medicationId")).sendKeys(MEDICATION_ID);
        driver.findElement(By.id("addDrugForm_unit")).click();
        driver.findElement(By.cssSelector(".ant-select-item-option-active > .ant-select-item-option-content")).click();
        driver.findElement(By.id("addDrugForm_quantity")).click();
        driver.findElement(By.id("addDrugForm_quantity")).sendKeys(QUANTITY);
        driver.findElement(By.id("addDrugForm_manufactureDate")).click();
        driver.findElement(By.id("addDrugForm_manufactureDate")).sendKeys(MANUFACTURE_DATE);
        driver.findElement(By.id("addDrugForm_expirationDate")).click();
        driver.findElement(By.id("addDrugForm_expirationDate")).sendKeys(EXPIRATION_DATE);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-with-icon")).click();
        assertThat(driver.findElement(By.cssSelector(".ant-notification-notice-success .ant-notification-notice-message")).getText(), is("Thành công"));


        assertEquals(driver.findElements(By.cssSelector("li.ant-list-item")).size(), Integer.parseInt(QUANTITY));

        for (int i = 1; i <= Integer.parseInt(QUANTITY); ++i) {
            assertNotEquals(driver.findElement(By.cssSelector("li:nth-child(" + i + ") div:nth-child(1) div:nth-child(1) div:nth-child(2)")).getText(), "");
            assertEquals(driver.findElement(By.cssSelector("li:nth-child(" + i + ") div:nth-child(1) div:nth-child(2) div:nth-child(2)")).getText(), MEDICATION_ID);
            assertEquals(driver.findElement(By.cssSelector("li:nth-child(" + i + ") div:nth-child(1) div:nth-child(3) div:nth-child(2)")).getText(), "Viên");
            assertEquals(driver.findElement(By.cssSelector("li:nth-child(" + i + ") div:nth-child(1) div:nth-child(4) div:nth-child(2)")).getText(), MANUFACTURE_DATE);
            assertEquals(driver.findElement(By.cssSelector("li:nth-child(" + i + ") div:nth-child(1) div:nth-child(5) div:nth-child(2)")).getText(), EXPIRATION_DATE);
        }
    }

    @Test
    public void checkSearchButton() throws InterruptedException {
        driver.get("http://localhost:3000/");
        driver.manage().window().maximize();
        driver.findElement(By.cssSelector("div[class='ant-space css-dev-only-do-not-override-k6wk4u ant-space-horizontal ant-space-align-center'] div[class='ant-space-item']")).click();
        driver.findElement(By.id("basic_email")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_password")).sendKeys("congtyduocphama@gmail.com");
        driver.findElement(By.id("basic_organization")).click();
        driver.findElement(By.cssSelector("body > div:nth-child(4) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(6) > div:nth-child(1)")).click();
        driver.findElement(By.cssSelector("button[type='submit'] span")).click();

        sleep(SLEEP_TIME);
        assertThat(driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).getText(), is("Quản lý thuốc"));
        driver.findElement(By.xpath("//span[contains(text(),'Quản lý thuốc')]")).click();
        sleep(SLEEP_TIME);

        driver.findElement(By.cssSelector("input[placeholder='ID loại thuốc']")).sendKeys(MEDICATION_ID);
        driver.findElement(By.cssSelector("input[placeholder='Ngày sản xuất']")).sendKeys(MANUFACTURE_DATE);
        driver.findElement(By.cssSelector("input[placeholder='Ngày hết hạn']")).sendKeys(EXPIRATION_DATE);

        sleep(SLEEP_TIME);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)")).getText(), MEDICATION_ID.substring(0, 4) + "..." + MEDICATION_ID.substring(MEDICATION_ID.length() - 4));
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(4)")).getText(), MANUFACTURE_DATE);
        assertEquals(driver.findElement(By.cssSelector("tbody tr:nth-child(1) td:nth-child(5)")).getText(), EXPIRATION_DATE);
    }
}
