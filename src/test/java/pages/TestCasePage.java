package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static tests.TestData.allureTestopsSession;

public class TestCasePage {
    private final String LOGIN_URL = "/";
    private final SelenideElement
            testCaseName = $(".TestCaseLayout__name"),
            testCaseScenario = $(".Scenario"),
            editTestCaseMenu = $(".Menu__trigger"),
            deleteItem = $(".Menu__item_danger "),
            deleteText =  $(byText("Delete")),
            submitDeleteButton = $(".TestCaseDeleteModal__confirm-button");


    public TestCasePage checkTestCaseName(String value) {
        testCaseName.shouldHave(text(value));
        return this;
    }
    public TestCasePage openPage() {
        open(LOGIN_URL);

        return this;
    }
    public void openTestCaseEditor(String projectId, int testCaseId) {
        open("/favicon.ico");
        Cookie authorizationCookie = new Cookie(
                "ALLURE_TESTOPS_SESSION", allureTestopsSession);
        getWebDriver().manage().addCookie(authorizationCookie);
        open("/project/" + projectId + "/test-cases/" + testCaseId);
    }

    public TestCasePage checkTestCaseStep(String value) {
        testCaseScenario.shouldHave(text(value));
        return this;
    }

    public TestCasePage deleteTestCase() {
        editTestCaseMenu.click();
        deleteText.click();
        deleteItem.click();
        submitDeleteButton.click();

        return this;
    }
}
