package specs;

import config.QualitConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class ProductSpecification {

    public static QualitConfig configOwner = ConfigFactory.create(QualitConfig.class);

    public static RequestSpecification requestSpecification = with()
            .baseUri(configOwner.baseUri())
            .contentType(ContentType.JSON)
            .log().method()
            .log().uri()
            .log().headers()
            .log().body();

    public static ResponseSpecification responseSpecification200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();
}
