package tests;

import models.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import specs.Specs;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.responseSpec;
import static java.lang.String.format;

public class ChangeTestCaseTest extends TestBase {

    @Test
    void createAndChangeTestCase() {

        step("Create testcase", () -> {

            TestCaseBody caseBody = new TestCaseBody();
            caseBody.setName(CaseName);

            ResponseId responseId = given()
                    .filter(withCustomTemplates())
                    .spec(Specs.request)
                    .body(caseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .spec(responseSpec)
                    .extract().as(ResponseId.class);

        step("Change the name of testcase", () -> {

             int testCaseID = responseId.getId();
             caseBody.setName(newTestCaseName);

             ChangedTestCaseResponse changedTestCaseResponse = given()
                     .filter(withCustomTemplates())
                     .spec(Specs.request)
                     .body(caseBody)
                     .queryParam("projectId", projectId)
                     .queryParam("leafId", testCaseID)
                     .when()
                     .post("/api/rs/testcasetree/leaf/rename")
                     .then()
                     .extract().as(ChangedTestCaseResponse.class);

        step("Verify that name is changed", () -> {
            open("/favicon.ico");
            Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
            getWebDriver().manage().addCookie(autorizationCookie);

            String testCaseUrl = format("/project/%s/test-cases/%s", projectId, testCaseID);
            open(testCaseUrl);

            $(".TestCaseLayout__name").shouldHave(text(newTestCaseName));

                });
            });
        });
    }
}
