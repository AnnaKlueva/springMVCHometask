import anna.klueva.Dog;
import anna.klueva.config.RootConfig;
import anna.klueva.dao.DogDAO;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes={RootConfig.class})
@ActiveProfiles("test")
public class DBTest extends AbstractTestNGSpringContextTests {

    @Autowired
    DogDAO dogDAO;

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
    public void verifySaveItemToDB() throws Exception {
        Dog expectedDog = dogDAO.save(Dog.builder().name("DBtest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build());

        Optional<Dog> actualDog = dogDAO.findById(expectedDog.getId());
        Assert.assertEquals("Item was not saved", expectedDog, actualDog.get());
    }

    @Test(groups = "dbTest")
    public void verifyFindAllItems() throws Exception {
        Iterable<Dog> initialArray = dogDAO.findAll();
        List<Dog> initialList = new ArrayList<>();
        initialArray.forEach(initialList::add);

        dogDAO.save(Dog.builder().name("DBtest" + TestUtil.createUniqueName())
                .dateOfBirth(TestUtil.getDateWithoutTime(new Date()))
                .height(65)
                .weight(40).build());

        Iterable<Dog> changedArray = dogDAO.findAll();
        List<Dog> changedList = new ArrayList<>();
        changedArray.forEach(changedList::add);

        Assert.assertEquals("Size of DB is not raised", initialList.size() + 1, changedList.size());
    }
}
