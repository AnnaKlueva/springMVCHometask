import anna.klueva.Dog;
import anna.klueva.config.RootConfig;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Created by akliuieva on 1/5/18.
 */

@ContextConfiguration(classes={RootConfig.class})
@ActiveProfiles("test")
public class SystemTest extends AbstractTestNGSpringContextTests {
    public static final String PATH_TO_JBOSS_WAR = "/Users/akliuieva/Desktop/WebAppProject/WebApp/src/test/resources/wildfly-11.0.0.Final/bin/standalone.sh";
    public static final String BASE_URI = "http://localhost:8080/WebApp-1.1/dog/";

    //TODO: move running JBOSS deployment to Jenkins level
    //Run start jboss script
    //Runtime.getRuntime().exec(PATH_TO_JBOSS_WAR);
    //TODO: add verification that JBOSS is up and running

    @Test(groups = "systemTest")
    public void verifyGetByIdRequest() throws Exception {
        Dog[] arrayOfDogs = given()
                .when()
                .get(BASE_URI + "allDogs")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class);

        Dog actualDog = given().baseUri(BASE_URI + arrayOfDogs[0].getId())
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        Assert.assertEquals("Get request works incorrectly",
                actualDog, arrayOfDogs[0]);
    }

    //TODO: understand why date is incorrect
    @Test(groups = "systemTest")
    public void verifyCreateDogRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        int initialDogTableSize = given().baseUri(BASE_URI+"allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class)
                .length;

        //preparation step
        Dog createdDog = given().baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        Dog[] arrayOfDogWithAddedItem = given().baseUri(BASE_URI+"allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class);

        int arrayOfDogWithAddedItemSize = arrayOfDogWithAddedItem.length;

        Assert.assertEquals("Dog table quantity was not increased",
                initialDogTableSize + 1, arrayOfDogWithAddedItemSize);
        Assert.assertEquals("Create request is working incorrectly",
                arrayOfDogWithAddedItem[arrayOfDogWithAddedItemSize-1], createdDog);
    }

    @Test(groups = "systemTest")
    public void verifyUpdateRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

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

        Dog actualDog = given().baseUri(BASE_URI+createdDog.getId())
                .when()
                .get()
                .then()
                .extract().body().as(Dog.class);

        Assert.assertTrue("Dog was not added" + actualDog.getId(),
                actualDog.getName().equals(expectedDog.getName()));

        createdDog.setName("Updated dog name");

        Dog updatedDog = given().baseUri(BASE_URI + createdDog.getId())
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(createdDog)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        Assert.assertEquals("Update request is working incorrectly", updatedDog.getName(), createdDog.getName());
    }

    @Test(groups = "systemTest")
    public void verifyDeleteRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        Dog[] arrayOfDogs = given().baseUri(BASE_URI+"allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class);

        int initialDogTableSize = arrayOfDogs.length;

        Dog createdDog = given().baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        given().baseUri(BASE_URI + createdDog.getId())
                .when()
                .delete()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        int arrayOfDogSizeAfterDeletingDog = given()
                .baseUri(BASE_URI + "allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class)
                .length;

        Assert.assertEquals("Delete request works incorrectly", arrayOfDogSizeAfterDeletingDog, initialDogTableSize);
    }

    @Test(groups = "systemTest")
    public void verifySystemRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        Dog[] arrayOfDogs = given().baseUri(BASE_URI+"allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class);

        int initialDogTableSize =  arrayOfDogs.length;

        Dog createdDog = given().baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        //verify that Dog was really added to DB
        int arrayOfDogWithAddedItem = given().baseUri(BASE_URI+"allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class).length;

        Assert.assertEquals("Dog with name " + expectedDog.getName() + "was not added to DB",
                initialDogTableSize + 1, arrayOfDogWithAddedItem);

        //update dog
        createdDog.setName("Updated dog name");

        Dog updatedDog = given().baseUri(BASE_URI + createdDog.getId())
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(createdDog)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog.class);

        Assert.assertEquals("Update request is working incorrectly", updatedDog.getName(), createdDog.getName());

        //delete
        given().baseUri(BASE_URI + createdDog.getId())
                .when()
                .delete()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

        int arrayOfDogSizeAfterDeletingDog = given()
                .baseUri(BASE_URI + "allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class)
                .length;

        Assert.assertEquals("Delete request works incorrectly", arrayOfDogSizeAfterDeletingDog, initialDogTableSize );
    }

    private String createUniqueName() {
        return "Dog" + (new Date()).getTime();
    }
}

