package custom.units;

import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс хранит аргументы, которые используются в тесте
 */
public class DataProvider {

    public static Stream<Arguments> testingYandexMarketSelenid() {

        List<String> brand = new ArrayList<>();
        brand.add("Apple");
        return Stream.of(
                Arguments.of("market", "Электроника", "Смартфоны", "Производитель", brand, "12", "iPhone", 300000));
    }

}
