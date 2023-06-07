package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import config.WebDriverProvider;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static config.WebDriverProvider.config;



public class TestBase {
 Faker faker = new Faker();
 String CaseName = faker.name().fullName();
 String newTestCaseName = faker.gameOfThrones().quote();
 String testCaseName = faker.name().fullName();
 String testStepName1 = faker.shakespeare().asYouLikeItQuote();
 String testStepName2 = faker.shakespeare().asYouLikeItQuote();
 String testStepName3 = faker.shakespeare().asYouLikeItQuote();



    @BeforeAll
    static void setUp() {
        WebDriverProvider.configure();
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