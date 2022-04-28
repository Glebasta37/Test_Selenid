import Pages.YandexMainPage;
import Pages.YandexMarketPage;
import custom.properties.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;

public class AllTest extends BaseTest {

    @DisplayName("Проверка ядекс маркета")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("custom.units.DataProvider#testingYandexMarketSelenid")
    public void validateFilterResultOnYandexMarket(String service, String chapter, String category, String manufacturer, List<String> brand, String count, String elementForValidate, long timerValueInMillis) {
        open(TestData.propsUrl.baseURLYandex(), YandexMainPage.class)
                .goToYandexServicePage(service, YandexMarketPage.class)
                .selectedChapterAndCategory(chapter, category)
                .setFilterValues(manufacturer, brand)
                .setNumberElementsOnPage(count).validateNumberPagesSwitchingAndValidateContainsThem(elementForValidate, timerValueInMillis);
    }
}
