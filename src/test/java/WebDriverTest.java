import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.security.Key;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zver on 12.01.2016.
 */
public class WebDriverTest {
    private WebDriver driver;
    private final String webDriverPath = "C:/chromedriver.exe";
    private String baseUrl;
    private String windowSize;
    private EmailGenerator eg;
    private String email;
    private JavascriptExecutor executor;

    @BeforeClass // предварительные настройки
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        driver = new ChromeDriver();
        eg = new EmailGenerator();
        baseUrl = "https://www.wrike.com/";
        executor = (JavascriptExecutor) driver;
    }

    @Test // тест на максимальном разрешении
    public void testLargeResolution() throws InterruptedException {

        // инициализация окна, и переход на baseUrl
        driver.manage().window().maximize();
        windowSize = "large";
        driver.get(baseUrl);

        // тестовое задание
        goToLoginPage(windowSize);
        goToCreateAccount();
        sendEmailOnFreeTrialPage();
        checkSentSuccessfully();
        goToPricingPage(windowSize);
        openModalFreeTrialProfessional(windowSize);
        sendEmailOnPricePage(windowSize);
        checkSentSuccessfully();
    }

    @Test // тест на разрешении < 720
    public void testSmallResolution() throws InterruptedException {

        // инициализация окна, и переход на baseUrl
        driver.manage().window().setSize(new Dimension(719, 768));
        windowSize = "small";
        driver.get(baseUrl);

        // тестовое задание
        goToLoginPage(windowSize);
        goToCreateAccount();
        sendEmailOnFreeTrialPage();
        checkSentSuccessfully();
        goToPricingPage(windowSize);
        openModalFreeTrialProfessional(windowSize);
        sendEmailOnPricePage(windowSize);
        checkSentSuccessfully();
    }

    @AfterClass // данный метод запустится после выполнения всех тестовых методов
    public void tearDown() {
        // Close all browser windows and safely end the session
        driver.quit();
    }

    /**
     * переход на страницу входа /login/
     *
     * @param size размер окна
     * @throws InterruptedException
     */
    private void goToLoginPage(String size) throws InterruptedException {
        if (size.equals("large")) {
            driver.findElement(By.cssSelector("a.nav_login")).click();
        } else if (size.equals("small")) {
            driver.findElement(By.cssSelector(".nav_pull")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("Login")).click();
        }

    }

    /**
     * переход на страницу создания аккаунта /free-trial
     */
    private void goToCreateAccount() {
        driver.findElement(By.linkText("Create account")).click();
    }

    /**
     * отправка рандомного email на странице /price/
     *
     * @throws InterruptedException
     */
    private void sendEmailOnPricePage(String size) throws InterruptedException {
        if (size.equals("large")) {
            driver.switchTo().frame(driver.findElement(By.id("ajaxIframeFeatures")));
        }
        email = eg.randomEmail(15, "yandex.ru");
        driver.findElement(By.id("email")).sendKeys(email);
        executor.executeScript("document.getElementById('start-project').click()");
        Thread.sleep(2000);
    }

    /**
     * переход на страницу /price/
     */
    private void goToPricingPage(String size) throws InterruptedException {
        if (size.equals("large")) {
            driver.findElement(By.linkText("Pricing")).click();
        } else if (size.equals("small")) {
            driver.findElement(By.cssSelector(".nav_pull")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("Pricing")).click();
        }
    }

    /**
     * открытие модального окна на странице /price/
     */
    private void openModalFreeTrialProfessional(String size) throws InterruptedException {
        if (size.equals("large")) {
            driver.findElement(By.cssSelector("div.wrp > div.tariffs-wrap-btn > #start-free-trial-professional")).click();
            Thread.sleep(2000);
        } else if (size.equals("small")) {
            driver.findElement(By.xpath("//ul[@class='table_mobile_tabs']/li/a/h2[contains(text(), 'Professional')]")).click();
            driver.findElement(By.cssSelector("div.wrp > div.tariffs-wrap-btn > #start-free-trial-professional")).click();
            Thread.sleep(2000);
        }

    }

    /**
     * отправка рандомного email на странице /free-trial
     *
     * @throws InterruptedException
     */
    private void sendEmailOnFreeTrialPage() throws InterruptedException {
        email = eg.randomEmail(12, "gmail.com");
        executor.executeScript("document.getElementsByName('email')[0].value = '" + email + "'");
        executor.executeScript("document.getElementsByClassName('btn btn-round btn-green')[0].click()");
        Thread.sleep(2000);
    }

    /**
     * проверка успешной отправки формы
     *
     * @throws InterruptedException
     */
    private void checkSentSuccessfully() throws InterruptedException {
        WebElement element = driver.findElement(By.cssSelector("h1"));
        Assert.assertEquals(element.getText(), "Thank you for choosing Wrike!");
        driver.findElement(By.id("resendEmail")).click();
        Thread.sleep(2000);
    }
}
