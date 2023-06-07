package config;

import com.codeborne.selenide.Configuration;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class WebDriverProvider {
    public static WebDriverConfig config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
    static AuthConfig authConfig = ConfigFactory.create(AuthConfig.class, System.getProperties());

    public static void configure() {
        Configuration.browser = config.getBrowser();
        Configuration.browserVersion = config.getBrowserVersion();
        Configuration.browserSize = config.getBrowserSize();
        Configuration.baseUrl = config.getBaseUrl();
        Configuration.pageLoadStrategy = "eager";
        String remoteUrl = "https://" + authConfig.userNameSelenoid() + ":" + authConfig.passwordSelenoid()
                + "@" + config.getRemoteUrl() + "/wd/hub";
        System.out.println(remoteUrl);

        if (config.isRemote()) {
            Configuration.remote = remoteUrl;

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));

            Configuration.browserCapabilities = capabilities;
        }
    }
}
