package custom.properties;

import org.aeonbits.owner.ConfigFactory;

/**
 * класс обсуживающий проперти файлы
 */
public class TestData {
    public static PropsUrl propsUrl = ConfigFactory.create(PropsUrl.class);
    public static PropsScreenExtension propsScreenExtension = ConfigFactory.create(PropsScreenExtension.class);
}
