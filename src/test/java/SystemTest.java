import anna.klueva.Dog;
import anna.klueva.dao.DogDAO;
import anna.klueva.util.RootConfig;
import anna.klueva.util.WebConfig;
import org.apache.http.HttpStatus;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

/**
 * Created by akliuieva on 1/5/18.
 */


//Need .war with profile "test"
@ContextConfiguration(classes={RootConfig.class})
@ActiveProfiles("dev")
public class SystemTest extends AbstractTestNGSpringContextTests {
    public static final String PATH_TO_JBOSS_WAR = "/Users/akliuieva/Desktop/WebAppProject/WebApp/src/test/resources/wildfly-11.0.0.Final/bin/standalone.sh";
    public static final String BASE_URI = "http://localhost:8080/WebApp-1.1/dog/1";

    @Autowired
    DogDAO dogDAO;

    @Test(groups = "systemTest", enabled = false)
    public void verifyGetByIdRequest() throws Exception {
        //TODO: move running JBOSS deployment to Jenkins level
        //Run start jboss script
        //Runtime.getRuntime().exec(PATH_TO_JBOSS_WAR);
        //TODO: add verification that JBOSS is up and running

         Dog response = given().baseUri(BASE_URI)
         .when()
         .get()
         .then()
         .assertThat()
         .statusCode(HttpStatus.SC_OK)
         .extract().body().as(Dog.class);

         assertTrue(response.getName().equals("RockStar"), "Response doesn't contain 'name: RockStar'");
    }
}

