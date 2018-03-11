import anna.klueva.Dog;
import anna.klueva.dao.DogDAO;
import anna.klueva.config.RootConfig;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

/**
 * Created by akliuieva on 1/5/18.
 */

@ContextConfiguration(classes={RootConfig.class})
@ActiveProfiles("test")
public class SystemTest extends AbstractTestNGSpringContextTests {
    public static final String PATH_TO_JBOSS_WAR = "/Users/akliuieva/Desktop/WebAppProject/WebApp/src/test/resources/wildfly-11.0.0.Final/bin/standalone.sh";
    public static final String BASE_URI = "http://localhost:8080/WebApp-1.1/dog/";

    @Autowired
    DogDAO dogDAO;

    @Test(groups = "systemTest", enabled = false)
    public void verifyGetByIdRequest() throws Exception {
        //TODO: move running JBOSS deployment to Jenkins level
        //Run start jboss script
        //Runtime.getRuntime().exec(PATH_TO_JBOSS_WAR);
        //TODO: add verification that JBOSS is up and running

        Dog expectedDog = Dog.builder()
                .name("Last dog 2")
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        //preparation step
        Dog createdDog = given()
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post(BASE_URI)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        Optional<Dog> actualDog = dogDAO.findById(createdDog.getId());
        Assert.assertEquals(actualDog.get().getName(), expectedDog.getName(), "Names are not equal for created god with id" + actualDog.get().getId());

        /*Dog response = given().baseUri(BASE_URI)
         .when()
         .get()
         .then()
         .assertThat()
         .statusCode(HttpStatus.SC_OK)
         .extract().body().as(Dog.class);

         assertTrue(response.getName().equals("RockStar"), "Response doesn't contain 'name: RockStar'");*/
    }
}

