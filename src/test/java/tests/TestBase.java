package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static config.WebConfigForProject.config;



public class TestBase {
    static Faker faker = new Faker();
    static String CaseName = faker.name().fullName();
    static String newTestCaseName = faker.gameOfThrones().quote();
    static String testCaseName = faker.name().fullName();
    static  String testStepName1 = faker.shakespeare().asYouLikeItQuote();
    static String testStepName2 = faker.shakespeare().asYouLikeItQuote();
    static String testStepName3 = faker.shakespeare().asYouLikeItQuote();


    public static String projectId = "2211",
            allureTestOpsSession = "b1b37ac1-ba73-4715-a836-cbb1fb5bf49e",
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
        if (config.isRemote()) {
            Attach.addVideo();
        }
        Selenide.closeWebDriver();
    }
}