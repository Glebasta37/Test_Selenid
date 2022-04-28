package custom.properties;

import org.aeonbits.owner.Config;

/**
 * Интерфейс обсуживающий проперти файл отвечающий за расширение экрана
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:screen.extension.properties"
})

public interface PropsScreenExtension extends Config {
    @Config.Key("base.screen.extension")
    String baseScreenExtension();
}
