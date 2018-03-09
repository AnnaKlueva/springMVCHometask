import anna.klueva.Dog;
import anna.klueva.DogController;
import anna.klueva.dao.DogDAO;
import anna.klueva.util.RootConfig;
import anna.klueva.util.WebConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by akliuieva on 1/5/18.
 */
@ContextConfiguration(classes={RootConfig.class, WebConfig.class})
@ActiveProfiles("test")
public class DBTest extends AbstractTestNGSpringContextTests {
}
