package tests;

import models.DashboardBody;
import models.DashboardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static specs.Specs.request;
import static specs.Specs.responseSpec;

public class DashboardApiTests extends TestBase {

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

        open("/favicon.ico");
        Cookie autorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", allureTestOpsSession);
        getWebDriver().manage().addCookie(autorizationCookie);

        String testCaseUrl = format("/project/%s/dashboards/%s", projectId, dashboardId);
        open(testCaseUrl);

        $(".ProjectDashboards__tabs").shouldHave((text("name")));

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
