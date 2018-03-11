import anna.klueva.config.RootConfig;
import anna.klueva.config.WebConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static org.mockito.Mockito.mock;

/**
 * Created by akliuieva on 1/5/18.
 */
@ContextConfiguration(classes={RootConfig.class, WebConfig.class})
@ActiveProfiles("test")
public class DBTest extends AbstractTestNGSpringContextTests {
}
