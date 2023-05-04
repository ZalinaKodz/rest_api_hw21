package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import models.DashboardBody;
import models.DashboardResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.Specs.request;
import static specs.Specs.responseSpec;

public class DashboardApiTests {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;

        RestAssured.baseURI = "https://allure.autotests.cloud";
    }

    @Test
    @DisplayName("Creating and deleting dashboard")
    void createUser() {

        DashboardBody dashboardBody = new DashboardBody();
        dashboardBody.setProjectId(2211);
        dashboardBody.setName("name");

        DashboardResponse dashboardResponse = step("Add dashboard", () ->
                given(request)
                        .filter(withCustomTemplates())
                        .spec(request)
                        .log().all()
                        .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                        .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                                "ALLURE_TESTOPS_SESSION", "c684f3d6-c5c7-4e18-81dd-211c7a897ea5")
                        .contentType("application/json;charset=UTF-8")
                        .body(dashboardBody)
                        .when()
                        .post("/api/rs/dashboard")
                        .then()
                        .spec(responseSpec)
                        .extract().as(DashboardResponse.class));

        step("Verify  dashboard`s name", () ->
                assertThat(dashboardResponse.getName()).isEqualTo("name"));


               int dashboardId = dashboardResponse.getId();

        step("Delete dashboard", () ->
                given(request)
                        .filter(withCustomTemplates())
                        .spec(request)
                        .log().all()
                        .header("X-XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34")
                        .cookies("XSRF-TOKEN", "379d4f4d-11b9-44b5-8afd-d2e2a6c06c34",
                                "ALLURE_TESTOPS_SESSION", "c684f3d6-c5c7-4e18-81dd-211c7a897ea5")
                        .contentType("application/json;charset=UTF-8")
                        .when()
                        .delete("/api/rs/dashboard/"+dashboardId)
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(202));
    }
}
