package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.String.format;
import static tests.TestData.allureTestopsSession;


public class DashboardPage {
    private final SelenideElement
    dashboardTab = $(".ProjectDashboards__tabs");

    public DashboardPage checkDashboardTabs(String value) {
        dashboardTab.shouldHave(text(value));
        return this;
    }
    public void openDashboard(String projectId, Integer value) {
        open("/favicon.ico");
        Cookie authorizationCookie = new Cookie(
                "ALLURE_TESTOPS_SESSION", allureTestopsSession);
        getWebDriver().manage().addCookie(authorizationCookie);
        String testCaseUrl = format("/project/%s/dashboards/%s", projectId, value);
        open(testCaseUrl);
    }
}
