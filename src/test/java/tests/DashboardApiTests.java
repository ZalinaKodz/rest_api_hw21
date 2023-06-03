package tests;

import models.DashboardBody;
import models.DashboardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.DashboardPage;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.Specs.request;
import static specs.Specs.responseSpec;
import static tests.TestData.projectId;

public class DashboardApiTests extends TestBase {
    DashboardPage dashboardPage = new DashboardPage();

    @Test
    @DisplayName("Dashboard")
    void createUser() {

        DashboardBody dashboardBody = new DashboardBody();
        dashboardBody.setProjectId(2211);
        dashboardBody.setName("name");

        DashboardResponse dashboardResponse = step("Add dashboard", () ->
                given(request)
                        .filter(withCustomTemplates())
                        .spec(request)
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
                        .when()
                        .delete("/api/rs/dashboard/"+dashboardId)
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(202));
    }
}
