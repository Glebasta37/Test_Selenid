package Pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

/**
 * Класс описывает главную страницу Янлекса
 */
public class YandexMainPage extends BasePage {

    /**
     * В методе  переходим по ссылке и переключаемся на новую открытую вкладку
     *
     * @param service - сервис Яндекса
     * @return - возвращаем объект класса наследника BasePage
     */
    @Step("Открываем Яндекс Маркет и переключаемся на новую вкладку")
    public <T extends BasePage> T goToYandexServicePage(String service, Class<T> typeNextPage) {
        $x("//a[@data-id='" + service + "']").click();
        switchTo().window(1);
        return typeNextPage.cast(page(typeNextPage));
    }

}
