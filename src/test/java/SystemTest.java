import anna.klueva.Dog;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SystemTest {
    public static final String BASE_URI = "http://localhost:8080/WebApp-1.1/dog/";

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

    @Test(groups = "systemTest")
    public void verifyCreateDogRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(35.0)
                .weight(23)
                .build();

        int initialDogTableSize = given().baseUri(BASE_URI + "allDogs")
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

        Dog[] arrayOfDogWithAddedItem = given().baseUri(BASE_URI + "allDogs")
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Dog[].class);

        int arrayOfDogWithAddedItemSize = arrayOfDogWithAddedItem.length;

        Assert.assertEquals("Dog table quantity was not increased",
                initialDogTableSize + 1, arrayOfDogWithAddedItemSize);
        Assert.assertEquals("Create request is working incorrectly", createdDog,
                arrayOfDogWithAddedItem[arrayOfDogWithAddedItemSize - 1]);
    }

    @Test(groups = "systemTest")
    public void verifyUpdateRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(TestUtil.createUniqueName())
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

        Dog actualDog = given().baseUri(BASE_URI + createdDog.getId())
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
                .name(TestUtil.createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        Dog[] arrayOfDogs = given().baseUri(BASE_URI + "allDogs")
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
    public void verifyGetRequest404Response() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("SystemTest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build();

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

        given().baseUri(BASE_URI + createdDog.getId())
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = "systemTest")
    public void verifyCreateIncorrectItem() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("")
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date(2019, 11, 14)))
                .height(-65)
                .weight(-40)
                .build();

        String actualDog = given().baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().body().jsonPath().get("fieldErrors.message").toString();

        Assert.assertTrue("Error message for name is incorrect", actualDog.contains("The name '' must be between 1 and 100 characters long"));
        Assert.assertTrue("Error message for date is incorrect", actualDog.contains("Must be a past date"));
        Assert.assertTrue("Error message for height is incorrect", actualDog.contains("The height must be more then 0"));
        Assert.assertTrue("Error message for weight is incorrect", actualDog.contains("The weight must be more then 0"));
    }

    @Test(groups = "systemTest")
    public void verifyCreateIncorrectNameLength() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(TestUtil.createStringWithLength(101))
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(36.666)
                .weight(40)
                .build();

        String actualDog = given().baseUri(BASE_URI)
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(expectedDog)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().body().jsonPath().get("fieldErrors.message").toString();

        Assert.assertTrue("Error message for name is incorrect",
                actualDog.contains("The name '"+TestUtil.createStringWithLength(101)+"' must be between 1 and 100 characters long"));
    }

    @Test(groups = "systemTest")
    public void verifyUpdateNotExistingItem() {
        Dog expectedDog = Dog.builder()
                .name("SystemTest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build();

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

        given().baseUri(BASE_URI + createdDog.getId())
                .accept(ContentType.JSON)
                .accept("application/json")
                .contentType(ContentType.JSON)
                .body(createdDog)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = "systemTest")
    public void verifyDeleteNotExistingItem() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("SystemTest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build();

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

        given().baseUri(BASE_URI + createdDog.getId())
                .when()
                .delete()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(is("Item with id=" + createdDog.getId() + " is not exist"));
    }

    @Test(groups = "systemTest")
    public void verifySystemRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name(TestUtil.createUniqueName())
                .dateOfBirth(new Date())
                .height(35.0)
                .weight(23)
                .build();

        Dog[] arrayOfDogs = given().baseUri(BASE_URI + "allDogs")
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

        //verify that Dog was really added to DB
        int arrayOfDogWithAddedItem = given().baseUri(BASE_URI + "allDogs")
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

        Assert.assertEquals("Delete request works incorrectly", arrayOfDogSizeAfterDeletingDog, initialDogTableSize);
    }
}

