package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static tests.TestData.allureTestopsSession;
import static tests.TestData.token;

public class Specs {

    public static RequestSpecification request = with()
            .log().all()
            .contentType("application/json;charset=UTF-8")
            .header("X-XSRF-TOKEN", token)
            .cookies("XSRF-TOKEN", token,
                            "ALLURE_TESTOPS_SESSION", allureTestopsSession);


    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(200)
            .build();
}
