package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static config.WebConfigForProject.config;



public class TestBase {
 Faker faker = new Faker();
 String CaseName = faker.name().fullName();
 String newTestCaseName = faker.gameOfThrones().quote();
 String testCaseName = faker.name().fullName();
 String testStepName1 = faker.shakespeare().asYouLikeItQuote();
 String testStepName2 = faker.shakespeare().asYouLikeItQuote();
 String testStepName3 = faker.shakespeare().asYouLikeItQuote();


    public static String projectId = "2211",
            allureTestOpsSession = "e6b20b21-6a15-4931-9043-1e370e9befd8",
            token = "e96309d1-e0a7-428e-9155-f0f226a0ef7c";

    @BeforeAll
    static void setUp() {
        config();
        RestAssured.baseURI = "https://allure.autotests.cloud";
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last step screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        closeWebDriver();
        if (config.isRemote()) {
            Attach.addVideo();
        }
    }
}