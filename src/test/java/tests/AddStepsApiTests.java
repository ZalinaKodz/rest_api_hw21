package tests;

import models.*;

import org.junit.jupiter.api.Test;
import pages.TestCasePage;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.request;
import static specs.Specs.responseSpec;
import static tests.TestData.projectId;

import java.util.List;

public class AddStepsApiTests extends TestBase {

    TestCasePage testCasePage = new TestCasePage();

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
             testCasePage.openTestCaseEditor(projectId, testCaseID);
             testCasePage.checkTestCaseName(testCaseName);
        });
        step("Check step 1", () -> {
            testCasePage.openTestCaseEditor(projectId, testCaseID);
            testCasePage.checkTestCaseStep(testStepName1);
        });
        step("Check step 2", () -> {
            testCasePage.openTestCaseEditor(projectId, testCaseID);
            testCasePage.checkTestCaseStep(testStepName2);
        });
        step("Check step 3", () -> {
            testCasePage.openTestCaseEditor(projectId, testCaseID);
            testCasePage.checkTestCaseStep(testStepName2);
               });
            });
        });
    }
}
