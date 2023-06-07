package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:auth.properties"
})

public interface AuthConfig extends Config {
    @Key("userNameSelenoid")
    String userNameSelenoid();

    @Key("passwordSelenoid")
    String passwordSelenoid();

}
