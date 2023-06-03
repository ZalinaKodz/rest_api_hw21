package tests;

import models.*;

import org.junit.jupiter.api.Test;
import pages.TestCasePage;
import specs.Specs;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.responseSpec;
import static tests.TestData.projectId;

public class ChangeTestCaseTest extends TestBase {
    TestCasePage testCasePage = new TestCasePage();

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

                      given()
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
            testCasePage.openTestCaseEditor(projectId, testCaseID);
            testCasePage.checkTestCaseName(newTestCaseName);

                });
            });
        });
    }
}
