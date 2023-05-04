package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import config.ConfigReader;
import config.WebConfig;
import config.WebConfigForProject;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;


public class TestBase {
    private static final WebConfig config = ConfigReader.Instance.read();

    @BeforeAll
    public static void beforeAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        WebConfigForProject webConfigForProject = new WebConfigForProject(config);
        webConfigForProject.webConfig();
    }
}
