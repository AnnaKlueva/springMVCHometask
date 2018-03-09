import anna.klueva.Dog;
import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.AssertJUnit.assertEquals;
import org.springframework.validation.BindingResult;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by akliuieva on 2/14/18.
 */
public class ValidatorTest {

    @Test(groups = "unitTest")
    public  void validatorTest() {
        Dog dog = Dog.builder()
                .name("")
                .dateOfBirth(new Date(2019, 11, 6))
                .height(-25)
                .weight(-35)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        String message = validator.validateProperty(dog, "name")
                .iterator()
                .next()
                .getMessage();
        assertEquals("The name '' must be between 1 and 100 characters long", message);

        message = validator.validateProperty(dog, "dateOfBirth")
                .iterator()
                .next()
                .getMessage();
        assertEquals("Must be a past date", message);

        message = validator.validateProperty(dog, "height").iterator().next().getMessage();
        assertEquals("The height must be more then 0", message);

        message = validator.validateProperty(dog, "weight").iterator().next().getMessage();
        assertEquals("The weight must be more then 0", message);
    }
}
