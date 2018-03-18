import anna.klueva.Dog;
import anna.klueva.config.RootConfig;
import anna.klueva.dao.DogDAO;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.Mockito.mock;

/**
 * Created by akliuieva on 1/5/18.
 */
@ContextConfiguration(classes={RootConfig.class})
@ActiveProfiles("test")
public class DBTest extends AbstractTestNGSpringContextTests {

    @Autowired
    DogDAO dogDAO;

    public static Date date = new Date();

    @Test(groups = "dbTest")
    public void verifyGetByIdRequestOkResponse() throws Exception {
        Dog expectedDog = dogDAO.save(Dog.builder().name("DBtest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build());
        Optional<Dog> actualDog = dogDAO.findById(expectedDog.getId());

        Assert.assertEquals("Get request returns incorrect value", expectedDog, actualDog.get());
    }

    @Test(groups = "dbTest")
    public void verifyGetByIdNotFoundResponse() throws Exception {
        Dog expectedDog = dogDAO.save(Dog.builder().name("DBtest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build());
        dogDAO.deleteById(expectedDog.getId());

        Optional<Dog> actualDog = dogDAO.findById(expectedDog.getId());
        Assert.assertFalse(actualDog.isPresent());
    }

    @Test(groups = "dbTest")
    public void verifyNotFoundResponse() throws Exception {
        Dog expectedDog = dogDAO.save(Dog.builder().name("DBtest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build());
        dogDAO.deleteById(expectedDog.getId());

        Optional<Dog> actualDog = dogDAO.findById(expectedDog.getId());
        Assert.assertFalse(actualDog.isPresent());
    }

}
