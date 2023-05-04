package tests;
import com.codeborne.selenide.Configuration;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.request;
import static specs.Specs.responseSpec;

import java.util.List;

public class AddStepsApiTests extends TestBase {
    static Faker faker = new Faker();
    static String projectId = "2211";
    static String testCaseName = faker.name().fullName();
    static  String testStepName1 = faker.shakespeare().asYouLikeItQuote();
    static String testStepName2 = faker.shakespeare().asYouLikeItQuote();
    static String testStepName3 = faker.shakespeare().asYouLikeItQuote();

    @BeforeAll
    static void setUp() {

        RestAssured.baseURI = "https://allure.autotests.cloud";
    }

    @Test
    void createWitApiOnlyTest() {

        step("Create testcase", () -> {

            TestCaseBody caseBody = new TestCaseBody();
            caseBody.setName(testCaseName);

            ResponseId responseId = given()
                    .filter(withCustomTemplates())
                    .spec(request)
                    .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                    .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                            "ALLURE_TESTOPS_SESSION", "c684f3d6-c5c7-4e18-81dd-211c7a897ea5")
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

                Response response = given()
                        .filter(withCustomTemplates())
                        .spec(request)
                        .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                        .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                                "ALLURE_TESTOPS_SESSION", "c684f3d6-c5c7-4e18-81dd-211c7a897ea5")
                        .body(requestBody)
                        .when()
                        .post("/api/rs/testcase/" + testCaseID + "/scenario")
                        .then()
                        .extract().response();

                step("Verify steps in testcase", () -> {
                    TestCaseResponse responseBody = response.getBody().as(TestCaseResponse.class);
                    assertThat(responseBody.getSteps()).hasSize(3);

                    step("Check step 1", () -> {
                        assertThat(responseBody.getSteps().get(0).getName()).isEqualTo(testStepName1);
                        assertThat(responseBody.getSteps().get(0).getSteps()).isEmpty();
                        assertThat(responseBody.getSteps().get(0).isLeaf()).isTrue();
                        assertThat(responseBody.getSteps().get(0).getStepsCount()).isEqualTo(0);
                        assertThat(responseBody.getSteps().get(0).isHasContent()).isFalse();
                    });
                    step("Check step 2", () -> {
                        assertThat(responseBody.getSteps().get(1).getName()).isEqualTo(testStepName2);
                        assertThat(responseBody.getSteps().get(1).getSteps()).isEmpty();
                        assertThat(responseBody.getSteps().get(1).isLeaf()).isTrue();
                        assertThat(responseBody.getSteps().get(1).getStepsCount()).isEqualTo(0);
                        assertThat(responseBody.getSteps().get(1).isHasContent()).isFalse();
                    });
                    step("Check step 3", () -> {
                        assertThat(responseBody.getSteps().get(2).getName()).isEqualTo(testStepName3);
                        assertThat(responseBody.getSteps().get(2).getSteps()).isEmpty();
                        assertThat(responseBody.getSteps().get(2).isLeaf()).isTrue();
                        assertThat(responseBody.getSteps().get(2).getStepsCount()).isEqualTo(0);
                        assertThat(responseBody.getSteps().get(2).isHasContent()).isFalse();
                    });
                });
            });
        });
    }
}
