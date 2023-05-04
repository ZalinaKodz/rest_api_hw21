package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;

import models.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import specs.Specs;


import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.responseSpec;

public class ChangeTestCaseTest {
    static Faker faker = new Faker();
    static String projectId = "2211";
    static String testCaseName = faker.name().fullName();
    static String newTestCaseName = faker.gameOfThrones().quote();

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;

        RestAssured.baseURI = "https://allure.autotests.cloud";
    }

    @Test
    void createAndChangeTestCase() {

        step("Create testcase", () -> {

            TestCaseBody caseBody = new TestCaseBody();
            caseBody.setName(testCaseName);

            ResponseId responseId = given()
                    .filter(withCustomTemplates())
                    .spec(Specs.request)
                    .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                    .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                            "ALLURE_TESTOPS_SESSION", "98f44c5a-da6b-4c40-a341-d038b07df73c")
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
                        .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                        .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                                "ALLURE_TESTOPS_SESSION", "98f44c5a-da6b-4c40-a341-d038b07df73c")
                        .body(caseBody)
                        .queryParam("projectId", projectId)
                        .queryParam("leafId", testCaseID)
                        .when()
                        .post("/api/rs/testcasetree/leaf/rename")
                        .then()
                        .extract().as(ChangedTestCaseResponse.class);

                step("Verify that name is changed", () -> {
                    assertThat(changedTestCaseResponse.getName()).isEqualTo(newTestCaseName);
                });
            });
        });
    }
}
