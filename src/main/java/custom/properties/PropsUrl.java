package custom.properties;


import org.aeonbits.owner.Config;

/**
 * Интерфейс обсуживающий проперти файл отвечающий за открытие страницы Яндекса
 */

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:url.properties"

})
public interface PropsUrl extends Config {

    @Key("base.url.yandex")
    String baseURLYandex();

}
