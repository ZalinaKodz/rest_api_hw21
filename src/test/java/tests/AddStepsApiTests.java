package tests;

import models.*;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.request;
import static specs.Specs.responseSpec;
import static java.lang.String.format;

import java.util.List;

public class AddStepsApiTests extends TestBase {

    @Test
    void createWitApiOnlyTest() {

        step("Create testcase", () -> {

            TestCaseBody caseBody = new TestCaseBody();
            caseBody.setName(testCaseName);

            ResponseId responseId = given()
                    .filter(withCustomTemplates())
                    .spec(request)
                    .body(caseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .spec(responseSpec)
                    .extract().as(ResponseId.class);

        step("Create steps in testcase", () -> {

            int testCaseID = responseId.getId();

            CreateTestCaseBody requestBody = new CreateTestCaseBody();
            requestBody.setSteps(List.of(new CreateSteps(testStepName1, ""), new CreateSteps(testStepName2, ""), new CreateSteps(testStepName3, "")));
            requestBody.setWorkPath(List.of(2));

                      given()
                     .filter(withCustomTemplates())
                     .spec(request)
                     .body(requestBody)
                     .when()
                     .post("/api/rs/testcase/" + testCaseID + "/scenario")
                     .then()
                     .extract().response();

        step("Check test case name", () -> {

             open("/favicon.ico");
             Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
             getWebDriver().manage().addCookie(autorizationCookie);

             String testCaseUrl = format("/project/%s/test-cases/%s", projectId, testCaseID);
             open(testCaseUrl);

             $(".TestCaseLayout__name").shouldHave(text(testCaseName));

        });
        step("Check step 1", () -> {

              open("/favicon.ico");
              Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
              getWebDriver().manage().addCookie(autorizationCookie);

              String testCaseUrl = format("/project/%s/test-cases/%s", projectId, testCaseID);
              open(testCaseUrl);

              $(".Scenario").shouldHave(text(testStepName1));
        });
        step("Check step 2", () -> {

               open("/favicon.ico");
               Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
               getWebDriver().manage().addCookie(autorizationCookie);

               String testCaseUrl = format("/project/%s/test-cases/%s", projectId, testCaseID);
               open(testCaseUrl);

               $(".Scenario").shouldHave(text(testStepName2));
        });
        step("Check step 3", () -> {

                open("/favicon.ico");
                Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
                getWebDriver().manage().addCookie(autorizationCookie);

                String testCaseUrl = format("/project/%s/test-cases/%s", projectId, testCaseID);
                open(testCaseUrl);

                $(".Scenario").shouldHave(text(testStepName3));
               });
            });
        });
    }
}
