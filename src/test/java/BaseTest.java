import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import custom.allure.selenid.CustomAllureSelenid;
import custom.properties.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class BaseTest {

    @BeforeAll
    public static void setup() {
        SelenideLogger.addListener("AllureSelenide", new CustomAllureSelenid().screenshots(true).savePageSource(true));
        Configuration.browserSize = TestData.propsScreenExtension.baseScreenExtension();
    }

    @BeforeEach
    public void option() {
        Configuration.timeout = 6000;

    }
}
