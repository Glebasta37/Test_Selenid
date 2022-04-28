package Pages;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

/**
 * Класс описывает страницу Яндекс Маркета
 */

public class YandexMarketPage extends BasePage {

    /**
     * xpath серого окна загрузки
     */
    String grayBoxElement = "//div[@aria-label='Результаты поиска']/div[not(@data-apiary-widget-name)]";
    /**
     * В методе выбираем раздел и категорию на Яндекс Маркете
     *
     * @param chapter  - раздел
     * @param category - категория
     * @return - возвращаем объект класса YandexMarketPage
     */
    @Step("Выбираем раздле -  '{chapter}', а далее категорию - '{category}'")
    public YandexMarketPage selectedChapterAndCategory(String chapter, String category) {
        $x("//button[@id='catalogPopupButton']").click();
        $x("//span[text()='" + chapter + "']").should(Condition.exist);
        actions().moveToElement($x("//span[text()='" + chapter + "']")).perform();
        $x("//a[text()='" + category + "']").click();
        return this;

    }

    /**
     * В методе проверяем, есть ли в переданном блоке кнопка "Показать все", если да, кликаем, если нет ничего не делаем
     *
     * @param xpathButton - xpath элемента кнопки "Показать все"
     */
    @Step("Проверяем наличие кнопки  '{xpathButton}', если она есть, нажимаем нее")
    private void validateVisibleButtonAndClick(String xpathButton) {
        if ($$x((xpathButton)).size() > 0) {
            $x(xpathButton).click();
        }
    }

    /**
     * В методе проверяем состояние чекбока, включен или выключен
     *
     * @param header  - заголовок раздела фильтра
     * @param element - элемент для проверки (чекбокс)
     * @return если true - значит чек бокс включен, если false - значит чекбокс выключен
     */
    @Step("Проверяем в разделе филтра '{header}' статус чекбочка '{element}' (включен или выключен)")
    private boolean isValidateCheckBoxOn(String header, String element) {
        String idHeader = $x("//legend[contains(.,'" + header + "')]/..").getAttribute("data-autotest-id");
        String idCheckbox = $x("//legend[text()='" + header + "']/..//label//input[contains(@name,' " + element + "')]").getAttribute("id");
        String hrefWithGlfilter = $x("//a[@data-auto='intent-link']").getAttribute("href");
        return hrefWithGlfilter.contains(idCheckbox.replace(idHeader + "_", "")) && hrefWithGlfilter.contains(idHeader);
    }

    /**
     * В методе устанавливаем значения фильтра
     *
     * @param header - заголовок раздела фильтра
     * @param brand  - элементы для установки фильтра
     * @return -  возвращаем объект класса YandexMarketPage
     */
    @Step("Устанавливаем значения фильтра")
    public YandexMarketPage setFilterValues(String header, List<String> brand) {
        String buttonShowAll = "//legend[text()='" + header + "']//following-sibling::footer/button[text()='Показать всё']";
        String xpath = "//input[@name='Поле поиска']";
        validateVisibleButtonAndClick(buttonShowAll);
        SelenideElement elementSearch = $x(xpath);
        for (String element : brand) {
            elementSearch.clear();
            elementSearch.sendKeys(element);
            if (!isValidateCheckBoxOn(header, element)) {
                $x("//legend[text()='" + header + "']/..//label//div//span[text()='" + element + "']").click();
            }
        }
        $x(grayBoxElement).should(Condition.disappear);
        return this;
    }

    /**
     * В методе установливаем количество показываемых элементов на странице
     *
     * @param count - количество отображаемых элементов на странице
     * @return - возвращаем объект класса YandexMarketPage
     */
    @Step("Устанавливаем количество показываемых элементов на странице '{count}'")
    public YandexMarketPage setNumberElementsOnPage(String count) {
        $x("//div[@data-apiary-widget-name='@MarketNode/SearchPager']//button[@type='button']").click();
        $x("//div[@data-apiary-widget-name='@MarketNode/SearchPager']//button[text()='Показывать по " + count + "']").click();
        $x(grayBoxElement).should(Condition.disappear);
        return this;
    }

    /**
     * Метод возвращаем список элементов выборки
     *
     * @return -  список элементов в выборке
     */
    @Step("Возвращаем список элементов выборки")
    private ElementsCollection getSearchResult() {
        return $$x("//div[@data-zone-name='SearchPartition']//a[@data-node-name='title']");
    }

    /**
     * В методе проверяем, что в списке находятся только те элементы, которые содержат выбранное значение elementForValidate
     *
     * @param listElementAfterSearch - список элементов для проверки
     * @param elementForValidate     - элемент который ищем в списке
     */
    @Step("Проверяем, что на переданной странице, находятся только те элементы, которые содержат выбранное значение '{elementForValidate}'")
    private void validateListContainsOnlyFilterElements(ElementsCollection listElementAfterSearch, String elementForValidate) {
        listElementAfterSearch.shouldBe(CollectionCondition
                .allMatch("", x -> x.getText().contains(elementForValidate))
                .because("В выборку попали значения отличные от " + elementForValidate));

    }


    /**
     * В методе переключаем страницы и проверяем выборку на наличие элемента elementForValidate
     *
     * @param elementForValidate - элемент для проверки элементов страницы
     * @param timerValueInMillis - значение таймера в милисекундах для контроля веремени теста
     * @return - возвращаем объект класса YandexMarketPage
     */
    @Step("Если страниц с результатами больше одной, переключаем и проверяем каждую на то," +
            " что в списке находятся только те элементы, которые содержат выбранное значение '{elementForValidate}' ")
    public YandexMarketPage validateNumberPagesSwitchingAndValidateContainsThem(String elementForValidate, long timerValueInMillis) {
        long startTimer = System.currentTimeMillis();
        String xpathNextPage = "//a[@aria-label='Следующая страница']";
        while ($$x(xpathNextPage).size() > 0 && System.currentTimeMillis() - startTimer < timerValueInMillis) {
            validateListContainsOnlyFilterElements(getSearchResult(), elementForValidate);
            $x(xpathNextPage).click();
            $x(grayBoxElement).should(Condition.disappear);
        }
        validateListContainsOnlyFilterElements(getSearchResult(), elementForValidate);
        return this;
    }


}