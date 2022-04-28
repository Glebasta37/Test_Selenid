package custom.allure.selenid;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLog;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import io.qameta.allure.util.ResultsUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Кастомный класс, созданный для переопределения метода сохранения скринштов и страниц(для аллюр отчета) на любое действие теста
 */
public class CustomAllureSelenid extends AllureSelenide {
    private boolean includeSelenideLocatorsSteps;
    private final Map<LogType, Level> logTypesToSave;
    private boolean savePageHtml;
    private final AllureLifecycle lifecycle;
    private boolean saveScreenshots;
    private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenide.class);

    public CustomAllureSelenid() {
        this(Allure.getLifecycle());
    }

    public CustomAllureSelenid(AllureLifecycle lifecycle) {
        this.includeSelenideLocatorsSteps = true;
        this.logTypesToSave = new HashMap<>();
        this.savePageHtml = true;
        this.lifecycle = lifecycle;
        this.saveScreenshots = true;
    }

    private boolean stepsShouldBeLogged(LogEvent event) {
        return this.includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
    }

    private static String getBrowserLogs(LogType logType, Level level) {
        return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
    }

    private static Optional<byte[]> getScreenshotBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)) : Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Could not get screen shot", var1);
            return Optional.empty();
        }
    }

    private static Optional<byte[]> getPageSourceBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted() ? Optional.of(WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8)) : Optional.empty();
        } catch (WebDriverException var1) {
            LOGGER.warn("Could not get page source", var1);
            return Optional.empty();
        }
    }

    @Override
    public void afterEvent(LogEvent event) {
        if (true) {
            this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
                if (this.saveScreenshots) {
                    getScreenshotBytes().ifPresent((bytes) -> {
                        this.lifecycle.addAttachment("Screenshot", "image/png", "png", bytes);
                    });
                }

                if (this.savePageHtml) {
                    getPageSourceBytes().ifPresent((bytes) -> {
                        this.lifecycle.addAttachment("Page source", "text/html", "html", bytes);
                    });
                }

                if (!this.logTypesToSave.isEmpty()) {
                    this.logTypesToSave.forEach((logType, level) -> {
                        byte[] content = getBrowserLogs(logType, level).getBytes(StandardCharsets.UTF_8);
                        this.lifecycle.addAttachment("Logs from: " + logType, "application/json", ".txt", content);
                    });
                }

            });
        }

        if (this.stepsShouldBeLogged(event)) {
            this.lifecycle.getCurrentTestCaseOrStep().ifPresent((parentUuid) -> {
                switch (event.getStatus()) {
                    case PASS:
                        this.lifecycle.updateStep((step) -> {
                            step.setStatus(Status.PASSED);
                        });
                        break;
                    case FAIL:
                        this.lifecycle.updateStep((stepResult) -> {
                            stepResult.setStatus((Status) ResultsUtils.getStatus(event.getError()).orElse(Status.BROKEN));
                            stepResult.setStatusDetails((StatusDetails) ResultsUtils.getStatusDetails(event.getError()).orElse(new StatusDetails()));
                        });
                        break;
                    default:
                        LOGGER.warn("Step finished with unsupported status {}", event.getStatus());
                }

                this.lifecycle.stopStep();
            });
        }

    }

    public AllureSelenide screenshots(boolean saveScreenshots) {
        this.saveScreenshots = saveScreenshots;
        return this;
    }

    public AllureSelenide savePageSource(boolean savePageHtml) {
        this.savePageHtml = savePageHtml;
        return this;
    }
}
